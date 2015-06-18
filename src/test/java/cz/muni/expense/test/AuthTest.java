/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.test;

import cz.muni.expense.auth.AuthAccessElement;
import cz.muni.expense.auth.AuthLoginElement;
import cz.muni.expense.auth.AuthService;
import cz.muni.expense.data.UserRepository;
import cz.muni.expense.model.User;
import java.util.List;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author majo
 */
@RunWith(Arquillian.class)
public class AuthTest {
    
    @Deployment
    public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addPackages(true, "cz.muni.expense")
                .addAsResource("META-INF/our-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/test-import.sql", "import.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("test-ds.xml");
	}
    
    private User defaultUser;
	
	@Inject
	UserRepository repository;
        
        @Inject
        AuthService service;
	
	@Before
	public void prepareDB(){
		List<User> users = repository.findAll();
		for(User u : users){
			repository.delete(u);
		}
		
		defaultUser = new User();
		defaultUser.setName("Jon Doe");
		defaultUser.setForname("Will Smith");
                defaultUser.setUsername("john");
                defaultUser.setPasswd("passwd");
		
		repository.create(defaultUser);
	}
        
        @Test
	public void findByUsernameAndPasswordTest(){
                User user = repository.findByUsernameAndPassword(
                        defaultUser.getUsername(),
                        defaultUser.getPasswd()
                );
		
		assertEquals("Bad user name.", user.getName(), defaultUser.getName());
		assertEquals("Bad user for name.", user.getForname(), defaultUser.getForname());
	}
        
        @Test
	public void loginTest(){
            
            AuthLoginElement authLogin = new AuthLoginElement(defaultUser.getUsername(), defaultUser.getPasswd());
            
            AuthAccessElement authAccess = service.login(authLogin);
                User user = repository.findByUsernameAndPassword(
                        defaultUser.getUsername(),
                        defaultUser.getPasswd()
                );
		
		assertEquals("Bad user name.", authLogin.getUsername(), authAccess.getAuthId());
                
                assertNotNull("aaa", authAccess.getAuthToken());
		//assertEquals("Bad user for name.", authLogin.getUsername(), authAccess.getAuthToken());
	}
    
}
