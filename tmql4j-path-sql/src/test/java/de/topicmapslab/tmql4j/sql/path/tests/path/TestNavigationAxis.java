/** 
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 * 
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package de.topicmapslab.tmql4j.sql.path.tests.path;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.tmapi.core.Association;
import org.tmapi.core.Construct;
import org.tmapi.core.Locator;
import org.tmapi.core.Name;
import org.tmapi.core.Occurrence;
import org.tmapi.core.Role;
import org.tmapi.core.Scoped;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Typed;
import org.tmapi.core.Variant;

import de.topicmapslab.tmql4j.components.processor.results.IResult;
import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.sql.path.tests.Tmql4JTestCase;
import de.topicmapslab.tmql4j.util.HashUtil;

/**
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 * 
 */
public class TestNavigationAxis extends Tmql4JTestCase {

	@Test
	public void testInstancesAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topics[i].addType(topic);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> instances";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testTypesAxisForTopic() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topic.addType(topics[i]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> types";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testTypesAxisForOccurrence() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topic.createOccurrence(topics[i], "Value", new Topic[0]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> characteristics >> types";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testTypesAxisForNames() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topic.createName(topics[i], "Value", new Topic[0]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> characteristics >> types";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testTypesAxisForTopicsBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topics[i].addType(topic);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic << types";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testTypedAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Set<Typed> typed = HashUtil.getHashSet();
		for (int i = 0; i < 10; i++) {
			if (i % 4 == 0) {
				typed.add(createAssociation(topic));
			} else if (i % 4 == 1) {
				typed.add(createTopic().createName(topic, "Name"));
			} else if (i % 4 == 2) {
				typed.add(createTopic().createOccurrence(topic, "Value"));
			} else {
				typed.add(createAssociation().createRole(topic, createTopic()));
			}
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> typed";
		set = execute(query);
		assertEquals(typed.size(), set.size());
		for (IResult r : set.getResults()) {
			assertEquals(1, r.size());
			assertTrue(typed.contains(r.first()));
		}
	}

	@Test
	public void testTypedAxisBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Set<Typed> typed = HashUtil.getHashSet();
		for (int i = 0; i < 10; i++) {
			if (i % 4 == 0) {
				typed.add(createAssociation(topic));
			} else if (i % 4 == 1) {
				typed.add(createTopic().createName(topic, "Name"));
			} else if (i % 4 == 2) {
				typed.add(createTopic().createOccurrence(topic, "Value"));
			} else {
				typed.add(createAssociation().createRole(topic, createTopic()));
			}
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> typed << typed";
		set = execute(query);
		assertEquals(10, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertEquals(topic, r.first());
		}
	}

	@Test
	public void testInstancesAxisForTopics() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topics[i].addType(topic);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> instances";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testSupertypesAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			addSupertype(topic, topics[i]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> supertypes";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testSupertypesAxisBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			addSupertype(topics[i], topic);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic << supertypes";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testSubtypesAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			addSupertype(topics[i], topic);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> subtypes";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testSubtypesAxisBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			addSupertype(topic, topics[i]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic << subtypes";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testPlayersAxisWithoutParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			createAssociation(topic).createRole(createTopic(), topics[i]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> players";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testPlayersAxisWithParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic roleType = createTopicBySI("roleType");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			createAssociation(topic).createRole(roleType, topics[i]);
			createAssociation(topic).createRole(createTopic(), createTopic());
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> players roleType";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testPlayersAxisWithoutParameterBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Association[] associations = new Association[10];
		for (int i = 0; i < associations.length; i++) {
			associations[i] = createAssociation();
			associations[i].createRole(createTopic(), topic);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic << players";
		set = execute(query);
		assertEquals(associations.length, set.size());

		Set<Association> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Association);
			result.add((Association) r.first());
		}

		for (int i = 0; i < associations.length; i++) {
			assertTrue(result.contains(associations[i]));
		}
	}

