package com.mli.model;

import java.util.ArrayList;
import java.util.List;

import com.mli.modal.email.EmailModel;
import com.mli.service.impl.MliEmailServiceImpl;

public class MaliTest {

	public static void main(String[] args) throws Exception {

		EmailModel mliEmailModel = new EmailModel();
		AttchDetailsModel attchDetail = new AttchDetailsModel();
		List<AttchDetailsModel> attchDetails = new ArrayList<AttchDetailsModel>();
		// byte[] allPdf =
		// Files.readAllBytes(Paths.get("Home/Downloads/sample.pdf"));
		// File file = new File("sample.pdf");
		// String allPdf = new byte[(int) file.length()].toString();

		attchDetail.setBytes("This is s string file !!!");
		attchDetail.setName("pqr");
		attchDetail.setType("pdf");
		attchDetails.add(attchDetail);

		mliEmailModel.setAttchDetails(attchDetails);
		mliEmailModel.setMailIdTo("nikhilesh.tiwari@kelltontech.com");
		mliEmailModel.setFromEmail("sayhellotoamit@xyz.com");
		mliEmailModel.setMailSubject("test Subject");
		mliEmailModel.setMailBody("Hi");

		MliEmailServiceImpl mliEmailServiceImpl = new MliEmailServiceImpl();
		mliEmailServiceImpl.sendEmail(mliEmailModel);
		System.out.println("Done !!!");
	}
}
