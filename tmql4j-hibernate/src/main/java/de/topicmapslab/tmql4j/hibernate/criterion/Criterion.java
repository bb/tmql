/**
 * 
 */
package de.topicmapslab.tmql4j.hibernate.criterion;

import de.topicmapslab.tmql4j.hibernate.IHibernateConstants;
import de.topicmapslab.tmql4j.hibernate.exception.InvalidModelException;

/**
 * @author Sven Krosse
 * 
 */
public class Criterion implements ICriterion {

	private final String value;

	/**
	 * constructor
	 * 
	 * @param value
	 *            the value to set
	 */
	public Criterion(final String value) {
		this.value = escape(value);
	}

	/**
	 * constructor
	 * 
	 * @param value
	 *            the value to set
	 */
	public Criterion(final Number value) {
		this.value = value.toString();
	}

	/**
	 * method to escape a string value
	 * 
	 * @param value
	 *            the value
	 * @return the string
	 */
	public static String escape(final String value) {
		if (value.contains(IHibernateConstants.TRIPLE_QUOTE)) {
			return IHibernateConstants.TRIPLE_QUOTE
					+ value.replace(IHibernateConstants.QUOTE, IHibernateConstants.ESCAPED_QUOTE)
					+ IHibernateConstants.TRIPLE_QUOTE;
		} else if (value.contains(IHibernateConstants.QUOTE)) {
			return IHibernateConstants.TRIPLE_QUOTE + value + IHibernateConstants.TRIPLE_QUOTE;
		}
		return IHibernateConstants.QUOTE + value + IHibernateConstants.QUOTE;

	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public String toTmql() throws InvalidModelException {
		return value;
	}
}
