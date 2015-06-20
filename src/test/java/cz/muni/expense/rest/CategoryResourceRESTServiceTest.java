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



/**
 * Test class for testing CategoryResourceRESTService
 *
 * @author Martin Drimal
 *
 */
@RunWith(Arquillian.class)
public class CategoryResourceRESTServiceTest {

	private final ObjectParser<Category> objectParser = new ObjectParser<Category>(Category.class);
	
	@Deployment(testable = false)
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "cz.muni.expense")
                .addAsResource("META-INF/our-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/test-import.sql", "import.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("test-ds.xml");
    }
	
	@Test
    @InSequence(1)
    public void listAllCategoriesTest() throws Exception {

        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/category");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, response.getStatus());

        List<Category> categories = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("Database should contains one bank.", 3, categories.size());
        assertEquals("Wrong category title.", "Home", categories.get(0).getTitle());
        assertEquals("Wrong category title.", "Job", categories.get(1).getTitle());
        assertEquals("Wrong category title.", "Garden", categories.get(2).getTitle());

        response.close();
    }
	
	@Test
    @InSequence(2)
    public void lookupByIdTest() throws Exception {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/category/0");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, response.getStatus());

        List<Category> categories = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("Request should response one object.", 1, categories.size());
        assertEquals("Wrong category title.", "Home", categories.get(0).getTitle());

        response.close();
    }
	
    @Test
    @InSequence(3)
    public void lookupByNonExistIdTest() throws Exception {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/category/20");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertEquals(404, response.getStatus());

        response.close();
    }
    
    //TODO analyze error test
    @Test
    @InSequence(4)
    public void createCategoryTest() throws Exception {
        Category category = new Category();
        category.setTitle("Hospital");
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/category");
        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(category));

        assertEquals("Response should be created.", 201, response.getStatus());
        //assertEquals("c", response.readEntity(String.class));
        List<Category> categories = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("After put should be retrieved just 1 object", 1, categories.size());
        assertEquals("Wrong category title.", "Hospital", categories.get(0).getTitle());

        response.close();
    }
    
    @Test
    @InSequence(5)
    public void updateCategoryTest() throws Exception {
        Category category = new Category();
        category.setId(2L);
        category.setTitle("Gardern-updated");
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/category/2");
        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(category));

        assertEquals("Response status should be updated - 201.", 201, response.getStatus());
        //Gardern-updated
        List<Category> categories = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("After put should be retrieved just 1 object", 1, categories.size());
        assertEquals("Wrong category title.", "Gardern-updated", categories.get(0).getTitle());

        response.close();
    }
    
    @Test
    @InSequence(6)
    public void deleteByIdTest() {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/category/0");
        Response response = target.request().delete();
        assertEquals("Category should be deleted", 200, response.getStatus());
    }

    @Test
    @InSequence(7)
    public void deleteByNonExistIdTest() {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/category/20");
        Response response = target.request().delete();
        assertEquals("Category should not be deleted", 400, response.getStatus());
    }
}
