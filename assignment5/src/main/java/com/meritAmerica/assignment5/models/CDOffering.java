package com.meritAmerica.assignment5.models;

public class CDOffering {
	/*public CDOffering(int term, double interestRate) {
		this.term = term;
		this.interestRate = interestRate;
		
	}*/
	public CDOffering(int term, double interestRate) {
		this.term = term;
		this.interestRate = interestRate;
		this.id=nextId++;
	}
	
	
	public CDOffering(int id) {
		CDOffering cd = MeritBank.getCDOfferingById(id);
		this.term = cd.term;
		this.interestRate = cd.interestRate;
		this.id=cd.id;
	}

	public int getTerm() {
		return this.term;
	}
	
	
	public double getInterestRate() {
		return this.interestRate;
	}
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	


	public static CDOffering readFromString(String cdOfferingDataString) throws java.lang.NumberFormatException{
		//expecting like this: 1,0.018
		CDOffering cd = null;

		if(cdOfferingDataString.indexOf(',')!=-1) {
			int term = Integer.parseInt(cdOfferingDataString.substring(0, cdOfferingDataString.indexOf(',')));  
			double rate = Double.parseDouble(cdOfferingDataString.substring(cdOfferingDataString.indexOf(',')+1));
			cd = new CDOffering(term,rate);
		} 
		else {
			throw new NumberFormatException();
		}

		return cd;
	}

	

	public String writeToString() {
		String cdString = this.getTerm() + "," + this.getInterestRate();
		return cdString;
	}
	//CDOffering cd = new CDOffering(getTerm(),getInterestRate());

	private int term;
	
	private double interestRate;
	private int id;
	private static int nextId=1;

}