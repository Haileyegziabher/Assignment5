package com.meritAmerica.assignment5.models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MeritBank {
	
	public static void addAccountHolder(AccountHolder accountHolder) {
		accHolderList.add(accountHolder);
	}
	public static AccountHolder[] getAccountHolders() {
		AccountHolder[] accHolderArr = accHolderList.toArray(new AccountHolder[0]);
		return accHolderArr;
	}
	public static AccountHolder getAccountHolderById(int accountHolderId) {
		
		return accHolderList.get(accountHolderId - 1);
	}
	
	public static CDOffering[] getCDOfferings() {
		return offerings;
	}
	public static CDOffering getCDOfferingById(int cdOfferingId) {
		return offeringsList.get(cdOfferingId-1);
	}
	public static CDOffering getBestCDOffering(double depositAmount) {
		//CDOffering[] offering= getCDOfferings();
		CDOffering bestCDOffer = offerings[0];
		for(int i=1; i<offerings.length;i++) {
			double futureVal=futureValue(depositAmount,offerings[i].getInterestRate(),offerings[i].getTerm());
			double bestFutureVal = futureValue(depositAmount,bestCDOffer.getInterestRate(),bestCDOffer.getTerm());
			if(futureVal>bestFutureVal) {
				bestFutureVal=futureVal;
				bestCDOffer=offerings[i];
			}
		}
		return bestCDOffer;
	}
	public static CDOffering getSecondBestCDOffering(double depositAmount) {
		
		CDOffering secondBestOffer = null;
		for(int i=1;i<offerings.length;i++) {
			for(int j=i+1;j<offerings.length;j++) {
				double bestFutureVal=futureValue(depositAmount,offerings[i].getInterestRate(),offerings[i].getTerm());
				double futureVal = futureValue(depositAmount,offerings[j].getInterestRate(),offerings[j].getTerm());
			//double secondBestFutureVal = futureValue(depositAmount,secondBestOffer.getInterestRate(),secondBestOffer.getTerm());
				CDOffering[] temp=new CDOffering[1];
				if(futureVal>bestFutureVal) {
					temp[0] = offerings[i];
					offerings[i]=offerings[j];
					offerings[j]=temp[0];
				}	
			}
		}
		secondBestOffer=offerings[1];
		return secondBestOffer;
	}
	public static void clearCDOfferings() {
		offerings=null;	
	}
	public static void setCDOfferings(CDOffering[] offerings) {
		MeritBank.offerings=offerings;                          //cannot access static variable with this keyword
		
	}
	public static long getNextAccountNumber() {
		
		return nextAccNumber++;
	}
	public static Date getDate() {
		Date date = new Date();
		Format f = new SimpleDateFormat("dd/MM/yy");
		String strDate = f.format(date);
		date = new Date(strDate);
		return date;
	}
	public static double totalBalances() {
		AccountHolder[] accountHolderArr= getAccountHolders();
		double totalBalance=0;
		for(int i=0;i<accountHolderArr.length;i++) {
			totalBalance+=accountHolderArr[i].getSavingsBalance()+accountHolderArr[i].getCheckingBalance()+accountHolderArr[i].getCDBalance();
		}
		return totalBalance;
	}
		
	public static double futureValue(double presentValue, double interestRate, int term) {
		double futureVal = presentValue * Math.pow((1+interestRate),term);
		return futureVal;
	}
	public static void setNextAccountNumber(long nextAccountNumber) {
		nextAccNumber = nextAccountNumber;
	}	
	public static boolean readFromFile(String fileName)  {
		try {
			BufferedReader rd = new BufferedReader(new FileReader(fileName));
			String line = rd.readLine(); 											//reads the first line
			setNextAccountNumber(Long.parseLong(line));
			 line = rd.readLine();													//reads the 2nd line
			 int cdLength = Integer.parseInt(line);
			 CDOffering[] cd = new CDOffering[cdLength];							//makes an array for CDOffering
			 for(int i=0; i<cdLength;i++) {											//length of array was read from 2nd line
				 line=rd.readLine();
				 //cd[i]=CDOffering.readFromString(line);									//stores the CDOffering with (term and rate) in each index
			 }
			 setCDOfferings(cd);		//requirement:"when reading from file, the data should overwrite the MeritBank data such that previous data no longer exists, only the data read from the file should exist"
			 line=rd.readLine();
			 int accHolderLength=Integer.parseInt(line);
			 accHolderList = new ArrayList<AccountHolder>();//the accHolderList now points to the newly created arrayList of accounts which is read from the file.//automatically the pointer has now changed from previous lists to the new arrayList.
			 for (int i = 0; i < accHolderLength; i++) {
					line = rd.readLine();
					//System.out.println("AH Read"+line);
					AccountHolder a = AccountHolder.readFromString(line);
					
					accHolderList.add(a);	
					line = rd.readLine();
					int numOfChecking = Integer.parseInt(line);
					if (numOfChecking != 0) {
						for (int j = 0; j < numOfChecking; j++) {
							
							line = rd.readLine();
							//System.out.println("CA Read"+line);
							CheckingAccount ch =CheckingAccount.readFromString(line);
							a.addCheckingAccount(ch);
						}
					}
					line = rd.readLine();
					int numOfSavings = Integer.parseInt(line);
					if (numOfSavings != 0) {
						for (int j = 0; j < numOfSavings; j++) {
							
							line = rd.readLine();
							//System.out.println("SA Read"+line);
							SavingsAccount sv = SavingsAccount.readFromString(line);
							a.addSavingsAccount(sv);
						}
					}
					line = rd.readLine();
					int numOfCD = Integer.parseInt(line);
					if (numOfCD != 0) {
						for (int j = 0; j < numOfCD; j++) {
							
							line = rd.readLine();
							CDAccount cdAcc = CDAccount.readFromString(line);
							a.addCDAccount(cdAcc);

							//a.addCDAccount(offerings[j], balance);
						}
					}
			}
			rd.close();
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public static boolean writeToFile(String fileName) {
		try{	
			PrintWriter wr = new PrintWriter(new FileWriter(fileName));
			//wr.print("");
			wr.println(nextAccNumber);
			wr.println(offerings.length);
			for(int i=0;i<offerings.length;i++) {
				wr.println(offerings[i].writeToString());
			}
			wr.println(accHolderList.size());
			for(int i=0;i<accHolderList.size();i++) {
				AccountHolder accInfo=accHolderList.get(i);
				wr.println(accInfo.writeToString());
				int numOfCheckings = accInfo.getNumberOfCheckingAccounts();
				wr.println(numOfCheckings);
				if(numOfCheckings!=0) {
					CheckingAccount[] checking=accInfo.getCheckingAccounts();
					for(int j=0;j<numOfCheckings;j++) {
						wr.println(checking[j].writeToString());
					}
				}
				int numOfSavings = accInfo.getNumberOfSavingsAccounts();
				wr.println(numOfSavings);
				if(numOfSavings!=0) {
					SavingsAccount[] savings=accInfo.getSavingsAccounts();
					for(int j=0;j<numOfSavings;j++) {
						wr.println(savings[j].writeToString());
					}
				}
				int numOfCD = accInfo.getNumberOfCDAccounts();
				wr.println(numOfCD);
				if(numOfCD!=0) {
					CDAccount[] cd=accInfo.getCDAccounts();
					for(int j=0;j<numOfSavings;j++) {
						wr.println(cd[j].writeToString());
					}
				}	
			}
			wr.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public static AccountHolder[] sortAccountHolders() {
		Collections.sort(accHolderList);											//Collections.sort(list) returns a void,and cannot be stored to any variable,the list itself is sorted 
		return accHolderList.toArray(new AccountHolder[0]);							//then to convert to array, separately use, list.toArray, 
																					//*Collections.sort(list).toArray()* will not work, it's an error.
	}
	
	public static ArrayList<AccountHolder> accHolderList = new ArrayList<AccountHolder>();
	private static long nextAccNumber=1;
	private static CDOffering[] offerings;
	
	private static List<CDOffering> offeringsList = new ArrayList<>();
	
	public static void addCDOffering(CDOffering cdo) {
		offeringsList.add(cdo);
	}
	
	public static List<CDOffering> getCDOffering() {
		return offeringsList;
	}
	
}