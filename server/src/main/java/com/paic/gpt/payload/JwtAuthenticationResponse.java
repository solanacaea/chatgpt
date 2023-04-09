package com.paic.gpt.payload;

public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String message;
    private String status;

    public JwtAuthenticationResponse(String accessToken, String message, String status) {
        this.accessToken = accessToken;
        this.message = message;
        this.status = status;
    }

    public JwtAuthenticationResponse(String accessToken, String message) {
        this.accessToken = accessToken;
        this.message = message;
    }

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
