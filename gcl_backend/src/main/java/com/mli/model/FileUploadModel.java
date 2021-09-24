package com.mli.model;

public class FileUploadModel {

	private String fileName;

	private byte[] importedFile;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getImportedFile() {
		return importedFile;
	}

	public void setImportedFile(byte[] importedFile) {
		this.importedFile = importedFile;
	}

}
