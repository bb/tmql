/*
 * Copyright: Copyright 2010 Topic Maps Lab, University of Leipzig. http://www.topicmapslab.de/    
 * License:   Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0.html
 *  
 * @author Sven Krosse
 * @email krosse@informatik.uni-leipzig.de
 *
 */
package tests.niederhausen;

import org.junit.Ignore;
import org.junit.Test;

import de.topicmapslab.tmql4j.components.processor.results.IResultSet;
import de.topicmapslab.tmql4j.majortom.tests.Tmql4JTestCase;

/**
 * @author Sven Krosse
 * 
 */
public class GroupByTest extends Tmql4JTestCase {

	@Test
	@Ignore
	public void test() throws Exception {
		fromXtm("src/test/resources/toytm.xtm");

		String query = "FOR $t IN // tm:subject " + "GROUP BY $0 " + "RETURN $t >> id, fn:best-label( $t ) , $t >> characteristics tm:name, $t >> characteristics tm:occurrence";

		query = "FOR $t IN // tm:subject GROUP BY $0 RETURN $t >> id, fn:best-label($t), $t >> characteristics tm:name >> atomify, $t >> characteristics tm:occurrence >> atomify ";

		execute(query);
	}

	@Test
	@Ignore
	public void testComplex() throws Exception {
		fromXtm("src/test/resources/toytm.xtm");

		final String query = "FOR $ot IN // tm:subject >> characteristics >> types " + "GROUP BY $0, $1, $2, $3 " + "OFFSET 1 " + "LIMIT 10 " + "RETURN {" + " FOR $t IN // tm:subject "
				+ "RETURN $#, $t >> id, fn:best-label( $t ),  $ot >> id , fn:best-label( $ot ), $t / $ot" + "}";

		execute(query);
	}

	@Test
	@Ignore
	public void testPerformanceProblem() throws Exception {
		for (int i = 0; i < 10000; i++) {
			createTopic();
		}

		final String query = " FOR $t IN // tm:subject OFFSET 0 LIMIT 10 RETURN $t , $t >> id , \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\", \"A\"";

		execute(query);
	}

	@Test
	public void testname() throws Exception {
		String query = "%prefix epg http://epg.topicmapslab.de/ FOR $t IN // epg:broadcast "
				+ " RETURN { FOR $t2 IN // epg:broadcast [ . / epg:start_time == $t / epg:end_time ] RETURN { FOR $t3 IN // epg:broadcast [ . / epg:start_time == $t2 / epg:end_time ] RETURN $t >> traverse epg:broadcasted / tm:name [0], $t2 >> traverse epg:broadcasted / tm:name [0], $t3 >> traverse epg:broadcasted / tm:name [0] } }";
		fromXtm("src/test/resources/entity-identity-test-after-production-identification.xtm");
		
		IResultSet<?> rs = execute(query);
		System.out.println(rs);
	}
}
