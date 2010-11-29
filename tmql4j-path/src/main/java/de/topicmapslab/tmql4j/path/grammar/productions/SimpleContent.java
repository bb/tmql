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
package de.topicmapslab.tmql4j.path.grammar.productions;

import java.util.List;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.core.QueryMatches;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;
import de.topicmapslab.tmql4j.grammar.productions.ExpressionImpl;
import de.topicmapslab.tmql4j.grammar.productions.IExpression;
import de.topicmapslab.tmql4j.path.components.parser.NonCanonicalUtils;
import de.topicmapslab.tmql4j.path.grammar.lexical.ShortcutAxisInstances;

/**
 * Special implementation of {@link ExpressionImpl} representing a
 * simple-content.
 * <p>
 * The grammar production rule of the expression is: <code>
 * <p>
 * simple-content ::= anchor [ navigation ]
 * </p>
 * </code> </p>
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class SimpleContent extends ExpressionImpl {

	/**
	 * base constructor to create a new instance.
	 * 
	 * @param parent
	 *            the known parent node
	 * @param tmqlTokens
	 *            the list of language-specific tokens contained by this
	 *            expression
	 * @param tokens
	 *            the list of string-represented tokens contained by this
	 *            expression
	 * @param runtime
	 *            the TMQL runtime
	 * @throws TMQLInvalidSyntaxException
	 *             thrown if the syntax of the given sub-query is invalid
	 * @throws TMQLGeneratorException
	 *             thrown if the sub-tree can not be generated
	 */
	@SuppressWarnings("unchecked")
	public SimpleContent(IExpression parent, List<Class<? extends IToken>> tmqlTokens, List<String> tokens, ITMQLRuntime runtime) throws TMQLInvalidSyntaxException, TMQLGeneratorException {
		super(parent, tmqlTokens, tokens, runtime);

		/*
		 * temporary token lists
		 */
		List<Class<? extends IToken>> tmqlTokens_ = tmqlTokens;
		List<String> tokens_ = tokens;

		Class<? extends IToken> token = tmqlTokens.get(0);

		/*
		 * simple content is non-canonical expression
		 */
		if (token.equals(ShortcutAxisInstances.class)) {
			List<?>[] lists = NonCanonicalUtils.toCanonicalInstancesAxis(tmqlTokens, tokens);
			tmqlTokens_ = (List<Class<? extends IToken>>) lists[0];
			tokens_ = (List<String>) lists[1];

		}
		/*
		 * create anchor expression
		 */
		checkForExtensions(Anchor.class, tmqlTokens_.subList(0, 1), tokens_.subList(0, 1), runtime);
		/*
		 * check if expression has navigation part
		 */
		if (tmqlTokens_.size() > 1) {
			/*
			 * create navigation expression
			 */
			checkForExtensions(Navigation.class, tmqlTokens_.subList(1, tmqlTokens_.size()), tokens_.subList(1, tokens_.size()), runtime);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		return !getTmqlTokens().isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryMatches interpret(ITMQLRuntime runtime, IContext context, Object... optionalArguments) throws TMQLRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

}
