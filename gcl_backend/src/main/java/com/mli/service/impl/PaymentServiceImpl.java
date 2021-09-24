package com.mli.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.RedirectView;

import com.mli.constants.Constant;
import com.mli.constants.MLIMessageConstants;
import com.mli.dao.CreditCardCustomerDAO;
import com.mli.dao.PaymentDAO;
import com.mli.dao.PaymentS2SLogDAO;
import com.mli.entity.CreditCardCustomerEntity;
import com.mli.entity.PaymentEntity;
import com.mli.entity.PaymentS2SLog;
import com.mli.enums.OTPType;
import com.mli.enums.PaymentStatus;
import com.mli.enums.Status;
import com.mli.model.ConsumerDataModel;
import com.mli.model.ConsumerResponse;
import com.mli.model.PaymentResponse;
import com.mli.model.response.ResponseModel;
import com.mli.model.sms.SMSResponseModel;
import com.mli.service.CreditCardJourneyService;
import com.mli.service.PaymentService;
import com.mli.service.SendMLISMSService;
import com.mli.utils.AES;
import com.mli.utils.DateUtil;

@Service
public class PaymentServiceImpl implements PaymentService{

	private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
	
	@Value("${techprocess.marchantID}")
	private String marchantID;

	@Value("${techprocess.itemID}")
	private String itemID;
	
	@Value("${techprocess.salt}")
	private String salt;
	
	@Value("${techprocess.returnUrl}")
	private String returnUrl;
	
	@Value("#{'${techprocess.success}'}")
	private String successPage;
	
	@Value("#{'${techprocess.error}'}")
	private String errorPage;
	
	@Value("${mli.aes.encryption.key}")
	private String aesSecratKey;
	
	@Value("${mli.customerscreen.redirect.url}")
	private String custScreenRdirectUrl;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CreditCardCustomerDAO creditCardCustomerDAO;
	
	@Autowired
	private PaymentDAO paymentDAO;
	
	@Autowired
	private CreditCardJourneyService creditCardJourneyService;
	
	@Autowired
	private PaymentS2SLogDAO paymentS2SLogDAO;
	
	@Autowired
	private SendMLISMSService sendMLISMSService;
	

