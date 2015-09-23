package nl.uva.sa.ft1;

import java.util.ArrayList;
import java.util.List;

import nl.uva.sa.ft1.filter.BranchingFilter;
import nl.uva.sa.ft1.filter.ExceptionCountFilter;
import nl.uva.sa.ft1.filter.ExceptionFilter;
import nl.uva.sa.ft1.filter.LogingFilter;
import nl.uva.sa.ft1.filter.VeboseCountFilter;
import nl.uva.sa.ft1.pipe.Pipe;
import nl.uva.sa.ft1.pipe.SynchronizedArrayListPipe;

public class PipeFilterDemo {

    public static void main(String[] args) {
    	//Create shared pipes
    	Pipe<String> logInPipe = new SynchronizedArrayListPipe<String>();
    	Pipe<String> logOutPipe = new SynchronizedArrayListPipe<String>();
    	Pipe<String> pipe3 = new SynchronizedArrayListPipe<String>();

    	List<Pipe<String>> logInPipes = new ArrayList<Pipe<String>>();
    	logInPipes.add(logInPipe);

    	List<Pipe<String>> logOutPipes = new ArrayList<Pipe<String>>();
    	logOutPipes.add(logOutPipe);
    	logOutPipes.add(pipe3);

    	List<Pipe<String>> extInPipes = new ArrayList<Pipe<String>>();
    	extInPipes.add(logOutPipe);
    	
    	List<Pipe<String>> verInPipes = new ArrayList<Pipe<String>>();
    	verInPipes.add(pipe3);
    	
    	//Create generator and pass pipe 1 to it
    	Thread logGenerator = new RandomLogGenerator(logInPipe);

    	//Create log filter and set output pipes
    	LogingFilter logingFilter = new LogingFilter(logInPipe, logOutPipe, 0, 1);
    	
    	Pipe<String> logBranchOutPipe1 = new SynchronizedArrayListPipe<>();
    	Pipe<String> logBranchOutPipe2 = new SynchronizedArrayListPipe<>();
    	Pipe<String> logBranchOutPipe3 = new SynchronizedArrayListPipe<>();
    	ArrayList<Pipe<String>> logBranchOutPipes = new ArrayList<>();
    	logBranchOutPipes.add(logBranchOutPipe1);
    	logBranchOutPipes.add(logBranchOutPipe2);
    	logBranchOutPipes.add(logBranchOutPipe3);
    	
    	BranchingFilter<String> branchingFilter = new BranchingFilter<>(logOutPipe, logBranchOutPipes);

    	//Create exception filter 
		ExceptionCountFilter exceptionCountFilter = new ExceptionCountFilter(logBranchOutPipe1, null);
    	//Create verbose filter 
    	VeboseCountFilter verboseFilter = new VeboseCountFilter(logBranchOutPipe2, null);
    	ExceptionFilter exceptionFilter = new ExceptionFilter(logBranchOutPipe3, null);

    	//Create threads of the filters, and run them
    	Thread logFilter = new Thread(logingFilter);
    	Thread brFilter = new Thread(branchingFilter);
    	Thread exctFilter = new Thread(exceptionCountFilter);
    	Thread vebFilter = new Thread(verboseFilter);
    	Thread exFilter = new Thread(exceptionFilter);
    	
    	logGenerator.start();
    	brFilter.start();
    	logFilter.start();
    	exctFilter.start();
    	vebFilter.start();
    	exFilter.start();
     }
}
