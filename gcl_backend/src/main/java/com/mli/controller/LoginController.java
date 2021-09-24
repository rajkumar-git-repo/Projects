package com.mli.controller;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mli.constants.MLIMessageConstants;
import com.mli.dao.UserDAO;
import com.mli.entity.UserEntity;
import com.mli.enums.UserType;
import com.mli.model.CustomUserDetail;
import com.mli.model.LoginModel;
import com.mli.model.TokenModel;
import com.mli.model.UserLoginModel;
import com.mli.model.response.ResponseModel;
import com.mli.model.response.SaveUpdateResponse;
import com.mli.security.JwtTokenUtil;
import com.mli.service.ReportEmailToBankService;
import com.mli.service.SellerService;
import com.mli.service.ValidateTokenService;
import com.mli.utils.DateUtil;
import com.mli.utils.ObjectsUtil;
import com.mli.utils.aes.AESService;

import javassist.NotFoundException;

/**
 * @author Nikhilesh.Tiwari Mobile verification, OTP generation, OTP verify,
 *         login from admin panel and seller site, token refreshing and draft
 *         count API. Seller can login via user id and password or by OTP.
 *
 */
@RestController
public class LoginController {

	@Value("${jwt.claim.secret}")
	private String claimSecret;

	@Autowired
	private SellerService sellerService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private AESService aesService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private ValidateTokenService validateTokenService;

	@Value("${jwt.expiration}")
	private Long expiration;
	
	@Autowired
	private ReportEmailToBankService reportEmailToBankService;

	private static final Logger logger = Logger.getLogger(LoginController.class);

