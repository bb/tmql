/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.majortom.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Association;
import org.tmapi.core.Topic;

import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * 
 */
public class TestIndexFunction extends Tmql4JTestCase {

	@Test
	public void testCharacteristicTypes() throws Exception {
		Topic t = createTopic();
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic type = createTopic();
			if (i % 2 == 0) {
				t.createName(type, "Name" + i);
			} else {
				t.createOccurrence(type, "Value" + i);
			}
			topics.add(type);
		}

		final String query = "fn:get-characteristic-types()";
		IResultSet<?> rs = execute(query);
		assertEquals(100, rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	@Test
	public void testNameTypes() throws Exception {
		Topic t = createTopic();
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic type = createTopic();
			t.createName(type, "Name" + i);
			topics.add(type);
		}

		final String query = "FOR $t IN fn:get-name-types() RETURN $t";
		IResultSet<?> rs = execute(query);
		assertEquals(100, rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	@Test
	public void testOccurrenceTypes() throws Exception {
		Topic t = createTopic();
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic type = createTopic();
			t.createOccurrence(type, "Value" + i);
			topics.add(type);
		}

		final String query = "FOR $t IN fn:get-occurrence-types() RETURN $t";
		IResultSet<?> rs = execute(query);
		assertEquals(100, rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	@Test
	public void testTopicTypes() throws Exception {
		Topic t = createTopic();
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic type = createTopic();
			t.addType(type);
			topics.add(type);
		}

		final String query = "FOR $t IN fn:get-topic-types() RETURN $t";
		IResultSet<?> rs = execute(query);
		assertEquals(100, rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	@Test
	public void testAssociationTypes() throws Exception {
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic type = createTopic();
			createAssociation(type);
			topics.add(type);
		}

		final String query = "FOR $t IN fn:get-association-types() RETURN $t";
		IResultSet<?> rs = execute(query);
		assertEquals(100, rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	@Test
	public void testRoleTypes() throws Exception {
		Association a = createAssociation();
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic type = createTopic();
			a.createRole(type, createTopic());
			topics.add(type);
		}

		final String query = "FOR $t IN fn:get-role-types() RETURN $t";
		IResultSet<?> rs = execute(query);
		assertEquals(100, rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	@Test
	public void testGetSupertypes() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic oTopic = createTopicBySI("myOtherTopic");
		Set<Topic> supertypes = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic st = createTopic();
			if (i % 2 == 0) {
				addSupertype(oTopic, st);
			} else {
				addSupertype(topic, st);
			}
			supertypes.add(st);
		}

		String query = "fn:get-supertypes()";
		IResultSet<?> rs = execute(query);
		assertEquals(supertypes.size(), rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(supertypes.contains(r.first()));
		}

		query = "fn:get-supertypes( myTopic )";
		rs = execute(query);
		assertEquals(50, rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(supertypes.contains(r.first()));
		}

		query = "fn:get-supertypes( myOtherTopic )";
		rs = execute(query);
		assertEquals(50, rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(supertypes.contains(r.first()));
		}

		query = "fn:get-supertypes( fn:get-subtypes() )";
		rs = execute(query);
		assertEquals(100, rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(supertypes.contains(r.first()));
		}
	}

	@Test
	public void testGetSubtypes() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic oTopic = createTopicBySI("myOtherTopic");
		Set<Topic> subtypes = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic st = createTopic();
			if (i % 2 == 0) {
				addSupertype(st, oTopic);
			} else {
				addSupertype(st, topic);
			}
			subtypes.add(st);
		}

		String query = "fn:get-subtypes()";
		IResultSet<?> rs = execute(query);
		assertEquals(subtypes.size(), rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(subtypes.contains(r.first()));
		}

		query = "fn:get-subtypes( myTopic )";
		rs = execute(query);
		assertEquals(50, rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(subtypes.contains(r.first()));
		}

		query = "fn:get-subtypes( myOtherTopic )";
		rs = execute(query);
		assertEquals(50, rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(subtypes.contains(r.first()));
		}

		query = "fn:get-subtypes( fn:get-supertypes() )";
		rs = execute(query);
		assertEquals(100, rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(subtypes.contains(r.first()));
		}
	}

	@Test
	public void testTopicByNames() throws Exception {
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.createName("Name");
				topics.add(t);
			} else {
				t.createName("Value");
			}

		}

		final String query = "FOR $t IN fn:get-topics-by-name-value( \"Name\" ) RETURN $t";
		IResultSet<?> rs = execute(query);
		assertEquals(topics.size(), rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}
	
	@Test
	public void testTopicByNameRegExp() throws Exception {
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.createName("Name" + i);
				topics.add(t);
			} else {
				t.createName("Value");
			}

		}

