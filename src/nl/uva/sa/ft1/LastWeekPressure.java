package nl.uva.sa.ft1;

import java.util.Calendar;
import java.util.Date;

import nl.uva.sa.ft1.filter.DateFilter;
import nl.uva.sa.ft1.filter.PrintFilter;
import nl.uva.sa.ft1.filter.TypeFilter;
import nl.uva.sa.ft1.pipe.Pipe;
import nl.uva.sa.ft1.pipe.SynchronizedArrayListPipe;

public class LastWeekPressure {

	public static void main(String[] args) {
		Pipe<String> pipe1 = new SynchronizedArrayListPipe<String>();
		Pipe<String> pipe2 = new SynchronizedArrayListPipe<String>();
		Pipe<String> pipe3 = new SynchronizedArrayListPipe<String>();
		
		RandomLogGenerator generator = new RandomLogGenerator(pipe1);
		TypeFilter type = new TypeFilter(pipe1, pipe2, "bloodpressure");
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		Date oneWeekAgo = calendar.getTime();
		DateFilter date = new DateFilter(pipe2, pipe3, oneWeekAgo, now);
		PrintFilter printer = new PrintFilter(pipe3, null);
		
		Thread generatorThread = new Thread(generator);
		Thread typeThread = new Thread(type);
		Thread dateThread = new Thread(date);
		Thread printerThread = new Thread(printer);
		generatorThread.start();
		typeThread.start();
		dateThread.start();
		printerThread.start();
	}

}
