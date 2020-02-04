package com.adsk.analytics.adapter;

/**
 * Created by aenugav on 9/23/2014.
 */

import com.adsk.analytics.ChartSeries;
import com.adsk.analytics.exception.NullWidgetException;
import com.adsk.analytics.geckoboard.GeckoboardHelper;
import com.adsk.analytics.geckoboard.widgets.*;
import com.adsk.analytics.model.AppAnnieDailyData;
import com.adsk.analytics.model.AppAnnieMonthlyData;
import com.adsk.analytics.model.DownloadSummary;
import com.adsk.analytics.model.MRRSummary;
import com.adsk.analytics.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

@Component(GeckoboardHelper.GECKOBOARD)
public class GeckoboardAdapter implements IDashboardAdapter
{
    private static GeckoboardHelper helper = GeckoboardHelper.Instance();
    private static final Logger logger = LoggerFactory.getLogger(GeckoboardAdapter.class);


    @Override
    public String getWidgetData(int widgetType) throws NullWidgetException
    {
        BaseWidget widget = null;
        switch (widgetType)
        {

            case GeckoboardHelper.BLINKY_DATA:
            {

                break;
            }
            case GeckoboardHelper.HIGHCHART_DAILY_NEW_SUBSCRIBERS_BY_APPID:
            {
                widget = prepareHighchartDailyNewSubscriberByAppid();
                break;
            }
            case GeckoboardHelper.HIGHCHART_DAILY_NEW_SUBSCRIBERS_BY_PRODID:
            {
                widget = prepareHighchartDailyNewSubscriberByProdid();
                break;
            }
            case GeckoboardHelper.HIGHCHART_MRR:
            {
                widget = prepareHighChartDataForMRR();
                break;
            }

            case GeckoboardHelper.ACCOUNT_TYPE_HIGH_CHART_WIDGET:
            {
                widget = prepareHighchartPieWidgetForTotalPaidSubscrber();
                break;
            }
            case GeckoboardHelper.DOWNLOADS_HIGH_CHART_WIDGET:
            {
                widget = prepareHighchartDailyDownloadsByPlatform();
                break;
            }
            case GeckoboardHelper.TOTAL_DOWNLOADS_TEXT_WIDGET:
            {
                widget = prepareTotalDownloadsTextWidget();
                break;
            }
            case GeckoboardHelper.ARPU_HIGH_CHART_WIDGET:
            {
                widget = prepareHighChartDataForARPU();
                break;
            }
            case GeckoboardHelper.DAILY_SALES_MONTHY_HIGH_CHART_WIDGET: {
                widget = prepareHighChartDataForDailyProMontlySales();
                break;
            }
            case GeckoboardHelper.DAILY_SALES_YEARLY_HIGH_CHART_WIDGET: {
                widget = prepareHighChartDataForDailyProYearlySales();
                break;
            }
            case GeckoboardHelper.MONTHLY_REVENUE_PROPLUS_HIGH_CHART_WIDGET: {
                widget = prepareHighChartDataForProPlusMonthlyRevenue();
                break;
            }
            case GeckoboardHelper.MONTHLY_REVENUE_PROMONTHLY_HIGH_CHART_WIDGET: {
                widget = prepareHighChartDataForProMonthMonthlyRevenue();
                break;
            }
            case GeckoboardHelper.MONTHLY_REVENUE_PROYEARLY_HIGH_CHART_WIDGET: {
                widget = prepareHighChartDataForProYearMonthlyRevenue();
                break;
            }
            case GeckoboardHelper.TOTAL_REVENUE_TEXT_WIDGET: {
                widget = prepareTotalRevenueTextWidget();
                break;
            }
            case GeckoboardHelper.MONTHLY_AVERAGE_RATING_ANDROID: {
                widget = prepareHighChartDataForMonthlyRatingAndroid();
                break;
            }
            case GeckoboardHelper.MONTHLY_AVERAGE_RATING_IOS: {
                widget = prepareHighChartDataForMonthlyRatingIos();
                break;
            }

        }
        String jSON = "{}";

        if (widget == null)
            throw new NullWidgetException(widgetType);

        jSON = widget.jsonToStringPoll();
        return jSON;
    }

