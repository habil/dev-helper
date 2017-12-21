package com.habil.devhelper.dto;

import java.util.List;

/**
 * @author Habil BOZALi
 * @18 Eyl 2015
 * @Translate.java
 * @com.habil.devhelper
 */
public class Translate {
    private ResponseData responseData;
    private String responseDetails;
    private String responseStatus;
    private String responderId;
    private List<Matches> matchList;

    public String getResponseDetails() {
        return responseDetails;
    }

    public void setResponseDetails(String responseDetails) {
        this.responseDetails = responseDetails;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponderId() {
        return responderId;
    }

    public void setResponderId(String responderId) {
        this.responderId = responderId;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setMatchList(List<Matches> matchList) {
        this.matchList = matchList;
    }

    public List<Matches> getMatchList() {
        return matchList;
    }
}