	@Test
	public void testPlayersAxisWithParameterBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic roleType = createTopicBySI("roleType");
		Association[] associations = new Association[10];
		for (int i = 0; i < associations.length; i++) {
			associations[i] = createAssociation();
			associations[i].createRole(roleType, topic);
			associations[i].createRole(createTopic(), topic);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic << players roleType";
		set = execute(query);
		assertEquals(associations.length, set.size());

		Set<Association> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Association);
			result.add((Association) r.first());
		}

		for (int i = 0; i < associations.length; i++) {
			assertTrue(result.contains(associations[i]));
		}
	}

	@Test
	public void testRolesAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			Association association = createAssociation(topic);
			association.createRole(topics[i], createTopic());
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> roles";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testRolesAxisWithoutParameterBW() throws Exception {
		Topic roleType = createTopicBySI("roleType");
		Association[] associations = new Association[10];
		for (int i = 0; i < associations.length; i++) {
			associations[i] = createAssociation(createTopic());
			associations[i].createRole(roleType, createTopic());
		}
		String query = null;
		IResultSet<?> set = null;

		query = "roleType << roles";
		set = execute(query);
		assertEquals(associations.length, set.size());

		Set<Association> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Association);
			result.add((Association) r.first());
		}

		for (int i = 0; i < associations.length; i++) {
			assertTrue(result.contains(associations[i]));
		}
	}

	@Test
	public void testRolesAxisWithParameterBW() throws Exception {
		Topic roleType = createTopicBySI("roleType");
		Topic topic = createTopicBySI("myTopic");
		Association[] associations = new Association[10];
		for (int i = 0; i < associations.length; i++) {
			associations[i] = createAssociation(topic);
			associations[i].createRole(roleType, createTopic());
			createAssociation().createRole(roleType, createTopic());
		}
		String query = null;
		IResultSet<?> set = null;

		query = "roleType << roles myTopic";
		set = execute(query);
		assertEquals(associations.length, set.size());

		Set<Association> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Association);
			result.add((Association) r.first());
		}

		for (int i = 0; i < associations.length; i++) {
			assertTrue(result.contains(associations[i]));
		}
	}

	@Test
	public void testTraverseAxisWithoutParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			Association a = createAssociation();
			a.createRole(createTopic(), topic);
			a.createRole(createTopic(), topics[i]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> traverse";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testTraverseAxisWithParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("assoType");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			Association a = createAssociation(type);
			a.createRole(createTopic(), topic);
			a.createRole(createTopic(), topics[i]);
			a = createAssociation();
			a.createRole(createTopic(), topic);
			a.createRole(createTopic(), createTopic());
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> traverse assoType";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testTraverseAxisWithoutParameterBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("assoType");
		Topic rtype = createTopicBySI("roleType");
		Association[] associations = new Association[10];
		for (int i = 0; i < associations.length; i++) {
			Association a = createAssociation(type);
			a.createRole(rtype, topic);

			associations[i] = createAssociation();
			associations[i].createRole(createTopic(), topic);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "assoType >> typed << traverse";
		set = execute(query);
		assertEquals(associations.length, set.size());

		Set<Association> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Association);
			result.add((Association) r.first());
		}

		for (int i = 0; i < associations.length; i++) {
			System.out.println(associations[i].getId());
			assertTrue(result.contains(associations[i]));
		}
	}

	@Test
	public void testTraverseAxisWithParameterBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic player = createTopic();
		player.addType(topic);
		Topic type = createTopicBySI("assoType");
		Association[] associations = new Association[10];
		for (int i = 0; i < associations.length; i++) {
			Association a = createAssociation(type);
			a.createRole(createTopic(), player);

			associations[i] = createAssociation();
			associations[i].createRole(createTopic(), player);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "assoType << traverse myTopic";
		set = execute(query);
		/*
		 * delta added to ignore the type-instance association if exists
		 */
		assertEquals(associations.length, set.size(), 1.0);

		Set<Association> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Association);
			result.add((Association) r.first());
		}

		for (int i = 0; i < associations.length; i++) {
			assertTrue(result.contains(associations[i]));
		}
	}

	@Test
	public void testCharacteristicsAxisWithoutParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Construct[] constructs = new Construct[10];
		for (int i = 0; i < constructs.length; i++) {
			if (i % 2 == 0) {
				constructs[i] = topic.createName("Value", new Topic[0]);
			} else {
				constructs[i] = topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			}
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> characteristics";
		set = execute(query);
		assertEquals(constructs.length, set.size());

		Set<Construct> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Construct);
			result.add((Construct) r.first());
		}

		for (int i = 0; i < constructs.length; i++) {
			assertTrue(result.contains(constructs[i]));
		}
	}

	@Test
	public void testCharacteristicsAxisWithParameter() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Construct[] constructs = new Construct[10];
		for (int i = 0; i < constructs.length; i++) {
			if (i % 2 == 0) {
				constructs[i] = topic.createName(type, "Value", new Topic[0]);
				topic.createName(createTopic(), "Value", new Topic[0]);
			} else {
				constructs[i] = topic.createOccurrence(type, "Value", new Topic[0]);
				topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			}
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> characteristics theType";
		set = execute(query);
		assertEquals(constructs.length, set.size());

		Set<Construct> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Construct);
			result.add((Construct) r.first());
		}

		for (int i = 0; i < constructs.length; i++) {
			assertTrue(result.contains(constructs[i]));
		}

		query = "myTopic >> characteristics theOtherType";
		set = execute(query);
		assertEquals(0, set.size());
	}

	@Test
	public void testCharacteristicsAxisWithTmName() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Name[] names = new Name[10];
		for (int i = 0; i < names.length; i++) {
			names[i] = topic.createName(type, "Value", new Topic[0]);
			topic.createOccurrence(type, "Value", new Topic[0]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> characteristics tm:name";
		set = execute(query);
		assertEquals(names.length, set.size());

		Set<Name> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Name);
			result.add((Name) r.first());
		}

		for (int i = 0; i < names.length; i++) {
			assertTrue(result.contains(names[i]));
		}
	}

	@Test
	public void testCharacteristicsAxisWithTmOccurrence() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Occurrence[] occurrences = new Occurrence[10];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(type, "Value", new Topic[0]);
			topic.createName(type, "Value", new Topic[0]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> characteristics tm:occurrence";
		set = execute(query);
		assertEquals(occurrences.length, set.size());

		Set<Occurrence> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Occurrence);
			result.add((Occurrence) r.first());
		}

		for (int i = 0; i < occurrences.length; i++) {
			assertTrue(result.contains(occurrences[i]));
		}
	}

	@Test
	public void testCharacteristicsAxisWithoutParameterBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Occurrence[] occurrences = new Occurrence[10];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(type, "Value", new Topic[0]);
			topic.createName(type, "Value", new Topic[0]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> characteristics << characteristics";
		set = execute(query);
		assertEquals(20, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertEquals(topic, r.first());
		}
	}

	@Test
	public void testCharacteristicsAxisWithParameterBW() throws Exception {
		Topic type = createTopicBySI("theType");
		Topic topic = createTopicBySI("myTopic");
		topic.addType(type);
		Occurrence[] occurrences = new Occurrence[10];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(createTopic(), "Value", new Topic[0]);
			topic.createName(createTopic(), "Value", new Topic[0]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> characteristics << characteristics theType";
		set = execute(query);
		assertEquals(20, set.size());
		for (IResult r : set) {
			assertEquals(1, r.size());
			assertEquals(topic, r.first());
		}
	}

	@Test
	public void testScopeAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Name n = topic.createName("Name", new Topic[0]);
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			n.addTheme(topics[i]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> characteristics >> scope";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}
	}

	@Test
	public void testScopeAxisBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Scoped[] scopeds = new Scoped[10];
		for (int i = 0; i < scopeds.length; i++) {
			if (i % 4 == 0) {
				scopeds[i] = createTopic().createName("Value", new Topic[0]);
			} else if (i % 4 == 1) {
				scopeds[i] = createTopic().createOccurrence(createTopic(), "Value", new Topic[0]);
			} else if (i % 4 == 2) {
				scopeds[i] = createTopic().createName("Value", new Topic[0]).createVariant("Value", createTopic());
			} else {
				scopeds[i] = createAssociation();
			}
			scopeds[i].addTheme(topic);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic << scope";
		set = execute(query);
		assertEquals(scopeds.length, set.size());

		Set<Scoped> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Scoped);
			result.add((Scoped) r.first());
		}

		for (int i = 0; i < scopeds.length; i++) {
			assertTrue(result.contains(scopeds[i]));
		}
	}

	@Test
	public void testLocatorsAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Locator[] locators = new Locator[10];
		for (int i = 0; i < locators.length; i++) {
			locators[i] = createLocator(Integer.toString(i));
			topic.addSubjectLocator(locators[i]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> locators";
		set = execute(query);
		assertEquals(locators.length, set.size());

		Set<Locator> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Locator);
			result.add((Locator) r.first());
		}

		for (int i = 0; i < locators.length; i++) {
			assertTrue(result.contains(locators[i]));
		}
	}

	@Test
	public void testLocatorsAxisBW() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		String query = null;
		IResultSet<?> set = null;

		query = "\"" + base + "myTopic\" << locators";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Topic);
		assertTrue(set.getResults().get(0).first().equals(topic));
	}

	@Test
	public void testIndicatorsAxis() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Locator[] locators = new Locator[10];
		for (int i = 0; i < locators.length; i++) {
			locators[i] = createLocator(Integer.toString(i));
			topic.addSubjectIdentifier(locators[i]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "\"" + base + "myTopic\" << locators >> indicators";
		set = execute(query);
		assertEquals(locators.length, set.size());

		Set<Locator> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Locator);
			result.add((Locator) r.first());
		}

		for (int i = 0; i < locators.length; i++) {
			assertTrue(result.contains(locators[i]));
		}
	}

	@Test
	public void testIndicatorsAxisBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		String query = null;
		IResultSet<?> set = null;

		query = "\"" + base + "myTopic\" << indicators";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Topic);
		assertTrue(set.getResults().get(0).first().equals(topic));
	}

	@Test
	public void testItemAxis() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Locator[] locators = new Locator[10];
		for (int i = 0; i < locators.length; i++) {
			locators[i] = createLocator(Integer.toString(i));
			topic.addItemIdentifier(locators[i]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "\"" + base + "myTopic\" << locators >> item";
		set = execute(query);
		assertEquals(locators.length, set.size());

		Set<Locator> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Locator);
			result.add((Locator) r.first());
		}

		for (int i = 0; i < locators.length; i++) {
			assertTrue(result.contains(locators[i]));
		}
	}

	@Test
	public void testItemAxisBW() throws Exception {
		Topic topic = createTopicByII("myTopic");
		String query = null;
		IResultSet<?> set = null;

		query = "\"" + base + "myTopic\" << item";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Topic);
		assertTrue(set.getResults().get(0).first().equals(topic));
	}

	@Test
	public void testReifierAxis() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		/*
		 * name
		 */
		Name n = createTopic().createName("Value", new Topic[0]);
		n.setReifier(topic);
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> reifier";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Name);
		assertTrue(set.getResults().get(0).first().equals(n));
		n.setReifier(null);

		/*
		 * occurrence
		 */
		Occurrence o = createTopic().createOccurrence(createTopic(), "Value", new Topic[0]);
		o.setReifier(topic);

		query = "myTopic >> reifier";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Occurrence);
		assertTrue(set.getResults().get(0).first().equals(o));
		o.setReifier(null);
		/*
		 * variant
		 */
		Variant v = createTopic().createName("Value", new Topic[0]).createVariant("Value", createTopic());
		v.setReifier(topic);

		query = "myTopic >> reifier";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Variant);
		assertTrue(set.getResults().get(0).first().equals(v));
		v.setReifier(null);
		/*
		 * association
		 */
		Association a = createAssociation();
		a.setReifier(topic);

		query = "myTopic >> reifier";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Association);
		assertTrue(set.getResults().get(0).first().equals(a));
		a.setReifier(null);
		/*
		 * association role
		 */
		Role r = createAssociation().createRole(createTopic(), createTopic());
		r.setReifier(topic);

		query = "myTopic >> reifier";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Role);
		assertTrue(set.getResults().get(0).first().equals(r));
		r.setReifier(null);
		/*
		 * topic map
		 */
		topicMap.setReifier(topic);
		query = "myTopic >> reifier";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof TopicMap);
		assertTrue(set.getResults().get(0).first().equals(topicMap));
	}

	@Test
	public void testReifierAxisBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic[] topics = new Topic[10];
		for (int i = 0; i < topics.length; i++) {
			topics[i] = createTopic();
			topic.createName("Value", new Topic[0]).setReifier(topics[i]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> characteristics << reifier";
		set = execute(query);
		assertEquals(topics.length, set.size());

		Set<Topic> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof Topic);
			result.add((Topic) r.first());
		}

		for (int i = 0; i < topics.length; i++) {
			assertTrue(result.contains(topics[i]));
		}

		/*
		 * the topic map reifier
		 */
		Topic t = createTopic();
		topicMap.setReifier(t);
		query = "%_ << reifier";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(t, set.first().first());
	}

	@Test
	public void testAtomifyAxisForLocators() throws Exception {
		Topic topic = createTopicBySL("myTopic");
		Locator[] locators = new Locator[10];
		for (int i = 0; i < locators.length; i++) {
			locators[i] = createLocator(Integer.toString(i));
			topic.addSubjectIdentifier(locators[i]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> indicators >> atomify";
		set = execute(query);
		assertEquals(locators.length, set.size());

		Set<String> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof String);
			result.add((String) r.first());
		}

		for (int i = 0; i < locators.length; i++) {
			assertTrue(result.contains(locators[i].getReference()));
		}
	}

	@Test
	public void testAtomifyAxisForNames() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Name[] names = new Name[10];
		for (int i = 0; i < names.length; i++) {
			names[i] = topic.createName(type, "Value" + i, new Topic[0]);
			topic.createOccurrence(type, "Value_" + i, new Topic[0]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> characteristics tm:name >> atomify";
		set = execute(query);
		assertEquals(names.length, set.size());

		Set<String> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof String);
			result.add((String) r.first());
		}

		for (int i = 0; i < names.length; i++) {
			assertTrue(result.contains(names[i].getValue()));
		}
	}

	@Test
	public void testAtomifyAxisForOccurrence() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Occurrence[] occurrences = new Occurrence[10];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(type, "Value" + i, new Topic[0]);
			topic.createName(type, "Value_" + i, new Topic[0]);
		}
		String query = null;
		IResultSet<?> set = null;

		query = "myTopic >> characteristics tm:occurrence >> atomify";
		set = execute(query);
		assertEquals(occurrences.length, set.size());

		Set<String> result = HashUtil.getHashSet();
		for (IResult r : set.getResults()) {
			assertTrue(r.first() instanceof String);
			result.add((String) r.first());
		}

		for (int i = 0; i < occurrences.length; i++) {
			assertTrue(result.contains(occurrences[i].getValue()));
		}
	}

	@Test
	public void testAtomifyAxisLocatorsBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		String query = null;
		IResultSet<?> set = null;

		query = "\"" + base + "myTopic\" << atomify";
		set = execute(query);
		assertEquals(1, set.size());
		assertTrue(set.getResults().get(0).first() instanceof Locator);
		assertTrue(set.getResults().get(0).first().equals(topic.getSubjectIdentifiers().iterator().next()));
	}

	@Test
	public void testAtomifyAxisNamesBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Name[] names = new Name[10];
		for (int i = 0; i < names.length; i++) {
			names[i] = topic.createName(type, "Value" + i, new Topic[0]);
			topic.createOccurrence(type, "Value_" + i, new Topic[0]);
		}
		String query = null;
		IResultSet<?> set = null;

		for (int i = 0; i < names.length; i++) {
			query = "\"Value" + i + "\" << atomify";
			set = execute(query);
			assertEquals(i + "", 1, set.size());
			assertTrue(set.getResults().get(0).first() instanceof Name);
			assertTrue(set.getResults().get(0).first().equals(names[i]));
		}
	}

	@Test
	public void testAtomifyAxisOccurrenceBW() throws Exception {
		Topic topic = createTopicBySI("myTopic");
		Topic type = createTopicBySI("theType");
		Occurrence[] occurrences = new Occurrence[10];
		for (int i = 0; i < occurrences.length; i++) {
			occurrences[i] = topic.createOccurrence(type, "Value" + i, new Topic[0]);
			topic.createName(type, "Value_" + i, new Topic[0]);
		}
		String query = null;
		IResultSet<?> set = null;

		for (int i = 0; i < occurrences.length; i++) {
			query = "\"Value" + i + "\" << atomify";
			set = execute(query);
			assertEquals(i + "", 1, set.size());
			assertTrue(set.getResults().get(0).first() instanceof Occurrence);
			assertTrue(set.getResults().get(0).first().equals(occurrences[i]));
		}
	}

	@Test
	public void testIdAxis() throws Exception {
		Topic t = createTopic();
		Name n = t.createName("Name");
		Variant v = n.createVariant("Value", createTopic());
		Occurrence o = t.createOccurrence(createTopic(), "Val");
		Association a = createAssociation();
		Role r = a.createRole(createTopic(), createTopic());

		String query = null;
		IResultSet<?> set = null;

		// for topic
		String id = t.getId();
		query = "\"" + id + "\" << id ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(t, set.first().first());

		query = "\"" + id + "\" << id >> id";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(id, set.first().first());

		// for name
		id = n.getId();
		query = "\"" + id + "\" << id ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(n, set.first().first());

		query = "\"" + id + "\" << id >> id";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(id, set.first().first());

		// for variant
		id = v.getId();
		query = "\"" + id + "\" << id ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(v, set.first().first());

		query = "\"" + id + "\" << id >> id";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(id, set.first().first());

		// for occurrence
		id = o.getId();
		query = "\"" + id + "\" << id ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(o, set.first().first());

		query = "\"" + id + "\" << id >> id";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(id, set.first().first());

		// for role
		id = r.getId();
		query = "\"" + id + "\" << id ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(r, set.first().first());

		query = "\"" + id + "\" << id >> id";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(id, set.first().first());

		// for association
		id = a.getId();
		query = "\"" + id + "\" << id ";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(a, set.first().first());

		query = "\"" + id + "\" << id >> id";
		set = execute(query);
		assertEquals(1, set.size());
		assertEquals(1, set.first().size());
		assertEquals(id, set.first().first());
	}

}
