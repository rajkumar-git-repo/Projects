
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
@JsonPropertyOrder({
    "schemeType",
    "sumassured",
    "InterestRate",
    "tenure",
    "coverOption",
    "hasFinancePremium",
    "edc",
    "MPH"
})
public class LoanDetails {

    @JsonProperty("schemeType")
    private String schemeType;
    @JsonProperty("sumAssured")
    private String sumAssured;
    @JsonProperty("interestRate")
    private String interestRate;
    @JsonProperty("tenure")
    private Integer tenure;
    @JsonProperty("coverOption")
    private String coverOption;
    @JsonProperty("hasFinancePremium")
    private String hasFinancePremium;
    @JsonProperty("edc")
    private String edc;
    @JsonProperty("MPH")
    private String mPH;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("schemeType")
    public String getSchemeType() {
        return schemeType;
    }

    @JsonProperty("schemeType")
    public void setSchemeType(String schemeType) {
        this.schemeType = schemeType;
    }

    @JsonProperty("sumAssured")
    public String getSumAssured() {
        return sumAssured;
    }

    @JsonProperty("sumAssured")
    public void setSumAssured(String sumassured) {
        this.sumAssured = sumassured;
    }


    @JsonProperty("tenure")
    public Integer getTenure() {
        return tenure;
    }
    
    @JsonProperty("interestRate")
    public String getInterestRate() {
		return interestRate;
	}
    @JsonProperty("interestRate")
	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}

	@JsonProperty("tenure")
    public void setTenure(Integer tenure) {
        this.tenure = tenure;
    }

    @JsonProperty("coverOption")
    public String getCoverOption() {
        return coverOption;
    }

    @JsonProperty("coverOption")
    public void setCoverOption(String coverOption) {
        this.coverOption = coverOption;
    }

    @JsonProperty("hasFinancePremium")
    public String getHasFinancePremium() {
        return hasFinancePremium;
    }

    @JsonProperty("hasFinancePremium")
    public void setHasFinancePremium(String hasFinancePremium) {
        this.hasFinancePremium = hasFinancePremium;
    }

    @JsonProperty("edc")
    public String getEdc() {
        return edc;
    }

    @JsonProperty("edc")
    public void setEdc(String edc) {
        this.edc = edc;
    }

    @JsonProperty("MPH")
    public String getMPH() {
        return mPH;
    }

    @JsonProperty("MPH")
    public void setMPH(String mPH) {
        this.mPH = mPH;
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
