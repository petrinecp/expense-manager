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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.expense.data.UserRepository;
import cz.muni.expense.model.User;

/**
 * Test class for testing PaymentRepository
 * 
 * @author Martin Drimal
 *
 */
@RunWith(Arquillian.class)
public class UserRepositoryTest {

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
	
	private User defaultUser;
	
	@Inject
	UserRepository repository;
	
	@Before
	public void prepareDB(){
		List<User> users = repository.findAll();
		for(User u : users){
			repository.delete(u);
		}
		
		defaultUser = new User();
		defaultUser.setName("Jon Doe");
		defaultUser.setForname("Will Smith");
		
		repository.create(defaultUser);
	}
	
	@Test
	public void createTest(){
		User user = new User();
		user.setName("Jon Doe");
		user.setForname("Christopher Paolini");
		
		repository.create(user);
		
		User u = repository.findById(user.getId());
		assertEquals("Bad user name.", "Jon Doe", u.getName());
		assertEquals("Bad user for name.", "Christopher Paolini", u.getForname());
	}
	
	@Test
	public void deleteUserTest(){
		repository.delete(defaultUser);
		
		List<User> users = repository.findAll();
		assertTrue("User database should be empty.", users.isEmpty());
	}
	
	@Test
	public void deleteByIdTest(){
		repository.delete(defaultUser);
		
		List<User> users = repository.findAll();
		assertTrue("User database should be empty.", users.isEmpty());
	}
	
	@Test
	public void findByIdTest(){
		User user = repository.findById(defaultUser.getId());
		
		assertEquals("Bad user name.", "Jon Doe", user.getName());
		assertEquals("Bad user for name", "Will Smith", user.getForname());
	}
	
	@Test
	public void findAllUserTest(){
		List<User> users = repository.findAll();
		assertEquals("In db should be just one user.", 1, users.size());
		assertEquals("Bad user name.", "Jon Doe", users.get(0).getName());
		assertEquals("Bad user for name", "Will Smith", users.get(0).getForname());
	}
}
