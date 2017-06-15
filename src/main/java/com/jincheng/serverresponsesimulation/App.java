package com.jincheng.serverresponsesimulation;



import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
	
	/**
	 * Cut off the Transient Part
	 */
	public static Result cutOffTransientPart(Result result, int transientJobNumber){
		Result finalResult = new Result();
		for(int i=0; i < result.getJobNumberList().size();i++){
			if(i >= transientJobNumber){
				finalResult.getJobNumberList().add(result.getJobNumberList().get(i));
				finalResult.getMeanResponseTimeList().add(result.getMeanResponseTimeList().get(i));
				finalResult.getResponseTimeList().add(result.getResponseTimeList().get(i));
				finalResult.getTimeList().add(result.getTimeList().get(i));
			}
		}
		return finalResult;
	}

	/**
	 * Calculate the Confidence Boundary
	 */
	public static double[] calculateConfidenceBoundary(List<Double> givenNumbers) {

		// calculate the mean value (= average)
		double sum = 0.0;

		for(int i=0;i<givenNumbers.size();i++){
			sum+=givenNumbers.get(i);
		}

		double mean = sum / givenNumbers.size();

		// calculate standard deviation
		double squaredDifferenceSum = 0.0;


		for(int i=0; i<givenNumbers.size();i++){
			squaredDifferenceSum += (givenNumbers.get(i) - mean) * (givenNumbers.get(i) - mean);
		}

		double variance = squaredDifferenceSum / givenNumbers.size();
		double standardDeviation = Math.sqrt(variance);

		double confidenceLevel = 1.753;// value for 95% confidence interval
		double temp = confidenceLevel * standardDeviation / Math.sqrt(givenNumbers.size());
		return new double[]{mean - temp, mean + temp};
	}


	public static void main(String[] args) {
		int transientJobNumber = 1100;
		XYSeriesCollection mCollection = new XYSeriesCollection();
		XYSeries mSeriesFirst = new XYSeries("Server Number");
		for(int serverNumber = 3; serverNumber<= Simulation.SERVER_NUMBER;serverNumber++){//From 3 to 10
			List<Double> replicationMeanResponseTime = new ArrayList<Double>();
			double allReplicationMeanResponseTime = 0;
			for(int replication=0; replication < Simulation.REPLICATION_NUMBER; replication++){//Replication
				Simulation simulation = new Simulation(serverNumber);
				Result result = cutOffTransientPart(simulation.start(), transientJobNumber);
				double allMeanResponseTime = result.getMeanResponseTimeList().get(result.getMeanResponseTimeList().size()-1);
				replicationMeanResponseTime.add(allMeanResponseTime);
				allReplicationMeanResponseTime+=allMeanResponseTime;//The all mean response time of replications
			}
			System.out.println("When "+serverNumber+" servers are on, the mean response time is "+ allReplicationMeanResponseTime/Simulation.REPLICATION_NUMBER);
			double[] confidienceInterval=calculateConfidenceBoundary(replicationMeanResponseTime);
			System.out.println("The 95% confidence interval =["+confidienceInterval[0]+", "+confidienceInterval[1]+"].\n");
			mSeriesFirst.add(serverNumber, allReplicationMeanResponseTime/Simulation.REPLICATION_NUMBER);
		}
		mCollection.addSeries(mSeriesFirst);
		JFreeChart lineChart = ChartFactory.createXYLineChart(
				"Mean Response Time Analyse",
				"Server Number","Mean Response Time",
				mCollection,
				PlotOrientation.VERTICAL,
				true,true,false);
		ChartFrame chartFrame = new ChartFrame("Mean Response Time Analyse",lineChart);
		chartFrame.pack();
		chartFrame.setVisible(true);
	} 
}
