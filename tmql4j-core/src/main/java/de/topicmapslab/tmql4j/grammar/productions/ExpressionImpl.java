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
package de.topicmapslab.tmql4j.grammar.productions;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.components.processor.util.CollectionsUtility;
import de.topicmapslab.tmql4j.exception.TMQLGeneratorException;
import de.topicmapslab.tmql4j.exception.TMQLInvalidSyntaxException;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.extensions.ILanguageExtension;
import de.topicmapslab.tmql4j.extensions.ILanguageExtensionEntry;
import de.topicmapslab.tmql4j.grammar.lexical.IToken;

/**
 * Base implementation of {@link IExpression} representing one specific
 * production rule of the TMQL language draft. The expression also represent one
 * node of the parsing-tree generated by the parser instance. The instance
 * encapsulates all tokens containing to the production rule, all variables and
 * provide utility functions to access information.
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public abstract class ExpressionImpl implements IExpression {

	private static final long serialVersionUID = 1L;
	/**
	 * a list of contained expression
	 */
	private final List<IExpression> children;
	/**
	 * the list of language-specific tokens contained by this expression
	 */
	private final List<Class<? extends IToken>> tmqlTokens;
	/**
	 * the list of string-represented tokens contained by this expression
	 */
	private final List<String> tokens;
	/**
	 * the grammar type of the production rule
	 */
	private int type;
	/**
	 * the parent node
	 */
	private final IExpression parent;
	/**
	 * the TMQL runtime
	 */
	private final ITMQLRuntime runtime;

	/**
	 * base constructor to create a new expression without sub-nodes
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
	public ExpressionImpl(IExpression parent,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			ITMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {
		this.children = new LinkedList<IExpression>();
		this.tmqlTokens = tmqlTokens;
		this.tokens = tokens;
		this.parent = parent;
		this.runtime = runtime;
		/*
		 * check if syntax is valid
		 */
		if (!isValid()) {
			throw new TMQLInvalidSyntaxException(tmqlTokens, tokens,
					getClass(), "Expression is invalid.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final void addExpression(IExpression ex) {
		this.children.add(ex);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<IExpression> getExpressions() {
		return children;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Class<? extends IToken>> getTmqlTokens() {
		return tmqlTokens;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getTokens() {
		return tokens;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getGrammarType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return getClass().getSimpleName();
	}

	/**
	 * {@inheritDoc}
	 */
	public IExpression getParent() {
		return parent;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setGrammarType(int type) {
		this.type = type;
	}

	/**
	 * Method checks if the current expression is valid in context of the
	 * current TMQL draft and their extensions.
	 * 
	 * @return <code>true</code> if the expression is valid, <code>false</code>
	 *         otherwise.
	 */
	abstract public boolean isValid();

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends IExpression> List<T> getExpressionFilteredByType(
			Class<? extends T> type) {
		/*
		 * iterate over list and extract the matching expressions
		 */
		List<T> expressions = new LinkedList<T>();
		for (IExpression ex : children) {
			if (ex.getClass().equals(type)) {
				expressions.add((T) ex);
			}
		}
		return expressions;
	}

	/**
	 * {@inheritDoc}
	 */
	public ITMQLRuntime getRuntime() {
		return runtime;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getVariables() {
		List<String> variables = new LinkedList<String>();
		/*
		 * iterate over string-represented tokens
		 */
		for (String token : getTokens()) {
			/*
			 * check if token is already known as variable
			 */
			if (!variables.contains(token)) {
				/*
				 * check if token is variable, has to start with % , $ or @
				 */
				if (token.startsWith("$") || token.startsWith("%")
						|| token.startsWith("@")) {
					variables.add(token);
				}
			}
		}

		return variables;
	}

	/**
	 * {@inheritDoc}
	 */
	public final void setExpressions(List<IExpression> reorder)
			throws TMQLRuntimeException {
		/*
		 * check if lists are equal
		 */
		if (!CollectionsUtility.isContentEqual(children, reorder)) {
			throw new TMQLRuntimeException(
					"Content of new reordered sub-tree is not equal to contained sub-tree");
		}
		this.children.clear();
		this.children.addAll(reorder);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return this.tokens.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IExpression) {
			return ((IExpression) obj).getTokens().equals(tokens);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void checkForExtensions(Class<? extends IExpression> clazz,
			List<Class<? extends IToken>> tmqlTokens, List<String> tokens,
			ITMQLRuntime runtime) throws TMQLInvalidSyntaxException,
			TMQLGeneratorException {

		boolean useDefault = true;

		/*
		 * get all extensions
		 */
		Set<ILanguageExtension> extensions = runtime.getExtensionPointAdapter()
				.getLanguageExtensions(clazz);
		if (extensions != null) {
			/*
			 * iterate over extensions
			 */
			for (ILanguageExtension extension : extensions) {
				/*
				 * get extension point entry
				 */
				ILanguageExtensionEntry entry = extension
						.getLanguageExtensionEntry();
				/*
				 * check if production is valid for current extension
				 */
				if (entry.isValidProduction(runtime, tmqlTokens, tokens, this)) {
					entry.parse(runtime, tmqlTokens, tokens, this, true);
					useDefault = false;
					break;
				}
			}
		}

		/*
		 * use default expression?
		 */
		if (useDefault) {
			try {
				/*
				 * get constructor
				 */
				Constructor<? extends IExpression> constructor = clazz
						.getConstructor(IExpression.class, List.class,
								List.class, ITMQLRuntime.class);
				/*
				 * create new instance
				 */
				IExpression ex = constructor.newInstance(this, tmqlTokens,
						tokens, runtime);
				/*
				 * add expression as child
				 */
				addExpression(ex);
			} catch (Exception e) {
				throw new TMQLGeneratorException(e);
			}
		}

	}

	/**
	 * Method checks if the current expression is a child of the specified
	 * expression type. The method walks up to the tree root and check all
	 * parent types.
	 * 
	 * @param clazz
	 *            the expression type
	 * @return <code>true</code> if the current expression is a child of the
	 *         specified type, <code>false</code> otherwise.
	 */
	public boolean isChildOf(Class<? extends IExpression> clazz) {
		IExpression parent = getParent();
		while (parent != null) {
			if (parent.getClass().isAssignableFrom(clazz)) {
				return true;
			}
			parent = parent.getParent();
		}
		return false;
	}

}
