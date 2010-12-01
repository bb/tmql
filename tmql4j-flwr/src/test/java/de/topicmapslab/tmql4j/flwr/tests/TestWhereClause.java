/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.flwr.tests;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Association;
import org.tmapi.core.Topic;

import de.topicmapslab.identifier.TmdmSubjectIdentifier;
import de.topicmapslab.majortom.util.FeatureStrings;
import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.path.components.processor.results.SimpleResultSet;
import de.topicmapslab.tmql4j.util.HashUtil;
import static junit.framework.Assert.*;
/**
 * @author Sven Krosse
 * 
 */
public class TestWhereClause extends Tmql4JTestCase {

	@Test
	public void testPredicateStrictAsFlwrPart() throws Exception {
		Topic type = createTopicBySI("myType");
		Topic topic = createTopicBySI("myTopic");
		topic.addType(type);
		Topic roleType = createTopicBySI("roleType");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			Association assocaition = createAssociation(topic);
			assocaition.createRole(roleType, topics[i]);

			assocaition = createAssociation(topic);
			assocaition.createRole(roleType, topics[i]);
			assocaition.createRole(createTopic(), createTopic());
		}
		String query = null;
		SimpleResultSet set = null;
		Set<Topic> result = null;

		query = "FOR $var IN // tm:subject WHERE myTopic ( roleType : $var ) RETURN $var";
		set = execute(query);
		assertEquals(topics.length, set.size());

		result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testPredicateNonStrictAsFlwrPart() throws Exception {
		Topic type = createTopicBySI("myType");
		Topic topic = createTopicBySI("myTopic");
		topic.addType(type);
		Topic roleType = createTopicBySI("roleType");
		Topic[] topics = new Topic[100];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			Association assocaition = createAssociation(topic);
			assocaition.createRole(roleType, topics[i]);

			if (i % 2 == 0) {
				assocaition.createRole(createTopic(), createTopic());
			}
		}
		String query = null;
		SimpleResultSet set = null;
		Set<Topic> result = null;

		query = "FOR $var IN // tm:subject WHERE myTopic ( roleType : $var , ... ) RETURN $var";
		set = execute(query);
		assertEquals(topics.length, set.size());

		result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testAkoNCL() throws Exception {
		String query = null;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			addSupertype(t, topic);
			topics.add(t);
		}

