package nl.uva.sa.ft1.filter;

import nl.uva.sa.ft1.LogEntry;
import nl.uva.sa.ft1.pipe.OperationFailedException;
import nl.uva.sa.ft1.pipe.Pipe;
import nl.uva.sa.ft1.pipe.PipeClosedException;

// Filter that filters out every log entry which is not of the specified type.
public class TypeFilter extends FilterBase<String,String> {
	private String type;

	// The type of the entries to keep is specified once during instantiation.
	public TypeFilter(Pipe<String> inPipe, Pipe<String> outPipe, String type) {
		super(inPipe, outPipe);
		this.type = type;
	}

	@Override
	public void run() {
		//Keep calling the inPipe.get() method while it returns elements
		//When it throws a PipeClosedException, terminate the counting and presume that the pipe is done
		//outputting elements
		try {
			try {
				while(true) {
					String s = inPipe.get();
					LogEntry entry = new LogEntry(s);
					if (entry.getType() == type) {
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
