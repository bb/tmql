package de.topicmapslab.tmql4j.draft2010.interpreter.functions.string;

import java.math.BigInteger;
import java.util.Map;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntime;
import de.topicmapslab.tmql4j.common.utility.HashUtil;
import de.topicmapslab.tmql4j.common.utility.VariableNames;
import de.topicmapslab.tmql4j.draft2010.expressions.FunctionCall;
import de.topicmapslab.tmql4j.draft2010.interpreter.base.BaseFunctionInterpreter;
import de.topicmapslab.tmql4j.interpreter.core.base.QueryMatches;
import de.topicmapslab.tmql4j.interpreter.utility.operation.LiteralUtils;

/**
 * Interpreter implementation of function 'string-length'.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class StringLengthFunctionInterpreter extends
		BaseFunctionInterpreter<FunctionCall> {

	/**
	 * constructor
	 * 
	 * @param ex
	 *            the function to interpret
	 */
	public StringLengthFunctionInterpreter(FunctionCall ex) {
		super(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public String getItemIdentifier() {
		return "string-length";
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExpectedNumberOfParameters(long numberOfParameters) {
		return numberOfParameters == 1;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public void interpret(TMQLRuntime runtime) throws TMQLRuntimeException {
		/*
		 * get arguments
		 */
		QueryMatches[] arguments = extractArguments(runtime);

		/*
		 * execute function operation
		 */
		QueryMatches matches = new QueryMatches(runtime);
		for (Object o : arguments[0].getPossibleValuesForVariable()) {
			final String value = LiteralUtils.asString(o);
			Map<String, Object> result = HashUtil.getHashMap();
			result.put(QueryMatches.getNonScopedVariable(), BigInteger
					.valueOf(value.length()));
			matches.add(result);
		}
		/*
		 * store result
		 */
		runtime.getRuntimeContext().peek().setValue(VariableNames.QUERYMATCHES,
				matches);
	}

}