		query = "WHERE $var AKO myType RETURN $var";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set.getResults()) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	@Test
	public void testIsaNCL() throws Exception {
		String query = null;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(topic);
			topics.add(t);
		}

		query = "WHERE $var ISA myType  RETURN $var";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set.getResults()) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	@Test
	public void testCountFunction() throws Exception {
		String query;
		SimpleResultSet set = null;

		Topic topicBySL = createTopicBySL("loc");

		query = "FOR $t IN // tm:subject WHERE fn:count( $t >>locators ) > 0 RETURN $t";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topicBySL, set.first().first());
	}

	@Test
	public void testConjunction() throws Exception {
		String query;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Topic supertype = createTopicBySI("mySupertype");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			t.addType(topic);
			addSupertype(t, supertype);
			topics.add(t);
		}

		query = "FOR $t IN // tm:subject WHERE $t ISA myType AND $t AKO mySupertype RETURN $t";
		set = execute(query);
		assertEquals(100, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	@Test
	public void testDisjunction() throws Exception {
		String query;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Topic supertype = createTopicBySI("mySupertype");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.addType(topic);
			} else {
				addSupertype(t, supertype);
			}
			topics.add(t);
		}

		query = "FOR $t IN // tm:subject WHERE $t ISA myType OR $t AKO mySupertype RETURN $t";
		set = execute(query);
		assertEquals(100, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	@Test
	public void testNegation() throws Exception {
		String query;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.addType(topic);
			} else {
				topics.add(t);
			}
		}
		int nr = 51;
		Set<Topic> others = HashUtil.getHashSet();
		others.add(topic);
		if (factory
				.getFeature(FeatureStrings.TOPIC_MAPS_TYPE_INSTANCE_ASSOCIATION)) {
			others.add(topicMap.getTopicBySubjectIdentifier(topicMap
					.createLocator(TmdmSubjectIdentifier.TMDM_TYPE_INSTANCE_ASSOCIATION_TYPE)));
			others.add(topicMap.getTopicBySubjectIdentifier(topicMap
					.createLocator(TmdmSubjectIdentifier.TMDM_TYPE_ROLE_TYPE)));
			others.add(topicMap.getTopicBySubjectIdentifier(topicMap
					.createLocator(TmdmSubjectIdentifier.TMDM_INSTANCE_ROLE_TYPE)));
			nr += 3;
		}

		query = "FOR $t IN // tm:subject WHERE NOT $t ISA myType RETURN $t";
		set = execute(query);
		assertEquals(nr, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()) || others.contains(r.first()));

		}
	}
	
	@Test
	public void testCrambledBoolean() throws Exception {
		String query;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.addType(topic);
			} else {
				t.addType(other);
			}
			if (i % 3 == 0) {
				t.createName("Name");
				topics.add(t);
			}
		}

		query = "FOR $t IN // tm:subject WHERE ( $t ISA myType OR $t ISA other ) AND EXISTS $t / tm:name RETURN $t";
		set = execute(query);
		assertEquals(topics.size(), set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	@Test
	public void testAtLeast() throws Exception {
		String query;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Topic supertype = createTopicBySI("mySupertype");
		Set<Topic> topics = HashUtil.getHashSet();
		addSupertype(topic, supertype);
		addSupertype(other, supertype);
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.addType(topic);
				t.createName("Name");
				topics.add(t);
			} else {
				t.addType(other);
			}
		}

		query = "FOR $t IN // tm:subject WHERE AT LEAST 1 $i IN $t >> instances SATISFIES $i >> characteristics tm:name RETURN $t";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic, set.first().first());
	}

	@Test
	public void testAtMost() throws Exception {
		String query;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Topic supertype = createTopicBySI("mySupertype");
		Set<Topic> topics = HashUtil.getHashSet();
		addSupertype(topic, supertype);
		addSupertype(other, supertype);
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.addType(topic);
				t.createName("Name");
			} else {
				t.addType(other);
			}
			topics.add(t);
		}
		int nr = 103;
		Set<Topic> others = HashUtil.getHashSet();
		others.add(supertype);
		others.add(other);
		others.add(topicMap.getTopicBySubjectIdentifier(topicMap
				.createLocator(TmdmSubjectIdentifier.TMDM_DEFAULT_NAME_TYPE)));
		if (factory
				.getFeature(FeatureStrings.TOPIC_MAPS_TYPE_INSTANCE_ASSOCIATION)) {
			others.add(topicMap.getTopicBySubjectIdentifier(topicMap
					.createLocator(TmdmSubjectIdentifier.TMDM_TYPE_INSTANCE_ASSOCIATION_TYPE)));
			others.add(topicMap.getTopicBySubjectIdentifier(topicMap
					.createLocator(TmdmSubjectIdentifier.TMDM_TYPE_ROLE_TYPE)));
			others.add(topicMap.getTopicBySubjectIdentifier(topicMap
					.createLocator(TmdmSubjectIdentifier.TMDM_INSTANCE_ROLE_TYPE)));
			nr += 3;
		}
		if (factory
				.getFeature(FeatureStrings.TOPIC_MAPS_SUPERTYPE_SUBTYPE_ASSOCIATION)) {
			others.add(topicMap.getTopicBySubjectIdentifier(topicMap
					.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_SUBTYPE_ASSOCIATION)));
			others.add(topicMap.getTopicBySubjectIdentifier(topicMap
					.createLocator(TmdmSubjectIdentifier.TMDM_SUPERTYPE_ROLE_TYPE)));
			others.add(topicMap.getTopicBySubjectIdentifier(topicMap
					.createLocator(TmdmSubjectIdentifier.TMDM_SUBTYPE_ROLE_TYPE)));
			nr += 3;
		}

		query = "FOR $t IN // tm:subject WHERE AT MOST 1 $i IN $t >> instances SATISFIES fn:count( $i >> characteristics tm:name ) > 0 RETURN $t";
		set = execute(query);
		assertEquals(nr, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertTrue(others.contains(r.first()) || topics.contains(r.first()));
			assertNotSame(topic, r.first());
		}
	}

	@Test
	public void testSome() throws Exception {
		String query;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Topic supertype = createTopicBySI("mySupertype");
		Set<Topic> topics = HashUtil.getHashSet();
		addSupertype(topic, supertype);
		addSupertype(other, supertype);
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.addType(topic);
				t.createName("Name");
				topics.add(t);
			} else {
				t.addType(other);
			}
		}

		query = "FOR $t IN // tm:subject WHERE SOME $i IN $t >> instances SATISFIES fn:count( $i >> characteristics tm:name ) > 0 RETURN $t";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic, set.first().first());
	}

	@Test
	public void testForAll() throws Exception {
		String query;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Topic supertype = createTopicBySI("mySupertype");
		Set<Topic> topics = HashUtil.getHashSet();
		addSupertype(topic, supertype);
		addSupertype(other, supertype);
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.addType(topic);
				t.createName("Name");
				topics.add(t);
			} else {
				t.addType(other);
			}
		}

		query = "FOR $t IN // tm:subject WHERE EVERY $i IN $t >> instances SATISFIES fn:count( $i >> characteristics tm:name ) > 0 RETURN $t";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(topic, set.first().first());
	}

	@Test
	public void testExists() throws Exception {
		String query;
		SimpleResultSet set = null;
		Topic topic = createTopicBySI("myType");
		Topic other = createTopicBySI("other");
		Topic supertype = createTopicBySI("mySupertype");
		Set<Topic> topics = HashUtil.getHashSet();
		addSupertype(topic, supertype);
		addSupertype(other, supertype);
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.addType(topic);
				t.createName("Name");
				topics.add(t);
			} else {
				t.addType(other);
			}
		}

		query = "FOR $t IN // tm:subject WHERE EXISTS $t >> instances >> characteristics tm:name RETURN $t";
		set = execute(query);
		assertEquals(50, set.size());
		for ( IResult r : set ){
			assertEquals(1, r.size());
			assertEquals(topic, r.first());
		}
		

		query = "FOR $t IN // tm:subject WHERE $t >> instances >> characteristics tm:name RETURN $t";
		set = execute(query);
		assertEquals(50, set.size());
		for ( IResult r : set ){
			assertEquals(1, r.size());
			assertEquals(topic, r.first());
		}
	}
}
