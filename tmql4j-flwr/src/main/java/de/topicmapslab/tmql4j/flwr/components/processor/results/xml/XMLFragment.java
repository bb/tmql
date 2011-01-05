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
package de.topicmapslab.tmql4j.flwr.components.processor.results.xml;

import de.topicmapslab.tmql4j.components.processor.results.Result;
import de.topicmapslab.tmql4j.components.processor.results.ResultSet;

/**
 * Result implementation representing a XML fragment. The result is generated by
 * the interpretation of xtm-content expressions.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class XMLFragment extends Result {

	/**
	 * base constructor to create an empty result
	 * 
	 * @param parent
	 *            the parent result set
	 */
	public XMLFragment(ResultSet<?> parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return size() == 0 ? "" : getResults().get(0).toString();
	}

}
