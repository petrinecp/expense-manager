/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.data;

import cz.muni.expense.model.Category;
import cz.muni.expense.model.Payment;
import cz.muni.expense.model.Rule;
import cz.muni.expense.model.User;
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
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author majo
 */
@RunWith(Arquillian.class)
public class RuleRepositoryTest2 {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "cz.muni.expense")
                .addAsResource("META-INF/our-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/test-import.sql", "import.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("test-ds.xml");
    }

    @Inject
    UserRepository userRepository;

    @Inject
    PaymentRepository paymentRepository;

    @Inject
    RuleRepository ruleRepository;

    @Inject
    CategoryRepository categoryRepository;

    User user;

    @Before
    public void prepareDB() {
        List<Payment> payments = paymentRepository.findAll();
        for (Payment i : payments) {
            paymentRepository.deleteById(i.getId());
        }

        List<Rule> rules = ruleRepository.findAll();
        for (Rule i : rules) {
            ruleRepository.deleteById(i.getId());
        }

        List<Category> categories = categoryRepository.findAll();
        for (Category i : categories) {
            categoryRepository.deleteById(i.getId());
        }

        List<User> users = userRepository.findAll();
        for (User i : users) {
            userRepository.deleteById(i.getId());
        }

        user = new User();
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

    }

    @Test
    public void findRulePerPayment() {
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal(10));
        payment.setBank(null);
        payment.setInfoForReceiver1("Info1");
        payment.setInfoForReceiver2("omv gas, Botanicka 43");
        payment.setPaymentDate(new Date());
        payment.setUser(user);

        
        Category foundCategory = ruleRepository.findCategory(payment);        
        assertNotNull("found category", foundCategory);
        payment.setCategory(foundCategory);
        paymentRepository.create(payment);

        Payment paymentFromDb = paymentRepository.findById(payment.getId());
        assertNotNull("found category", paymentFromDb.getCategory());
        assertEquals("Bad category", "Car", paymentFromDb.getCategory().getTitle());

        payment = new Payment();
        payment.setAmount(new BigDecimal(23));
        payment.setBank(null);
        payment.setInfoForReceiver1("cinema, Mad Max");
        payment.setInfoForReceiver2("Info2");
        payment.setPaymentDate(new Date());
        payment.setUser(user);

        payment.setCategory(ruleRepository.findCategory(payment));
        paymentRepository.create(payment);

        paymentFromDb = paymentRepository.findById(payment.getId());
        assertEquals("Bad category", "Entertainment", paymentFromDb.getCategory().getTitle());
    }

    @Test(expected = Exception.class)
    public void findRuleWithNullUserTestNullPointerException() throws Exception{
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal(10));
        payment.setBank(null);
        payment.setInfoForReceiver1("Info1");
        payment.setInfoForReceiver2("omv gas, Botanicka 43");
        payment.setPaymentDate(new Date());

        payment.setCategory(ruleRepository.findCategory(payment));
    }

    @Test
    public void findRuleNoRuleForPayment() {
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal(10));
        payment.setBank(null);
        payment.setInfoForReceiver1("Info1");
        payment.setInfoForReceiver2("Info2");
        payment.setPaymentDate(new Date());
        payment.setUser(user);

        payment.setCategory(ruleRepository.findCategory(payment));
        paymentRepository.create(payment);

        Payment paymentFromDb = paymentRepository.findById(payment.getId());
        assertNull("Bad category have been found", paymentFromDb.getCategory());
    }
    
}
