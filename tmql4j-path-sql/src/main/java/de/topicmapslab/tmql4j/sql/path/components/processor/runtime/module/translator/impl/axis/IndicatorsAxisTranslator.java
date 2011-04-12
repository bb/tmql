/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.processor.runtime.module.translator.impl.axis;

import de.topicmapslab.tmql4j.sql.path.utils.ISchema;

/**
 * @author Sven Krosse
 * 
 */
public class IndicatorsAxisTranslator extends IdentityAxisTranslator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getRelationColumn() {
		return ISchema.RelSubjectIdentifiers.ID_TOPIC;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getRelationTable() {
		return ISchema.RelSubjectIdentifiers.TABLE;
	}

}
