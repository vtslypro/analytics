package com.adsk.analytics.model;

/**
 * Created by deepa on 6/18/2015.
 */
public class AppAnnieDailyData {
    private String date;
    private double proMonthlyIOSSubscribers;
    private double proMonthlyAndroidSubscribers;
    private double proYearlyIOSSubscribers;
    private double proYearlyAndroidSubscribers;

    public String getDate() {
        return date;
    }

    public double getProMonthlyIOSSubscribers() {
        return proMonthlyIOSSubscribers;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setProMonthlyIOSSubscribers(double proMonthlyIOSSubscribers) {
        this.proMonthlyIOSSubscribers = proMonthlyIOSSubscribers;
    }

    public double getProMonthlyAndroidSubscribers() {
        return proMonthlyAndroidSubscribers;
    }

    public void setProMonthlyAndroidSubscribers(double proMonthlyAndroidSubscribers) {
        this.proMonthlyAndroidSubscribers = proMonthlyAndroidSubscribers;
    }

    public double getProYearlyIOSSubscribers() {
        return proYearlyIOSSubscribers;
    }

    public void setProYearlyIOSSubscribers(double proYearlyIOSSubscribers) {
        this.proYearlyIOSSubscribers = proYearlyIOSSubscribers;
    }

    public double getProYearlyAndroidSubscribers() {
        return proYearlyAndroidSubscribers;
    }

    public void setProYearlyAndroidSubscribers(double proYearlyAndroidSubscribers) {
        this.proYearlyAndroidSubscribers = proYearlyAndroidSubscribers;
    }

    public void updateAppAnnieDailyData(AppAnnieDailyData appAnnieDailyData){
        double proMonthlyIOSSales = appAnnieDailyData.getProMonthlyIOSSubscribers();
        double proMonthlyAndroidSales = appAnnieDailyData.getProMonthlyAndroidSubscribers();
        double proYearlyIOSSales = appAnnieDailyData.getProYearlyIOSSubscribers();
        double proYearlyAndroidSales = appAnnieDailyData.getProYearlyAndroidSubscribers();

        if(proMonthlyIOSSales > 0.0){
            this.setProMonthlyIOSSubscribers(proMonthlyIOSSales);
        }

        if(proMonthlyAndroidSales > 0.0){
            this.setProMonthlyAndroidSubscribers(proMonthlyAndroidSales);
        }

        if(proYearlyIOSSales > 0.0){
            this.setProYearlyIOSSubscribers(proYearlyIOSSales);
        }

        if(proYearlyAndroidSales > 0.0){
            this.setProYearlyAndroidSubscribers(proYearlyAndroidSales);
        }

    }
}
