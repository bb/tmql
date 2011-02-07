/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.components.runtime.module.translator.impl.axis;

import java.text.MessageFormat;

import de.topicmapslab.tmql4j.components.processor.core.IContext;
import de.topicmapslab.tmql4j.components.processor.runtime.ITMQLRuntime;
import de.topicmapslab.tmql4j.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.from.FromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.core.selection.Selection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.IFromPart;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISelection;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.ISqlDefinition;
import de.topicmapslab.tmql4j.sql.path.components.definition.model.SqlTables;

/**
 * @author Sven Krosse
 * 
 */
public class ScopeAxisTranslator extends AxisTranslatorImpl {

	static final String SCOPEABLES = "scopeables";
	static final String REL_THEMES = "rel_themes";

	static final String FORWARD_SELECTION = "id_theme";
	static final String BACKWARD_SELECTION = "id";

	static final String CONDITION = "{0}.id_scope = {1}.id_scope";
	static final String FORWARD_CONDITION = "{0} = {1}.id";
	static final String BACKWARD_CONDITION = "{0} = {1}.id_theme";

	/**
	 * {@inheritDoc}
	 */
	protected ISqlDefinition forward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		/*
		 * append from clauses
		 */
		IFromPart fromPartRel = new FromPart(REL_THEMES, result.getAlias(), true);
		result.addFromPart(fromPartRel);
		IFromPart fromPart = new FromPart(SCOPEABLES, result.getAlias(), true);
		result.addFromPart(fromPart);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		result.add(MessageFormat.format(FORWARD_CONDITION, selection.getSelection(), fromPart.getAlias()));
		result.add(MessageFormat.format(CONDITION, fromPart.getAlias(), fromPartRel.getAlias()));
		/*
		 * add new selection
		 */
		result.addSelection(new Selection(FORWARD_SELECTION, fromPartRel.getAlias()));
		result.setCurrentTable(SqlTables.TOPIC);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	protected ISqlDefinition backward(ITMQLRuntime runtime, IContext context, String optionalType, ISqlDefinition definition) throws TMQLRuntimeException {
		ISqlDefinition result = definition.clone();
		result.clearSelection();
		/*
		 * append from clauses
		 */
		IFromPart fromPartRel = new FromPart(REL_THEMES, result.getAlias(), true);
		result.addFromPart(fromPartRel);
		IFromPart fromPart = new FromPart(SCOPEABLES, result.getAlias(), true);
		result.addFromPart(fromPart);
		/*
		 * append condition as connection to incoming SQL definition
		 */
		ISelection selection = definition.getLastSelection();
		result.add(MessageFormat.format(BACKWARD_CONDITION, selection.getSelection(), fromPartRel.getAlias()));
		result.add(MessageFormat.format(CONDITION, fromPart.getAlias(), fromPartRel.getAlias()));
		/*
		 * add new selection
		 */
		result.addSelection(new Selection(BACKWARD_SELECTION, fromPart.getAlias()));
		result.setCurrentTable(SqlTables.ANY);
		return result;
	}
}