    private BaseWidget prepareTotalRevenueTextWidget() {
        logger.debug("prepareTotalRevenueTextWidget() : prepare total revenue Text Widget");

        return new TextWidget(NumberFormat.getNumberInstance(Locale.US).format(helper.getTotalAccumulatedRevenue()));
    }
    private BaseWidget prepareHighchartDailyBlinks()
    {
        Chart blinkyChart = new Chart();
        logger.debug("Preparing widget for subscribers by Platform information. Size = {}", helper.getDailyNewSubscribersTotal().size());
        blinkyChart.getValues().add(new ChartSeries(helper.getDailyNewSubscribersIOS(), "iOS"));
        blinkyChart.setxAxesLabels(helper.getQueryDates());
        blinkyChart.setyAxesLable("Total");
        logger.debug("The widget is created using the chart data .Size = {}", blinkyChart.getxAxesLabels().size());
        return new HighchartLineWidget(blinkyChart);
    }


    private BaseWidget prepareHighchartDailyNewSubscriberByAppid()
    {
        Chart subscriberByAppIDChart = new Chart();
        logger.debug("Preparing widget for subscribers by Platform information. Size = {}", helper.getDailyNewSubscribersTotal().size());
        subscriberByAppIDChart.getValues().add(new ChartSeries(helper.getDailyNewSubscribersIOS(), "iOS"));
        subscriberByAppIDChart.getValues().add(new ChartSeries(helper.getDailyNewSubscribersAndroid(), "Android"));
        subscriberByAppIDChart.getValues().add(new ChartSeries(helper.getDailyNewSubscribersTotal(), "Total"));
        subscriberByAppIDChart.setxAxesLabels(helper.getQueryDates());
        subscriberByAppIDChart.setyAxesLable("Total");
        logger.debug("The widget is created using the chart data .Size = {}", subscriberByAppIDChart.getxAxesLabels().size());
        return new HighchartLineWidget(subscriberByAppIDChart);
    }

    private BaseWidget prepareHighchartDailyNewSubscriberByProdid()
    {
        Chart subscribersByProdIDChart = new Chart();

        logger.debug("Preparing widget for subscribers by Product information. Size = {}",helper.getDailyNewSubscribersByProTotal().size());
        subscribersByProdIDChart.getValues().add(new ChartSeries(helper.getDailyNewSubscribersMonthly(), "Monthly"));
        subscribersByProdIDChart.getValues().add(new ChartSeries(helper.getDailyNewSubscribersYearly(), "Yearly"));
        subscribersByProdIDChart.getValues().add(new ChartSeries(helper.getDailyNewSubscribersYearlyPlus(), "YearlyPlus"));
        subscribersByProdIDChart.getValues().add(new ChartSeries(helper.getDailyNewSubscribersByProTotal(), "Total"));
        subscribersByProdIDChart.setxAxesLabels(helper.getQueryDates());
        subscribersByProdIDChart.setyAxesLable("Total");
        logger.debug("The widget is created using the chart data .Size = {}", subscribersByProdIDChart.getxAxesLabels().size());
        return new HighchartLineWidget(subscribersByProdIDChart);
    }

    private HighchartPieWidget prepareHighchartPieWidgetForTotalPaidSubscrber()
    {
        Vector<String> values = new Vector<String>();
        Vector<String> labels = new Vector<String>();

        int total = prepareHighchartData(values, labels);

        boolean isLegendWithNumbers = true;

        return new HighchartPieWidget(values, labels, "Account Types", total, isLegendWithNumbers);
    }

