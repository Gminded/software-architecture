package nl.uva.sa.ft1.filter;

import nl.uva.sa.ft1.LogEntry;
import nl.uva.sa.ft1.pipe.OperationFailedException;
import nl.uva.sa.ft1.pipe.Pipe;
import nl.uva.sa.ft1.pipe.PipeClosedException;

//Sample filter that filters out log levels out of specified bounds.
//It reads log entries from the input pipe.
public class LogingFilter extends FilterBase<String, String> implements Filter {
	private Integer lowerBound;
	private Integer upperBound;
	
	//Constructor that takes one input and one output pipe, and sends them up to the FilterBase class
	public LogingFilter(Pipe<String> inPipe, Pipe<String> outPipe, Integer lowerBound, Integer upperBound) {
		super(inPipe, outPipe);
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	//A runnable component that can be run in a Thread or just by calling the function
	public void run() {
		//Keep calling the inPipe.get() method while it returns elements
		//When it throws a PipeClosedException, terminate the counting and presume that the pipe is done
		//outputting elements
		try {
			try {
				while(true) {
					String s = inPipe.get();
					LogEntry entry = new LogEntry(s);
					if (lowerBound <= entry.getLevel() && entry.getLevel() <= upperBound) {
						this.outPipe.put(s);
					}
				}
			} catch (PipeClosedException e) {
				outPipe.close();
			} 
		}catch (OperationFailedException iex) {
			iex.printStackTrace();
		}
	}
}