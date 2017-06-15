package project;

public class RandomNumberTest {

	public static void main(String[] args) {
		Simulation simulation = new Simulation(4);
		boolean serviceTimeCorrect = true;
		for(int i =0; i < 5000000; i++){//Try 5000000 times
			double serviceTime = simulation.getServiceTime();
			//check whether in the interval
			if(serviceTime < Simulation.ALPHA_ONE/simulation.getFrequency() || serviceTime>Simulation.ALPHA_TWO/simulation.getFrequency()){
				serviceTimeCorrect = false;
				System.out.println(serviceTime);
				break;
			}
		}
		if(serviceTimeCorrect){
			System.out.println("Service Time Function is correct");
		}else{
			System.out.println("Service Time Function is not correct");
		}
		
	}

}
