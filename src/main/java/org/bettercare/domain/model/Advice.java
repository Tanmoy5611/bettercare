package org.bettercare.domain.model;

// This object keeps the risk number and the advice text together

public class Advice {
    private int riskLevel;
    private String adviceInfo;

    public Advice() {
    }

    public Advice(int riskLevel, String adviceInfo) {
        this.riskLevel = riskLevel;
        this.adviceInfo = adviceInfo;
    }

    public int getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(int riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getAdviceInfo() {
        return adviceInfo;
    }

    public void setAdviceInfo(String adviceInfo) {
        this.adviceInfo = adviceInfo;
    }

    @Override
    public String toString() {
        return "Advice{" +
                "riskLevel=" + riskLevel +
                ", adviceInfo='" + adviceInfo + '\'' +
                '}';
    }
}