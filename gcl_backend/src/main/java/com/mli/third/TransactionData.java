
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
    "ruleName",
    "ruleGroup",
    "ruleInput"
})
public class TransactionData {

    @JsonProperty("ruleName")
    private String ruleName;
    @JsonProperty("ruleGroup")
    private String ruleGroup;
    @JsonProperty("ruleInput")
    private RuleInput ruleInput;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ruleName")
    public String getRuleName() {
        return ruleName;
    }

    @JsonProperty("ruleName")
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    @JsonProperty("ruleGroup")
    public String getRuleGroup() {
        return ruleGroup;
    }

    @JsonProperty("ruleGroup")
    public void setRuleGroup(String ruleGroup) {
        this.ruleGroup = ruleGroup;
    }

    @JsonProperty("ruleInput")
    public RuleInput getRuleInput() {
        return ruleInput;
    }

    @JsonProperty("ruleInput")
    public void setRuleInput(RuleInput ruleInput) {
        this.ruleInput = ruleInput;
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
