package cz.muni.expense.data;

import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.expense.data.MemberListProducer;
import cz.muni.expense.model.Member;

/**
 * Test class for testing MemberListProducer
 * 
 * @author Martin Drimal
 *
 */
@RunWith(Arquillian.class)
public class MemberListProducerTest {

	@Deployment
    public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addPackages(true, "cz.muni.expense")
                .addAsResource("META-INF/our-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/test-import.sql", "import.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("test-ds.xml");
	}
	
	@Inject
	MemberListProducer memberProducer;
	
	@Test
	public void getMembersTest(){
		memberProducer.retrieveAllMembersOrderedByName();
		List<Member> members = memberProducer.getMembers();
		assertEquals("Two members should be in database.", 2, members.size());
		boolean isOrdered = members.get(0).getName().compareTo(members.get(1).getName()) < 0 ? true : false;
		assertTrue("Members should be ordered", isOrdered);
	}
}
