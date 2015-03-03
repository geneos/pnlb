package org.eevolution.process;

import java.util.Enumeration;
import java.util.Hashtable;

import org.compiere.model.MPInstance;
import org.compiere.model.MPInstancePara;
import org.compiere.model.MProcess;

import org.compiere.process.ProcessInfo;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * @author Gunther Hoppe, tranSIT GmbH Ilmenau/Germany
 * @version 1.0, October 14th 2005
 */
public class ProcessInfoHandler {

	protected ProcessInfo pi;
	protected MPInstance pinstance;
	protected Hashtable param;
	protected MProcess process;
	
	public ProcessInfoHandler(int processID) {
	
		init(processID);
	}
	
	private void init(int processID) {
		
		process = new MProcess(Env.getCtx(), processID, null);

		if(process != null) {
			
			pi = getProcessInfo(Msg.translate(Env.getCtx(), process.getName()), process.get_ID());
			pinstance = getProcessInstance(pi);
			pi.setAD_PInstance_ID (pinstance.getAD_PInstance_ID());
		}
	}
	
	protected ProcessInfo getProcessInfo(String name, int id) {
		
		ProcessInfo info = new ProcessInfo(name, id);
		info.setAD_User_ID (Env.getAD_User_ID(Env.getCtx()));
		info.setAD_Client_ID(Env.getAD_Client_ID(Env.getCtx()));
		
		return info;
	}
	
	protected MPInstance getProcessInstance(ProcessInfo info) {
		
		MPInstance instance = new MPInstance(Env.getCtx(), info.getAD_Process_ID(), info.getRecord_ID());
		if (!instance.save()) {
			
			info.setSummary (Msg.getMsg(Env.getCtx(), "ProcessNoInstance"));
			info.setError (true);
			return null;
		}
		
		return instance;
	}

	protected int countParams() {
		
		return (process != null) ? process.getParameters().length : 0;
	}
	
	protected Hashtable extractParameters() {
		
		Hashtable param = new Hashtable();
		
		MPInstancePara p = null;
        int i = 0;
		int b = countParams();
		while(i < b) {
			
			p = new MPInstancePara(getProcessInstance(), i);
			p.load(null);

			param.put(p.getParameterName(), getValueFrom(p));
			i++;
		}

		return param;
	}
	
	protected Object getValueFrom(MPInstancePara p) {

		Object o = null;
		
		o = (o == null) ? p.getP_Date() : o;
		o = (o == null) ? p.getP_Date_To() : o;
		o = (o == null) ? p.getP_Number() : o;
		o = (o == null) ? p.getP_Number_To() : o;
		o = (o == null) ? p.getP_String() : o;
		o = (o == null) ? p.getP_String_To() : o;
		
		return o;
	}
	
	public void setProcessError() {
		
		pi.setSummary(Msg.getMsg(Env.getCtx(), "ProcessCancelled"));
		pi.setError(true);
	}
	
	public MPInstance getProcessInstance() {
		
		return pinstance;
	}

	public ProcessInfo getProcessInfo() {
		
		return pi;
	}
	
	public Object getParameterValue(String param) {
		
		if(this.param == null) {
			
			this.param = extractParameters();
		}
		
		return this.param.get(param);
	}
	
	public Enumeration getParameters() {
		
		if(this.param == null) {
			
			this.param = extractParameters();
		}

		return param.keys();
	}
}
