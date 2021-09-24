package com.mli.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mli.model.response.SaveUpdateResponse;
import com.mli.utils.aes.AESService;

@RestController
@RequestMapping("/api")
public class EncryptionAndDecryptionTest {

	@Autowired
	private AESService aesService;

	/** encrypt password
	 * encryption
	 */
	@GetMapping("/encrytion/{password}")
	public ResponseEntity<?> encryption(@RequestHeader("Authorization") String token,
			@PathVariable(value = "password") String password) {
		SaveUpdateResponse response = new SaveUpdateResponse();
		String str = aesService.encryptData(password);
		response.setStatus(SaveUpdateResponse.SUCCESS);
		response.setMessage(str);
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	/**decrypt password
	 * Description
	 */
	@PostMapping("/description")
	public ResponseEntity<?> description(@RequestBody EnrcyDetails details) {
		SaveUpdateResponse response = new SaveUpdateResponse();
		String str = aesService.decryptData(details.getPassword());
		response.setStatus(SaveUpdateResponse.SUCCESS);
		response.setMessage(str);
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}
	
}
