package de.tu_darmstadt.kom.stackoverflow.utility;

/**
 * @author Asit
 * Exeception class for Applicaton
 *
 */

public class AppExceptions extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor 
	 * @param msg String message
	 */
	public AppExceptions(String msg) {
		super(msg);
	}
	
	/**
	 * Creates PostProcessing object 
	 * @param msg String message
	 * @param t Throwable object
	 */

	public AppExceptions(String msg, Throwable t) {
		super(msg, t);
	}
}
