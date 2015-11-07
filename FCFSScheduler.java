import java.io.*;
import java.util.*;

/**
 * First comes first served scheduler.
 */
public class FCFSScheduler implements Scheduler {
	
	@Override
	public void schedule(String inputFile, String outputFile) {
        
        int totalProcesses = 0;
	ArrayList<MyProcess> allProcesses = new ArrayList<MyProcess>();
		
	try {
		
            FileReader inFile = new FileReader(inputFile);
            BufferedReader readBuff = new BufferedReader(inFile);
            
            String line = null;
            //read the entire file and create process objects out of the data
            while ( (line = readBuff.readLine()) != null )
                allProcesses.add(new MyProcess(line.split("\\s+")));
            totalProcesses = allProcesses.size();
			
	}
	catch (IOException ex) {
		System.out.println("IOException: An exception while reading input file: " + inputFile + " has occured.");
	}
		
		
	//sort by start time (for FCFS algorithm)
	Collections.sort(allProcesses, new Comparator<MyProcess>() {
        @Override public int compare(MyProcess p1, MyProcess p2) {
            return p1.getArrivalTime() - p2.getArrivalTime();
        }
	});
    
        //begin simulation
	int currentTime = allProcesses.get(0).getArrivalTime();
        int timeSlice = 0;
        for ( MyProcess currentProcess : allProcesses ) {
            
            //run process for its whole burst time
            timeSlice = currentProcess.getBurstTime();
            currentProcess.runFor(currentTime, timeSlice);
            currentTime += timeSlice; //time goes on...
                
        }
		
        
        //sort by pid (for printing to file)
	Collections.sort(allProcesses, new Comparator<MyProcess>() {
        @Override public int compare(MyProcess p1, MyProcess p2) {
            return (int)p1.getPid() - (int)p2.getPid();
        }
	});
        
		
	try {
	    //output the results to a file
            int totalWaitTime = 0;
            int totalTurnaroundTime = 0;
	    PrintStream outFile = new PrintStream(new FileOutputStream(outputFile));
	    
	    for ( MyProcess p : allProcesses ) {
		
                totalWaitTime += p.waitTime;
                totalTurnaroundTime += p.turnaroundTime;
		outFile.println(p.getPid() + " " + p.finishTime + " " + p.waitTime + " " + p.turnaroundTime);
            }
			
            outFile.print( Math.round((double)totalWaitTime/(double)totalProcesses) + " ");
            outFile.print( Math.round((double)totalTurnaroundTime/(double)totalProcesses) );
            
	    outFile.close();
	}
	catch(FileNotFoundException ex) {
		System.out.println("FileNotFoundException: Couldn't find the output file.");
	}
		
		
		
    }
}
