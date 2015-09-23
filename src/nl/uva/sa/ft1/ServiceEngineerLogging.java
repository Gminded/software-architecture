package nl.uva.sa.ft1;

import nl.uva.sa.ft1.filter.LogingFilter;
import nl.uva.sa.ft1.filter.PrintFilter;
import nl.uva.sa.ft1.pipe.Pipe;
import nl.uva.sa.ft1.pipe.SynchronizedArrayListPipe;

public class ServiceEngineerLogging {

	public static void main(String[] args) {
		Pipe<String> pipe1 = new SynchronizedArrayListPipe<String>();
		Pipe<String> pipe2 = new SynchronizedArrayListPipe<String>();
		RandomLogGenerator generator = new RandomLogGenerator(pipe1);
		LogingFilter loglevel = new LogingFilter(pipe1, pipe2, 0, 0);
		PrintFilter printer = new PrintFilter(pipe2, null);

		Thread generatorThread = new Thread(generator);
		Thread loglevelThread = new Thread(loglevel);
		Thread printerThread = new Thread(printer);
		generatorThread.start();
		loglevelThread.start();
		printerThread.start();
	}

}
