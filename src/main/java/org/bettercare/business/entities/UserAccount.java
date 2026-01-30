package org.bettercare.business.entities;

import java.sql.SQLException;
import java.time.LocalDate;


public class UserAccount {
    private String name;
    private int userId;
    private String email;
    private String password;
    private LocalDate dateSubscription;
    private UserProfile profile;
    private boolean receiveEmailAlerts = false;

    public UserAccount(String password,String email, int userId, String name) throws SQLException {

        this.password = password;
        this.email = email;
        this.userId = userId;
        this.name = name;

    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public UserAccount(String name, int userId, String email, String password, LocalDate dateSubscription, UserProfile profile) {
        this.name = name;
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.dateSubscription = dateSubscription;
        this.profile = profile;
    }
    public UserAccount(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDateSubscription(LocalDate dateSubscription) {this.dateSubscription = dateSubscription;}

    public boolean getReceiveEmailAlerts() {
        return receiveEmailAlerts;
    }

    public void setReceiveEmailAlerts(boolean receiveEmailAlerts) {
        this.receiveEmailAlerts = receiveEmailAlerts;
    }

    public boolean isReceiveEmailAlerts() {
        return receiveEmailAlerts;
    }
}