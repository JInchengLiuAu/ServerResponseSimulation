package project;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Comp9334
 * @author Jincheng Liu
 *
 */
public class Server {
	private Map<Double, Double> jobList;//Job list, each job has <arrival time, remaing service time>
	
	public Server() {
		jobList = new HashMap<Double, Double>();
	}
	
	public double getServiceTime(double arrivalTime){
		return jobList.get(arrivalTime);
	}
	
	public void addJob(double arrivalTime, double departureTime){
		jobList.put(arrivalTime, departureTime);
	}
	
	public void removeJob(double arrivalTime){
		jobList.remove(arrivalTime);
	}
	
	public void updateJob(double arrivalTime, double departureTime){
		jobList.put(arrivalTime, departureTime);
	}
	
	public int jobSize(){
		return jobList.size();
	}
	
	public void emptyJobList(){
		jobList.clear();
	}
	
	public void updateAllJob(double passTime){
		int size = jobSize();
		double averageTime = passTime/size;
		for(double key : jobList.keySet()){
			updateJob(key, jobList.get(key)-averageTime);
		}
	}
	
	/**
	 * Get the least service time job
	 */
	public double getLeastServiceTimeJob(){
		double leaveJob = 0;
		double minServiceTime = Double.MAX_VALUE;
		for(double key:jobList.keySet()){
			if(minServiceTime>jobList.get(key)){
				leaveJob = key;
				minServiceTime = jobList.get(key);
			}
		}
		return leaveJob;
	}
	
	public double getLeastServiceTime(){
		return getServiceTime(getLeastServiceTimeJob());
	}
}
