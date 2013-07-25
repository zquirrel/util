package zquirrel.util.datasource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static SessionFactory sessionFactory;
	private static ThreadLocal<Session> localSession;
	
	static {
		Configuration configuration = new Configuration().configure();
		sessionFactory = configuration.buildSessionFactory();
		localSession = new ThreadLocal<Session>();
	}

	private HibernateUtil() {
	}
	
	public static Session getSession() {
		Session session = localSession.get();
		if (session == null) {
			session = sessionFactory.openSession();
			localSession.set(session);
		}
		return session;
	}
	
	public static void closeSession() {
		Session session = localSession.get();
		if (session != null && session.isOpen()) {
			session.close();
			localSession.set(null);
		}
	}

}