	/**
	 * return hash token based on Consumer data
	 */
	@Transactional
	@Override
	public ResponseModel<ConsumerResponse> getToken(ConsumerDataModel consumerDataModel) {
		ResponseModel<ConsumerResponse> responseModel = new ResponseModel<ConsumerResponse>();
		try {
			PaymentEntity paymentEntity = new PaymentEntity();
			String txnId = getTxnId();
			ConsumerResponse consumerResponse = new ConsumerResponse();
		    String hashSequence = "merchantId|txnId|totalamount|accountNo|consumerId|consumerMobileNo|consumerEmailId|"
								+ "debitStartDate|debitEndDate|maxAmount|amountType|frequency|"
								+ "cardNumber|expMonth|expYear|cvvCode|SALT";
			hashSequence = hashSequence.replace("merchantId", marchantID);
			hashSequence = hashSequence.replace("txnId", txnId);
			hashSequence = hashSequence.replace("totalamount", consumerDataModel.getTotalamount());
			hashSequence = hashSequence.replace("accountNo", consumerDataModel.getAccountNo());
			hashSequence = hashSequence.replace("consumerId", consumerDataModel.getConsumerId());
			hashSequence = hashSequence.replace("consumerMobileNo", consumerDataModel.getConsumerMobileNo());
			hashSequence = hashSequence.replace("consumerEmailId", consumerDataModel.getConsumerEmailId());
			hashSequence = hashSequence.replace("debitStartDate", consumerDataModel.getDebitStartDate());
			hashSequence = hashSequence.replace("debitEndDate", consumerDataModel.getDebitEndDate());
			hashSequence = hashSequence.replace("maxAmount", consumerDataModel.getMaxAmount());
			hashSequence = hashSequence.replace("amountType", consumerDataModel.getAmountType());
			hashSequence = hashSequence.replace("frequency", consumerDataModel.getFrequency());
			hashSequence = hashSequence.replace("cardNumber", consumerDataModel.getCardNumber());
			hashSequence = hashSequence.replace("expMonth", consumerDataModel.getExpMonth());
			hashSequence = hashSequence.replace("expYear", consumerDataModel.getExpYear());
			hashSequence = hashSequence.replace("cvvCode", consumerDataModel.getCvvCode());
			hashSequence = hashSequence.replace("SALT", salt);
			consumerResponse.setItemId(itemID);
			consumerResponse.setMarchantId(marchantID);
			consumerResponse.setSalt(salt);
			consumerResponse.setTxnId(txnId);
			consumerResponse.setToken(hashCal("SHA-512" , hashSequence));;
			consumerResponse.setReturnUrl(returnUrl);
			if(!StringUtils.isEmpty(consumerDataModel.getProposalNumber())) {
			   CreditCardCustomerEntity creditCardCustomerEntity = creditCardCustomerDAO.findByProposalNumber(consumerDataModel.getProposalNumber());
			   if(!ObjectUtils.isEmpty(creditCardCustomerEntity)) {
				   paymentEntity.setCreditCardCustomerId(creditCardCustomerEntity);
				   paymentEntity.setTxnId(txnId);
				   paymentEntity.setAmount(Double.parseDouble(consumerDataModel.getTotalamount()));
				   paymentEntity.setEmail(consumerDataModel.getConsumerEmailId());
				   paymentEntity.setPhone(consumerDataModel.getConsumerMobileNo());
				   paymentEntity.setPaymentStatus("INITIATED");
				   paymentEntity.setPaymentOn(DateUtil.toCurrentUTCTimeStamp());
				   paymentEntity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
				   paymentEntity.setCreatedBy(creditCardCustomerEntity.getFirstName()+" "+creditCardCustomerEntity.getLastName());
				   paymentDAO.saveOrUpdate(paymentEntity);
			   }
			   logger.info("-------Token------- : "+consumerResponse.getToken());
			}
			responseModel.setStatus(Constant.SUCCESS);
			responseModel.setMessage(MLIMessageConstants.PAYMENT_TOKEN_MSG);
			responseModel.setData(consumerResponse);
			return responseModel;
		}catch (Exception e) {
			logger.error(":::::::::::::::: Error in getting hash token  :::::::::::::" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param type
	 * @param str
	 * @return hash string based on gevin hashing method and string
	 * @author rajkumar
	 */
	 public String hashCal(String type, String str) {
	        byte[] hashseq = str.getBytes();
	        StringBuffer hexString = new StringBuffer();
	        try {
	            MessageDigest algorithm = MessageDigest.getInstance(type);
	            algorithm.reset();
	            algorithm.update(hashseq);
	            byte messageDigest[] = algorithm.digest();
	            for (int i = 0; i < messageDigest.length; i++) {
	                String hex = Integer.toHexString(0xFF & messageDigest[i]);
	                if (hex.length() == 1) {
	                    hexString.append("0");
	                }
	                hexString.append(hex);
	            }

	        } catch (NoSuchAlgorithmException nsae) {
	        	logger.error(":::::::::::::::: Error in getting hash  :::::::::::::" + nsae.getMessage());
	        }
	        return hexString.toString();
	    }


	 /**
	  * save payment response into DB
	  */
	@Transactional
	@Override
	public RedirectView savePaymentResponse(String msg) {
		logger.info("****** Response Msg ***** :"+msg);
		RedirectView redirectView = new RedirectView();
		try {
			if(!StringUtils.isEmpty(msg)) {
				String[] response = msg.split("\\|");
				String txnId = response[3];
				if(!StringUtils.isEmpty(txnId)) {
					String encryptedTxn = AES.encrypt(txnId, aesSecratKey);
					PaymentEntity paymentEntity = paymentDAO.findByTxnId(txnId);
					if(!ObjectUtils.isEmpty(paymentEntity)) {
						CreditCardCustomerEntity creditCardCustomerEntity = paymentEntity.getCreditCardCustomerId();
						PaymentStatus statusEnum = PaymentStatus.getPaymentStatus(response[0]);
						String status = statusEnum == null ? response[1].toUpperCase() : statusEnum.getLabel() ;
						paymentEntity.setTxnDate(response[8]);
						paymentEntity.setBankCode(response[4]);
						if(response[0].equals("0300") || response[0].equals("0400")) {
						  paymentEntity.setPaymentStatus(status);
						  redirectView.setUrl(successPage+"/"+encryptedTxn);
						  if(!ObjectUtils.isEmpty(creditCardCustomerEntity)) {
							  creditCardCustomerEntity.setPaymentStatus(Status.PAYMENT_SUCCESS);
							  creditCardCustomerEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
							  creditCardCustomerDAO.saveOrUpdate(creditCardCustomerEntity);
						  }
						}else {
						  paymentEntity.setPaymentStatus(status);
						  redirectView.setUrl(errorPage+"/"+encryptedTxn);
						  if(!ObjectUtils.isEmpty(creditCardCustomerEntity)) {
							  creditCardCustomerEntity.setPaymentStatus(Status.PAYMENT_FAILED);
							  creditCardCustomerEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
							  creditCardCustomerDAO.saveOrUpdate(creditCardCustomerEntity);
							  sendPaymentFailureSms(creditCardCustomerEntity);
						  }
						}
						paymentEntity.setAliasName(response[11]);
						paymentEntity.setBankTxnId(response[12]);
						paymentEntity.setRequestToken(response[14]);
						paymentDAO.saveOrUpdate(paymentEntity);
					}
				}
			}
			return redirectView;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return redirectView;
	}
	
	/**
	 * return transaction id
	 * @return
	 * @author rajkumar
	 */
	private String getTxnId() {
		Random rand = new Random();
		String randomId = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
        String txnId = "YBLCC" + hashCal("SHA-256", randomId).substring(0, 12);
        return txnId.toUpperCase();
	}


	/**
	 * GET payment detail based on Transaction ID
	 */
	@Transactional
	@Override
	public ResponseModel<PaymentResponse> getPaymentStatus(String txnId) {
		ResponseModel<PaymentResponse> response = new ResponseModel<PaymentResponse>();
		try {
			PaymentResponse paymentResponse = new PaymentResponse();
			if (!StringUtils.isEmpty(txnId)) {
				String decryptTxn = AES.decrypt(txnId, aesSecratKey);
				PaymentEntity paymentEntity = paymentDAO.findByTxnId(decryptTxn);
				if (!ObjectUtils.isEmpty(paymentEntity)) {
					CreditCardCustomerEntity creditCardCustomerEntity = paymentEntity.getCreditCardCustomerId();
					if(!ObjectUtils.isEmpty(creditCardCustomerEntity) && "No".equals(creditCardCustomerEntity.getPdfSent()) && Constant.PAYMENT_SUCCESS.equals(creditCardCustomerEntity.getPaymentStatus().toString())) {
						creditCardJourneyService.generatePDF(creditCardCustomerEntity);
						creditCardCustomerEntity.setPdfSent("Yes");
						creditCardCustomerEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
						creditCardCustomerDAO.saveOrUpdate(creditCardCustomerEntity);
						logger.info("**************PDF sent set to Yes*****************");
					}
					paymentResponse = modelMapper.map(paymentEntity, PaymentResponse.class);
					if(!ObjectUtils.isEmpty(creditCardCustomerEntity)) {
						String encryptedString = AES.encrypt(creditCardCustomerEntity.getPhone() + "_" + creditCardCustomerEntity.getProposalNumber(),aesSecratKey);
						String redirectPaymentURL = "" + custScreenRdirectUrl + "/cs/yblcc/" + encryptedString;
						paymentResponse.setRedirectUrl(redirectPaymentURL);
					}
					response.setStatus(Constant.SUCCESS);
					response.setMessage(MLIMessageConstants.PAYMENT_SUCCESS_MSG);
					response.setData(paymentResponse);
					return response;
				}
			}
			response.setStatus(Constant.FAILURE);
			response.setMessage(MLIMessageConstants.PAYMENT_SUCCESS_MSG);
			response.setData(paymentResponse);
			return response;
		}catch (Exception e) {
			logger.error(":::::::::::::::: Error in getting payment response  :::::::::::::" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}


	/**\
	 * get server to server response and send Acknowledgement to the Payment getway either 0 or 1
	 */
	@Transactional
	@Override
	public String getS2SResponse(String msg) {
		logger.info("-------S2SResponse------- : " + msg);
		String statusCode = "1";
		String[] response = msg.split("\\|");
		try {
			String txnId = response[3];
			if (!StringUtils.isEmpty(txnId)) {
				PaymentEntity paymentEntity = paymentDAO.findByTxnId(txnId);
				if (!ObjectUtils.isEmpty(paymentEntity)) {
					CreditCardCustomerEntity creditCardCustomerEntity = paymentEntity.getCreditCardCustomerId();
					PaymentStatus statusEnum = PaymentStatus.getPaymentStatus(response[0]);
					String status = statusEnum == null ? response[1].toUpperCase() : statusEnum.getLabel();
					// case 1 when browser response comming successfully
					if (!ObjectUtils.isEmpty(creditCardCustomerEntity)) {
						paymentEntity.setAliasName(response[11]);
						paymentEntity.setBankTxnId(response[12]);
						if (!"INITIATED".equals(paymentEntity.getPaymentStatus())) {
							if (response[0].equals("0300") || response[0].equals("0400")) {
								paymentEntity.setPaymentStatus(status);
								creditCardCustomerEntity.setPaymentStatus(Status.PAYMENT_SUCCESS);
							} else {
								paymentEntity.setPaymentStatus(status);
								creditCardCustomerEntity.setPaymentStatus(Status.PAYMENT_FAILED);
								sendPaymentFailureSms(creditCardCustomerEntity);
							}
							creditCardCustomerEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
							creditCardCustomerDAO.saveOrUpdate(creditCardCustomerEntity);
							statusCode = "1";
						} else {
							// case 2 when browser response not comming successfully
							paymentEntity.setTxnDate(response[8]);
							paymentEntity.setBankCode(response[4]);
							paymentEntity.setRequestToken(response[14]);
							if (response[0].equals("0300") || response[0].equals("0400")) {
								paymentEntity.setPaymentStatus(status);
								creditCardCustomerEntity.setPaymentStatus(Status.PAYMENT_SUCCESS);
							} else {
								paymentEntity.setPaymentStatus(status);
								creditCardCustomerEntity.setPaymentStatus(Status.PAYMENT_FAILED);
								sendPaymentFailureSms(creditCardCustomerEntity);
							}
							creditCardCustomerEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
							creditCardCustomerDAO.saveOrUpdate(creditCardCustomerEntity);
							statusCode = "1";
						}
						paymentDAO.saveOrUpdate(paymentEntity);
					}
				}
			}
			PaymentS2SLog paymentS2SLog = new PaymentS2SLog();
			paymentS2SLog.setResponse(msg);
			paymentS2SLogDAO.save(paymentS2SLog);
		} catch (Exception e) {
			statusCode = "0";
			logger.error(":::::::::::::::: Error in getting payment s2s response  :::::::::::::" + e.getMessage());
			e.printStackTrace();
		}
		return response[3] + "|" + response[5] + "|" + statusCode;
	}


	/**
	 * get payment log
	 */
	@Transactional
	@Override
	public ResponseModel<List<PaymentS2SLog>> getPaymentLog() {
		ResponseModel<List<PaymentS2SLog>> response = new ResponseModel<List<PaymentS2SLog>>();
		try {
			List<PaymentS2SLog> list = paymentS2SLogDAO.getAll();
			response.setStatus(Constant.SUCCESS);
			response.setMessage(MLIMessageConstants.PAYMENT_TOKEN_MSG);
			response.setData(list);
			return response;
		} catch (Exception e) {
			logger.error(":::::::::::::::: Error in getting payment s2s response  :::::::::::::" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * method is used to send payment failure sms
	 * @param creditCardCustomerEntity
	 * @author rajkumar
	 */
	private void sendPaymentFailureSms(CreditCardCustomerEntity creditCardCustomerEntity) {
		try {
			SMSResponseModel smsResponse = sendMLISMSService.sendSmsToYBLCCCustomer(null,creditCardCustomerEntity.getPhone(),OTPType.YBLCC_PAYMENT_FAIL,creditCardCustomerEntity);
			if (!("OK").equalsIgnoreCase(smsResponse.getMliSmsServiceResponse().getResponseHeader().getGeneralResponse().getStatus())) {
			   logger.error("::::::::::::::::: SMS not Send to Customer :::::::::::::::Mob="+ creditCardCustomerEntity.getPhone() + " at time : " + (new Date())); 
			} else{ 
			   logger.info("::::::::::::::::: SMS Send to Customer :::::::::::::::Mob=" +creditCardCustomerEntity.getPhone() + " at time : " + (new Date())); 
			}
		}catch (Exception e) {
			logger.error(":::::::::::::::: Error in getting payment failure sms  :::::::::::::" + e.getMessage());
			e.printStackTrace();
		}
	}
}
