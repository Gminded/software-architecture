package nl.uva.sa.ft1.filter;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import nl.uva.sa.ft1.LogEntry;
import nl.uva.sa.ft1.pipe.OperationFailedException;
import nl.uva.sa.ft1.pipe.Pipe;
import nl.uva.sa.ft1.pipe.PipeClosedException;

public class AverageFilter extends FilterBase<String,String> {
	private Integer[][] hourRanges;
	private Queue<LogEntry>[] entryPartitions;

	public AverageFilter(Pipe<String> inPipe, Pipe<String> outPipe, Integer[][] hourRanges) {
		super(inPipe, outPipe);
		this.hourRanges = hourRanges;
		entryPartitions =  new LinkedList[hourRanges.length];
		for (Integer i = 0; i < hourRanges.length; i++) {
			entryPartitions[i] = new LinkedList<LogEntry>();
		}
	}

	protected String average (Integer index) {
		Queue<LogEntry> entries = entryPartitions[index];
		Integer tot = 0;
		Integer size = entries.size();
		if (size > 0) {
			for (Integer i=0; i<size; i++) {
				LogEntry entry = entries.remove();
				tot += entry.getValue();
			}
			Integer avg = tot/size;
			return avg.toString();
		}
		else return null;
	}

	@Override
	public void run() {
		try {
			try {
				Integer index = 0; // position in hourRanges
				while(true) {
					String s = inPipe.get();
					LogEntry entry = new LogEntry(s);
					Calendar calendar = Calendar.getInstance();
					Integer[] range = hourRanges[index];
					calendar.setTime(entry.getDate());
					calendar.set(Calendar.HOUR_OF_DAY, range[0]);
					Date start = calendar.getTime();
					calendar.set(Calendar.HOUR_OF_DAY, range[1]);
					if (range[0] > range[1]) {
						calendar.add(Calendar.DATE, 1); // add 1 day if interval ends in the next day
					}
					Date end = calendar.getTime();
					if (entry.getDate().after(start) && entry.getDate().before(end)) {
						entryPartitions[index].add(entry);
					}
					else {
						String avg = average(index);
						if (avg != null)
							this.outPipe.put(avg);
					}
					if (++index >= hourRanges.length) index = 0; //circularly increase index
				}
			} catch (PipeClosedException e) {
				for (Integer i = 0; i < hourRanges.length; i++) {
					String avg = average(i);
					if (avg != null) {
						try {
							this.outPipe.put(avg);
						} catch(PipeClosedException cl) {}
					}
				}
				outPipe.close();
			} 
		}catch (OperationFailedException iex) {
			iex.printStackTrace();
		}
	}

}
