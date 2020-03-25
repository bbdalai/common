package jpackage3.test;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
// --- <<IS-END-IMPORTS>> ---

public final class java

{
	// ---( internal utility methods )---

	final static java _instance = new java();

	static java _newInstance() { return new java(); }

	static java _cast(Object o) { return (java)o; }

	// ---( server methods )---




	public static final void getP (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getP)>> ---
		// @sigtype java 3.5
		System.out.println("getP");
		
		jpackage2.test.java.getX(pipeline);
		jpackage2.test.java.getY(pipeline);
		jpackage1.test.java.fileInfo(pipeline);
		jpackage1.test.java.getFileName(pipeline);
		// --- <<IS-END>> ---

                
	}



	public static final void getQ (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getQ)>> ---
		// @sigtype java 3.5
		System.out.println("getQ");
		// --- <<IS-END>> ---

                
	}
}

