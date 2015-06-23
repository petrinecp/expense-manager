package cz.muni.expense.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

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
import cz.muni.expense.model.Payment;
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
                .addPackages(true, "org.apache.commons.lang3")
                .addPackages(true, "org.codehaus.jackson")
                .addAsResource("META-INF/our-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/test-import.sql", "import.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("test-ds.xml");
    }

    @Inject
    RuleRepository ruleRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    PaymentRepository paymentRepository;

    @Inject
    CategoryRepository categoryRepository;

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

    @Test
    @InSequence(8)
    public void findRulePerPayment() {
        User user = prepareDb();

        Payment payment = new Payment();
        payment.setAmount(new BigDecimal(10));
        payment.setBank(null);
        payment.setAdditionalInfo("omv, Botanicka 43");
        payment.setPaymentDate(new Date());
        payment.setUser(user);

//	    Category foundCategory = ruleRepository.findCategory(payment);        
//	    assertNotNull("Found category is null.", foundCategory);
//	    payment.setCategory(foundCategory);
        paymentRepository.create(payment);

        Payment paymentFromDb = paymentRepository.findById(payment.getId());
        assertNotNull("Found category is null.", paymentFromDb.getCategory());
        assertEquals("Bad category", "Car", paymentFromDb.getCategory().getTitle());

        payment = new Payment();
        payment.setAmount(new BigDecimal(23));
        payment.setBank(null);
        payment.setAdditionalInfo("cinema, Mad Max");
        payment.setPaymentDate(new Date());
        payment.setUser(user);

//	    payment.setCategory(ruleRepository.findCategory(payment));
        paymentRepository.create(payment);

        paymentFromDb = paymentRepository.findById(payment.getId());
        assertEquals("Bad category", "Entertainment", paymentFromDb.getCategory().getTitle());
    }

    private User prepareDb() {
        User user = new User();
        user.setForname("forname");
        user.setName("name");
        userRepository.create(user);

        Category categoryCar = new Category();
        categoryCar.setTitle("Car");
        categoryRepository.create(categoryCar);

        Category categoryEntertainment = new Category();
        categoryEntertainment.setTitle("Entertainment");
        categoryEntertainment.setUser(user);
        categoryRepository.create(categoryEntertainment);

        Rule ruleOmv = new Rule();
        ruleOmv.setCategory(categoryCar);
        ruleOmv.setUser(user);
        ruleOmv.setRuleString("OMV");
        ruleRepository.create(ruleOmv);

        Rule ruleCinema = new Rule();
        ruleCinema.setCategory(categoryEntertainment);
        ruleCinema.setUser(user);
        ruleCinema.setRuleString("cinema");
        ruleRepository.create(ruleCinema);

        Rule ruleFalse = new Rule();
        ruleFalse.setCategory(categoryEntertainment);
        ruleFalse.setUser(user);
        ruleFalse.setRuleString("travel");
        ruleRepository.create(ruleFalse);
        return user;
    }

    @Test(expected = Exception.class)
    @InSequence(9)
    public void findRuleWithNullUserTestNullPointerException() throws Exception {
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal(10));
        payment.setBank(null);
        payment.setAdditionalInfo("Info1");
        payment.setPaymentDate(new Date());

        paymentRepository.create(payment);

        //payment.setCategory(ruleRepository.findCategory(payment));
    }

    @Test
    @InSequence(10)
    public void findRuleNoRuleForPayment() {
        User user = new User();
        user.setForname("forname");
        user.setName("name");
        userRepository.create(user);

        Payment payment = new Payment();
        payment.setAmount(new BigDecimal(10));
        payment.setBank(null);
        payment.setAdditionalInfo("Info1");
        payment.setPaymentDate(new Date());
        payment.setUser(user);

        payment.setCategory(ruleRepository.findCategory(payment));
        paymentRepository.create(payment);

        Payment paymentFromDb = paymentRepository.findById(payment.getId());
        assertNull("Bad category have been found", paymentFromDb.getCategory());
    }
}
