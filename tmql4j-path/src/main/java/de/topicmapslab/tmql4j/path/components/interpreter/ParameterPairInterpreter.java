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
package de.topicmapslab.tmql4j.path.components.interpreter;

import de.topicmapslab.tmql4j.components.interpreter.ExpressionInterpreterImpl;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.path.grammar.productions.ParameterPair;
import de.topicmapslab.tmql4j.path.grammar.productions.ValueExpression;

/**
 * 
 * Special interpreter class to interpret parameter-pairs.
 * 
 * 
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * parameter-pair ::= identifier : value-expression
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class ParameterPairInterpreter extends
		ExpressionInterpreterImpl<ParameterPair> {

	/**
	 * base constructor to create a new instance
	 * 
	 * @param ex
	 *            the expression which shall be interpreted
	 */
	public ParameterPairInterpreter(ParameterPair ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * execute value-expression to extract values of identifier
		 */
		QueryMatches bindings = extractArguments(runtime,
				ValueExpression.class, 0);
		/*
		 * set to stack
		 */
		runtime.getRuntimeContext().peek().setValue(
				VariableNames.QUERYMATCHES, bindings);

	}

}
