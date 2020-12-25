package org.cloudbus.cloudsim.photovoltaics;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class PVChart extends JFrame {
    private double[] data;
    String chartName;
    Color color;

    public PVChart(double[] data, String chartName, Color color) {
        this.data = data;
        this.chartName = chartName;
        this.color = color;
        init();
    }

    private void init() {
        XYDataset dataSet = createDataSet(data, chartName);
        JFreeChart jFreeChart = createChart(dataSet, chartName);

        ChartPanel chartPanel = new ChartPanel(jFreeChart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);

        pack();
        setTitle("Line chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private XYDataset createDataSet(double[] data, String name) {
        XYSeries xySeries = new XYSeries(name);
        for (int i = 0; i < data.length; i++) {
            xySeries.add((double) i, data[i]);
        }
        XYSeriesCollection dataSet = new XYSeriesCollection();
        dataSet.addSeries(xySeries);
        return dataSet;
    }

    private JFreeChart createChart(XYDataset dataset, String chartName) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                chartName,
                "Hours",
                "Power [W]",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, color);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle(chartName,
                        new Font("Serif", java.awt.Font.BOLD, 18)
                )
        );

        return chart;
    }
}
