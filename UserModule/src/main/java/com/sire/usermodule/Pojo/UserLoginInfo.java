package com.sire.usermodule.Pojo;

public class UserLoginInfo {

    private String identityType;
    private String identifier;
    private String credential;

    public UserLoginInfo(String identityType, String identifier, String credential) {
        this.identityType = identityType;
        this.identifier = identifier;
        this.credential = credential;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    @Override
    public String toString() {
        return "UserLoginInfo{" +
                "identityType='" + identityType + '\'' +
                ", identifier='" + identifier + '\'' +
                ", credential='" + credential + '\'' +
                '}';
    }
}