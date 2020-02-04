package com.adsk.analytics.model;

/**
 * Created by aenugav on 5/29/2015.
 */
public class AppAnnieMonthlyData
{
    private String monthYear;
    private float monthlyRev;
    private int monthlySubscribers;
    private float yearlyRev;
    private int yearlySubscribers;
    private float proYearlyRev;
    private int proYearlySubscribers;
    private float yearlyRevDivBy12;
    private float netMrr ;
    private int totalSubscribers;

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public float getMonthlyRev() {
        return monthlyRev;
    }

    public void setMonthlyRev(float monthlyRev) {
        this.monthlyRev = monthlyRev;
    }

    public float getYearlyRev() {
        return yearlyRev;
    }

    public void setYearlyRev(float yearlyRev) {
        this.yearlyRev = yearlyRev;
    }

    public float getProYearlyRev() {
        return proYearlyRev;
    }

    public void setProYearlyRev(float proYearlyRev) {
        this.proYearlyRev = proYearlyRev;
    }

    public float getYearlyRevDivBy12() {
        return yearlyRevDivBy12;
    }

    public void setYearlyRevDivBy12(float yearlyRevDivBy12) {
        this.yearlyRevDivBy12 = yearlyRevDivBy12;
    }

    public float getNetMrr() {
        return netMrr;
    }

    public void setNetMrr(float netMrr) {
        this.netMrr = netMrr;
    }

    public int getMonthlySubscribers() {
        return monthlySubscribers;
    }

    public void setMonthlySubscribers(int monthlySubscribers) {
        this.monthlySubscribers = monthlySubscribers;
    }

    public int getYearlySubscribers() {
        return yearlySubscribers;
    }

    public void setYearlySubscribers(int yearlySubscribers) {
        this.yearlySubscribers = yearlySubscribers;
    }

    public int getProYearlySubscribers() {
        return proYearlySubscribers;
    }

    public void setProYearlySubscribers(int proYearlySubscribers) {
        this.proYearlySubscribers = proYearlySubscribers;
    }

    public int getTotalSubscribers() {
        return totalSubscribers;
    }

    public void setTotalSubscribers(int totalSubscribers) {
        this.totalSubscribers = totalSubscribers;
    }
}
