package zquirrel.util.context;

import java.lang.reflect.Method;

/**
 * The intercepter for context.
 * 
 * @author plux
 * 
 */
public interface ContextIntercepter {

	/**
	 * Intercept the method calling stack. Engaged with chain design pattern.
	 * 
	 * @param target
	 *            the target object.
	 * @param method
	 *            the method/behaviour of the target object.
	 * @param args
	 *            the parameters/arguments.
	 * @param chain
	 *            the chain.
	 * @return
	 */
	Object intercept(Object target, Method method, Object[] args,
			TargetChain chain);

	/**
	 * The chain class, which eventually invokes the method.
	 * 
	 * @author plux
	 * 
	 */
	interface TargetChain {

		/**
		 * Chain the calling of specific object.
		 * 
		 * @param target
		 *            the target object.
		 * @param method
		 *            the method/behaviour of the target object.
		 * @param args
		 *            the parameters/arguments.
		 * @return
		 */
		Object chain(Object target, Method method, Object[] args);
	}
}
