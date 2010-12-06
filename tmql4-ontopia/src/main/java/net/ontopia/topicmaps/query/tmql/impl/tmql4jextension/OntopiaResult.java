/**
 * TMQL4J Plugin for Ontopia
 * 
 * Copyright: Copyright 2009 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * Author: Sven Krosse
 * 
 */
package net.ontopia.topicmaps.query.tmql.impl.tmql4jextension;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.tmapi.core.Association;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Topic;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.components.processor.results.IResult;

public class OntopiaResult implements IResult {

	private final List<Object> results;
	private final List<Object> ontopiaStringRepresentations;
	private Iterator<Object> iterator;

	/**
	 * constructor
	 */
	public OntopiaResult() {
		this.results = new LinkedList<Object>();
		this.ontopiaStringRepresentations = new LinkedList<Object>();
	}

	/**
	 * constructor
	 * 
	 * @param results
	 *            the results
	 */
	public OntopiaResult(Collection<Object> results) {
		this.results = new LinkedList<Object>();
		this.ontopiaStringRepresentations = new LinkedList<Object>();
		this.results.addAll(results);
		for (Object o : results) {
			this.ontopiaStringRepresentations.add(tmapiObjectToString(o));
		}
	}

	/**
	 * constructor
	 * 
	 * @param results
	 *            the results
	 */
	public OntopiaResult(Object... results) {
		this.results = Arrays.asList(results);
		this.ontopiaStringRepresentations = new LinkedList<Object>();
		for (Object o : results) {
			this.ontopiaStringRepresentations.add(tmapiObjectToString(o));
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public Object first() {
		return ontopiaStringRepresentations.iterator().next();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public Object last() {
		return ontopiaStringRepresentations.get(ontopiaStringRepresentations.size() - 1);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public Iterator<Object> iterator() {
		return ontopiaStringRepresentations.iterator();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public void add(Object... values) {
		results.addAll(Arrays.asList(values));
		for (Object o : values) {
			add(o);
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		Iterator<Object> iterator = iterator();
		while (iterator.hasNext()) {
			builder.append(iterator.next().toString() + (iterator.hasNext() ? ", " : ""));
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public int size() {
		return this.results.size();
	}

	/**
	 * Returns all values
	 * 
	 * @return all values
	 */
	public Object[] getValues() {
		return ontopiaStringRepresentations.toArray();
	}

	/**
	 * Returns the values at the specified positions
	 * 
	 * @param indizes
	 *            the indexes
	 * @return the values
	 */
	public Object[] getValues(Integer... indizes) {
		List<Object> values = new LinkedList<Object>();
		for (Integer index : indizes) {
			if (index < size()) {
				values.add(ontopiaStringRepresentations.get(index));
			}
		}
		return values.toArray();
	}

	/**
	 * Returns the value at the specific index
	 * 
	 * @param index
	 *            the index
	 * @return the value or <code>null</code>
	 */
	public Object getValue(Integer index) {
		if (index < size()) {
			return ontopiaStringRepresentations.get(index);
		}
		return null;
	}

	/**
	 * Utility method to convert given object to a string
	 * 
	 * @param object
	 *            the object
	 * @return the string and never <code>null</code>
	 */
	private String tmapiObjectToString(Object object) {
		if (object instanceof Topic) {
			Topic topic = (Topic) object;
			if (!topic.getItemIdentifiers().isEmpty()) {
				return topic.getItemIdentifiers().iterator().next().getReference();
			} else if (!topic.getSubjectIdentifiers().isEmpty()) {
				return topic.getSubjectIdentifiers().iterator().next().getReference();
			} else if (!topic.getSubjectLocators().isEmpty()) {
				return topic.getSubjectLocators().iterator().next().getReference();
			}
		} else if (object instanceof Association) {
			Association association = (Association) object;
			if (!association.getItemIdentifiers().isEmpty()) {
				return association.getItemIdentifiers().iterator().next().getReference();
			}
		} else if (object instanceof Name) {
			return ((Name) object).getValue();
		} else if (object instanceof Variant) {
			return ((Variant) object).getValue();
		} else if (object instanceof Occurrence) {
			return ((Occurrence) object).getValue();
		} else if (object instanceof Role) {
			Role role = (Role) object;
			return tmapiObjectToString(role.getType());
		} else if (object instanceof Locator) {
			return ((Locator) object).getReference();
		}
		return "FAILED: " + object.toString();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public List<Object> getResults() {
		return results;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void add(Object o) {
		this.ontopiaStringRepresentations.add(tmapiObjectToString(o));
		iterator = null;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void add(Collection<Object> values) {
		add(values.toArray());
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Object next() throws NoSuchElementException {
		if (iterator == null) {
			iterator = ontopiaStringRepresentations.iterator();
		}
		return iterator.next();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> T get(int index) {
		if (getResults().size() <= index) {
			throw new IndexOutOfBoundsException("Result does not contains an element at position '" + index + "'.");
		}
		return (T) getResults().get(index);
	}

}