    private int prepareHighchartData(Vector<String> values, Vector<String> labels)
    {
        Integer resultProUsers = helper.getProUsers() ;
        Integer resultProPlusUsers = helper.getProPlusUsers();
        Integer sum = resultProUsers + resultProPlusUsers;

       /* String value0 = sum.toString();
        String label0 = "Pro + Pro Plus";*/
        String value1 = resultProUsers.toString();
        String label1 = "Pro";
        String value2 = resultProPlusUsers.toString();
        String label2 = "Pro +";

        /*labels.add(label0);*/
        labels.add(label1);
        labels.add(label2);

        /*values.add(value0);*/
        values.add(value1);
        values.add(value2);

        return sum;
    }

    private BaseWidget prepareHighChartDataForMRR()
    {
        Chart mrrRevenueChart = new Chart();
        logger.debug("prepareHighChartDataForMRR() : Preparing widget for MRR Revenue . Size = {}",helper.getMrrSummary().size());
        Vector<String> mRRDates = new Vector<String>();
        Vector<String> iOSMRR = new Vector<String>();
        Vector<String> androidMRR = new Vector<String>();
        Vector<String> totalMRR = new Vector<String>();

        for(MRRSummary mrrSummary : helper.getMrrSummary())
        {
            mRRDates.add(mrrSummary.getMonthYear());
            iOSMRR.add(String.valueOf(mrrSummary.getIosMRR()));
            androidMRR.add(String.valueOf(mrrSummary.getAndroidMRR()));
            totalMRR.add(String.valueOf(mrrSummary.getTotalMRR()));
            logger.debug("prepareHighChartDataForMRR(): MRR Summary Total : {} for the month {} ",mrrSummary.getTotalMRR(), mrrSummary.getMonthYear());
        }

        mrrRevenueChart.getValues().add(new ChartSeries(iOSMRR, " iOS MRR"));
        mrrRevenueChart.getValues().add(new ChartSeries(androidMRR, "android MRR"));
        mrrRevenueChart.getValues().add(new ChartSeries(totalMRR, "Total MRR"));

        mrrRevenueChart.setxAxesLabels(mRRDates);
        mrrRevenueChart.setyAxesLable("Total MRR");

        logger.debug("prepareHighChartDataForMRR() : The MRR widget is created using the chart data .Size = {}", mrrRevenueChart.getxAxesLabels().size());
        return new HighchartLineWidget(mrrRevenueChart);

    }

    private BaseWidget prepareHighChartDataForARPU()
    {
        Chart arpuChart = new Chart();
        logger.debug("prepareHighChartDataForARPU() : Preparing widget for ARPU . Size = {}", helper.getMrrSummary().size());
        Vector<String> arpuDates = new Vector<String>();
        Vector<String> totalARPU = new Vector<String>();

        for(MRRSummary mrrSummary : helper.getMrrSummary())
        {
            arpuDates.add(mrrSummary.getMonthYear());
            totalARPU.add(String.valueOf(mrrSummary.getARPU()));
            logger.debug("prepareHighChartDataForARPU(): ARPU Summary Total : {} for the month {} ",mrrSummary.getARPU(), mrrSummary.getMonthYear());
        }

        arpuChart.getValues().add(new ChartSeries(totalARPU, " ARPU "));

        arpuChart.setxAxesLabels(arpuDates);
        arpuChart.setyAxesLable(" ARPU ");

        logger.debug("prepareHighChartDataForARPU() : The ARPU widget is created using the chart data .Size = {}", arpuChart.getxAxesLabels().size());
        return new HighchartLineWidget(arpuChart);

    }

