package com.mli.service;

import java.util.List;

import org.springframework.web.servlet.view.RedirectView;

import com.mli.entity.PaymentS2SLog;
import com.mli.model.ConsumerDataModel;
import com.mli.model.ConsumerResponse;
import com.mli.model.PaymentResponse;
import com.mli.model.response.ResponseModel;

public interface PaymentService {

	ResponseModel<ConsumerResponse> getToken(ConsumerDataModel consumerDataModel);

	RedirectView savePaymentResponse(String msg);

	ResponseModel<PaymentResponse> getPaymentStatus(String txnId);

	String getS2SResponse(String msg);

	ResponseModel<List<PaymentS2SLog>> getPaymentLog();

}
