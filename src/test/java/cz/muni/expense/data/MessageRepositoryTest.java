package cz.muni.expense.data;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.expense.model.Message;
import cz.muni.expense.model.User;

@RunWith(Arquillian.class)
public class MessageRepositoryTest {

	@Deployment
    public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "cz.muni.expense")
                .addPackages(true, "org.apache.commons.lang3")
                .addPackages(true, "org.codehaus.jackson")
                .addAsResource("META-INF/our-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/test-import.sql", "import.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("test-ds.xml");
	}
	
	@Inject
	MessageRepository repository;
	
	@Test
	@InSequence(1)
	public void createTest(){
		User user = new User();
		user.setId(1L);
		user.setName("Peter");
		user.setForname("Nash");
		Message message = new Message();
		message.setTimestamp(new Date());
		message.setUser(user);
		message.setAction("Tested action");
		
		repository.create(message);
		
		List<Message> messages = repository.findAll();
		assertEquals("Expected just one object.", 1, messages.size());
		assertEquals("Wrong action.", "Tested action", messages.get(0).getAction());
	}
	
	@Test
	@InSequence(2)
	public void findByIdentifierTest(){
		Message message = repository.findById(1L);
		assertEquals("Wrong action.", "Tested action", message.getAction());		
	}
	
	@Test
	@InSequence(3)
	public void deleteByIdTest(){
		
	}
	
	@Test(expected=Exception.class)
	@InSequence(4)
	public void deleteNonExistIdTest(){
		repository.deleteById(20L);
	}
}