    private BaseWidget prepareHighchartDailyDownloadsByPlatform() {
        Chart downloadsChart = new Chart();
        logger.debug("prepareHighchartDailyDownloadsByPlatform() : Preparing widget for Daily Downloads . Size = {}", helper.getDownloadSummary().size());
        Vector<String> iOSDownloads = new Vector<String>();
        Vector<String> androidDownloads = new Vector<String>();
        Vector<String> windowDownloads = new Vector<String>();
        Vector<String> totalDownloads = new Vector<String>();

        String startDate = DateUtil.getTime(helper.getDownloadSummary().get(0).getDate());

        for (DownloadSummary downloadSummary : helper.getDownloadSummary()) {
            iOSDownloads.add(String.valueOf(downloadSummary.getiOSDownload()));
            androidDownloads.add(String.valueOf(downloadSummary.getAndroidDownload()));
            windowDownloads.add(String.valueOf(downloadSummary.getWindowsDownload()));
            totalDownloads.add(String.valueOf(downloadSummary.getTotalDownloads()));
        }

        downloadsChart.setChartTitle("Daily Downloads for past six months");
        downloadsChart.getValues().add(new ChartSeries(iOSDownloads, " iOS ", startDate));
        downloadsChart.getValues().add(new ChartSeries(androidDownloads, "android ", startDate));
        downloadsChart.getValues().add(new ChartSeries(windowDownloads, "windows ", startDate));
        downloadsChart.getValues().add(new ChartSeries(totalDownloads, "Total", startDate));
        downloadsChart.setyAxesLable("Total");

        logger.debug("prepareHighchartDailyDownloadsByPlatform() : The Downloads widget is created using the chart data .Size = {}", downloadsChart.getxAxesLabels().size());
        return new HighchartTimeDateWidget(downloadsChart);

    }

    private BaseWidget prepareTotalDownloadsTextWidget()
    {
        logger.debug("prepareTotalDownloadsTextWidget() : prepare total downloads Text Widget {} ", helper.getTotalDownloads());

        double totalDownloads = helper.getTotalDownloads();

        return new TextWidget(NumberFormat.getNumberInstance(Locale.US).format(totalDownloads));
    }

    private BaseWidget prepareHighChartDataForDailyProMontlySales() {
        Chart dailySalesChart = new Chart();

        logger.debug("prepareHighChartDataForDailyProMontlySales() : Preparing widget for daily sales");

        Vector<String> iosMonthlyProSales = new Vector<>();
        Vector<String> androidMonthlyProSales = new Vector<String>();
        String startDate = DateUtil.getTime(helper.getAppAnnieDailySalesDataStartDate());

        Iterator dailyProMonthlySalesIter = helper.getAppAnnieDailyDataHashMap().entrySet().iterator();

        while (dailyProMonthlySalesIter.hasNext()) {
            Map.Entry pair = (Map.Entry) dailyProMonthlySalesIter.next();
            iosMonthlyProSales.add(Double.toString(((AppAnnieDailyData) pair.getValue()).getProMonthlyIOSSubscribers()));
            androidMonthlyProSales.add(Double.toString(((AppAnnieDailyData) pair.getValue()).getProMonthlyAndroidSubscribers()));
        }

        dailySalesChart.getValues().add(new ChartSeries(iosMonthlyProSales, " iOS Pro Monthly ", startDate));
        dailySalesChart.getValues().add(new ChartSeries(androidMonthlyProSales, " Android Pro Monthly ", startDate));
        dailySalesChart.setChartTitle("Daily Pro Monthly Sales for past six months");
        dailySalesChart.setyAxesLable("Daily Sales");

        logger.debug("prepareHighChartDataForDailyProMontlySales() : The Daily Sales for Pro Monthly users widget is created using the chart data .Size = {}", helper.getAppAnnieDailyDataHashMap().size());

        return new HighchartTimeDateWidget(dailySalesChart);
    }

