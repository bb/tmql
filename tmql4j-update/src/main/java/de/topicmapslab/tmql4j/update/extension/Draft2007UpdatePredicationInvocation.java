/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.update.extension;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.runtime.module.model.IInterpreterRegistry;
import de.topicmapslab.tmql4j.exception.TMQLExtensionRegistryException;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.extension.ILanguageExtension;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.update.components.interpreter.PredicateInvocationInterpreter;
import de.topicmapslab.tmql4j.update.components.interpreter.PredicateInvocationRolePlayerExpressionInterpreter;
import de.topicmapslab.tmql4j.update.grammar.productions.PredicateInvocation;
import de.topicmapslab.tmql4j.update.grammar.productions.PredicateInvocationRolePlayerExpression;
import de.topicmapslab.tmql4j.update.grammar.productions.UpdateClause;

/**
 * @author Sven Krosse
 * 
 */
public class Draft2007UpdatePredicationInvocation implements ILanguageExtension {

	/**
	 * {@inheritDoc}
	 */
	public void registerExtension(ITMQLRuntime runtime) throws TMQLExtensionRegistryException {

		IInterpreterRegistry interpreterRegistry = runtime.getLanguageContext().getInterpreterRegistry();
		interpreterRegistry.registerInterpreterClass(PredicateInvocation.class, PredicateInvocationInterpreter.class);
		interpreterRegistry.registerInterpreterClass(PredicateInvocationRolePlayerExpression.class, PredicateInvocationRolePlayerExpressionInterpreter.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends IExpression> getExpressionType() {
		return de.topicmapslab.tmql4j.path.grammar.productions.PredicateInvocation.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean extendsExpressionType(Class<? extends IExpression> expressionType) {
		return de.topicmapslab.tmql4j.path.grammar.productions.PredicateInvocation.class.equals(expressionType);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValidProduction(ITMQLRuntime runtime, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, IExpression caller) {
		return !tmqlTokens.isEmpty() && caller instanceof UpdateClause;
	}

	/**
	 * {@inheritDoc}
	 */
	public IExpression parse(ITMQLRuntime runtime, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, IExpression caller, boolean autoAdd) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		IExpression expression = new PredicateInvocation(caller, tmqlTokens, tokens, runtime);
		if (autoAdd) {
			caller.addExpression(expression);
		}
		return expression;
	}

}
