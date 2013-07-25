package zquirrel.util.datasource;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

public interface HibDao<T extends Serializable> {

	/**
	 * Find an entity of specific restriction.
	 * 
	 * @param restriction
	 *            the restriction.
	 * @return the entity.
	 */
	public abstract T find(Criterion restriction);

	/**
	 * Find an entity.
	 * 
	 * @param id
	 *            the id property.
	 * @return the entity.
	 */
	public abstract T find(Serializable id);

	/**
	 * Obtain a filtered list of the entity.
	 * 
	 * @param criterion
	 *            the restriction to limit the result range.
	 * @return the list of entities.
	 */
	public abstract List<T> list(Criterion criterion);

	/**
	 * Obtain a filtered list of the entity.
	 * 
	 * @param criterion
	 *            the restriction to limit the result range.
	 * @param order
	 *            the order used to organize th result.
	 * @param start
	 *            the first row to fetch.
	 * @param length
	 *            the max results to fetch.
	 * @return the list of entities.
	 */
	public abstract List<T> list(Criterion criterion, Order order, int start,
			int length);

	/**
	 * Obtain a filtered list of the entity with start and length.
	 * 
	 * @param criterion
	 *            the restriction to limit the result range.
	 * @return the list of entities.
	 */
	public abstract List<T> list(Criterion criterion, int start, int length);

	/**
	 * List all the entities.
	 * 
	 * @return the list of all entities.
	 */
	public abstract List<T> listAll();

	/**
	 * Obtain a list of entities of a specific range.
	 * 
	 * @param start
	 *            the start of the range.
	 * @param length
	 *            the length of the range.
	 * @return the list of entities.
	 */
	public abstract List<T> list(int start, int length);

	/**
	 * Save the entity to the database.
	 * 
	 * @param object
	 *            the entity to be saved.
	 * @return the new id.
	 */
	public abstract Object save(T object);

	/**
	 * Update the specific entity.
	 * 
	 * @param object
	 *            the entity to be updated
	 * @return the status sign.
	 */
	public abstract int update(T object);

	/**
	 * Delete the specific entity from the database.
	 * 
	 * @param obj
	 *            the entity to be deleted
	 * @return the status sign.
	 */
	public abstract int delete(T obj);

	/**
	 * Get the number of users.
	 * 
	 * @return the number of users.
	 */
	public abstract int count();

	/**
	 * Get the number of users.
	 * 
	 * @param restrictions
	 *            the restriction condition.
	 * @return the number of users.
	 */
	public abstract int count(Criterion restrictions);

}