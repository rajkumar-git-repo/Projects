package com.mli.third;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "SAPercentage", "isRequired", "Code", "tenure", "benefitType" })
public class AcceleratedCriticalIllness {

	@JsonProperty("SAPercentage")
	private String SAPercentage;
	@JsonProperty("isRequired")
	private String isRequired;
	@JsonProperty("code")
	private String code;
	@JsonProperty("tenure")
	private Integer tenure;
	@JsonProperty("benefitType")
	private String benefitType;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("SAPercentage")
	public String getSAPercentage() {
		return SAPercentage;
	}

	@JsonProperty("SAPercentage")
	public void setSAPercentage(String sAPercentage) {
		SAPercentage = sAPercentage;
	}

	@JsonProperty("isRequired")
	public String getIsRequired() {
		return isRequired;
	}

	@JsonProperty("isRequired")
	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}

	@JsonProperty("Code")
	public String getCode() {
		return code;
	}

	@JsonProperty("Code")
	public void setCode(String code) {
		this.code = code;
	}

	@JsonProperty("tenure")
	public Integer getTenure() {
		return tenure;
	}

	@JsonProperty("tenure")
	public void setTenure(Integer tenure) {
		this.tenure = tenure;
	}

	@JsonProperty("benefitType")
	public String getBenefitType() {
		return benefitType;
	}

	@JsonProperty("benefitType")
	public void setBenefitType(String benefitType) {
		this.benefitType = benefitType;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