    private BaseWidget prepareHighChartDataForDailyProYearlySales() {
        Chart dailySalesChart = new Chart();
        Vector<String> iosYearlyProSales = new Vector<>();
        Vector<String> androidYearlyProSales = new Vector<>();

        logger.debug("prepareHighChartDataForDailyProYearlySales() : Preparing widget for daily sales");

        String startDate = DateUtil.getTime(helper.getAppAnnieDailySalesDataStartDate());

        Iterator dailyProYearlySalesIter = helper.getAppAnnieDailyDataHashMap().entrySet().iterator();

        while (dailyProYearlySalesIter.hasNext()) {
            Map.Entry pair = (Map.Entry) dailyProYearlySalesIter.next();
            iosYearlyProSales.add(Double.toString(((AppAnnieDailyData) pair.getValue()).getProYearlyIOSSubscribers()));
            androidYearlyProSales.add(Double.toString(((AppAnnieDailyData) pair.getValue()).getProYearlyAndroidSubscribers()));
        }

        dailySalesChart.getValues().add(new ChartSeries(iosYearlyProSales, " iOS Pro Yearly ", startDate));
        dailySalesChart.getValues().add(new ChartSeries(androidYearlyProSales, " Android Pro Yearly ", startDate));
        dailySalesChart.setChartTitle("Daily Pro Yearly Sales for past six months");
        dailySalesChart.setyAxesLable("Daily Sales");

        logger.debug("prepareHighChartDataForDailyProYearlySales() : The Daily Sales for Pro Yearly users widget is created using the chart data .Size = {}", helper.getAppAnnieDailyDataHashMap().size());

        return new HighchartTimeDateWidget(dailySalesChart);
    }

    private BaseWidget prepareHighChartDataForProPlusMonthlyRevenue() {
        Chart proPlusMonthlyRevenueChart = new Chart();
        Vector<String> proPlusMonthlyRevenueDates = new Vector<>();
        Vector<String> proPlusMonthlyRevenue = new Vector<>();

        logger.debug("prepareHighChartDataForProPlusMonthlyRevenue() : Preparing widget for monthly revenue for pro plus users");

        for (AppAnnieMonthlyData appAnnieMonthlyData : helper.getAppAnnieMonthlyData()) {
            proPlusMonthlyRevenueDates.add(appAnnieMonthlyData.getMonthYear());
            proPlusMonthlyRevenue.add(String.valueOf(appAnnieMonthlyData.getProYearlyRev()));
        }

        proPlusMonthlyRevenueChart.getValues().add(new ChartSeries(proPlusMonthlyRevenue, "Monthly Revenue For ProYearly Plus"));

        proPlusMonthlyRevenueChart.setChartTitle("Monthly Revenue for ProYearly Plus");
        proPlusMonthlyRevenueChart.setxAxesLabels(proPlusMonthlyRevenueDates);
        proPlusMonthlyRevenueChart.setyAxesLable("Monthly Revenue");

        logger.debug("prepareHighChartDataForProPlusMonthlyRevenue() : The ProPlusMonthlyRevenue widget is created using the chart data .Size = {}", proPlusMonthlyRevenueChart.getxAxesLabels().size());

        return new HighchartLineWidget(proPlusMonthlyRevenueChart);
    }

    private BaseWidget prepareHighChartDataForProMonthMonthlyRevenue() {
        Chart proMonthMonthlyRevenueChart = new Chart();
        Vector<String> proMonthMonthlyRevenueDates = new Vector<>();
        Vector<String> proMonthMonthlyRevenue = new Vector<>();

        logger.debug("prepareHighChartDataForProMonthMonthlyRevenue() : Preparing widget for monthly revenue for pro monthly users");

        for (AppAnnieMonthlyData appAnnieMonthlyData : helper.getAppAnnieMonthlyData()) {
            proMonthMonthlyRevenueDates.add(appAnnieMonthlyData.getMonthYear());
            proMonthMonthlyRevenue.add(String.valueOf(appAnnieMonthlyData.getMonthlyRev()));
        }

        proMonthMonthlyRevenueChart.getValues().add(new ChartSeries(proMonthMonthlyRevenue, "Monthly Revenue For ProMonthly Users"));

        proMonthMonthlyRevenueChart.setChartTitle("Monthly Revenue for Pro Monthly");
        proMonthMonthlyRevenueChart.setxAxesLabels(proMonthMonthlyRevenueDates);
        proMonthMonthlyRevenueChart.setyAxesLable("Monthly Revenue");

        logger.debug("prepareHighChartDataForProMonthMonthlyRevenue() : The ProMonthMonthlyRevenue widget is created using the chart data .Size = {}", proMonthMonthlyRevenueChart.getxAxesLabels().size());

        return new HighchartLineWidget(proMonthMonthlyRevenueChart);
    }

