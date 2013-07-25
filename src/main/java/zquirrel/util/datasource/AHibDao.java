package zquirrel.util.datasource;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

/**
 * A handful generalized facade of data access object.
 * 
 * @author yfwz100
 * 
 * @param <T>
 *            the entity.
 */
public class AHibDao<T extends Serializable> implements HibDao<T> {

	// the log.
	private final static Log log = LogFactory.getLog(AHibDao.class);

	/**
	 * The entity class.
	 */
	private Class<T> clazz;

	/**
	 * The previous error.
	 */
	private Throwable error = null;

	/**
	 * Construct a new HibernateDao object of specific entity class.
	 * 
	 * @param clazz
	 *            the entity class.
	 */
	public AHibDao(Class<T> clazz) {
		this.clazz = clazz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * zquirrel.util.datasource.HibDao#find(org.hibernate.criterion.Criterion)
	 */
	@Override
	public T find(Criterion restriction) {
		Session session = HibernateUtil.getSession();
		T obj = null;
		try {
			Criteria query = session.createCriteria(clazz);
			query.add(restriction);
			obj = clazz.cast(query.uniqueResult());
		} catch (HibernateException e) {
			log.debug(e);
			error = e;
		} finally {
			HibernateUtil.closeSession();
		}
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zquirrel.util.datasource.HibDao#find(java.io.Serializable)
	 */
	@Override
	public T find(Serializable id) {
		Session session = HibernateUtil.getSession();
		T obj = null;
		try {
			session.beginTransaction();
			obj = clazz.cast(session.get(clazz, id));
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			error = e;
		} finally {
			HibernateUtil.closeSession();
		}
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * zquirrel.util.datasource.HibDao#list(org.hibernate.criterion.Criterion)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> list(Criterion criterion) {
		Session session = HibernateUtil.getSession();
		List<T> emps = null;
		try {
			session.beginTransaction();
			Criteria query = session.createCriteria(clazz);
			query.add(criterion);
			emps = query.list();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			error = e;
		} finally {
			HibernateUtil.closeSession();
		}
		return emps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * zquirrel.util.datasource.HibDao#list(org.hibernate.criterion.Criterion,
	 * int, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> list(Criterion criterion, int start, int length) {
		Session session = HibernateUtil.getSession();
		List<T> emps = null;
		try {
			session.beginTransaction();

			Criteria query = session.createCriteria(clazz);
			query.add(criterion);
			query.setFirstResult(start);
			query.setMaxResults(length);

			emps = query.list();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			error = e;
		} finally {
			HibernateUtil.closeSession();
		}
		return emps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zquirrel.util.datasource.HibDao#listAll()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> listAll() {
		Session session = HibernateUtil.getSession();
		List<T> emps = null;
		try {
			session.beginTransaction();
			Criteria query = session.createCriteria(clazz);
			emps = query.list();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			error = e;
		} finally {
			HibernateUtil.closeSession();
		}
		return emps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zquirrel.util.datasource.HibDao#list(int, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> list(int start, int length) {
		Session session = HibernateUtil.getSession();
		List<T> emps = null;
		try {
			session.beginTransaction();
			Criteria query = session.createCriteria(clazz);
			query.setFirstResult(start);
			query.setMaxResults(length);

			log.debug("start: " + start + "; length: " + length);

			emps = query.list();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			error = e;
		} finally {
			HibernateUtil.closeSession();
		}
		return emps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zquirrel.util.datasource.HibDao#save(T)
	 */
	@Override
	public Object save(T object) {
		Session session = HibernateUtil.getSession();
		Object id = null;
		try {
			session.beginTransaction();
			id = session.save(object);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			error = e;
		} finally {
			HibernateUtil.closeSession();
		}
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zquirrel.util.datasource.HibDao#update(T)
	 */
	@Override
	public int update(T object) {
		Session session = HibernateUtil.getSession();
		try {
			session.beginTransaction();
			session.update(object);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			error = e;
		} finally {
			HibernateUtil.closeSession();
		}
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zquirrel.util.datasource.HibDao#delete(T)
	 */
	@Override
	public int delete(T obj) {
		Session session = HibernateUtil.getSession();
		try {
			session.beginTransaction();
			session.delete(obj);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			error = e;
		} finally {
			HibernateUtil.closeSession();
		}
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zquirrel.util.datasource.HibDao#count()
	 */
	@Override
	public int count() {
		int count = -1;
		try {
			Session session = HibernateUtil.getSession();
			Criteria query = session.createCriteria(clazz);
			query.setProjection(Projections.rowCount());
			count = (Integer) query.uniqueResult();
		} catch (HibernateException e) {
			log.debug(e);
			error = e;
		} finally {
			HibernateUtil.closeSession();
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * zquirrel.util.datasource.HibDao#count(org.hibernate.criterion.Criterion)
	 */
	@Override
	public int count(Criterion restrictions) {
		int count = 0;
		try {
			Session session = HibernateUtil.getSession();
			Criteria query = session.createCriteria(clazz);
			query.setProjection(Projections.rowCount()).add(restrictions);
			count = (Integer) query.uniqueResult();
		} catch (HibernateException e) {
			log.debug(e);
			error = e;
		} finally {
			HibernateUtil.closeSession();
		}
		return count;
	}

	public Throwable getError() {
		return error;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> list(Criterion criterion, Order order, int start, int length) {
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		List<T> li = null;
		try {
			li = session.createCriteria(clazz).add(criterion).addOrder(order)
					.setFirstResult(start).setMaxResults(length).list();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			error = e;
		} finally {
			HibernateUtil.closeSession();
		}
		return li;
	}

}
