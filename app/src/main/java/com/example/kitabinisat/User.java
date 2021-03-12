package com.example.kitabinisat;

public class User {
    private String userId;
    private String userName;
    private String userPhone;
    private String userMail;
    private String userAddress;
    private String userImageUrl;
    private long userSignupDate;

    public User() {
    }

    public User(String userId, String userName, String userPhone, String userMail, String userAddress, String userImageUrl, long userSignupDate) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userMail = userMail;
        this.userAddress = userAddress;
        this.userImageUrl = userImageUrl;
        this.userSignupDate = userSignupDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public long getUserSignupDate() {
        return userSignupDate;
    }

    public void setUserSignupDate(long userSignupDate) {
        this.userSignupDate = userSignupDate;
    }
}

