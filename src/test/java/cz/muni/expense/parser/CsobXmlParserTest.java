package cz.muni.expense.parser;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.expense.exception.ParserException;
import cz.muni.expense.model.Payment;
import cz.muni.expense.parser.CsobXmlParser;

/**
 * Test class for testing CsobXmlParser functionality
 * 
 * @author Martin Drimal
 *
 */
@RunWith(Arquillian.class)
public class CsobXmlParserTest {
	
	@Deployment
    public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addPackages(true, "cz.muni.expense")
                .addAsResource("META-INF/our-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("paymentCsob.xml", "paymentCsob.xml")
                .addAsResource("badPaymentCSOB.xml", "badPaymentCSOB.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("test-ds.xml");
	}	
	
	@Inject
    Logger log;
	
	@Test
	public void parserTest() throws Exception {
		CsobXmlParser parser = new CsobXmlParser();
		Future<List<Payment>> payments = parser.parse(CsobXmlParserTest.class.getClassLoader().getResourceAsStream("paymentCsob.xml"));
		assertFalse("Parsed payments should not be empty.", payments.get().isEmpty());
        List<Payment> paymentList = payments.get();
        assertEquals("Expected payments size is 2.", 2, paymentList.size());
        log.info("CSOB payment xml was parsed successful.");
	}
	
	@Test(expected=ParserException.class)
	public void badInputParserTest() throws ParserException{
			CsobXmlParser parser = new CsobXmlParser();
			parser.parse(CsobXmlParserTest.class.getClassLoader().getResourceAsStream("badPaymentCSOB.xml"));
	}
}
