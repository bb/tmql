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
package de.topicmapslab.tmql4j.path.components.processor.results;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.processor.results.IResultProcessor;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;

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
	 * @param clazz
	 *            the class of the result set to generate
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
	public void proceed(QueryMatches matches, Class<? extends IResultSet<?>> clazz) throws TMQLRuntimeException {
		try {
			/*
			 * create new instance of result set
			 */
			resultSet = clazz.getConstructor().newInstance();
			/*
			 * get ordered keys of querying matches
			 */
			List<String> keys = matches.getOrderedKeys();
			/*
			 * iterate over query matches per tuple
			 */
			for (Map<String, Object> tuple : matches) {
				/*
				 * create new instance of result
				 */
				IResult result = resultSet.getResultClass().getConstructor()
						.newInstance();
				/*
				 * iterate over values of current tuple
				 */
				for (String key : keys) {
					Object value = tuple.get(key);
					/*
					 * add values as collection or as atomic value
					 */
					if (value instanceof Collection<?>) {
						result.add(((Collection<?>) value));
					} else {
						result.add(value);
					}
				}
				/*
				 * add result to result set
				 */
				resultSet.addResult(result);
			}

			/*
			 * check if result set should be reduced to two dimensions
			 */
			if (resultSet.canReduceTo2Dimensions()) {
				resultSet.reduceTo2Dimensions();
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
}
