package com.mli.filter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

/*this class is specially identified the file signature
 * @parm jpg,gif,jpeg,pdf,png
 * 
 * @purpose to detect the malicious file
 * */

public class FileTypeTest {
	private static final int BUFFER_SIZE = 4096;
	private static final int MAX_SIGNATURE_SIZE = 8;

	// PDF files starts with: %PDF
	// Java does not support byte literals. Use int literals instead.
	private static final int[] pdfSig = { 0x25, 0x50, 0x44, 0x46 };
	private static final int[] gif = { 0x47, 0x49, 0x46, 0x38, 0x37, 0x61 };
	private static final int[] gif1 = { 0x47, 0x49, 0x46, 0x38, 0x39, 0x61 };

	private static final int[] png = { 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A };

	private static final int[] jpg = { 0xFF, 0xD8 };
	private static final int[] jpeg = { 0xFF, 0xD8 };
	private static final int[] xlxs = { 0x50, 0x4B, 0x03, 0x04 };
	private static final int[] xl = { 0xD0, 0xCF, 0x11, 0xE0, 0xA1, 0xB1, 0x1A, 0xE1 };

	private Map<String, int[]> signatureMap;
	private Map<String, int[]> signatureMapForCam;
	private Map<String, int[]> signatureMapForExcel;

	public FileTypeTest() {
		signatureMap = new HashMap<String, int[]>();
		signatureMap.put("PDF", pdfSig);
		signatureMap.put("GIF", gif);
		signatureMap.put("GIF", gif1);
		signatureMap.put("PNG", png);
		signatureMap.put("JPG", jpg);
		signatureMap.put("JPEG", jpeg);
		signatureMap.put("xlxs", xlxs);
		signatureMap.put("xl", xl);
		
		signatureMapForCam = new HashMap<String, int[]>();
		signatureMapForCam.put("PDF", pdfSig);
		signatureMapForCam.put("GIF", gif);
		signatureMapForCam.put("GIF", gif1);
		signatureMapForCam.put("PNG", png);
		signatureMapForCam.put("JPG", jpg);
		signatureMapForCam.put("JPEG", jpeg);
		
		signatureMapForExcel = new HashMap<String, int[]>();
		signatureMapForExcel.put("xlxs", xlxs);
		signatureMapForExcel.put("xl", xl);
	}

	public boolean getFileType(List<File> files) throws IOException {
		String fileType = "UNKNOWN";
		boolean isTrue = false;
		for (File f : files) {
			byte[] buffer = new byte[BUFFER_SIZE];
			InputStream in = new FileInputStream(f);
			try {
				int n = in.read(buffer, 0, BUFFER_SIZE);
				int m = n;
				while ((m < MAX_SIGNATURE_SIZE) && (n > 0)) {
					n = in.read(buffer, m, BUFFER_SIZE - m);
					m += n;
				}

				for (Iterator<String> i = signatureMapForCam.keySet().iterator(); i.hasNext();) {
					String key = i.next();
					if (matchesSignature(signatureMapForCam.get(key), buffer, m)) {
						fileType = key;
						isTrue = true;
						break;
					} else {
						isTrue = false;
					}
				}
				if (isTrue == false) {
					return isTrue;
				}
			} finally {
				in.close();
			}
		}
		return isTrue;
	}

	private static boolean matchesSignature(int[] signature, byte[] buffer, int size) {
		if (size < signature.length) {
			return false;
		}

		boolean b = true;
		for (int i = 0; i < signature.length; i++) {
			if (signature[i] != (0x00ff & buffer[i])) {
				b = false;
				break;
			}
		}

		return b;
	}

	public boolean getFileType(File files) throws IOException {
		String fileType = "UNKNOWN";
		boolean isTrue = false;
		byte[] buffer = new byte[BUFFER_SIZE];
		InputStream in = new FileInputStream(files);
		try {
			int n = in.read(buffer, 0, BUFFER_SIZE);
			int m = n;
			while ((m < MAX_SIGNATURE_SIZE) && (n > 0)) {
				n = in.read(buffer, m, BUFFER_SIZE - m);
				m += n;
			}

			for (Iterator<String> i = signatureMap.keySet().iterator(); i.hasNext();) {
				String key = i.next();
				if (matchesSignature(signatureMap.get(key), buffer, m)) {
					fileType = key;
					isTrue = true;
					break;
				} else {
					isTrue = false;
				}
			}
			if (isTrue == false) {
				return isTrue;
			}
		} finally {
			in.close();
		}
		return isTrue;
	}

	public boolean getFileType(MultipartFile files) throws IOException {
		String fileType = "UNKNOWN";
		boolean isTrue = false;
		byte[] buffer = new byte[BUFFER_SIZE];
		InputStream in = new BufferedInputStream(files.getInputStream());
		try {
			int n = in.read(buffer, 0, BUFFER_SIZE);
			int m = n;
			while ((m < MAX_SIGNATURE_SIZE) && (n > 0)) {
				n = in.read(buffer, m, BUFFER_SIZE - m);
				m += n;
			}

			for (Iterator<String> i = signatureMapForExcel.keySet().iterator(); i.hasNext();) {
				String key = i.next();
				if (matchesSignature(signatureMapForExcel.get(key), buffer, m)) {
					fileType = key;
					isTrue = true;
					break;
				} else {
					isTrue = false;
				}
			}
			if (isTrue == false) {
				return isTrue;
			}
		} finally {
			in.close();
		}
		return isTrue;
	}
	
	public boolean checkFileExtension(MultipartFile file) {
		List<String> list = Arrays.asList("csv","xls","xlsx","xlsb");
		String extension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
		if(list.contains(extension)) {
			try {
				return getFileType(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
