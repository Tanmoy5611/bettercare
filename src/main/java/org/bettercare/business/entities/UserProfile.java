package org.bettercare.business.entities;

import org.bettercare.business.entities.enums.SKIN_COLOR;
import java.util.List;

public class UserProfile {

    private int profileId;
    private String name;
    private SKIN_COLOR skinColor;
    private int age;
    private int userAccountId;
    private double sunExposure;

    public int getProfileId() {
        return profileId;
    }

    public int getUserAccountId() {
        return userAccountId;
    }

    public double getSunExposure() {
        return sunExposure;
    }

    public void setSunExposure(double sunExposure) {
        this.sunExposure = sunExposure;
    }

    public void setUserAccountId(int userAccountId) {
        this.userAccountId = userAccountId;
    }

    public UserProfile() {}

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSkinColor(SKIN_COLOR skinColor) {
        this.skinColor = skinColor;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHealthConditions(List<String> healthConditions) {
        this.healthConditions = healthConditions;
    }

    private List<String> healthConditions;

    public UserProfile(String name, SKIN_COLOR skinColor, int age
                       ) {
        this.name = name;
        this.skinColor = skinColor;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public SKIN_COLOR getSkinColor() {
        return skinColor;
    }

    public int getAge() {
        return age;
    }

    public List<String> getHealthConditions() {
        return healthConditions;
    }

}
