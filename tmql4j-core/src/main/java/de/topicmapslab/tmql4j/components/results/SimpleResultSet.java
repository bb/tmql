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

import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.processor.results.ResultSet;
import de.topicmapslab.tmql4j.components.processor.results.ResultType;

/**
 * Class representing a simple result set similar to a table result of a data
 * base. The result set only contains a number of simple tuple results.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SimpleResultSet extends ResultSet<SimpleResult> {

	/**
	 * base constructor create an empty result set
	 */
	public SimpleResultSet() {
		// VOID
	}

	/**
	 * {@inheritDoc}
	 */
	public String getResultType() {
		return ResultType.TMAPI.name();
	}

	/**
	 * {@inheritDoc}
	 */
	public IResult createResult() {
		return new SimpleResult(this);
	}
}
