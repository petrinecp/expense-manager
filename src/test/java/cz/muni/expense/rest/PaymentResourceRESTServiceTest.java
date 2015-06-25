package cz.muni.expense.rest;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.Date;
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

import cz.muni.expense.common.BaseRestTestSuite;
import cz.muni.expense.common.ObjectParser;
import cz.muni.expense.enums.BankIdentifier;
import cz.muni.expense.model.Bank;
import cz.muni.expense.model.Category;
import cz.muni.expense.model.Payment;
import cz.muni.expense.model.User;

/**
 * Test class for testing PaymentResourceRESTService
 *
 * @author Martin Drimal
 *
 */
@RunWith(Arquillian.class)
public class PaymentResourceRESTServiceTest extends BaseRestTestSuite {
	
	private final ObjectParser<Payment> objectParser = new ObjectParser<Payment>(Payment.class);
	
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
	
	private Bank bank;
	
	private User user;
	
	@Test
	@InSequence(1)
	public void createPaymentTest() throws Exception {
		bank = new Bank();
		bank.setId(0L);
		bank.setIdentifier(BankIdentifier.CSOB);
		bank.setTitle("ČSOB");
		
		user = new User();
		user.setId(1L);
		user.setName("Peter");
		user.setForname("Nash");
		
		Payment payment = new Payment();
		payment.setAmount(new BigDecimal(10830));
		payment.setBank(bank);
		payment.setUser(user);
		payment.setAdditionalInfo("infoForReceiver");
		payment.setPaymentDate(new Date());
		Client client = createRestClient();
        WebTarget target = client.target("https://localhost:8443/test/rest/payment");
        Response response = target.request(MediaType.APPLICATION_JSON).header("auth-id", "test").header("auth-token", "test").post(Entity.json(payment));
        
        assertEquals("Response should be created.", 201, response.getStatus());
        List<Payment> payments = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("After post should be retrieved just 1 object", 1, payments.size());
        assertEquals("Wrong payment info1", "infoForReceiver1", payments.get(0).getAdditionalInfo());
        assertEquals("Wrong payment amount", new BigDecimal(10830), payments.get(0).getAmount());
        
        response.close();
	}
	
	@Test
	@InSequence(2)
	public void listAllPaymentsTest() throws Exception {
		Client client = createRestClient();
        WebTarget target = client.target("https://localhost:8443/test/rest/payment");
        Response response = target.request(MediaType.APPLICATION_JSON).header("auth-id", "test").header("auth-token", "test").get();
        assertEquals(200, response.getStatus());
	        
        List<Payment> payments = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("Database should contains one payment", 1, payments.size());
        
        response.close();
	}
	
	@Test
    @InSequence(3)
    public void lookupByIdTest() throws Exception {
		Client client = createRestClient();
        WebTarget target = client.target("https://localhost:8443/test/rest/payment/1");
        Response response = target.request(MediaType.APPLICATION_JSON).header("auth-id", "test").header("auth-token", "test").get();
        assertEquals(200, response.getStatus());

        List<Payment> payments = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("Request should response one object.", 1, payments.size());
        assertEquals("Wrong payment info", "infoForReceiver1", payments.get(0).getAdditionalInfo());

        response.close();
    }
	
	@Test
    @InSequence(4)
    public void lookupByNonExistIdTest() throws Exception {
		Client client = createRestClient();
        WebTarget target = client.target("https://localhost:8443/test/rest/payment/20");
        Response response = target.request(MediaType.APPLICATION_JSON).header("auth-id", "test").header("auth-token", "test").get();
        assertEquals(404, response.getStatus());

        response.close();
    }
	
	@Test
	@InSequence(5)
	public void updateTest() throws Exception {
		Payment payment = new Payment();
		payment.setId(1L);
		payment.setAmount(new BigDecimal(10830));
		payment.setBank(bank);
		payment.setUser(user);
		payment.setAdditionalInfo("updatedInfoForReceiver1");
		payment.setPaymentDate(new Date());
		
		Client client = createRestClient();
        WebTarget target = client.target("https://localhost:8443/test/rest/payment/1");
        Response response = target.request(MediaType.APPLICATION_JSON).header("auth-id", "test").header("auth-token", "test").post(Entity.json(payment));
        
        List<Payment> payments = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("Response status should be updated - 201.", 201, response.getStatus());
        assertEquals("Wrong payment info", "updatedInfoForReceiver1", payments.get(0).getAdditionalInfo());

        response.close();
	}
    
	@Test
    @InSequence(6)
    public void deleteByIdTest() throws Exception {		
		Payment payment = new Payment();
		payment.setAmount(new BigDecimal(649));
		payment.setBank(bank);
		payment.setUser(user);
		payment.setAdditionalInfo("infoForReceiver1");
		payment.setPaymentDate(new Date());
		Client client = createRestClient();
        WebTarget target = client.target("https://localhost:8443/test/rest/payment");
        Response response = target.request(MediaType.APPLICATION_JSON).header("auth-id", "test").header("auth-token", "test").post(Entity.json(payment));
        
        client = ClientBuilder.newBuilder().build();
        target = client.target("http://localhost:8080/test/rest/category/3");
        response = target.request().delete();
        assertEquals("Category should be deleted", 200, response.getStatus());
        
        response.close();
    }

    @Test
    @InSequence(7)
    public void deleteByNonExistIdTest() throws Exception {
    	Client client = createRestClient();
        WebTarget target = client.target("https://localhost:8443/test/rest/payment/20");
        Response response = target.request(MediaType.APPLICATION_JSON).header("auth-id", "test").header("auth-token", "test").delete();
        assertEquals("Category should not be deleted", 400, response.getStatus());
        response.close();
    }

}
