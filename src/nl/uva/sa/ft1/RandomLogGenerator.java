package nl.uva.sa.ft1;

import java.util.Random;

public class RandomLogGenerator extends Thread {
	private Pipe<String> pipe = null;
	private String [] logs = {"log:exception", "log:verbose", "log:warning", "vital:pulse", "vital:pressure"};

	public RandomLogGenerator(Pipe<String> _pipe) {
		pipe = _pipe;
	}

	public void run() {
		Random randomGenerator = new Random();
		for (int i = 1; i<= 100; i++){
			int rndNum = randomGenerator.nextInt(5);
			String logLine = logs[rndNum];
			//System.out.println(logLine + " - start");
			pipe.put(logLine);
		}
		
		//'null' signals that thats the end of the input
		String nul = null;
		pipe.put(nul);
	}
}