/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.insert.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import org.junit.Test;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.results.SimpleResultSet;
import de.topicmapslab.tmql4j.path.query.TMQLQuery;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestInsertExpression extends Tmql4JTestCase {

	@Test
	public void testInsertTopic() throws Exception {

		String subjectIdentifier = "http://psi.example.org/topic";
		assertNull(topicMap.getTopicBySubjectIdentifier(topicMap
				.createLocator(subjectIdentifier)));

		String query = null;
		SimpleResultSet set = null;

		query = " INSERT ''' " + subjectIdentifier + ". '''";
		set = execute(new TMQLQuery(topicMap,query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(1L, set.first().first());

		assertNotNull(topicMap.getTopicBySubjectIdentifier(topicMap
				.createLocator(subjectIdentifier)));
	}

	@Test
	public void testInsertAnAssociation() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("myType");

		assertEquals(0, topic.getRolesPlayed().size());

		String query = null;
		SimpleResultSet set = null;

		query = " INSERT ''' " + base + "myType ( " + base + "myType : "
				+ base + "myTopic ) '''";
		set = execute(new TMQLQuery(topicMap,query));
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(3L, set.first().first());

		assertEquals(1, topic.getRolesPlayed().size());
		assertEquals(1, topic.getRolesPlayed(type).size());
		assertEquals(type, topic.getRolesPlayed(type).iterator().next()
				.getParent().getType());
	}

}
