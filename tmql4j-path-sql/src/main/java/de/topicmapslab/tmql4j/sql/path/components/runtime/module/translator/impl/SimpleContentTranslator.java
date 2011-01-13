/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.grammar.productions.Anchor;
import de.topicmapslab.tmql4j.path.grammar.productions.Navigation;
import de.topicmapslab.tmql4j.path.grammar.productions.SimpleContent;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.ITranslatorContext;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TmqlSqlTranslatorImpl;
import de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.TranslatorRegistry;

/**
 * @author Sven Krosse
 * 
 */
public class SimpleContentTranslator extends TmqlSqlTranslatorImpl<SimpleContent> {

	/**
	 * {@inheritDoc}
	 */
	public ITranslatorContext transform(ITMQLRuntime runtime, IContext context, IExpression expression, ITranslatorContext state) throws TMQLRuntimeException {

		ITranslatorContext newState = TranslatorRegistry.getTranslator(Anchor.class).transform(runtime, context, expression.getExpressions().get(0), state);
		if (expression.getExpressions().size() > 1) {
			newState = TranslatorRegistry.getTranslator(Navigation.class).transform(runtime, context, expression.getExpressions().get(1), newState);
		}
		return newState;
	}

}
