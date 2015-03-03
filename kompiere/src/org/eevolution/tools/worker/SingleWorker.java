package org.eevolution.tools.worker;

/**
* @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
* @version 1.0, October 14th 2005
*/
public abstract class SingleWorker extends MultiWorker {

	public SingleWorker() {
		
		super();
	}
	
	public void start() {

		workerThread = new WorkerThread() {

			public Object doWork() {

				return doIt();
			};	
		};
		workerThread.start();
	}

	protected abstract Object doIt();
}
