package com.mli.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mli.constants.Constant;
import com.mli.constants.DocType;

/**
 * 
 * @author Devendra.Kumar
 *
 */
public class ZipUtils {

	private static final Logger logger = LoggerFactory.getLogger(ZipUtils.class);

	private List<String> fileList;
	private String outputZipFile;
	private String sourceFolder;
	
	public ZipUtils() {
		fileList = new ArrayList<String>();
	}

	public boolean zipDirectory(String sourceFolder, String outputZipFile) {

		boolean zippedFlag = false;
		try {
			this.sourceFolder = sourceFolder;
			this.outputZipFile = outputZipFile;
			this.generateFileList(new File(sourceFolder));
			this.zipIt(outputZipFile);
			zippedFlag = true;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return zippedFlag;
	}

	public void zipIt(String zipFile) {
		byte[] buffer = new byte[1024];
		String source = new File(sourceFolder).getName();
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(fos);

			logger.info("Output to Zip : " + zipFile);
			FileInputStream in = null;

			for (String file : this.fileList) {
				logger.info("File Added : " + file);
				ZipEntry ze = new ZipEntry(source + File.separator + file);
				zos.putNextEntry(ze);
				try {
					in = new FileInputStream(sourceFolder + File.separator + file);
					int len;
					while ((len = in.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
				} finally {
					if(in != null)
					in.close();
				}
			}

			zos.closeEntry();
			logger.info("Folder successfully compressed");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(zos != null)
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void generateFileList(File node) {
		// add file only
		if (node.isFile()) {
			fileList.add(generateZipEntry(node.toString()));
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename));
			}
		}
	}

	private String generateZipEntry(String file) {
		return file.substring(sourceFolder.length() + 1, file.length());
	}
	
	public void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
	
	/**
	 * folder verfied_dd-MM-yyyy
	 * 
	 * @param date
	 * @return
	 */
	public static String getDirNameOfVerified(Long date,String doctype,String proposalNumber) {
		StringBuilder destination = new StringBuilder(Constant.VERIFIED);
		destination.append("_");
		destination.append(DateUtil.extractDateAsStringDashFormate(date));
		destination.append("/");
		destination.append(doctype);
		if (proposalNumber != null) {
			destination.append("/");
			destination.append(proposalNumber);
			destination.append(".pdf");
			
		}
		logger.info("copied path "+destination);
		return destination.toString();
	}
	
	public static String getDirNameOfVerifiedFor7days(Long date,String doctype) {
		StringBuilder destination = new StringBuilder(Constant.VERIFIED);
		destination.append("_");
		destination.append(DateUtil.extractDateAsStringDashFormate(date));
		destination.append("/");
		destination.append(doctype);
		logger.info("copied path "+destination);
		return destination.toString();
	}
	public static String getDirNameOfVerified(Long date,String doctype) {
		StringBuilder destination = new StringBuilder(Constant.VERIFIED);
		destination.append("_");
		destination.append(DateUtil.extractDateAsStringDashFormate(date));
		destination.append("/");
		//destination.append(doctype);
		logger.info("copied path "+destination);
		return destination.toString();
	}
	
	
}
