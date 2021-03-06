package nl.uva.sa.ft1.filter;

import nl.uva.sa.ft1.pipe.OperationFailedException;
import nl.uva.sa.ft1.pipe.Pipe;
import nl.uva.sa.ft1.pipe.PipeClosedException;

// Sample filter that counts Verbose messages in the logs
// Verbose messages are messages that start with 'log:verbose'
public class VeboseCountFilter extends FilterBase<String, Integer> {

	//Constructor that takes one input and one output pipe, and sends them up to the FilterBase class
	public VeboseCountFilter(Pipe<String> inPipe, Pipe<Integer> outPipe) {
		super(inPipe, outPipe);
	}
	
	//A runnable component that can be run in a Thread or just by calling the function
	public void run() {
		Integer counter = 0;
		//Keep calling the inPipe.get() method while it returns elements
		//When it throws a PipeClosedException, terminate the counting and presume that the pipe is done
		//outputting elements
		try {
			while(true) {
				String s = inPipe.get();			
				if(s.startsWith("log:verbose")){
					counter++;
				}				
			}	
		} catch (PipeClosedException e) {
			System.out.println("Counted verbose: " + counter);
		} catch (OperationFailedException e) {
			e.printStackTrace();
		}		
	}
}
