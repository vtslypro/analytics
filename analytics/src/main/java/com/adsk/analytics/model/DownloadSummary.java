package com.adsk.analytics.model;

/**
 * Created by aenugav on 6/2/2015.
 */
public class DownloadSummary {

    private String date;
    private String iOSDownload;
    private String androidDownload;
    private String windowsDownload;
    private String totalDownloads;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getiOSDownload() {
        return iOSDownload;
    }

    public void setiOSDownload(String iOSDownload) {
        this.iOSDownload = iOSDownload;
    }

    public String getAndroidDownload() {
        return androidDownload;
    }

    public void setAndroidDownload(String androidDownload) {
        this.androidDownload = androidDownload;
    }

    public String getTotalDownloads() {
        return totalDownloads;
    }

    public void setTotalDownloads(String totalDownloads) {
        this.totalDownloads = totalDownloads;
    }

    public String getWindowsDownload() {
        return windowsDownload;
    }

    public void setWindowsDownload(String windowsDownload) {
        this.windowsDownload = windowsDownload;
    }
}
