package project;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Jincheng Liu
 *
 */
public class Simulation {
	public static final int REPLICATION_NUMBER = 14;
	public static final int SERVER_NUMBER = 10;
	public static final int MASTER_CLOCK = 5000;
	public static final double POWER = 2000;
	public static final double ALPHA_ONE = 0.43;
	public static final double ALPHA_TWO = 0.98;
	public static final double BETA = 0.86;
	public static final double LAMBDA = 7.2;
	public static final double INTERVAL_FRONT = 0.75;
	public static final double INTERVAL_BACK = 1.17;
	public static final double GAMMA = (1-BETA)/(Math.pow(ALPHA_TWO, (1-BETA))-Math.pow(ALPHA_ONE, (1-BETA)));
	private List<Server> serverList;
	private Random random;
	private int serverNumber;
	private double power;
	private double frequency;
	
	public Simulation(int serverNumber){
		this.serverNumber = serverNumber;
		this.power = calculatePower(this.serverNumber);
		this.frequency = calculateFrequency(this.power);
		this.serverList = new ArrayList<Server>();
		this.random = new Random(1);
	}
	
	public double getFrequency(){
		return frequency;
	}

	public double calculatePower(int serverNumber){
		return POWER/serverNumber;
	}
	
	public double calculateFrequency(double power){
		return 1.25+0.31*(power/200 - 1);
	}
	
	/**
	 * get the inter-arrival time
	 */
	public double getArrivalTime(){
		return (-Math.log(1-random.nextDouble())/LAMBDA) * (random.nextDouble()*(INTERVAL_BACK-INTERVAL_FRONT)+INTERVAL_FRONT);
	}
	
	/**
	 * get the service time
	 */
	public double getServiceTime(){
		return Math.pow(( random.nextDouble() + (GAMMA/(1-BETA))*(Math.pow(ALPHA_ONE,1-BETA)) )*(1-BETA)/GAMMA, 1/(1-BETA))/frequency;
	}
	
	/**
	 * get the least remain service time server
	 */
	public int getLeastTimeServer(){
		int serverIndex = 0;
		double leastServiceTimeValue = Double.MAX_VALUE;
		for(int i =0; i<serverList.size(); i++){
			if(serverList.get(i).jobSize()!=0){
				if(leastServiceTimeValue >serverList.get(i).getLeastServiceTime()*serverList.get(i).jobSize()){
					serverIndex = i;
					leastServiceTimeValue = serverList.get(i).getLeastServiceTime()*serverList.get(i).jobSize();
				}
			}
		}
		return serverIndex;
	}
	
	/**
	 * get the least remain service time
	 */
	public double getLeastServiceTime(){
		double leastServiceTimeValue = Double.MAX_VALUE;

		for(int i =0; i<serverList.size(); i++){
			if(serverList.get(i).jobSize()!=0){
				if(leastServiceTimeValue >serverList.get(i).getLeastServiceTime()*serverList.get(i).jobSize()){
					leastServiceTimeValue = serverList.get(i).getLeastServiceTime()*serverList.get(i).jobSize();
				}
			}
		}
		return leastServiceTimeValue;
	}
	
	/**
	 * update all job's remain service time
	 * @param time
	 */
	public void updateAllServerJob(double time){
		for(int i =0; i<serverList.size(); i++){
			if(serverList.get(i).jobSize()!=0){
				serverList.get(i).updateAllJob(time);
			}
			
		}
	}
	

	
	/**
	 * start simulation
	 */
	public Result start(){
		double clock = 0;
		int jobNumber = 0;
		double responseTime = 0;
		double arrivalTime = getArrivalTime();
		double serviceTime = getServiceTime();
		Result result = new Result();
		double eventTime = 0;
		double tmpTime = 0;
		int currentServer = 0;
		double departureTime;
		int departureTimeServer;
		serverList.clear();
		for(int i=0; i < this.serverNumber;i++){
			serverList.add(new Server());
		}
		while(clock < MASTER_CLOCK){//only simulate the time less than 5000
			boolean arrivalEventType = true;
			departureTime = clock+getLeastServiceTime();//departure time 
			
			departureTimeServer = getLeastTimeServer();//which server departure job
			
			if(arrivalTime < departureTime){//if arrival time is smaller than departure time, the next event is arrival
				eventTime = arrivalTime;
			}else{// the next event is departure
				eventTime = departureTime;
				arrivalEventType = false;
			}
			
			tmpTime = eventTime-clock;

			clock = eventTime;
			updateAllServerJob(tmpTime);//update all jobs' remain service time.
			//start action
			if(arrivalEventType){//If it's an arrival event
				serverList.get(currentServer).addJob(arrivalTime, serviceTime);
				currentServer++;
				if(currentServer==serverNumber){
					currentServer=0;
				}
				arrivalTime = clock + getArrivalTime();;
				serviceTime = getServiceTime();
			}else{//If it's a departure event
				double arriveTimeOfLST = serverList.get(departureTimeServer).getLeastServiceTimeJob();
				double value = clock-arriveTimeOfLST;//the response time = leave time - arrival time
				responseTime = responseTime + value;
				serverList.get(departureTimeServer).removeJob(arriveTimeOfLST);
				jobNumber+=1;
				result.getJobNumberList().add(jobNumber);
				result.getTimeList().add(clock);
				result.getResponseTimeList().add(value);
				result.getMeanResponseTimeList().add(responseTime/jobNumber);
			}
		}
		return result;
	}
	

}
