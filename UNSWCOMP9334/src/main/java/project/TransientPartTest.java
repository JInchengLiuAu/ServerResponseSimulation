package project;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

/**
 * Created by Jincheng Liu.
 */
public class TransientPartTest {

    public static XYSeriesCollection createDataset(Result result){
        XYSeriesCollection mCollection = new XYSeriesCollection();
        XYSeries mSeriesFirst = new XYSeries("Server Number = 5");

        int width = 0;
        mSeriesFirst.add(result.getJobNumberList().get(0), result.getMeanResponseTimeList().get(0));
        for(int i=1;i<result.getTimeList().size();i++){
            if(width<500){
                width++;
            }else {
                width = 0;
                //dataSet.addValue(result.getMeanResponseTimeList().get(i),"Respone Time", result.getTimeList().get(i));
            }
            mSeriesFirst.add(result.getJobNumberList().get(i), result.getMeanResponseTimeList().get(i));
        }
        mCollection.addSeries(mSeriesFirst);
        return mCollection;
    }

    public static void main(String[] args){
        int number = 5;
        Simulation simulation = new Simulation(number);
        Result resultOfFive = simulation.start();
        JFreeChart lineChart = ChartFactory.createXYLineChart(
                "Transient Behaviour Analyse",
                "Clock Time","Mean Response Time",
                createDataset(resultOfFive),
                PlotOrientation.VERTICAL,
                true,true,false);
        ChartFrame chartFrame = new ChartFrame("Transient Behaviour Analyse",lineChart);
        chartFrame.pack();
        chartFrame.setVisible(true);

    }
}
