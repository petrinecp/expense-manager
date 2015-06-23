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

/**
 * Test class for testing BankResourceRESTService
 *
 * @author Martin Drimal
 *
 */
@RunWith(Arquillian.class)
public class BankResourceRESTServiceTest {

    private final ObjectParser<Bank> objectParser = new ObjectParser<Bank>(Bank.class);

    private Bank bank;

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
    public void listAllBanksTest() throws Exception {

        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/bank");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, response.getStatus());

        List<Bank> banks = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("Database should contains one bank.", 1, banks.size());
        assertEquals("Wrong bank title.", "ČSOB", banks.get(0).getTitle());
        assertEquals("Wrong bank identifier.", BankIdentifier.CSOB, banks.get(0).getIdentifier());
        assertEquals("Wrong bank id.", 0, banks.get(0).getId().intValue());

        response.close();
    }

    @Test
    @InSequence(2)
    public void lookupByIdTest() throws Exception {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/bank/0");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, response.getStatus());

        List<Bank> banks = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("Database should contains one bank.", 1, banks.size());
        assertEquals("Wrong bank title.", "ČSOB", banks.get(0).getTitle());
        assertEquals("Wrong bank identifier.", BankIdentifier.CSOB, banks.get(0).getIdentifier());
        assertEquals("Wrong bank id.", 0, banks.get(0).getId().intValue());

        response.close();
    }

    @Test
    @InSequence(3)
    public void lookupByNonExistIdTest() throws Exception {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/bank/20");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertEquals(404, response.getStatus());

        response.close();
    }

    @Test
    @InSequence(4)
    public void createBankTest() throws Exception {
        bank = new Bank();
        bank.setTitle("CSAS");
        bank.setIdentifier(BankIdentifier.CSAS);

        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/bank");
        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(bank));

        assertEquals("Response should be created.", 201, response.getStatus());
        List<Bank> banks = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("After put should be retrieved just 1 object", 1, banks.size());
        assertEquals("Wrong bank title.", "CSAS", banks.get(0).getTitle());
        assertEquals("Wrong bank identifier.", BankIdentifier.CSAS, banks.get(0).getIdentifier());
        assertEquals("Wrong bank id.", 1, banks.get(0).getId().intValue());

        response.close();
    }

    @Test
    @InSequence(5)
    public void updateBankTest() throws Exception {
        Bank bank = new Bank();
        bank.setId(1L);
        bank.setTitle("CSASu");
        bank.setIdentifier(BankIdentifier.CSAS);
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/bank/1");
        Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.json(bank));

        assertEquals("Response status should be updated - 201.", 201, response.getStatus());
        List<Bank> banks = objectParser.getObjectFromJson(response.readEntity(String.class));
        assertEquals("After put should be retrieved just 1 object", 1, banks.size());
        assertEquals("Wrong bank title.", "CSASu", banks.get(0).getTitle());
        assertEquals("Wrong bank identifier.", BankIdentifier.CSAS, banks.get(0).getIdentifier());
        assertEquals("Wrong bank id.", 1, banks.get(0).getId().intValue());

        response.close();
    }

    @Test
    @InSequence(5)
    public void deleteByIdTest() {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/bank/0");
        Response response = target.request().delete();
        assertEquals("Bank should be deleted", 200, response.getStatus());
    }

    @Test
    @InSequence(6)
    public void deleteByNonExistIdTest() {
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/test/rest/bank/20");
        Response response = target.request().delete();
        assertEquals("Bank should be deleted", 400, response.getStatus());
    }
}

