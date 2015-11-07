import java.io.*;
import java.util.*;


/**
 * Proportional share scheduler
 * Take total shares to be 100.import java.io.*;
import java.util.*;

 * A process will not run unless it is completely given the requested share.
 */
public class PSScheduler implements Scheduler {

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

    
    //sort by pid
    Collections.sort(allProcesses, new Comparator<MyProcess>() {
    @Override public int compare(MyProcess p1, MyProcess p2) {
	return (int)p1.getPid() - (int)p2.getPid();
    }
    });
    
    
    //BEGIN SIMULATION 
    boolean stillAlive = true;
    ArrayList<MyProcess> currentProcesses = new ArrayList<MyProcess>();
    int currentShare = 100;  
    int currentTime = 0;

    while ( stillAlive ) {
	
	//find eligible processes
	for ( MyProcess p : allProcesses) {
		
	    if ( !currentProcesses.contains(p) && p.isAlive() && (p.getArrivalTime() <= currentTime) ) {
		
		if ( p.getShare() <= currentShare ) {
		    currentShare -= p.getShare(); //use resources
		    currentProcesses.add(p);
		}
		else
		    break; //need to wait until there is enough room, DON'T move onto next process with higher PID
	    }
	}
	
	//sort eligible processes by pid
	Collections.sort(currentProcesses, new Comparator<MyProcess>() {
	@Override public int compare(MyProcess p1, MyProcess p2) {
	    return (int)p1.getPid() - (int)p2.getPid();
	}
	});
	    
	//if found one to run
	if ( currentProcesses.size() > 0 ) {
		
	    Iterator<MyProcess> runProcessIter = currentProcesses.iterator();
	    while ( runProcessIter.hasNext() ) {
		MyProcess currentProcess = runProcessIter.next();
		
		//run process
		currentProcess.runFor(currentTime, 1);
		
		//remove process and reclaim resources if done
		if ( !currentProcess.isAlive() ) {
		    currentShare += currentProcess.getShare();
		    runProcessIter.remove();
		}
	    }
	    //increment once per loop iteration since running in parallel
	    currentTime++; //times goes on...

	}
	else
	    currentTime++; //time goes on...
	    
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
