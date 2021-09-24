
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
    "CreationDate",
    "CreationTime",
    "SourceInfoName",
    "TransactionId",
    "RequestorToken",
    "LastSyncDate",
    "Version"
})
public class RequestInfo {

    @JsonProperty("CreationDate")
    private String creationDate;
    @JsonProperty("CreationTime")
    private String creationTime;
    @JsonProperty("SourceInfoName")
    private String sourceInfoName;
    @JsonProperty("TransactionId")
    private String transactionId;
    @JsonProperty("RequestorToken")
    private String requestorToken;
    @JsonProperty("LastSyncDate")
    private String lastSyncDate;
    @JsonProperty("Version")
    private String version;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("CreationDate")
    public String getCreationDate() {
        return creationDate;
    }

    @JsonProperty("CreationDate")
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @JsonProperty("CreationTime")
    public String getCreationTime() {
        return creationTime;
    }

    @JsonProperty("CreationTime")
    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    @JsonProperty("SourceInfoName")
    public String getSourceInfoName() {
        return sourceInfoName;
    }

    @JsonProperty("SourceInfoName")
    public void setSourceInfoName(String sourceInfoName) {
        this.sourceInfoName = sourceInfoName;
    }

    @JsonProperty("TransactionId")
    public String getTransactionId() {
        return transactionId;
    }

    @JsonProperty("TransactionId")
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @JsonProperty("RequestorToken")
    public String getRequestorToken() {
        return requestorToken;
    }

    @JsonProperty("RequestorToken")
    public void setRequestorToken(String requestorToken) {
        this.requestorToken = requestorToken;
    }

    @JsonProperty("LastSyncDate")
    public String getLastSyncDate() {
        return lastSyncDate;
    }

    @JsonProperty("LastSyncDate")
    public void setLastSyncDate(String lastSyncDate) {
        this.lastSyncDate = lastSyncDate;
    }

    @JsonProperty("Version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("Version")
    public void setVersion(String version) {
        this.version = version;
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
