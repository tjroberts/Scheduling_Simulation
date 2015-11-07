
import java.io.*;
import java.util.*;

/**
 * Shortest remaining time first scheduler
 */
public class SRTFScheduler implements Scheduler {
    
    

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
    
	
	//sort by pid PASS ONE
	Collections.sort(allProcesses, new Comparator<MyProcess>() {
        @Override public int compare(MyProcess p1, MyProcess p2) {
            return (int)p1.getPid() - (int)p2.getPid();
        }
	});
    
    	//sort by start time PASS TWO (if two have same time, lower PID will be first)
	Collections.sort(allProcesses, new Comparator<MyProcess>() {
        @Override public int compare(MyProcess p1, MyProcess p2) {
            return p1.getArrivalTime() - p2.getArrivalTime();
        }
	});
	
	
	//BEGIN SIMULATION 
	boolean stillAlive = true;
	MyProcess currentProcess = null;
	int currentTime = 0;

	while ( stillAlive ) {
	    
	    //sort by remaining time
	    Collections.sort(allProcesses, new Comparator<MyProcess>() {
	    @Override public int compare(MyProcess p1, MyProcess p2) {
		return p1.getBurstTime() - p2.getBurstTime();
	    }
	    });
	    
	    //find one to run
	    currentProcess = null;
	    for ( MyProcess p : allProcesses ) {
		
		if ( p.isAlive() && (p.getArrivalTime() <= currentTime) ) {
		    currentProcess = p;
		    break;
		}
	    }
	    
	    //if found one to run
	    if ( currentProcess != null ) {
		
		currentProcess.runFor(currentTime, 1);
		currentTime++; //time goes on...
	    }
	    else
		currentTime++; //time goes on..
		
	    //check if one still alive
	    stillAlive = false;
	    for ( MyProcess p : allProcesses ) {
		if ( p.isAlive() )
		    stillAlive = true;
	    }
	}
        
	//sort by pid for output
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
