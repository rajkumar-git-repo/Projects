
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
    "loanDetails",
    "borrowerDetails",
    "AcceleratedCriticalIllness"
})
public class RuleInput {

    @JsonProperty("loanDetails")
    private LoanDetails loanDetails;
    @JsonProperty("borrowerDetails")
    private BorrowerDetails borrowerDetails;
    @JsonProperty("acceleratedCriticalIllness")
    private AcceleratedCriticalIllness acceleratedCriticalIllness;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("loanDetails")
    public LoanDetails getLoanDetails() {
        return loanDetails;
    }

    @JsonProperty("loanDetails")
    public void setLoanDetails(LoanDetails loanDetails) {
        this.loanDetails = loanDetails;
    }

    @JsonProperty("borrowerDetails")
    public BorrowerDetails getBorrowerDetails() {
        return borrowerDetails;
    }

    @JsonProperty("borrowerDetails")
    public void setBorrowerDetails(BorrowerDetails borrowerDetails) {
        this.borrowerDetails = borrowerDetails;
    }

    @JsonProperty("AcceleratedCriticalIllness")
    public AcceleratedCriticalIllness getAcceleratedCriticalIllness() {
        return acceleratedCriticalIllness;
    }

    @JsonProperty("AcceleratedCriticalIllness")
    public void setAcceleratedCriticalIllness(AcceleratedCriticalIllness acceleratedCriticalIllness) {
        this.acceleratedCriticalIllness = acceleratedCriticalIllness;
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
