package com.listwidget.server.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Embedded;
import javax.persistence.Transient;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.helper.DAOBase;
import com.listwidget.domain.AppUser;
import com.listwidget.domain.ItemList;
import com.listwidget.domain.ListItem;
import com.listwidget.shared.TooManyResultsException;

/**
 * Generic DAO for use with Objectify
 * 
 * @author turbomanage
 *
 * @param <T>
 */
public class ObjectifyDao<T> extends DAOBase
{

	static final int BAD_MODIFIERS = Modifier.FINAL | Modifier.STATIC | Modifier.TRANSIENT;

	static
	{
		ObjectifyService.register(ItemList.class);
		ObjectifyService.register(AppUser.class);
//		ObjectifyService.register(ListItem.class);
	}

	protected Class<T> clazz;

	public ObjectifyDao()
	{
		clazz = ((Class) ((ParameterizedType) getClass().getGenericSuperclass())
			.getActualTypeArguments()[0]);
	}
	
	public Key<T> put(T entity)

	{
		return ofy().put(entity);
	}

	public Map<Key<T>, T> putAll(Iterable<T> entities)
	{
		return ofy().put(entities);
	}

	public void delete(T entity)
	{
		ofy().delete(entity);
	}

	public void deleteKey(Key<T> entityKey)
	{
		ofy().delete(entityKey);
	}

	public void deleteAll(Iterable<T> entities)
	{
		ofy().delete(entities);
	}

	public void deleteKeys(Iterable<Key<T>> keys)
	{
		ofy().delete(keys);
	}

	public T get(Long id) throws EntityNotFoundException
	{
		return ofy().get(this.clazz, id);
	}

	public T get(Key<T> key) throws EntityNotFoundException
	{
		return ofy().get(key);
	}

	public Map<Key<T>, T> get(Iterable<Key<T>> keys)
	{
		return ofy().get(keys);
	}

	public List<T> listAll()
	{
		Query<T> q = ofy().query(clazz);
		return q.list();
	}

	/**
	 * Convenience method to get all objects matching a single property
	 *
	 * @param propName
	 * @param propValue
	 * @return T matching Object
	 * @throws TooManyResultsException
	 */
	public T getByProperty(String propName, Object propValue) throws TooManyResultsException
	{
		Query<T> q = ofy().query(clazz);
		q.filter(propName, propValue);
		Iterator<T> fetch = q.limit(2).fetch().iterator();
		if (!fetch.hasNext())
		{
			return null;
		}
		T obj = fetch.next();
		if (fetch.hasNext())
		{
			throw new TooManyResultsException(q.toString() + " returned too many results");
		}
		return obj;
	}

	public List<T> listByProperty(String propName, Object propValue)
	{
		Query<T> q = ofy().query(clazz);
		q.filter(propName, propValue);
		return asList(q.fetch());
	}

	public List<Key<T>> listKeysByProperty(String propName, Object propValue)
	{
		Query<T> q = ofy().query(clazz);
		q.filter(propName, propValue);
		return asKeyList(q.fetchKeys());
	}

	public T getByExample(T exampleObj) throws TooManyResultsException
	{
		Query<T> q = buildQueryByExample(exampleObj);
		Iterator<T> fetch = q.limit(2).fetch().iterator();
		if (!fetch.hasNext())
		{
			return null;
		}
		T obj = fetch.next();
		if (fetch.hasNext())
		{
			throw new TooManyResultsException(q.toString() + " returned too many results");
		}
		return obj;
	}

	public List<T> listByExample(T exampleObj)
	{
		Query<T> queryByExample = buildQueryByExample(exampleObj);
		return asList(queryByExample.fetch());
	}

//	public T getByOwner(AppUser u) throws TooManyResultsException
//	{
//		Key<AppUser> userKey = new Key<AppUser>(AppUser.class, u.getId());
//		return getByProperty("ownerKey", userKey);
//	}
//
//	public List<T> listByOwner(AppUser u)
//	{
//		Key<AppUser> userKey = new Key<AppUser>(AppUser.class, u.getId());
//		return listByProperty("ownerKey", userKey);
//	}

	public Key<T> getKey(Long id)
	{
		return new Key<T>(this.clazz, id);
	}

	public Key<T> key(T obj)
	{
		return ObjectifyService.factory().getKey(obj);
	}

	public List<T> listChildren(Object parent)
	{
		return asList(ofy().query(clazz).ancestor(parent).fetch());
	}

	public List<Key<T>> listChildKeys(Object parent)
	{
		return asKeyList(ofy().query(clazz).ancestor(parent).fetchKeys());
	}

	protected List<T> asList(Iterable<T> iterable)
	{
		ArrayList<T> list = new ArrayList<T>();
		for (T t : iterable)
		{
			list.add(t);
		}
		return list;
	}

	protected List<Key<T>> asKeyList(Iterable<Key<T>> iterableKeys)
	{
		ArrayList<Key<T>> keys = new ArrayList<Key<T>>();
		for (Key<T> key : iterableKeys)
		{
			keys.add(key);
		}
		return keys;
	}

	protected Query<T> buildQueryByExample(T exampleObj)
	{
		Query<T> q = ofy().query(clazz);

		// Add all non-null properties to query filter
		for (Field field : clazz.getDeclaredFields())
		{
			// Ignore transient, embedded, array, and collection properties
			if (field.isAnnotationPresent(Transient.class)
				|| (field.isAnnotationPresent(Embedded.class)) || (field.getType().isArray())
				|| (Collection.class.isAssignableFrom(field.getType()))
				|| ((field.getModifiers() & BAD_MODIFIERS) != 0))
				continue;

			field.setAccessible(true);

			Object value;
			try
			{
				value = field.get(exampleObj);
			}
			catch (IllegalArgumentException e)
			{
				throw new RuntimeException(e);
			}
			catch (IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
			if (value != null)
			{
				q.filter(field.getName(), value);
			}
		}

		return q;
	}
}
