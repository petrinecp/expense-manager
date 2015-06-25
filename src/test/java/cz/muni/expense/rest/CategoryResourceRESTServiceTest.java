package cz.muni.expense.rest;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.ws.rs.client.Client;
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

import cz.muni.expense.common.BaseRestTestSuite;
import cz.muni.expense.common.ObjectParser;
import cz.muni.expense.model.Category;
import cz.muni.expense.model.User;



/**
 * Test class for testing CategoryResourceRESTService
 *
 * @author Martin Drimal
 *
 */
@RunWith(Arquillian.class)
public class CategoryResourceRESTServiceTest extends BaseRestTestSuite {

	private final ObjectParser<Category> objectParser = new ObjectParser<Category>(Category.class);
	
	@Deployment(testable = false)
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "cz.muni.expense")
                .addPackages(true, "org.apache.commons.lang3")
                .addPackages(true, "org.codehaus.jackson")
                .addPackages(true, "org.apache.http")
                .addAsResource("META-INF/our-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/test-import.sql", "import.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("test-ds.xml");
    }
	
	@Test
    @InSequence(1)
    public void listAllCategoriesTest() throws Exception {

		Client client = createRestClient();
        WebTarget target = client.target("https://localhost:8443/test/rest/category");
        Response response = target.request(MediaType.APPLICATION_JSON).header("auth-id", "test").header("auth-token", "test").get();
        assertEquals(200, response.getStatus());

        List<Category> categories = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("Database should contains two categories.", 2, categories.size());
        assertEquals("Wrong category title.", "Car", categories.get(0).getTitle());
        assertEquals("Wrong category title.", "Travel", categories.get(1).getTitle());

        response.close();
    }
	
    @Test
    @InSequence(2)
    public void createCategoryTest() throws Exception {
    	User user = new User();
    	user.setId(1L);
    	
        Category category = new Category();
        category.setTitle("world");
        category.setUser(user);
        Client client = createRestClient();
        WebTarget target = client.target("https://localhost:8443/test/rest/category/1");
        Response response = target.request(MediaType.APPLICATION_JSON).header("auth-id", "test").header("auth-token", "test").post(Entity.json(category));

        assertEquals("Response should be created.", 201, response.getStatus());
        List<Category> categories = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("After put should be retrieved just 1 object", 1, categories.size());
        assertEquals("Wrong category title.", "world", categories.get(0).getTitle());

        response.close();
    }
	
	@Test
    @InSequence(3)
    public void lookupByIdTest() throws Exception {
		Client client = createRestClient();
        WebTarget target = client.target("https://localhost:8443/test/rest/category/1");
        Response response = target.request(MediaType.APPLICATION_JSON).header("auth-id", "test").header("auth-token", "test").get();
        assertEquals(200, response.getStatus());

        List<Category> categories = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("Request should response one object.", 1, categories.size());
        assertEquals("Wrong category title.", "Car", categories.get(0).getTitle());

        response.close();
    }
	
    @Test
    @InSequence(4)
    public void lookupByNonExistIdTest() throws Exception {
    	Client client = createRestClient();
        WebTarget target = client.target("https://localhost:8443/test/rest/category/20");
        Response response = target.request(MediaType.APPLICATION_JSON).header("auth-id", "test").header("auth-token", "test").get();
        assertEquals(500, response.getStatus());

        response.close();
    }
    
    @Test
    @InSequence(5)
    public void updateCategoryTest() throws Exception {
    	User user = new User();
    	user.setId(1L);
    	
        Category category = new Category();
        category.setId(2L);
        category.setTitle("Gardern-updated");
        category.setUser(user);
        Client client = createRestClient();
        WebTarget target = client.target("https://localhost:8443/test/rest/category/");
        Response response = target.request(MediaType.APPLICATION_JSON).header("auth-id", "test").header("auth-token", "test").post(Entity.json(category));

        assertEquals("Response status should be updated - 201.", 201, response.getStatus());
        List<Category> categories = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("After put should be retrieved just 1 object", 1, categories.size());
        assertEquals("Wrong category title.", "Gardern-updated", categories.get(0).getTitle());

        response.close();
    }
    
    @Test
    @InSequence(6)
    public void deleteByIdTest() throws Exception {
    	Category category = new Category();
    	category.setTitle("Hospital");
    	Client client = createRestClient();
        WebTarget target = client.target("https://localhost:8443/test/rest/category");
        Response response = target.request(MediaType.APPLICATION_JSON).header("auth-id", "test").header("auth-token", "test").post(Entity.json(category));
        response.close();
        client.close();
        
        client = createRestClient();
        target = client.target("https://localhost:8443/test/rest/category/1");
        response = target.request(MediaType.APPLICATION_JSON).header("auth-id", "test").header("auth-token", "test").delete();
        assertEquals("Category should be deleted", 200, response.getStatus());
    }

    @Test
    @InSequence(7)
    public void deleteByNonExistIdTest() throws Exception {
    	Client client = createRestClient();
        WebTarget target = client.target("https://localhost:8443/test/rest/category/20");
        Response response = target.request(MediaType.APPLICATION_JSON).header("auth-id", "test").header("auth-token", "test").delete();
        assertEquals("Category should not be deleted", 400, response.getStatus());
        response.close();
    }
}
