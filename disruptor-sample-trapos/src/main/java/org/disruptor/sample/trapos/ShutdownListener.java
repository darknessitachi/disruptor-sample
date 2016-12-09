package org.disruptor.sample.trapos;

/**
 * Used by the gateway to terminate the system on the special
 * command
 * <pre>
 *   C|STOP
 * </pre>
 * @author ewhite
 */
public interface ShutdownListener {
	
	void notifyShutdown();
}
