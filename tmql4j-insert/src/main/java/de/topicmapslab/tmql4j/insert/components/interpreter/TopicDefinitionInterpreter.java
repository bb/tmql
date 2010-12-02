/*
 * TMQL4J - Javabased TMQL Engine
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.insert.components.interpreter;

import java.util.Map;

import org.tmapi.core.Locator;
import org.tmapi.core.TopicMap;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.insert.grammar.productions.TopicDefinition;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisIndicators;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisItem;
import de.topicmapslab.tmql4j.path.grammar.lexical.AxisLocators;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisIndicators;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisItem;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisLocators;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * Special implementation of {@link ExpressionInterpreterImpl} representing the
 * interpreter of a topic-definition as a kind of update-clauses.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * topic-definition ::= string-literal ( ! | ~ | = )
 * topic-definition ::= string-literal << ( item | locators | indicators )
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TopicDefinitionInterpreter extends
		ExpressionInterpreterImpl<TopicDefinition> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the expression to interpret
	 */
	public TopicDefinitionInterpreter(TopicDefinition ex) {
		super(ex);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		TopicMap topicMap = runtime.getTopicMap();
		Class<? extends IToken> identifierType = getExpression()
				.getIdentifierType();

		/*
		 * extract string-represented reference
		 */
		String reference = getTokens().get(0);
		if (reference.startsWith("\"")) {
			reference = reference.substring(1, reference.length() - 1);
		}
		Locator locator = topicMap.createLocator(runtime.getLanguageContext()
				.getPrefixHandler().toAbsoluteIRI(reference));

		/*
		 * store of update-results
		 */
		long count = 0;

		if (identifierType.equals(AxisIndicators.class)
				|| identifierType.equals(ShortcutAxisIndicators.class)) {
			/*
			 * check if there is a topic with the same subject-identifier or
			 * item-identifier
			 */
			if (topicMap.getTopicBySubjectIdentifier(locator) == null
					&& topicMap.getConstructByItemIdentifier(locator) == null) {
				topicMap.createTopicBySubjectIdentifier(locator);
				count++;
			}
		} else if (identifierType.equals(AxisItem.class)
				|| identifierType.equals(ShortcutAxisItem.class)) {
			/*
			 * check if there is a topic with the same subject-identifier or
			 * item-identifier
			 */
			if (topicMap.getTopicBySubjectIdentifier(locator) == null
					&& topicMap.getConstructByItemIdentifier(locator) == null) {
				topicMap.createTopicByItemIdentifier(locator);
				count++;
			}
		} else if (identifierType.equals(AxisLocators.class)
				|| identifierType.equals(ShortcutAxisLocators.class)
				|| getTokens().get(1).equalsIgnoreCase("=")) {
			/*
			 * check if there is a topic with the same subject-locator
			 */
			if (topicMap.getTopicBySubjectLocator(locator) == null) {
				topicMap.createTopicBySubjectLocator(locator);
				count++;
			}
		}

		/*
		 * create result
		 */
		QueryMatches results = new QueryMatches(runtime);
		Map<String, Object> result = HashUtil.getHashMap();
		result.put(QueryMatches.getNonScopedVariable(), count);
		results.add(result);

		/*
		 * set to stack
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				results);

	}

}
