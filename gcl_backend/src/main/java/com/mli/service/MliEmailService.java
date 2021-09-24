package com.mli.service;

import java.io.IOException;

import com.mli.modal.email.EmailModel;
import com.mli.modal.email.MliEmailServiceModel;
import com.mli.model.response.ResponseModel;

public interface MliEmailService {

	ResponseModel<MliEmailServiceModel> sendEmail(EmailModel mliEmailModel) throws IOException, Exception;

}
