package com.mli.enums;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public enum LoanType {

	
	AUTO_LOAN(1,"Auto Loan","Auto Loan"),
	PERSONAL_LOAN(2,"Personal Loan" ,"PLN"), 
	GOLD_LOAN(3, "Gold Loan","GLN"), 
	AFFORDABLE_HOUSING_LOAN(4, "Affordable Housing Loan" , "AFH"), 
	TWO_WHEELER_LOAN(5, "Two Wheeler Loan" ,"TWL"), 
	MORTGAGE_LOAN(6,  "Mortgage Loan" ,"Mortgage Loan"), 
	PRINTING_EQUIPMENT_LOAN(7, "Printing Equipment Loan","PEN"),
	COMMERCIAL_VEHICLE_LOAN(8, "Commercial Vehicle Loan" , "CVL" ), 
	USED_CAR_LOAN(9, "Used Car Loan" , "UCL" ),
	USED_COMMERCIAL_VEHICLE_LOAN(10, "Used Commercial Vehicle Loan" , "UCV" ), 
	MEDICAL_EQUIPMENT_LOAN(11, "Medical Equipment Loan" , "MEN" ), 
	LOAN_AGAINST_RENT_DISCOUNTING(12, "Loan Against Rent Discounting" , "LRD" ),
	COMMERCIAL_EQUIPMENT_LOAN(13, "Commercial Equipment Loan" , "CEL" ),
	HOME_LOAN(14, "Home Loan" , "Home Loan" ), 
	BUSINESS_LOAN(15, "Business Loan" , "BLN" ),
	USED_COMMERCIAL_EQUIPMENT_LOAN(16, "Used Commercial Equipment Loan" , "UCE" ), 
	EDUCATION_LOAN(18,"Education Loan","Education Loan"),
	ASHA_HOME_LOAN(19,"Asha Home Loan","Asha Home Loan"),
	LOAN_AGAINST_PROPERTY(20,"Loan Against Property","Loan Against Property"),
	LOAN_AGAINST_SECURITY(21,"Loan Against Security","Loan Against Security"),
	AGRI_LOAN(22,"Agri Loan","Agri Loan"),
	TRACTOR_LOAN(23,"Tractor Loan","Tractor Loan"),
	SBB_LOAN(24,"SBB Loan","SBB Loan"),
	Agri_MSME_Loan(25,"Agri_MSME_Loan","Agri MSME Loan"),
	SMALL_BUSINESS_LOAN(26,"Small Business Loan","SBL"),
	BUSINESS_BANKING_GCL(27,"Business Banking GCL","Business Banking GCL"),
	HEALTH_CARE_FINANCE(28,"Health Care Finance","HCF"),
	BUSINESS_BANKING_GTL(29,"Business Banking GTL","Business Banking GTL"),
	GOLD_LOAN_GCL(30,"Gold Loan GCL","Gold Loan GCL"),
	GOLD_LOAN_GTL(31,"Gold Loan GTL","Gold Loan GTL"),
	VEHICLE_LOAN(32,"Vehicle Loan","Vehicle Loan"),
	MORTGAGE_LOAN_GTL(33,"Mortgage Loan GTL","Mortgage Loan GTL"),
	RETAIL_LOAN(34,"Retail Loan","Retail Loan"),
	SMALL_BUSINESS_BANKING_LOAN(35,"Small Business Banking Loan","Small Business Banking Loan"),
	UNSECURED_LOAN(36,"Unsecured Loan","Unsecured Loan");
	

	private Integer id;
	private String text;
	private String code;

	LoanType(Integer id, String text, String code) {
		this.id = id;
		this.text = text;
		this.code = code;
	}

	public static LoanType getLoanType(String code) {
		if (code == null) {
			return null;
		}
		
		switch(code){
		
		/*case "ALN" : return LoanType.AUTO_LOAN;		
		case "PLN" : return LoanType.PERSONAL_LOAN;			
		case "GLN" : return LoanType.GOLD_LOAN;		
		case "AFH" : return LoanType.AFFORDABLE_HOUSING_LOAN;		
		case "TWL" : return LoanType.TWO_WHEELER_LOAN;		
		case "MOR" : return LoanType.MORTGAGE_LOAN;		
		case "PEN" : return LoanType.PRINTING_EQUIPMENT_LOAN;		
		case "CVL" : return LoanType.COMMERCIAL_EQUIPMENT_LOAN;		
		case "UCL" : return LoanType.USED_CAR_LOAN;
		case "UCV" : return LoanType.USED_COMMERCIAL_VEHICLE_LOAN;		
		case "MEN" : return LoanType.MEDICAL_EQUIPMENT_LOAN;
		case "LRD" : return LoanType.LOAN_AGAINST_RENT_DISCOUNTING ;
		case "CEL" : return LoanType.COMMERCIAL_EQUIPMENT_LOAN;
		case "HLN" : return LoanType.HOME_LOAN;		
		case "BLN" : return LoanType.BUSINESS_LOAN;		
		case "UCE" : return LoanType.USED_COMMERCIAL_EQUIPMENT_LOAN;
		case "ZAP" : return LoanType.ZAP_LOAN;
		case "ELN" : return LoanType.EDUCATION_LOAN;*/
		
		
		case "Home Loan" : return LoanType.HOME_LOAN;
		case "Asha Home Loan" : return LoanType.ASHA_HOME_LOAN;
		case "Loan Against Property" : return LoanType.LOAN_AGAINST_PROPERTY;
		case "Loan Against Security" : return LoanType.LOAN_AGAINST_SECURITY;
		case "Education Loan" : return LoanType.EDUCATION_LOAN;
		case "Mortgage Loan" : return LoanType.MORTGAGE_LOAN;
		case "Agri Loan" : return LoanType.AGRI_LOAN;
		case "Tractor Loan" : return LoanType.TRACTOR_LOAN;
		case "Auto Loan" : return LoanType.AUTO_LOAN;
		case "SBB Loan" : return LoanType.SBB_LOAN;
		case "Agri MSME Loan" : return LoanType.Agri_MSME_Loan;
		case "Small Business Banking Loan" : return LoanType.SMALL_BUSINESS_BANKING_LOAN;
		case "Unsecured Loan" : return LoanType.UNSECURED_LOAN;
		}
		return null;	
	}

	public static String getLoanType(Integer id) {
		if (id == null) {
			return null;
		}
		switch(id){
		case 1 :  return LoanType.AUTO_LOAN.getText();		
		case 2 :  return LoanType.PERSONAL_LOAN.getText();			
		case 3 :  return LoanType.GOLD_LOAN.getText();		
		case 4:   return LoanType.AFFORDABLE_HOUSING_LOAN.getText();		
		case 5 :  return LoanType.TWO_WHEELER_LOAN.getText();		
		case 6 :  return LoanType.MORTGAGE_LOAN.getText();		
		case 7 :  return LoanType.PRINTING_EQUIPMENT_LOAN.getText();		
		case 8 :  return LoanType.COMMERCIAL_EQUIPMENT_LOAN.getText();		
		case 9 :  return LoanType.USED_CAR_LOAN.getText();
		case 10 : return LoanType.USED_COMMERCIAL_VEHICLE_LOAN.getText();		
		case 11 : return LoanType.MEDICAL_EQUIPMENT_LOAN.getText();
		case 12 : return LoanType.LOAN_AGAINST_RENT_DISCOUNTING.getText();
		case 13 : return LoanType.COMMERCIAL_EQUIPMENT_LOAN.getText();
		case 14 : return LoanType.HOME_LOAN.getText();		
		case 15 : return LoanType.BUSINESS_LOAN.getText();		
		case 16 : return LoanType.USED_COMMERCIAL_EQUIPMENT_LOAN.getText();
		}
		return null;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return this.getCode();
	}

}
