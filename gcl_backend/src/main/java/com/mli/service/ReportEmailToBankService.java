package com.mli.service;

public interface ReportEmailToBankService {
	
    public void mailDailyReportToBank() throws Exception;
    
    public void deleteOTPDetails() throws Exception;
    public void sendExcelToBank();
    public void retryExcelToBank();
    public void deleteLocalFolder();

	String mailDailyYBLCCReportToBank() throws Exception;

}
