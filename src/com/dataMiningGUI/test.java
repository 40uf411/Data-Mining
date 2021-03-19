package com.dataMiningGUI;

import java.awt.*;

        import java.util.List;
        import java.util.ArrayList;
        import java.util.Date;

        import org.jfree.chart.ChartFactory;
        import org.jfree.chart.ChartPanel;
        import org.jfree.chart.JFreeChart;
        import org.jfree.data.statistics.BoxAndWhiskerCalculator;
        import org.jfree.data.statistics.BoxAndWhiskerXYDataset;
        import org.jfree.data.statistics.DefaultBoxAndWhiskerXYDataset;
        import org.jfree.date.DateUtilities;
        import org.jfree.ui.ApplicationFrame;
        import org.jfree.ui.RefineryUtilities;

public class test extends ApplicationFrame {
    private static boolean modeAttr;
    private static float[][] chartData;

    public test(String titel, float[][] chartData) {
        super(titel);

        test.chartData = chartData;
        test.modeAttr = false;
        init();
    }
    public test(String titel, float[] attrData) {
        super(titel);
        test.chartData = new float[1][attrData.length];

        test.chartData[0] = attrData;
        test.modeAttr = true;
        init();
    }

    private void init() {
        final BoxAndWhiskerXYDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
        setContentPane(chartPanel);
    }

    private BoxAndWhiskerXYDataset createDataset() {
        final int ENTITY_COUNT = chartData.length;

        DefaultBoxAndWhiskerXYDataset dataset = new
                DefaultBoxAndWhiskerXYDataset("Dataset box plot");

        for (int i = 0; i < ENTITY_COUNT; i++) {
            Date date = DateUtilities.createDate(2003, 7, i + 1, 12, 0);
            List values = new ArrayList();
            for (int j = 0; j < chartData[i].length; j++) {
                values.add(chartData[i][j]);
            }
            dataset.add(date,
                    BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(values));
        }
        return dataset;
    }

    private JFreeChart createChart(
            final BoxAndWhiskerXYDataset dataset) {
        JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(
                "Box and Whisker Chart", "Time", "Value", dataset, true);
        chart.setBackgroundPaint(new Color(249, 231, 236));

        return chart;
    }
    public static void run() {
        System.out.println("test in test");
        float[][] f = new float[6][4];
        final test demo = new test("", f);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
    public static void main(final String[] args) {
        float[][] f = new float[6][4];
        final test demo = new test("", f);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}