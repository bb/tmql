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
package de.topicmapslab.tmql4j.draft2010.grammar.lexical;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.grammar.lexical.Token;

/**
 * The conjunction token AND
 * 
 * @author Sven Krosse
 * 
 */
public class And extends Token {

	/**
	 * the token
	 */
	public static final String TOKEN = "AND";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isToken(ITMQLRuntime runtime, String literal) {
		return literal.equalsIgnoreCase(TOKEN);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteral() {
		return TOKEN;
	}

}
