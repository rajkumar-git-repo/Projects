package com.mli.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.mli.constants.Constant;
import com.mli.dao.MISRecipientDAO;
import com.mli.dao.MasterLoanTypeBankDAO;
import com.mli.entity.MISRecipientEntity;
import com.mli.entity.MasterLoanTypeBankEntity;

/**
 * this class is used to load data on application startup
 * @author rajkumar
 *
 */
@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private MasterLoanTypeBankDAO loanTypeBankDAO;
	
	@Autowired
	private MISRecipientDAO misRecipientDao;
	
	@Value("${mli.axisbank.mailto}")
	private String axisbankmailto;
	@Value("${mli.axisbank.mailIdCc}")
	private String axisbankmailIdCc;
	@Value("${mli.axisbank.mailIdBcc}")
	private String axisbankmailIdBcc;
	@Value("${mli.yesbank.mailto}")
	private String yesbankmailto;
	@Value("${mli.yesbank.mailIdCc}")
	private String yesbankmailIdCc;
	@Value("${mli.yesbank.mailIdBcc}")
	private String yesbankmailIdBcc;
	
	@Transactional
	private void uploadMasterLoanType() {
		MasterLoanTypeBankEntity  masterLoanTypeBankEntity1 = new MasterLoanTypeBankEntity(1l,"Retail Loan","Level","N","N","Yes","10","No","NA","NA","10","10");
		MasterLoanTypeBankEntity  masterLoanTypeBankEntity2 = new MasterLoanTypeBankEntity(2l,"Mortgage Loan","Level","Y","Silver","Yes","20","Yes","NA","NA","10","10");
		MasterLoanTypeBankEntity  masterLoanTypeBankEntity3 = new MasterLoanTypeBankEntity(3l,"Mortgage Loan","Reducing","Y","Silver","Yes","10","Yes","10-12,12.01-15","12,15","10","12,15");
		MasterLoanTypeBankEntity  masterLoanTypeBankEntity4 = new MasterLoanTypeBankEntity(4l,"Gold Loan","Level","Y","Silver","Yes","30","Yes","NA","NA","10","10");
		MasterLoanTypeBankEntity  masterLoanTypeBankEntity5 = new MasterLoanTypeBankEntity(5l,"Personal Loan","Level","Y","Silver","Yes","80","No","NA","NA","10","10");
		MasterLoanTypeBankEntity  masterLoanTypeBankEntity6 = new MasterLoanTypeBankEntity(6l,"Personal Loan","Reducing","Y","Silver","Yes","80","No","15","15","10","15");
		MasterLoanTypeBankEntity  masterLoanTypeBankEntity7 = new MasterLoanTypeBankEntity(7l,"Vehicle Loan","Level","Y","Silver","Yes","55","Yes","NA","NA","10","10");
		MasterLoanTypeBankEntity  masterLoanTypeBankEntity8 = new MasterLoanTypeBankEntity(8l,"Vehicle Loan","Reducing","Y","Silver","Yes","15","Yes","15","15","10","15");
		MasterLoanTypeBankEntity  masterLoanTypeBankEntity9 = new MasterLoanTypeBankEntity(9l,"Mortgage Loan GTL","Level","N","N","Yes","10","No","NA","NA","10","10");
	
		if(ObjectUtils.isEmpty(loanTypeBankDAO.findByUserId(1l)))
		loanTypeBankDAO.saveData(masterLoanTypeBankEntity1);
		if(ObjectUtils.isEmpty(loanTypeBankDAO.findByUserId(2l)))
		loanTypeBankDAO.saveData(masterLoanTypeBankEntity2);
		if(ObjectUtils.isEmpty(loanTypeBankDAO.findByUserId(3l)))
		loanTypeBankDAO.saveData(masterLoanTypeBankEntity3);
		if(ObjectUtils.isEmpty(loanTypeBankDAO.findByUserId(4l)))
		loanTypeBankDAO.saveData(masterLoanTypeBankEntity4);
		if(ObjectUtils.isEmpty(loanTypeBankDAO.findByUserId(5l)))
		loanTypeBankDAO.saveData(masterLoanTypeBankEntity5);
		if(ObjectUtils.isEmpty(loanTypeBankDAO.findByUserId(6l)))
		loanTypeBankDAO.saveData(masterLoanTypeBankEntity6);
		if(ObjectUtils.isEmpty(loanTypeBankDAO.findByUserId(7l)))
		loanTypeBankDAO.saveData(masterLoanTypeBankEntity7);
		if(ObjectUtils.isEmpty(loanTypeBankDAO.findByUserId(8l)))
		loanTypeBankDAO.saveData(masterLoanTypeBankEntity8);
		if(ObjectUtils.isEmpty(loanTypeBankDAO.findByUserId(9l)))
		loanTypeBankDAO.saveData(masterLoanTypeBankEntity9);
	
	}
	
	@Transactional
	public void uploadMISRecipients() {
		MISRecipientEntity misRecipientEntity1 = new MISRecipientEntity(axisbankmailto, Constant.AXIS, Constant.TO);
		MISRecipientEntity misRecipientEntity2 = new MISRecipientEntity(axisbankmailIdCc, Constant.AXIS, Constant.CC);
		MISRecipientEntity misRecipientEntity3 = new MISRecipientEntity(axisbankmailIdBcc, Constant.AXIS, Constant.BCC);
		MISRecipientEntity misRecipientEntity4 = new MISRecipientEntity(yesbankmailto, Constant.YES, Constant.TO);
		MISRecipientEntity misRecipientEntity5 = new MISRecipientEntity(yesbankmailIdCc, Constant.YES, Constant.CC);
		MISRecipientEntity misRecipientEntity6 = new MISRecipientEntity(yesbankmailIdBcc, Constant.YES, Constant.BCC);
		MISRecipientEntity misRecipientEntity7 = new MISRecipientEntity("", Constant.YESBANKCC, Constant.TO);
		MISRecipientEntity misRecipientEntity8 = new MISRecipientEntity("", Constant.YESBANKCC, Constant.CC);
		MISRecipientEntity misRecipientEntity9 = new MISRecipientEntity("", Constant.YESBANKCC, Constant.BCC);
		
		if(ObjectUtils.isEmpty(misRecipientDao.findByMPHTypeAndMailType(Constant.AXIS, Constant.TO))) 
			misRecipientDao.saveData(misRecipientEntity1);
		if(ObjectUtils.isEmpty(misRecipientDao.findByMPHTypeAndMailType(Constant.AXIS, Constant.CC))) 
			misRecipientDao.saveData(misRecipientEntity2);
		if(ObjectUtils.isEmpty(misRecipientDao.findByMPHTypeAndMailType(Constant.AXIS, Constant.BCC))) 
			misRecipientDao.saveData(misRecipientEntity3);
		if(ObjectUtils.isEmpty(misRecipientDao.findByMPHTypeAndMailType(Constant.YES, Constant.TO))) 
			misRecipientDao.saveData(misRecipientEntity4);
		if(ObjectUtils.isEmpty(misRecipientDao.findByMPHTypeAndMailType(Constant.YES, Constant.CC))) 
			misRecipientDao.saveData(misRecipientEntity5);
		if(ObjectUtils.isEmpty(misRecipientDao.findByMPHTypeAndMailType(Constant.YES, Constant.BCC))) 
			misRecipientDao.saveData(misRecipientEntity6);
		if(ObjectUtils.isEmpty(misRecipientDao.findByMPHTypeAndMailType(Constant.YESBANKCC, Constant.TO))) 
			misRecipientDao.saveData(misRecipientEntity7);
		if(ObjectUtils.isEmpty(misRecipientDao.findByMPHTypeAndMailType(Constant.YESBANKCC, Constant.CC))) 
			misRecipientDao.saveData(misRecipientEntity8);
		if(ObjectUtils.isEmpty(misRecipientDao.findByMPHTypeAndMailType(Constant.YESBANKCC, Constant.BCC))) 
			misRecipientDao.saveData(misRecipientEntity9);
	}
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.out.println("*******event listener run successfully*******");
		//uploadMasterLoanType();
		uploadMISRecipients();
	}

}
