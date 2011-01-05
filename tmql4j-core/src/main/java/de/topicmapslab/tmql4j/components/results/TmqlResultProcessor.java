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
package de.topicmapslab.tmql4j.components.results;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.processor.results.IResultProcessor;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.components.processor.results.ProjectionUtils;
import de.topicmapslab.tmql4j.components.processor.results.ResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Base implementation of {@link IResultProcessor}. A result processor transform
 * the tuple sequences generated by the interpretation task to a
 * {@link IResultSet}.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TmqlResultProcessor implements IResultProcessor {

	private Lock lock = new ReentrantLock(true);
	
	/**
	 * the state of auto-reduction
	 */
	private boolean autoReduction = true;
	/**
	 * the class of used result set
	 */
	private Class<? extends IResultSet<?>> resultSetClass = SimpleResultSet.class;

	/**
	 * map to store alias of columns
	 */
	private Map<Integer, String> columnAlias;

	/**
	 * map to store alias of columns
	 */
	private Map<String, Integer> aliasIndex;

	/**
	 * the generated result set
	 */
	private IResultSet<?> resultSet;

	/**
	 * the TMQL4J runtime
	 */
	private final ITMQLRuntime runtime;

	/**
	 * base constructor to create a new instance
	 * 
	 * @param <T>
	 *            the type of contained result type of the result set
	 * @param runtime
	 *            the TMQL4J runtime
	 */
	public <T extends IResult> TmqlResultProcessor(final ITMQLRuntime runtime) {
		this.runtime = runtime;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends IResultSet<?>> T getResultSet() {
		return (T) resultSet;
	}

	/**
	 * {@inheritDoc}
	 */
	public void proceed(QueryMatches matches) throws TMQLRuntimeException {
		try {
			List<List<Object>> results = null;
			if (autoReduction) {
				results = ProjectionUtils.asTwoDimensional(matches);
			} else {
				results = ProjectionUtils.asNDimensional(matches);
			}
			/*
			 * create new instance of result set
			 */
			resultSet = resultSetClass.getConstructor().newInstance();
			/*
			 * set alias
			 */
			if ( resultSet instanceof ResultSet<?>){
				((ResultSet<?>) resultSet).setAlias(aliasIndex);
			}
			/*
			 * iterate over query matches per tuple
			 */
			for (List<Object> tuple : results) {
				/*
				 * create new instance of result
				 */
				IResult result = resultSet.createResult();
				result.add(tuple);
				/*
				 * add result to result set
				 */
				resultSet.addResult(result);
			}
		} catch (Exception e) {
			throw new TMQLRuntimeException("Failed to generate result set", e);
		}
	}

	/**
	 * @return the runtime
	 */
	public ITMQLRuntime getRuntime() {
		return runtime;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setResultType(Class<? extends IResultSet<?>> clazz) {
		this.resultSetClass = clazz;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAutoReduction(boolean autoReduction) {
		this.autoReduction = autoReduction;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setColumnAlias(int index, String alias) {		
		while(!lock.tryLock()){
			// WAIT
		}		
		if (columnAlias == null) {
			columnAlias = HashUtil.getHashMap();
		}
		columnAlias.put(index, alias);
		if (aliasIndex == null) {
			aliasIndex = HashUtil.getHashMap();
		}
		aliasIndex.put(alias, index);
		lock.unlock();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isKnownAlias(String alias) {
		return aliasIndex != null && aliasIndex.containsKey(alias);
	}
}
