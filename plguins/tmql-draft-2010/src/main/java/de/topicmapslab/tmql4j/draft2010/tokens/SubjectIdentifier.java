package de.topicmapslab.tmql4j.draft2010.tokens;

import de.topicmapslab.tmql4j.lexer.model.Token;

public class SubjectIdentifier extends Token {

	/**
	 * {@inheritDoc}
	 */
	
	public String getLiteral() {
		return "subject-identifier";
	}

}
