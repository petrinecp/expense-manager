package cz.muni.expense.data;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.expense.model.Category;
import cz.muni.expense.model.Rule;
import cz.muni.expense.model.User;


/**
 * Test class for testing BankRepository
 * 
 * @author Martin Drimal
 *
 */
@RunWith(Arquillian.class)
public class RuleRepositoryTest {
	
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
	RuleRepository ruleRepository;
	
	@Test
    @InSequence(1)
    public void createRoleTest() throws Exception {
    	User user = new User();
    	user.setId(1L);
    	user.setName("Peter");
    	user.setForname("Nash");
    	
    	Category cat = new Category();
    	cat.setId(1L);
    	cat.setUser(user);
    	cat.setTitle("Travel");
    	
        Rule rule = new Rule();
        rule.setUser(user);
        rule.setRuleString("test");
        rule.setCategory(cat);
        
        ruleRepository.create(rule);
        List<Rule> rules = ruleRepository.findAll();
        assertEquals("Wrong size", 1, rules.size());
    }
	
    @Test
    @InSequence(2)
    public void listAllRulesTest() throws Exception {

    	List<Rule> rules = ruleRepository.findAll();
        assertEquals("Wrong size", 1, rules.size());
        Category cat = rules.get(0).getCategory();
        assertEquals("Wrong category title.", "Car", cat.getTitle());
        assertEquals("Wrong rule string.", "test", rules.get(0).getRuleString());
    }
    
	@Test
    @InSequence(3)
    public void lookupByIdTest() throws Exception {
        Rule rule = ruleRepository.findById(1L);
        assertEquals("Wrong rule string.", "test", rule.getRuleString());
    }
	
    @Test
    @InSequence(4)
    public void lookupByNonExistIdTest() throws Exception {
        ruleRepository.findById(20L);
    }
    
    @Test
    @InSequence(5)
    public void updateCategoryTest() throws Exception {
    	User user = new User();
    	user.setId(1L);
    	user.setName("Peter");
    	user.setForname("Nash");
    	
    	Category cat = new Category();
    	cat.setId(1L);
    	cat.setUser(user);
    	cat.setTitle("Travel");
    	
        Rule rule = new Rule();
        rule.setId(2L);
        rule.setUser(user);
        rule.setRuleString("updated");
        rule.setCategory(cat);
        Rule updated = ruleRepository.update(rule);
        assertEquals("Wrong rule string.", "updated", updated.getRuleString());
    }
    
    @Test
    @InSequence(6)
    public void deleteByIdTest() {
        
        ruleRepository.deleteById(1L);
    }

    @Test(expected = Exception.class)
    @InSequence(7)
    public void deleteByNonExistIdTest() {
        ruleRepository.deleteById(20L);
    }

}
