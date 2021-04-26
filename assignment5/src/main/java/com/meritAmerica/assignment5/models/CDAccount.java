package com.meritAmerica.assignment5.models;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

//import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonProperty;


public class CDAccount extends BankAccount {
		
	//@JsonCreator
	public CDAccount( CDOffering cdOffering,  double balance) {
		
		super(MeritBank.getNextAccountNumber(), balance,cdOffering.getInterestRate(),new Date());
		
		this.cdOffering=cdOffering;
	}
	


	public CDAccount(long accountNumber, double balance, double interestRate,java.util.Date accountOpenedOn,int term) {
		super(accountNumber,balance,interestRate,accountOpenedOn);
		
		this.cdOffering = new CDOffering(term, interestRate);
		
	}
	
	public double getInterestRate() {
		return this.cdOffering.getInterestRate();
	}
	@JsonIgnore
	public int getTerm() {
		return this.cdOffering.getTerm();
	}
	
	@JsonIgnore
	public int getId() {
		return this.cdOffering.getId();
	}
	@JsonIgnore
	public java.util.Date getStartDate(){
		Date date=new Date();
		return date;
	}
	public boolean withdraw(double amount) {
		return false;
	}
	public boolean deposit(double amount) {
		return false;
	}
	public double futureValue() {
		
		double futureVal = getBalance() * Math.pow((1+this.getInterestRate()),this.getTerm());
		return futureVal;
	}
	public static CDAccount readFromString(String accountData) throws  java.lang.NumberFormatException{
		StringTokenizer token = new StringTokenizer(accountData, ",");
		int numAccount = Integer.parseInt(token.nextToken());
		long balance = Long.parseLong(token.nextToken());
		double rate = Double.parseDouble(token.nextToken());
		
		Date date = new Date(token.nextToken());
		Format f = new SimpleDateFormat("dd/MM/yy");
		String strDate = f.format(date);
		 date = new Date(strDate);
		
		 int term = Integer.parseInt(token.nextToken());

		CDAccount cdAcc = new CDAccount(numAccount, balance, rate, date, term);
		return cdAcc;
	}
	public String writeToString() {
		String cdString = getAccountNumber()+","+getBalance()+","+getInterestRate()+","+getStartDate()+","+getTerm(); 
		return cdString;
	}
	
	
	private  CDOffering cdOffering;
	
	public CDOffering getCDOffering() {
		return cdOffering;
	}

}