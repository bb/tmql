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
package de.topicmapslab.tmql4j.path.grammar.lexical;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.grammar.lexical.Token;

public class Variable extends Token {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isToken(ITMQLRuntime runtime, String literal) {
		return literal.length() > 1
				&& (literal.startsWith("$") || literal.startsWith("%") || literal
						.startsWith("@"))
				&& !literal.equalsIgnoreCase("%prefix")
				&& !literal.equalsIgnoreCase("%pragma");
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLiteral() {
		return "$...";
	}

}
