package nl.uva.sa.ft1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import nl.uva.sa.ft1.pipe.OperationFailedException;
import nl.uva.sa.ft1.pipe.Pipe;
import nl.uva.sa.ft1.pipe.PipeClosedException;

//A random log generator thread that takes a list of predefined log messages and outputs 100 randomly selected ones 
//to a pipe output pipe
public class RandomLogGenerator extends Thread {
	private Pipe<String> pipe = null;
	private String [] levels = {"error", "verbose", "warning", "info"};
	private String [] type = {"bloodpressure","heartrate"};

	private Integer generateRandomNumber() {
		
		Integer number = 80;
		Integer seed = 5;
		Integer delta;
		
		Random randomGenerator = new Random();
	
			delta = randomGenerator.nextInt(seed) % 10;
			if (randomGenerator.nextInt(seed) % 2 == 0) {
				number += delta;
			}
			else {
				number -= delta;
			}
			
			return number;
		}
	
	public RandomLogGenerator(Pipe<String> _pipe) {
		pipe = _pipe;
	}

	public void run() {
		Random randomGenerator = new Random();
		
		try {
			//Randomly pick one of the predefined logs and send it to the pipe
			for (int i = 1; i<= 100; i++) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.HOUR, -240 + (240/100)*i);
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
				
				String logTime = dateFormat.format(calendar.getTime());
				String logLevel = levels[randomGenerator.nextInt(levels.length)];
				String logType = type[randomGenerator.nextInt(type.length)];
				String logValue = generateRandomNumber().toString();
				String logLine = logTime +";"+ logLevel +";"+ logType +";"+ logValue;
				//System.out.println(logLine);
				pipe.put(logLine);
			}
			//Close the pipe
			pipe.close();
		} catch (PipeClosedException e) {
			e.printStackTrace();
		} catch (OperationFailedException e) {
			e.printStackTrace();
		}
	}
}