    private BaseWidget prepareHighChartDataForProYearMonthlyRevenue() {
        Chart proYearMonthlyRevenueChart = new Chart();
        Vector<String> proYearMonthlyRevenueDates = new Vector<>();
        Vector<String> proYearMonthlyRevenue = new Vector<>();

        logger.debug("prepareHighChartDataForProYearMonthlyRevenue() : Preparing widget for monthly revenue for pro yearly users");

        for (AppAnnieMonthlyData appAnnieMonthlyData : helper.getAppAnnieMonthlyData()) {
            proYearMonthlyRevenueDates.add(appAnnieMonthlyData.getMonthYear());
            proYearMonthlyRevenue.add(String.valueOf(appAnnieMonthlyData.getYearlyRev()));
        }

        proYearMonthlyRevenueChart.getValues().add(new ChartSeries(proYearMonthlyRevenue, "Monthly Revenue For ProYearly Users"));

        proYearMonthlyRevenueChart.setChartTitle("Monthly Revenue for Pro Yearly");
        proYearMonthlyRevenueChart.setxAxesLabels(proYearMonthlyRevenueDates);
        proYearMonthlyRevenueChart.setyAxesLable("Monthly Revenue");

        logger.debug("prepareHighChartDataForProMonthMonthlyRevenue() : The ProYearMonthlyRevenue widget is created using the chart data .Size = {}", proYearMonthlyRevenueChart.getxAxesLabels().size());

        return new HighchartLineWidget(proYearMonthlyRevenueChart);
    }

    private BaseWidget prepareHighChartDataForMonthlyRatingIos()
    {
        Chart averageMonthlyRateIOSChart = new Chart();
        logger.debug("prepareHighChartDataForMonthlyRatingIos() : Preparing widget for Monthly Average Rating for IOS . Size = {}", helper.getiOSAverageRating().size());

        averageMonthlyRateIOSChart.getValues().add(new ChartSeries(helper.getiOSAverageRating(), " iOS Average Rating"));
        averageMonthlyRateIOSChart.setxAxesLabels(helper.getMonthYear());
        averageMonthlyRateIOSChart.setyAxesLable("iOS Avg Rating");

        logger.debug("prepareHighChartDataForMRR() : The Monthly Average Rating for IOS widget is created using the chart data .Size = {}", averageMonthlyRateIOSChart.getxAxesLabels().size());
        return new HighchartLineWidget(averageMonthlyRateIOSChart);

    }

    private BaseWidget prepareHighChartDataForMonthlyRatingAndroid()
    {
        Chart averageMonthlyRateAndroidChart = new Chart();
        logger.debug("prepareHighChartDataForMonthlyRatingIos() : Preparing widget for Monthly Average Rating for Google-Play . Size = {}", helper.getAndroidAverageRating().size());

        averageMonthlyRateAndroidChart.getValues().add(new ChartSeries(helper.getAndroidAverageRating(), " Google-Play Average Rating"));
        averageMonthlyRateAndroidChart.setxAxesLabels(helper.getMonthYear());
        averageMonthlyRateAndroidChart.setyAxesLable("Google-Play Avg Rating");

        logger.debug("prepareHighChartDataForMRR() : The Monthly Average Rating for Google-Play widget is created using the chart data .Size = {}", averageMonthlyRateAndroidChart.getxAxesLabels().size());
        return new HighchartLineWidget(averageMonthlyRateAndroidChart);

    }
}
