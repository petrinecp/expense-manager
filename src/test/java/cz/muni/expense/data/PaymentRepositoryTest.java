package cz.muni.expense.data;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;
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

import cz.muni.expense.data.PaymentRepository;
import cz.muni.expense.model.Bank;
import cz.muni.expense.model.Payment;
import cz.muni.expense.model.User;

/**
 * Test class for testing PaymentRepository
 * 
 * @author Martin Drimal
 *
 */
@RunWith(Arquillian.class)
public class PaymentRepositoryTest {
	
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
	
	private Payment defaultPayment;
	
	@Inject
	PaymentRepository repository;
	
	@Before
	public void prepareDB(){
		List<Payment> payments = repository.findAll();
		for(Payment p : payments){
			repository.deleteById(p.getId());
		}
		
		User user = new User();
		user.setId(1L);
		
		Bank bank = new Bank();
		bank.setId(1L);
		
		defaultPayment = new Payment();
		defaultPayment.setAmount(new BigDecimal(900));
		defaultPayment.setBank(bank);
		defaultPayment.setAdditionalInfo("Info1a");
		defaultPayment.setPaymentDate(new Date());
		defaultPayment.setUser(user);
		
		repository.create(defaultPayment);
	}
	
	@Test
	public void createPaymentTest(){
		User user = new User();
		user.setId(1L);
		
		Bank bank = new Bank();
		bank.setId(1L);
		
		Payment payment = new Payment();
		payment.setAmount(new BigDecimal(10));
		payment.setBank(bank);
		payment.setAdditionalInfo("Info1");
		payment.setPaymentDate(new Date());
		payment.setUser(user);
		
		repository.create(payment);
		
		Payment paymentFromDb = repository.findById(payment.getId());
		assertEquals("Bad info for receiverOne", "Info1", paymentFromDb.getAdditionalInfo());
	}
	
	@Test
	public void deletePaymentTest(){
		repository.delete(defaultPayment);
		
		List<Payment> payments = repository.findAll();
		assertTrue("Payment database should be empty.", payments.isEmpty());
	}
	
	@Test
	public void deleteByIdTest(){
		repository.deleteById(defaultPayment.getId());
		
		List<Payment> payments = repository.findAll();
		assertTrue("Payment database should be empty.", payments.isEmpty());
	}
	
	@Test
	public void findByIdTest(){
		Payment p = repository.findById(defaultPayment.getId());
		assertEquals("Info1a", p.getAdditionalInfo());
	}
	
	@Test
	public void findAllPaymentsTest(){
		List<Payment> payments = repository.findAll();
		assertEquals("Database should contains one payment.", 1, payments.size());
		assertEquals("Bad info for receiverOne", "Info1a", payments.get(0).getAdditionalInfo());
	}
}
