
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
    "RequestInfo",
    "RequestPayload"
})
public class Request {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;
    @JsonProperty("RequestPayload")
    private RequestPayload requestPayload;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("RequestInfo")
    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    @JsonProperty("RequestInfo")
    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    @JsonProperty("RequestPayload")
    public RequestPayload getRequestPayload() {
        return requestPayload;
    }

    @JsonProperty("RequestPayload")
    public void setRequestPayload(RequestPayload requestPayload) {
        this.requestPayload = requestPayload;
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
