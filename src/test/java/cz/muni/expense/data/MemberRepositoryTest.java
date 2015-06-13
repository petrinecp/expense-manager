package cz.muni.expense.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

import cz.muni.expense.data.MemberRepository;
import cz.muni.expense.model.Member;

/**
 * Test class for testing MemberRepository
 * 
 * @author Martin Drimal
 *
 */
@RunWith(Arquillian.class)
public class MemberRepositoryTest {

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
	MemberRepository repository;
	
	@Test
	public void findByEmailTest(){
		Member member = repository.findByEmail("john.smith@mailinator.com");
		assertEquals("Wrong member name.", "John Smith", member.getName());
		assertEquals("Wrong member email.", "john.smith@mailinator.com", member.getEmail());
	}
	
	@Test
	public void test(){
		List<Member> membersOrdered = repository.findAllOrderedByName();
		assertEquals("Expected collection of 2 members.", 2, membersOrdered.size());	
		boolean isOrdered = membersOrdered.get(0).getName().compareTo(membersOrdered.get(1).getName()) < 0 ? true : false;
		assertTrue("Members should be ordered", isOrdered);
	}
}
