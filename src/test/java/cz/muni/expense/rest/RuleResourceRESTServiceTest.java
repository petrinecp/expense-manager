package cz.muni.expense.rest;

import static org.junit.Assert.assertEquals;

import java.util.List;

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

import cz.muni.expense.common.ObjectParser;
import cz.muni.expense.model.Category;
import cz.muni.expense.model.Rule;
import cz.muni.expense.model.User;

/**
 * Test class for testing RuleResourceRESTService
 *
 * @author Martin Drimal
 *
 */
@RunWith(Arquillian.class)
public class RuleResourceRESTServiceTest {

private final ObjectParser<Rule> objectParser = new ObjectParser<Rule>(Rule.class);
	
	@Deployment(testable = false)
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
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://127.0.0.1:8080/test/rest/rule");
        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(rule));
        
        assertEquals("Response should be created.", 201, response.getStatus());
        List<Rule> rules = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("After put should be retrieved just 1 object", 1, rules.size());
        assertEquals("Wrong rule string.", "test", rules.get(0).getRuleString());

        response.close();
    }
	
    @Test
    @InSequence(2)
    public void listAllRulesTest() throws Exception {

        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/rule");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, response.getStatus());

        List<Rule> rules = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("Database should contains one object.", 1, rules.size());
        Category cat = rules.get(0).getCategory();
        assertEquals("Wrong category title.", "Car", cat.getTitle());
        assertEquals("Wrong rule string.", "test", rules.get(0).getRuleString());

        response.close();
    }
	@Test
    @InSequence(3)
    public void lookupByIdTest() throws Exception {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/rule/1");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, response.getStatus());

        List<Rule> rules = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("Request should response one object.", 1, rules.size());
        assertEquals("Wrong rule string.", "test", rules.get(0).getRuleString());

        response.close();
    }
	
    @Test
    @InSequence(4)
    public void lookupByNonExistIdTest() throws Exception {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/rule/20");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertEquals(404, response.getStatus());

        response.close();
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
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/rule/2");
        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(rule));

        assertEquals("Response status should be updated - 201.", 201, response.getStatus());
        List<Rule> rules = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("After put should be retrieved just 1 object", 1, rules.size());
        assertEquals("Wrong rule string.", "updated", rules.get(0).getRuleString());

        response.close();
    }
    
    @Test
    @InSequence(6)
    public void deleteByIdTest() {
//    	User user = new User();
//    	user.setId(1L);
//    	user.setName("Peter");
//    	user.setForname("Nash");
//    	
//    	Category cat = new Category();
//    	cat.setId(1L);
//    	cat.setUser(user);
//    	cat.setTitle("Travel");
//    	
//        Rule rule = new Rule();
//        rule.setUser(user);
//        rule.setRuleString("rested");
//        rule.setCategory(cat);
//    	Client client = ClientBuilder.newBuilder().build();
//    	WebTarget target = client.target("http://127.0.0.1:8080/test/rest/rule");
//        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(rule));
//        response.close();
//        client.close();
        
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/rule/1");
        Response response = target.request().delete();
        assertEquals("Category should be deleted", 200, response.getStatus());
    }

    @Test
    @InSequence(7)
    public void deleteByNonExistIdTest() {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/rule/20");
        Response response = target.request().delete();
        assertEquals("Category should not be deleted", 400, response.getStatus());
        response.close();
    }
}
