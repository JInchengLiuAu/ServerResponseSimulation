package project;

import java.util.ArrayList;
import java.util.List;

public class Result {
	private List<Double> meanResponseTimeList;//mean response time of each first k jobs
	private List<Integer> jobNumberList;//each first k jobs
	private List<Double> timeList;//each departure time
	private List<Double> responseTimeList;//response time

	public Result(){
	    this.meanResponseTimeList = new ArrayList<Double>();
	    this.timeList = new ArrayList<Double>();
	    this.jobNumberList = new ArrayList<Integer>();
	    this.responseTimeList = new ArrayList<Double>();
    }

    public List<Double> getResponseTimeList() {
        return responseTimeList;
    }

    public void setResponseTimeList(List<Double> responseTimeList) {
        this.responseTimeList = responseTimeList;
    }

    public List<Integer> getJobNumberList() {
        return jobNumberList;
    }

    public void setJobNumberList(List<Integer> jobNumberList) {
        this.jobNumberList = jobNumberList;
    }

    public List<Double> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<Double> timeList) {
        this.timeList = timeList;
    }

    public List<Double> getMeanResponseTimeList() {
        return meanResponseTimeList;
    }

    public void setMeanResponseTimeList(List<Double> meanResponseTimeList) {
        this.meanResponseTimeList = meanResponseTimeList;
    }
}
