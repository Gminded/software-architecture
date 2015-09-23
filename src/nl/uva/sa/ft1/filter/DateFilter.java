package nl.uva.sa.ft1.filter;

import java.util.Date;

import nl.uva.sa.ft1.LogEntry;
import nl.uva.sa.ft1.pipe.OperationFailedException;
import nl.uva.sa.ft1.pipe.Pipe;
import nl.uva.sa.ft1.pipe.PipeClosedException;

// Filter that extracts only log entries in a certain temporal range.
public class DateFilter extends FilterBase<String,String> {
	private Date start;
	private Date end;

	public DateFilter(Pipe<String> inPipe, Pipe<String> outPipe, Date start, Date end) {
		super(inPipe, outPipe);
		this.start = start;
		this.end = end;
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
					if (entry.getDate().after(start) && entry.getDate().before(end)) {
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