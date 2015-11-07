
//Class for encapsulation of processes

public class MyProcess {
	
	//starting info
	private long pid;
	private int arrivalTime;
	private int burstTime;
	private int share;
	
	//other info
    boolean hasRun;
	int finishTime;
	int turnaroundTime;
	
	int waitTime;
	int lastRunTime;
	
	
	public MyProcess(String[] processInfo) {
		
		finishTime = 0;
		turnaroundTime = 0;
		waitTime = 0;
		lastRunTime = 0;
        hasRun = false;
		
		int counter = 0;
		for ( String info: processInfo ) {
			
			switch (counter) {
				case 0:
					pid = Integer.parseInt(info);
					break;
				case 1:
					arrivalTime = Integer.parseInt(info);
					break;
				case 2:
					burstTime = Integer.parseInt(info);
					break;
				case 3 :
					share = Integer.parseInt(info);
					break;
				default:
					//shouldnt be here
					break;
			}
			counter++;
		}
	}
	
	public MyProcess(int pid, int arrivalTime, int burstTime, int share) {

		finishTime = 0;
		turnaroundTime = 0;
		waitTime = 0;
		lastRunTime = 0;
        hasRun = false;
		
		this.pid = pid;
		this.arrivalTime = arrivalTime;
		this.burstTime = burstTime;
		this.share = share;
	}
	
	public long getPid() {
		return pid;
	}
	
	public int getArrivalTime() {
		return arrivalTime;
	}
	
	public int getBurstTime() {
		return burstTime;
	}
	
	public int getShare() {
		return share;
	}
		
	public boolean isAlive() {
		return burstTime > 0;
	}
	
	
	
	//runs the process for specified time, does computations
	public void runFor(int currentTime, int timeSlice) {
		
		if ( burstTime > 0 ) {
            
            //check if first run
            if ( !hasRun ) {
                hasRun = true;
                waitTime = (currentTime - arrivalTime);
            }
            else
                waitTime += (currentTime - lastRunTime);
            
			burstTime -= timeSlice;
        
			//if done, record finish time and turnaround time
			if ( burstTime <= 0 ) {
				finishTime = currentTime + timeSlice;
                turnaroundTime = (currentTime + timeSlice) - arrivalTime;
            }
				
			lastRunTime = currentTime + timeSlice; //last time process finished
		}
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		result.append("PID: " + this.pid + "    ArrivalTime: " + this.arrivalTime + "    BurstTime: " + this.burstTime + "\n");
		return result.toString();
	}

	
}
