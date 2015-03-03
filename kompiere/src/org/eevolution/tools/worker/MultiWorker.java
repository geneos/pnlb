package org.eevolution.tools.worker;

/**
* @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
* @version 1.0, October 14th 2005
*/
public abstract class MultiWorker {

	protected abstract class WorkerThread extends Thread {

		abstract public Object doWork();
		
		public void run() {
			
			isWorking = true;
			value = doWork();
			isWorking = false;
		}

		public void interrupt() {
			
			super.interrupt();
			isWorking = false;
		}
	}

	protected boolean isWorking;
	protected WorkerThread workerThread;
	protected int timeout;
	protected Object value;

	public MultiWorker() {
	
		setTimeout(-1);
	}
	
	public abstract void start();


	public int getTimeout() {

		return timeout;
	}

	public void setTimeout(int timeout) {
		
		this.timeout = timeout;
	}
	
	public boolean isWorking() {
		
		return isWorking;
	}
	
	public void waitForComplete(int timeout) {
		
		setTimeout(timeout);
		waitForComplete();
	}
	
	public void stop() {
		
		workerThread.interrupt();
	}
	
	public void waitForComplete() {
		
		boolean to = getTimeout() > -1;
		
		int c = 0;
		int i = 1000;
		while(isWorking()) {
			
			try {
				
				Thread.sleep(i);
				c+= to ? c+=i : -1;
			}			
			catch(Exception e) {}
		
			if(to && c >= getTimeout()) {
				
				workerThread.interrupt();
				workerThread = null;
				break;
			}
		}
	}
}
