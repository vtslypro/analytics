package com.adsk.analytics.model;

/**
 * Created by aenugav on 5/29/2015.
 */
public class MRRSummary
{
    private String monthYear;
    private double iosMRR;
    private double androidMRR;
    //TODO:vatsalya add WindowsMRR member variable

    private double totalMRR;
    //ACSA-211 - average revenue per user
    private double ARPU;

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public double getIosMRR() {
        return iosMRR;
    }

    public void setIosMRR(double iosMRR) {
        this.iosMRR = iosMRR;
    }

    public double getAndroidMRR() {
        return androidMRR;
    }

    public void setAndroidMRR(double androidMRR) {
        this.androidMRR = androidMRR;
    }

    public double getTotalMRR() {
        return totalMRR;
    }

    public void setTotalMRR(double totalMRR) {
        this.totalMRR = totalMRR;
    }

    public double getARPU() {
        return ARPU;
    }

    public void setARPU(double ARPU) {
        this.ARPU = ARPU;
    }
}
