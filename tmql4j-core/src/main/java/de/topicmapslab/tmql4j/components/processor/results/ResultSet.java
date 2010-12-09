/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.components.processor.results;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Base implementation of {@link IResultSet} to implement the method providing
 * base functionality. The result set represents a 2-dimension container
 * containing a number of {@link IResult}s. functionality.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 * @param <T>
 *            the type of contained results
 */
public abstract class ResultSet<T extends IResult> implements IResultSet<T> {

	/**
	 * list of contained results
	 */
	private final List<T> results;

	/**
	 * internal iterator instance
	 */
	private Iterator<T> iterator;
	/**
	 * the type of contained results
	 */
	private final Class<T> clazz;

	/**
	 * base constructor to create a new instance
	 */
	@SuppressWarnings("unchecked")
	public ResultSet() {
		/*
		 * get current class
		 */
		Class<?> clazz = getClass();
		/*
		 * walk through class hierarchy till the superclass is a parameterized
		 * type
		 */
		while (!(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
			clazz = clazz.getSuperclass();
		}

		/*
		 * extract class information from type
		 */
		this.clazz = (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];

		/*
		 * create result list
		 */
		this.results = new LinkedList<T>();

		/*
		 * create iterator instance
		 */
		this.iterator = results.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public void addResults(Collection<T> results) {
		for (T result : results) {
			addResult(result);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void addResults(T... results) {
		for (T result : results) {
			addResult(result);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends T> getResultClass() {
		return clazz;
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator<T> iterator() {
		return results.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public T first() throws NoSuchElementException {
		iterator = results.iterator();
		return iterator.next();
	}

	/**
	 * {@inheritDoc}
	 */
	public T next() throws NoSuchElementException {
		return iterator.next();
	}

	/**
	 * {@inheritDoc}
	 */
	public T last() throws NoSuchElementException {
		try {
			return this.results.get(results.size() - 1);
		} catch (IndexOutOfBoundsException e) {
			throw new NoSuchElementException(e.getLocalizedMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void addResult(IResult result) {
		this.results.add((T) result);
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return results.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Method return a string representation of the result set. The string
	 * representation is a comma-separated list of the string-representations of
	 * all contained results. The returned pattern is { &lt; results &gt; }.
	 * 
	 * @return the string representation of the result set
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\r\n");
		Iterator<T> iterator = iterator();
		while (iterator.hasNext()) {
			builder.append(iterator.next().toString() + (iterator.hasNext() ? "," : "") + "\r\n");
		}
		builder.append("}");
		return builder.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return results.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (clazz.isInstance(obj)) {
			return clazz.cast(obj).getResults().equals(results);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void clear() {
		this.results.clear();
		this.iterator = this.results.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> getResults() {
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	public T get(int index) {
		if (getResults().size() <= index) {
			throw new IndexOutOfBoundsException("Result set does not contains an element at position '" + index + "'.");
		}
		return getResults().get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <R extends Object> R get(int rowIndex, int colIndex) {
		T result = get(rowIndex);
		return (R) result.get(colIndex);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isNullValue(int rowIndex, int colIndex) {
		Object obj = get(rowIndex, colIndex);
		return obj == null;
	}

	private static ResultSet<?> emptyResultSet;

	/**
	 * Returns an empty unmodifiable result set.
	 * 
	 * @return the result set
	 */
	public static ResultSet<?> emptyResultSet() {
		if (emptyResultSet == null) {
			emptyResultSet = new ResultSet<IResult>() {
				public String getResultType() {
					return ResultType.TMAPI.name();
				}

				/**
				 * {@inheritDoc}
				 */
				public void addResult(IResult result) {
					throw new UnsupportedOperationException("Unmodifiable result set does not supports method add.");
				}

				/**
				 * {@inheritDoc}
				 */
				public void addResults(Collection<IResult> results) {
					throw new UnsupportedOperationException("Unmodifiable result set does not supports method add.");
				}

				/**
				 * {@inheritDoc}
				 */
				public void addResults(IResult... results) {
					throw new UnsupportedOperationException("Unmodifiable result set does not supports method add.");
				}

				/**
				 * {@inheritDoc}
				 */
				public Class<? extends IResult> getResultClass() {
					return IResult.class;
				}
				
				/**
				 * {@inheritDoc}
				 */
				public void unify() {
					// NOTHING TO DO					
				}
				
				/**
				 * {@inheritDoc}
				 */
				public boolean isNullValue(int rowIndex, int colIndex) {					
					return false;
				}
			};
		}
		return emptyResultSet;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void unify() {
		List<T> results = HashUtil.getList();
		for ( T result : getResults()){
			if ( !results.contains(result)){
				results.add(result);
			}
		}
		this.results.clear();
		this.results.addAll(results);
	}

}
