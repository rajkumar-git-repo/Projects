package com.mli.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mli.modal.email.EmailModel;
import com.mli.modal.email.GeneralConsumerInformationModel;
import com.mli.modal.email.MailRequestBodyModel;
import com.mli.modal.email.MliEmailServiceModel;
import com.mli.modal.email.RequestHeaderModel;

@Service
public class EmailDetailHelper implements BaseConverter<EmailModel, MliEmailServiceModel> {

	private static final Logger logger = LoggerFactory.getLogger(EmailDetailHelper.class);

	@Value("#{'${mli.email.messageversion}'}")
	private String EmailMessageversion;
	
	
	@Value("#{'${mli.email.appId}'}")
	private String EmailAppId;
	
	
//	@Value("#{'${mli.email.correlationId}'}")
//	private String EmailCorrelationId;
	
	@Override
	public MliEmailServiceModel convertToModel(EmailModel source) {

		MliEmailServiceModel MliEmailService = new MliEmailServiceModel();

		GeneralConsumerInformationModel generalConsumerInformation = new GeneralConsumerInformationModel();
		RequestHeaderModel requestHeader = new RequestHeaderModel();
		MailRequestBodyModel requestBody = new MailRequestBodyModel();

		List<String> embeddedAttachments = new ArrayList<>();
		List<String> reqParam1 = new ArrayList<>();

		// Plz don't delete
		// requestBody.setReqParam2(null);
		// requestBody.setReqParam3(null);
		// requestBody.setAppId(null);
		// requestBody.setAppName(null);
		// requestBody.setUserType(null);
		// requestBody.setUserName(null);
		// requestBody.setAuthenticationToken(null);
		// requestBody.setChannel(null);
		// requestBody.setTo(null);
		// requestBody.setRequestTimestamp(null);
		
		requestBody.setMailIdCc(source.getMailIdCc() != null ? source.getMailIdCc() : null);
		requestBody.setMailIdBcc(source.getMailIdBcc() != null ? source.getMailIdBcc() : null);
		requestBody.setAttchDetails(source.getAttchDetails());
		requestBody.setMailIdTo(source.getMailIdTo());
		requestBody.setMailSubject(source.getMailSubject() != null ? source.getMailSubject() : null);
		requestBody.setFromName(source.getFromName() != null ? source.getFromName() : null);
		requestBody.setFromEmail(source.getFromEmail() != null ? source.getFromEmail() : null);
		requestBody.setMailBody(source.getMailBody() != null ? source.getMailBody() : null);
		requestBody.setIsFileAttached("true");
		requestBody.setEmbeddedAttachments(embeddedAttachments);
		requestBody.setIsConsolidate("0");
		requestBody.setIsZip(false);
		requestBody.setReqParam1(reqParam1);
		requestBody.setBodyFormat("html");
		requestBody.setFileAttached(true);
		requestBody.setConsolidate(false);
		requestBody.setPdf(false);
		requestBody.setZip(false);

		generalConsumerInformation.setAppId(EmailAppId);
		generalConsumerInformation.setMessageVersion(EmailMessageversion);
//		generalConsumerInformation.setCorrelationId(EmailCorrelationId);
		generalConsumerInformation.setCorrelationId(UUID.randomUUID().toString());
		logger.info("Email CorrelationId : "+generalConsumerInformation.getCorrelationId());
		requestHeader.setGeneralConsumerInformation(generalConsumerInformation);

		MliEmailService.setRequestBody(requestBody);
		MliEmailService.setRequestHeader(requestHeader);

		return MliEmailService;
	}

	@Override
	public EmailModel convertToEntity(MliEmailServiceModel source) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<MliEmailServiceModel> convertToModelList(Collection<EmailModel> source) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<EmailModel> convertToEntityList(Collection<MliEmailServiceModel> source) {
		// TODO Auto-generated method stub
		return null;
	}

}