		final String query = "FOR $t IN fn:get-topics-by-name-regular-expression( \"Name.*\" ) RETURN $t";
		IResultSet<?> rs = execute(query);
		assertEquals(topics.size(), rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}
	
	@Test
	public void testTopicByOccurrences() throws Exception {
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.createOccurrence(createTopic(),"Name");
				topics.add(t);
			} else {
				t.createOccurrence(createTopic(),"Value");
			}

		}

		final String query = "FOR $t IN fn:get-topics-by-occurrence-value( \"Name\" ) RETURN $t";
		IResultSet<?> rs = execute(query);
		assertEquals(topics.size(), rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}
	
	@Test
	public void testTopicByOccurrenceRegExp() throws Exception {
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				t.createOccurrence(createTopic(),"Name" + i);
				topics.add(t);
			} else {
				t.createOccurrence(createTopic(),"Value");
			}

		}

		final String query = "FOR $t IN fn:get-topics-by-occurrence-regular-expression( \"Name.*\" ) RETURN $t";
		IResultSet<?> rs = execute(query);
		assertEquals(topics.size(), rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}
	
	@Test
	public void testTopicByCharacteristics() throws Exception {
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				if ( i % 4 == 0 ){
					t.createOccurrence(createTopic(),"Name");
				}else{
					t.createName(createTopic(),"Name");
				}
				topics.add(t);
			} else {
				t.createOccurrence(createTopic(),"Value");
			}

		}

		final String query = "FOR $t IN fn:get-topics-by-characteristic-value( \"Name\" ) RETURN $t";
		IResultSet<?> rs = execute(query);
		assertEquals(topics.size(), rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}
	
	@Test
	public void testTopicByCharacteristicsRegExp() throws Exception {
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				if ( i % 4 == 0 ){
					t.createOccurrence(createTopic(),"Name" + i);
				}else{
					t.createName(createTopic(),"Name" + i);
				}
				topics.add(t);
			} else {
				t.createOccurrence(createTopic(),"Value");
			}

		}

		final String query = "FOR $t IN fn:get-topics-by-characteristic-regular-expression( \"Name.*\" ) RETURN $t";
		IResultSet<?> rs = execute(query);
		assertEquals(topics.size(), rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}
	
	@Test
	public void testRatomify() throws Exception {
		Set<Topic> topics = HashUtil.getHashSet();
		for (int i = 0; i < 100; i++) {
			Topic t = createTopic();
			if (i % 2 == 0) {
				if ( i % 4 == 0 ){
					t.createOccurrence(createTopic(),"Name" + i);
				}else{
					t.createName(createTopic(),"Name" + i);
				}
				topics.add(t);
			} else {
				t.createOccurrence(createTopic(),"Value");
			}

		}

		final String query = "\"Name.*\" << ratomify << characteristics";
		IResultSet<?> rs = execute(query);
		assertEquals(topics.size(), rs.size());
		for (IResult r : rs) {
			assertEquals(1, r.size());
			assertTrue(topics.contains(r.first()));
		}
	}

	// @Test
	// public void testNull() throws Exception{
	// final String query = "RETURN fn:get-null-value()";
	// IResultSet<?> rs = execute(query);
	// assertEquals(1, rs.size());
	// assertEquals(1, rs.first().size());
	// assertNull(rs.get(0,0));
	// assertTrue(rs.isNullValue(0, 0));
	// }
}
