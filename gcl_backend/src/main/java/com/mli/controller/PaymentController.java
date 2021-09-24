package com.mli.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.mli.constants.Constant;
import com.mli.model.ConsumerDataModel;
import com.mli.service.PaymentService;
import com.mli.utils.CustomResponse;

@RestController
public class PaymentController {

	private static final Logger logger = Logger.getLogger(PaymentController.class);
	
	@Autowired
	private PaymentService paymentService;
	
	/**
	 * 
	 * @param consumerDataModel
	 * @return
	 * @author rajkumar
	 */
	@PostMapping("/payment/token")
	public ResponseEntity<?> getTokenHash(@Valid @RequestBody ConsumerDataModel consumerDataModel) {
		return ResponseEntity.status(HttpStatus.OK).body(paymentService.getToken(consumerDataModel));
	}
	
	/**
	 * It is techprocess return url which return msg param as a response 
	 * @param msg
	 * @return
	 */
	@PostMapping("/payment/response")
	public RedirectView getResponse(@RequestParam("msg") String msg) {
	    return paymentService.savePaymentResponse(msg);
	}
	
	/**
	 * api is used to get payment status
	 * @param txnId
	 * @return
	 * @author rajkumar
	 */
	@GetMapping("/payment/status")
	public ResponseEntity<?> getPaymentStatus(@RequestParam(name="txnId" , required = true) String txnId) {
		return ResponseEntity.status(HttpStatus.OK).body(paymentService.getPaymentStatus(txnId));
	}
	
	/**
	 * api to get response from techprocess
	 * @param msg
	 * @return
	 * @author rajkumar
	 */
	@PostMapping("/payment/servertoserver")
	public ResponseEntity<?> servertoserver(@RequestParam("msg") String msg) {
		logger.info("-------S2SResponse------- : "+msg);
	    return ResponseEntity.status(HttpStatus.OK).body(paymentService.getS2SResponse(msg));
	}
	
	
	/**
	 * get server to server payament logs
	 * @return
	 * @author rajkumar
	 */
	@GetMapping("/payment/logs")
	public ResponseEntity<?> getPaymentStatus() {
		return ResponseEntity.status(HttpStatus.OK).body(paymentService.getPaymentLog());
	}
}
