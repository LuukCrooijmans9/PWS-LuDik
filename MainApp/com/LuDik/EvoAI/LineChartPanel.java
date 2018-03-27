package com.LuDik.EvoAI;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class LineChartPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static ArrayList<LineChartPanel> lineChartPanels;
	
	private JFreeChart chart;
	private ChartPanel chartPanel;
	private XYSeriesCollection dataset;

	public LineChartPanel(JPanel container, String chartTitle, String xLabel, String yLabel) {
		dataset = new XYSeriesCollection();
        
        
        
       
        chart = createChart(dataset, chartTitle, xLabel, yLabel);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension((int) container.getPreferredSize().getWidth(), (int) (container.getPreferredSize().getHeight() / 6)));
        add(chartPanel);
        
        if(getLineChartPanels() == null)  setLineChartPanels(new ArrayList<LineChartPanel>());
        getLineChartPanels().add(this);
	}
	
	private JFreeChart createChart(XYSeriesCollection dataset, String title, String xLabel, String yLabel) {
		
		JFreeChart chart = ChartFactory.createXYLineChart(
	            title,
	            xLabel,
	            yLabel,
	            dataset
	        );
	        return chart;
	        
	}
	
	public void updateDataset(XYSeries series) {
		dataset.removeAllSeries();
		dataset.addSeries(series);
	}
	
	public BufferedImage getBufferedImageOfChart(int width, int height) {
		return chart.createBufferedImage(width, height);
	}
	
	public String getChartTitle() {
		return chart.getTitle().getText();
	}

	public static ArrayList<LineChartPanel> getLineChartPanels() {
		return lineChartPanels;
	}

	public static void setLineChartPanels(ArrayList<LineChartPanel> lineChartPanels) {
		LineChartPanel.lineChartPanels = lineChartPanels;
	}

}
