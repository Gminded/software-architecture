package nl.uva.sa.ft1.filter;

import nl.uva.sa.ft1.pipe.OperationFailedException;
import nl.uva.sa.ft1.pipe.Pipe;
import nl.uva.sa.ft1.pipe.PipeClosedException;

public class PrintFilter extends FilterBase<String,String> {

	public PrintFilter(Pipe<String> inPipe, Pipe<String> outPipe) {
		super(inPipe, outPipe);
	}

	@Override
	public void run() {
		try {
			try {
				while(true) {
					String s = inPipe.get();
					System.out.println(s);
					if (outPipe != null) {
						this.outPipe.put(s);
					}
				}
			} catch (PipeClosedException e) {
				if (outPipe != null) {
					outPipe.close();
				}
			} 
		}catch (OperationFailedException iex) {
			iex.printStackTrace();
		}

	}

}