	/**
	 * verifying seller mobile before proceeding generating OTP.
	 */
	@RequestMapping(value = "/mobileverify", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> mobileVerify(@RequestBody LoginModel login) throws Exception {
		try {
			ResponseModel<UserLoginModel> loginResponse = new ResponseModel<>();
			Boolean status = sellerService.isSellerActive(Long.valueOf(login.getContactNo()));
			if (status == null) {
				loginResponse.setStatus(SaveUpdateResponse.FAILURE);
				loginResponse.setMessage(MLIMessageConstants.SELLER_NOT_EXIST);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(loginResponse);
			} else if (!status) {
				logger.error("::Mobile verify FAILED for USER because of inactive status :" + login.getContactNo());
				loginResponse.setStatus(SaveUpdateResponse.FAILURE);
				loginResponse.setMessage(MLIMessageConstants.INACTIVE_STATUS);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(loginResponse);
			}
			Long contNo = Long.valueOf(login.getContactNo());
			loginResponse = sellerService.sellerLogin(contNo);
			return ResponseEntity.ok(loginResponse);
		} catch (NotFoundException e) {
			logger.error("::::::::::::::::::  Error in /login/conNo API :::::::::::::::" + e);
			SaveUpdateResponse response = new SaveUpdateResponse();
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(MLIMessageConstants.EXCEPTION_MSG);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}
	}

	/**
	 * Generating OTP for seller mobile and also for customer mobile verification.
	 */
	@RequestMapping(value = "/otp/generate", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> otpGenerate(@RequestBody LoginModel login,
			@RequestParam(name = "userType", required = true) String userType,
			@RequestParam(name = "regenerateOtp", required = true) Boolean regenerateOtp,
			@RequestParam(name = "proposalNumber", required = false) String proposalNumber,
			@RequestParam(name = "trigger-screen", required = false) String triggerScreen) throws Exception {

		try {
			Long contNo = Long.valueOf(login.getContactNo());
			ResponseModel<UserLoginModel> generateOTPResponse = sellerService.generateOTP(contNo, userType,
					regenerateOtp, proposalNumber, triggerScreen);
			return ResponseEntity.ok(generateOTPResponse);
		} catch (Exception e) {
			logger.error("::::::::::::::::::  Error in /otp/generate API :::::::::::::::" + e);
			SaveUpdateResponse response = new SaveUpdateResponse();
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(MLIMessageConstants.EXCEPTION_MSG);
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}
	}

	/**
	 * Verifying OTP for seller mobile and also for customer mobile.
	 */
	@RequestMapping(value = "/otp/verify", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> verifyLoginOPT(HttpServletRequest request ,@RequestBody LoginModel login,
			@RequestParam(name = "otp", required = true) String otp,
			@RequestParam(name = "userType", required = true) String userType,
			@RequestParam(name = "proposalNumber", required = false) String proposalNumber,
			@RequestParam(name = "trigger-screen", required = false) String triggerScreen) throws Exception {
		try {
			Integer decOtp = null;
			String decUserType = null;
			String decProposalNumber = null;
			String decTriggerScreen = null;
			if(!ObjectUtils.isEmpty(otp)) {
				decOtp = Integer.parseInt(aesService.decryptData(java.net.URLDecoder.decode(otp, StandardCharsets.UTF_8.name())));
	    	}
			if(!StringUtils.isEmpty(userType)) {
				decUserType = aesService.decryptData(java.net.URLDecoder.decode(userType, StandardCharsets.UTF_8.name()));
	    	}
			if(!StringUtils.isEmpty(proposalNumber)) {
				decProposalNumber = aesService.decryptData(java.net.URLDecoder.decode(proposalNumber, StandardCharsets.UTF_8.name()));
	    	}
			if(!StringUtils.isEmpty(triggerScreen)) {
				decTriggerScreen = aesService.decryptData(java.net.URLDecoder.decode(triggerScreen, StandardCharsets.UTF_8.name()));
	    	}

			if (!ObjectsUtil.isNull(login.getContactNo())) {
				Long contNo = Long.valueOf(login.getContactNo());
				ResponseModel<UserLoginModel> verifiedOTPResponse = sellerService.verifyLoginOPT(contNo, decOtp, decUserType,
						decProposalNumber, decTriggerScreen);
				if(!ObjectUtils.isEmpty(verifiedOTPResponse) && !ObjectUtils.isEmpty(verifiedOTPResponse.getData()) && !ObjectUtils.isEmpty(verifiedOTPResponse.getData().getTokenDetails())) {
					if(!StringUtils.isEmpty(verifiedOTPResponse.getData().getTokenDetails().getToken())) {
						validateTokenService.saveToken(jwtTokenUtil.getUsernameFromToken(verifiedOTPResponse.getData().getTokenDetails().getToken()), verifiedOTPResponse.getData().getTokenDetails().getToken(), request.getRemoteAddr());
					}
				}
				// last login update of seller
				if (verifiedOTPResponse.getData() != null) {
					if (!jwtTokenUtil.getUserRoleFromToken(verifiedOTPResponse.getData().getTokenDetails().getToken()).contains("ROLE_ADMIN")) {
						sellerService.updateSellerLastLogIn(contNo);
					}
				}
				logger.info("::Login SUCCESS for USER :" + contNo + " by OTP");
				return ResponseEntity.ok(verifiedOTPResponse);
			} else {
				SaveUpdateResponse response = new SaveUpdateResponse();
				response.setStatus(SaveUpdateResponse.FAILURE);
				response.setMessage(MLIMessageConstants.CON_NO);
				return ResponseEntity.ok(response);
			}

		} catch (Exception e) {
			logger.error("::::::::::::::::::  Error in /otp/verify API :::::::::::::::" + e);
			SaveUpdateResponse response = new SaveUpdateResponse();
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(MLIMessageConstants.EXCEPTION_MSG);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}
	}

	/**
	 * @param loginModel
	 * @throws AuthenticationException Login from seller site by user id and
	 *                                 password. For seller user id is mobile
	 *                                 number. Generating JWT token for session
	 *                                 management at FE.
	 */
	@Transactional
	@RequestMapping(value = "login", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> createAuthenticationToken(HttpServletRequest request ,@RequestBody LoginModel loginModel)
			throws AuthenticationException {
		ResponseModel<TokenModel> responseModel = new ResponseModel<TokenModel>();
		String encrptUserName = aesService.decryptData(loginModel.getUsername());
		String encrptPass = aesService.decryptData(loginModel.getPassword());
		UserEntity userEntity = userDAO.findByUserName(encrptUserName);
		try {
			Boolean status = sellerService.isSellerActive(Long.parseLong(encrptUserName));
			if (status == null) {
				logger.error("::LOGIN FAILED-USER NOT EXIST:" + loginModel.getUsername());
				responseModel.setStatus(SaveUpdateResponse.FAILURE);
				responseModel.setMessage(MLIMessageConstants.INVALID_USERNAME_PASSWORD);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseModel);
			}
			if (!status) {
				logger.error("::Login FAILED for USER because of inactive status :" + loginModel.getUsername());
				responseModel.setStatus(SaveUpdateResponse.FAILURE);
				responseModel.setMessage(MLIMessageConstants.INACTIVE_STATUS);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseModel);
			}
			// Perform the security
			if(!ObjectUtils.isEmpty(userEntity) && userEntity.getLockTimestamp() != null && DateUtil.getDiffHourMinSecond(userEntity.getLockTimestamp()).get("hour") >=1) {
				userEntity.setLockTimestamp(null);
				userEntity.setLocked(false);
			    userEntity.setLockCount(0);
			    userEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
			    userDAO.saveOrUpdate(userEntity);
			    logger.info("ACCOUNT IS UNLOCKED::"+encrptUserName);
			}

			if (!ObjectUtils.isEmpty(userEntity) && !userEntity.isLocked()) {
				final Authentication authentication = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(encrptUserName, encrptPass));
				SecurityContextHolder.getContext().setAuthentication(authentication);

				// Reload password post-security so we can generate token
				final CustomUserDetail userDetails = (CustomUserDetail) userDetailsService
						.loadUserByUsername(encrptUserName);
				// last login update of seller
				final String token = jwtTokenUtil.generateToken(userDetails);
				validateTokenService.saveToken(encrptUserName, token, request.getRemoteAddr());
				// last login update of seller
				if (!jwtTokenUtil.getUserRoleFromToken(token).contains("ROLE_ADMIN")) {
					sellerService.updateSellerLastLogIn(Long.parseLong(encrptUserName));
				}
				responseModel.setMessage(MLIMessageConstants.LOGIN_SUCCESS);
				responseModel.setStatus(SaveUpdateResponse.SUCCESS);
				responseModel.setData(new TokenModel(token, userDetails.getUserRoles(), expiration));
				logger.info("::Login SUCCESS for USER:" + encrptUserName);
				userEntity.setLockTimestamp(null);
				userEntity.setLocked(false);
			    userEntity.setLockCount(0);
			    userEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
			    userDAO.saveOrUpdate(userEntity);
				return ResponseEntity.ok(responseModel);
			} else {
				HashMap<String, Long> map = new HashMap<String, Long>();
				if(!ObjectUtils.isEmpty(userEntity)) {
					map = DateUtil.getDiffHourMinSecond(userEntity.getLockTimestamp());
				}
				responseModel.setMessage("Your account is locked : Try after "+(new Long(59)-map.get("minute"))+" min."+(new Long(59)-map.get("second"))+" sec.");
				responseModel.setStatus(SaveUpdateResponse.FAILURE);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseModel);
			}
		}catch (BadCredentialsException exception) {
			String msg = "";
			try {
				if (!ObjectUtils.isEmpty(userEntity)) {
					if (userEntity.getLockCount() == 4) {
						userEntity.setLockTimestamp(DateUtil.toCurrentUTCTimeStamp());
						userEntity.setLocked(true);
						logger.info("ACCOUNT IS LOCKED::"+encrptUserName);
					}
					userEntity.setLockCount(userEntity.getLockCount() + 1);
					userEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
					userDAO.saveOrUpdate(userEntity);
					if(userEntity.isLocked()) {
						msg = "Your account is locked for 60 minutes";
					}else {
						msg = "You have "+(5-userEntity.getLockCount())+" attempts before your account is locked for 60 minutes";
					}
				}
			}catch (Exception exp) {
				logger.error("::BAD CREDENTIAL FOR USER::" + encrptUserName + "::::::" + exp);
			}
			logger.error("::BAD CREDENTIAL FOR USER::" + encrptUserName + "::::::" + exception);
			responseModel.setStatus(SaveUpdateResponse.FAILURE);
			responseModel.setMessage(MLIMessageConstants.INVALID_USERNAME_PASSWORD+". "+msg);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseModel);
		}
		catch (Exception e) {
			logger.error("::Login FAILED for USER:" + encrptUserName + "::::::" + e);
			responseModel.setStatus(SaveUpdateResponse.FAILURE);
			responseModel.setMessage(MLIMessageConstants.INVALID_USERNAME_PASSWORD);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseModel);

		}
	}

	/**
	 * @param loginModel
	 * @throws AuthenticationException Login from admin panel. Only user having role
	 *                                 ADMIN can login via this method. Generating
	 *                                 JWT token for session management at FE.
	 */
	@RequestMapping(value = "login/admin", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> adminLogin(HttpServletRequest request, @RequestBody LoginModel loginModel) throws AuthenticationException {
		ResponseModel<TokenModel> responseModel = new ResponseModel<TokenModel>();
		String encrptUserName = null;
		try {
			/* decrypt user credential */
			encrptUserName = aesService.decryptData(loginModel.getUsername());
			String encrptPass = aesService.decryptData(loginModel.getPassword());

			// Perform the security
			final Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(encrptUserName, encrptPass));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			// Reload password post-security so we can generate token
			final CustomUserDetail userDetails = (CustomUserDetail) userDetailsService
					.loadUserByUsername(encrptUserName);
			if (userDetails.getUserRoles().contains(UserType.ROLE_ADMIN.toString())) {
				final String token = jwtTokenUtil.generateToken(userDetails);
				validateTokenService.saveToken(encrptUserName, token, request.getRemoteAddr());
				responseModel.setMessage(MLIMessageConstants.LOGIN_SUCCESS);
				responseModel.setStatus(SaveUpdateResponse.SUCCESS);
				responseModel.setData(new TokenModel(token, userDetails.getUserRoles(), expiration));
				logger.info("::Admin Login SUCCESS for USER:" + encrptUserName);
				return ResponseEntity.ok(responseModel);
			} else {
				logger.info("::Admin login atempt FAILED for user:" + encrptUserName);
				responseModel.setStatus(SaveUpdateResponse.FAILURE);
				responseModel.setMessage("Sorry! You do not have rights to access admin portal.");
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseModel);
			}
		}

		catch (Exception e) {
			logger.error("::::::::ADMIN login FAILED for user :" + encrptUserName + "::::::::::::" + e);
			responseModel.setStatus(SaveUpdateResponse.FAILURE);
			responseModel.setMessage(MLIMessageConstants.INVALID_USER_NAME_OR_PASSWORD);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseModel);

		}
	}

	/**
	 * Token refreshing.
	 */
	@RequestMapping(value = "/refreshtoken", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) {
		SaveUpdateResponse response = new SaveUpdateResponse();
		response.setStatus(SaveUpdateResponse.SUCCESS);
		response.setMessage(MLIMessageConstants.TOKEN_REFRESHED);
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}
	
	/**
	 * Check Mail Api.
	 */
	@RequestMapping(value = "/checkAutoMailer", method = RequestMethod.GET)
	public ResponseEntity<?> checkAutoMailer(@RequestHeader("Authorization") String token) {
		reportEmailToBankService.sendExcelToBank();
		return ResponseEntity.status(HttpStatus.OK).body("working fine");

	}
	
	/**
	 * @param loginModel
	 * this method is update the lastLogin time of user
	 */
	
	
	@RequestMapping(value = "/signout", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> logOut(HttpServletRequest request ,@RequestHeader("Authorization") String token,
			@RequestParam(name = "userName", required = true) String userName) {
		SaveUpdateResponse response = new SaveUpdateResponse();
		String decUserName = null;
		try {
			if (!StringUtils.isEmpty(userName)) {
				decUserName = aesService.decryptData(java.net.URLDecoder.decode(userName, StandardCharsets.UTF_8.name()));
				validateTokenService.updateToken(decUserName, request.getRemoteAddr());
				response.setStatus(SaveUpdateResponse.SUCCESS);
				response.setMessage(MLIMessageConstants.USER_LOGOUT);
				logger.info("::logout SUCCESS for USER:" + decUserName);
			} else {
				response.setStatus(SaveUpdateResponse.FAILURE);
				response.setMessage(MLIMessageConstants.INVALID_USER_TYPE);
			}
			return ResponseEntity.ok(response);
		}
		catch (Exception e) {
			logger.error("::logout FAILED for USER:" + decUserName + "::::::" + e);
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(MLIMessageConstants.INVALID_USER_TYPE);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

		}
	
	}

}
