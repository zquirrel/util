package zquirrel.util.context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The service context for application.
 * 
 * @author plux
 * 
 */
public abstract class ServiceContextSupport {

	/**
	 * The interface and implementation map.
	 */
	private static final Map<Class<?>, Class<?>> contextMap = new HashMap<Class<?>, Class<?>>();
	/**
	 * The local instance pool.
	 */
	private static final ThreadLocal<Map<Class<?>, Object>> localMap = new ThreadLocal<Map<Class<?>, Object>>();
	/**
	 * The intercepter list.
	 */
	private List<ContextIntercepter> intercepters = new LinkedList<ContextIntercepter>();

	/**
	 * The logging tool.
	 */
	private static final Log log = LogFactory
			.getLog(ServiceContextSupport.class);

	/**
	 * The service context.
	 */
	protected ServiceContextSupport() {
	}

	/**
	 * Get the certain object of the certain type.
	 * 
	 * @param <T>
	 *            the type of the object you want.
	 * @param itfCls
	 *            the class.
	 * @return the instance of T
	 */
	public <T> T getInstance(Class<T> itfCls) {
		// obtain the local object map.
		Map<Class<?>, Object> map = localMap.get();

		if (map == null) {
			// register one if needed.
			map = new HashMap<Class<?>, Object>();
			localMap.set(map);
		}

		Object object = map.get(itfCls);
		if (object == null || itfCls.isInstance(object)) {
			try {
				final Object target = contextMap.get(itfCls).newInstance();
				final TargetChainImpl targetChainImpl = new TargetChainImpl(
						intercepters.iterator());
				object = Proxy.newProxyInstance(this.getClass()
						.getClassLoader(), new Class[] { itfCls },
						new InvocationHandler() {
							public Object invoke(final Object proxy,
									final Method method, final Object[] args)
									throws Throwable {
								return targetChainImpl.chain(target, method,
										args);
							}
						});
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			map.put(itfCls, object);
		}

		return itfCls.cast(object);
	}

	/**
	 * Register the interface with an implementation class.
	 * 
	 * @param itf
	 *            the service interface class.
	 * @param impl
	 *            the service implementation class.
	 */
	public <T> void register(Class<T> itf, Class<? extends T> impl) {
		log.debug("Register the interface.");
		contextMap.put(itf, impl);
	}

	/**
	 * Unregister the interface.
	 * 
	 * @param itf
	 *            the service interface class.
	 */
	public <T> void unregister(Class<T> itf) {
		log.debug("Unregister the interface.");
		contextMap.remove(itf);
	}

	public ServiceContextSupport addIntercepter(ContextIntercepter intercepter) {
		intercepters.add(intercepter);
		return this;
	}

	public ServiceContextSupport removeIntercepter(
			ContextIntercepter intercepter) {
		intercepters.remove(intercepter);
		return this;
	}

	private class TargetChainImpl implements ContextIntercepter.TargetChain {

		private final Iterator<ContextIntercepter> intercepters;

		TargetChainImpl(Iterator<ContextIntercepter> intercepters) {
			super();
			this.intercepters = intercepters;
		}

		public Object chain(Object target, Method method, Object[] args) {
			if (intercepters.hasNext()) {
				ContextIntercepter intercepter = intercepters.next();
				return intercepter.intercept(target, method, args, this);
			} else {
				try {
					return method.invoke(target, args);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

}
