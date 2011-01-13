/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.axis;

import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.IState;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.IState.State;

/**
 * @author Sven Krosse
 * 
 */
public class ScopeAxisTranslator extends AxisTranslatorImpl {

	static final String FORWARD = "SELECT id_theme FROM rel_themes AS r, scopeables AS s WHERE s.id_scope = r.id_scope AND id IN ( {0} )";
	static final String BACKWARD =  "SELECT id FROM rel_themes AS r, scopeables AS s WHERE s.id_scope = r.id_scope AND id_theme IN ( {0} )";

	/**
	 * {@inheritDoc}
	 */
	protected String getForward(IState state) {
		return FORWARD;
	}

	/**
	 * {@inheritDoc}
	 */
	protected String getBackward(IState state) {
		return BACKWARD;
	}

	/**
	 * {@inheritDoc}
	 */
	protected State getBackwardState() {
		return State.ANY;
	}

	/**
	 * {@inheritDoc}
	 */
	protected State getForwardState() {
		return State.TOPIC;
	}

}
