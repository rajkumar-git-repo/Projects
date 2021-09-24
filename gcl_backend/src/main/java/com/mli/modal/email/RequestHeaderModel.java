package com.mli.modal.email;

public class RequestHeaderModel {

	private GeneralConsumerInformationModel generalConsumerInformation;

	public GeneralConsumerInformationModel getGeneralConsumerInformation() {
		return generalConsumerInformation;
	}

	public void setGeneralConsumerInformation(GeneralConsumerInformationModel generalConsumerInformation) {
		this.generalConsumerInformation = generalConsumerInformation;
	}

	@Override
	public String toString() {
		return "RequestHeaderModel [generalConsumerInformation=" + generalConsumerInformation + "]";
	}
}

