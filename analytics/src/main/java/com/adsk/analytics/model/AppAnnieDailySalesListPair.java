package com.adsk.analytics.model;

import com.google.gson.internal.LinkedTreeMap;

/**
 * Created by deepa on 7/13/2015.
 */
public class AppAnnieDailySalesListPair {

    private LinkedTreeMap iosSalesItem;
    private LinkedTreeMap androidSalesItem;

    public LinkedTreeMap getIosSalesItem() {
        return iosSalesItem;
    }

    public LinkedTreeMap getAndroidSalesItem() {
        return androidSalesItem;
    }

    public void setIosSalesItem(LinkedTreeMap iosSalesItem) {
        this.iosSalesItem = iosSalesItem;
    }

    public void setAndroidSalesItem(LinkedTreeMap androidSalesItem) {
        this.androidSalesItem = androidSalesItem;
    }
}
