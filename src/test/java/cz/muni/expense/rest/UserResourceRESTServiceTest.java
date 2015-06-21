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
import cz.muni.expense.enums.BankIdentifier;
import cz.muni.expense.model.Bank;
import cz.muni.expense.model.User;

/**
 * Test class for testing BankResourceRESTService
 *
 * @author Martin Drimal
 *
 */
@RunWith(Arquillian.class)
public class UserResourceRESTServiceTest {

	private final ObjectParser<User> objectParser = new ObjectParser<User>(User.class);
	
    @Deployment(testable = false)
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "cz.muni.expense")
                .addAsResource("META-INF/our-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("test-ds.xml");
    }
    
    @Test
    @InSequence(1)
    public void createTest() throws Exception {
        User user = new User();
        user.setName("Adam");
        user.setForname("Eagel");
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/user");
        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(user));

        assertEquals("Response should be created.", 201, response.getStatus());
        List<User> users = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("After put should be retrieved just 1 object", 1, users.size());
        assertEquals("Wrong user name.", "Adam", users.get(0).getName());
        assertEquals("Wrong user forname.", "Eagel", users.get(0).getForname());
        assertEquals("Wrong user id.", 1, users.get(0).getId().intValue());

        response.close();
    }
    
    @Test
    @InSequence(2)
    public void listAllTest() throws Exception {

        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/user");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, response.getStatus());

        List<User> users = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("Database should contains two users.", 1, users.size());
        assertEquals("Wrong user name", "Adam", users.get(0).getName());
        assertEquals("Wrong user forname", "Eagel", users.get(0).getForname());      

        response.close();
    }
	
    @Test
    @InSequence(3)
    public void lookupByIdTest() throws Exception {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/user/1");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, response.getStatus());

        List<User> users = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("Database should contains two users.", 1, users.size());
        assertEquals("Wrong user name", "Adam", users.get(0).getName());
        assertEquals("Wrong user forname", "Eagel", users.get(0).getForname());

        response.close();
    }
	
    @Test
    @InSequence(4)
    public void lookupByNonExistIdTest() throws Exception {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/user/20");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertEquals(404, response.getStatus());

        response.close();
    }
    
    @Test
    @InSequence(5)
    public void updateTest() throws Exception {
    	User user = new User();
    	user.setId(1L);
        user.setName("Adamic");
        user.setForname("Eagel");
        
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/user/1");
        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(user));

        assertEquals("Response status should be updated - 201.", 201, response.getStatus());
        List<User> users = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("After put should be retrieved just 1 object", 1, users.size());
        assertEquals("Wrong user name.", "Adamic", users.get(0).getName());
        assertEquals("Wrong user forname.", "Eagel", users.get(0).getForname());
        assertEquals("Wrong user id.", 1, users.get(0).getId().intValue());

        response.close();
    }

    @Test
    @InSequence(6)
    public void deleteByIdTest() {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/user/1");
        Response response = target.request().delete();
        assertEquals("Bank should be deleted", 200, response.getStatus());
    }

    @Test
    @InSequence(7)
    public void deleteByNonExistIdTest() {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/user/20");
        Response response = target.request().delete();
        assertEquals("Bank should not be deleted", 400, response.getStatus());
    }
}
