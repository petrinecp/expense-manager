package cz.muni.expense.data;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.expense.data.BankRepository;
import cz.muni.expense.enums.BankIdentifier;
import cz.muni.expense.model.Bank;

/**
 * Test class for testing BankRepository
 * 
 * @author Martin Drimal
 *
 */
@RunWith(Arquillian.class)
public class BankRepositoryTest {
	
	@Deployment
    public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addPackages(true, "cz.muni.expense")
                .addPackages(true, "org.apache.commons.lang3")
                .addPackages(true, "org.codehaus.jackson")
                .addAsResource("META-INF/our-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("test-ds.xml");
	}
	
	@Inject
	BankRepository bankRepository;
	
	@Test
	public void findByIdentifierCsobTest(){
		Bank csobBank = new Bank();
		csobBank.setIdentifier(BankIdentifier.CSOB);
		csobBank.setTitle("CSOB");
		bankRepository.create(csobBank);
		
		Bank bank = bankRepository.findByIdentifier(BankIdentifier.CSOB);
		assertEquals("CSOB", bank.getTitle());
		assertEquals(BankIdentifier.CSOB, bank.getIdentifier());
	}
	
	@Test(expected=Exception.class)
	public void findByIdentifierCsasTest(){
		bankRepository.findByIdentifier(BankIdentifier.CSAS);
	}

}
