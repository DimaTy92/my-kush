package com.example.mykush.graph;

import com.example.mykush.entity.dto.DeviceControlDTO;
import com.example.mykush.entity.dto.PriceDeviceDTO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class LineChartEx {
    public static int WIDTH = 640;
    public static int HEIGHT = 480;

    public byte[] gettingChartInUA(DeviceControlDTO device) {

        List<PriceDeviceDTO> priceDevicesList = device.getPriceDevicesList();

        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();

        for (int i = 0; i < priceDevicesList.size(); i++) {
            PriceDeviceDTO priceDeviceDTO = priceDevicesList.get(i);
            String date = priceDeviceDTO.getLocalDate().format(DateTimeFormatter.ofPattern("dd-MMM-yy"));

            line_chart_dataset.addValue((Integer.parseInt(priceDeviceDTO.getPriceUA().replaceAll("[^0-9]", ""))), "price", date);
        }

        JFreeChart chart = ChartFactory.createLineChart(
                device.getModel(), "Дата",
                "Ціна в гривнях",
                line_chart_dataset, PlotOrientation.VERTICAL,
                true, true, false);

        createStyle(chart);

        byte[] img = null;
        try {
            img = ChartUtils.encodeAsPNG(chart.createBufferedImage(WIDTH, HEIGHT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public void createStyle(JFreeChart chart) {
        String fontName = "Arial Bold Italic";

        StandardChartTheme theme = (StandardChartTheme) org.jfree.chart.StandardChartTheme.createJFreeTheme();

        theme.setTitlePaint(Color.decode("#000000"));
        theme.setExtraLargeFont(new Font(fontName, Font.BOLD, 18)); //title
        theme.setLargeFont(new Font(fontName, Font.BOLD, 15)); //axis-title
        theme.setRegularFont(new Font(fontName, Font.PLAIN, 11));
        theme.setRangeGridlinePaint(Color.decode("#C0C0C0"));
        theme.setPlotBackgroundPaint(Color.white);
        theme.setChartBackgroundPaint(Color.white);
        theme.setGridBandPaint(Color.red);
        theme.setAxisOffset(new RectangleInsets(0, 0, 0, 0));
        theme.setBarPainter(new StandardBarPainter());
        theme.setAxisLabelPaint(Color.decode("#000000"));
        theme.apply(chart);
        chart.getCategoryPlot().setOutlineVisible(false);
        chart.getCategoryPlot().getRangeAxis().setAxisLineVisible(false);
        chart.getCategoryPlot().getRangeAxis().setTickMarksVisible(false);
        chart.getCategoryPlot().setRangeGridlineStroke(new BasicStroke());
        chart.getCategoryPlot().getRangeAxis().setTickLabelPaint(Color.decode("#000000"));
        chart.getCategoryPlot().getDomainAxis().setTickLabelPaint(Color.decode("#000000"));
        chart.setTextAntiAlias(true);
        chart.setAntiAlias(true);
        chart.getCategoryPlot().getRenderer().setSeriesPaint(0, Color.decode("#4572a7"));
        CategoryItemRenderer renderer = chart.getCategoryPlot().getRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
    }

    public byte[] gettingChartInUSA(DeviceControlDTO device) {

        List<PriceDeviceDTO> priceDevicesList = device.getPriceDevicesList();

        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();

        for (int i = 0; i < priceDevicesList.size(); i++) {
            PriceDeviceDTO priceDeviceDTO = priceDevicesList.get(i);
            String date = priceDeviceDTO.getLocalDate().format(DateTimeFormatter.ofPattern("dd-MMM-yy"));

            line_chart_dataset.addValue((Integer.parseInt(priceDeviceDTO.getPriceUSA().replaceAll("[^0-9]", ""))), "price", date);
        }

        JFreeChart chart = ChartFactory.createLineChart(
                device.getModel(), "Дата",
                "Ціна в USA",
                line_chart_dataset, PlotOrientation.VERTICAL,
                true, true, false);

        createStyle(chart);

        byte[] img = null;
        try {
            img = ChartUtils.encodeAsPNG(chart.createBufferedImage(WIDTH, HEIGHT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
}
