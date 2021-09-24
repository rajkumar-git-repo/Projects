package com.mli.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mli.constants.Constant;
import com.mli.model.RQModel;
import com.mli.model.RQTypeModel;
import com.mli.security.JwtTokenUtil;
import com.mli.service.CustomerDetailService;
import com.mli.service.ReflexiveQuestionService;
import com.mli.utils.CustomResponse;

/**
 * 
 * @author Devendra.Kumar
 *
 */
@RequestMapping("/rq")
@RestController
public class MasterQuestionController {

	@Autowired
	private ReflexiveQuestionService service;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private CustomerDetailService customerDetailService;

	/**
	 * Admin is saved Reflexive Question
	 * 
	 * @author Devendra.Kumar
	 * @param token
	 * @param premiumCalculator
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<?> saveQuestion(@RequestHeader("Authorization") String token,
			@RequestBody RQModel reflexiveQuestion) {
		return (!jwtTokenUtil.isTokenExpired(token))
				? ResponseEntity.status(HttpStatus.OK)
						.body(new CustomResponse(Constant.SUCCESS, "Reflexive Question saved Service",
								service.save(reflexiveQuestion)))
				: ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new CustomResponse(Constant.FAILURE, Constant.UNAUTHORIZED_REQUEST));
	}

	/**
	 * Save type and subType
	 * 		type : NATIONALITY
	 * 		subType : INDAIN / NRI / OTHER
	 * 
	 * @param token
	 * @param rqTypeModel
	 * @return
	 */
	@PostMapping("/type")
	public ResponseEntity<?> saveQuestionTypes(@RequestHeader("Authorization") String token,
			@RequestBody RQTypeModel rqTypeModel) {
		return (!jwtTokenUtil.isTokenExpired(token))
				? ResponseEntity.status(HttpStatus.OK)
						.body(new CustomResponse(Constant.SUCCESS, "Question Type saved Service",
								service.saveQuestionTypes(rqTypeModel)))
				: ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new CustomResponse(Constant.FAILURE, Constant.UNAUTHORIZED_REQUEST));
	}

	/**
	 * Get Type detail
	 * 
	 * @param token
	 * @param typeId
	 * @return
	 */
	@GetMapping("/type")
	public ResponseEntity<?> getQuestionTypes(@RequestHeader(value = "Authorization",required=false) String token,
			@RequestParam(value = "id", required = false) String typeId,@RequestParam(name = "trigger-screen",  required = false) String triggerScreen
			,@RequestParam(name = "data",  required = false) String data) {
		if (Constant.CS.equalsIgnoreCase(triggerScreen)) {
			Map<String, Object> result = customerDetailService.getStatus(data);
			if (result != null && result.get(Constant.MOBILE_VERIFIED) != null &&(boolean) result.get(Constant.MOBILE_VERIFIED)) {
				return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(Constant.SUCCESS,
						"Question Type get Service", service.getQuestionTypes(typeId)));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new CustomResponse(Constant.FAILURE, Constant.UNAUTHORIZED_REQUEST));
			}
		} else {
			return (!jwtTokenUtil.isTokenExpired(token))
					? ResponseEntity.status(HttpStatus.OK)
							.body(new CustomResponse(Constant.SUCCESS, "Question Type get Service",
									service.getQuestionTypes(typeId)))
					: ResponseEntity.status(HttpStatus.UNAUTHORIZED)
							.body(new CustomResponse(Constant.FAILURE, Constant.UNAUTHORIZED_REQUEST));
		}
		
	}

	/**
	 * Get Master Question
	 * 
	 * @param token
	 * @param stepNo
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> getAllQuestion(@RequestHeader("Authorization") String token,@RequestParam(value="step",required=false) String stepNo) {
		return (!jwtTokenUtil.isTokenExpired(token))
				? ResponseEntity.status(HttpStatus.OK).body(
						new CustomResponse(Constant.SUCCESS, "Question get Service", service.getAllQuestion(stepNo)))
				: ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new CustomResponse(Constant.FAILURE, Constant.UNAUTHORIZED_REQUEST));
	}
	
	/**
	 * Get Answer given by customer 
	 * 
	 * @param token
	 * @param type	Ex : OCCUPATION / NATIONALITY / DISEASE
	 * @param custId
	 * @return
	 */
	@GetMapping("/answer")
	public ResponseEntity<?> getAllAnswer(@RequestHeader("Authorization") String token,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "customerId", required = false) Long custId,@RequestParam(value = "proposer", required = false) String proposerNo) {
		return (!jwtTokenUtil.isTokenExpired(token))
				? ResponseEntity.status(HttpStatus.OK)
						.body(new CustomResponse(Constant.SUCCESS, "Answer get Service",
								service.getAllAnswer(type, custId,proposerNo)))
				: ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new CustomResponse(Constant.FAILURE, Constant.UNAUTHORIZED_REQUEST));
	}
}
