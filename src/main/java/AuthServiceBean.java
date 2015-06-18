
import cz.muni.expense.data.UserRepository;
import cz.muni.expense.model.User;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.inject.Inject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author majo
 */
@Stateless(name = "AuthService")
public class AuthServiceBean implements AuthService {
    
    @Inject
    UserRepository userRepository;// = CDI.current().select(BankRepository.class).get();

    @Override
    public AuthAccessElement login(AuthLoginElement loginElement) {
        User user = userRepository.findByUsernameAndPassword(loginElement.getUsername(), loginElement.getPassword());
        if (user != null) {
            user.setAuthToken(UUID.randomUUID().toString());
            userRepository.update(user);
            return new AuthAccessElement(loginElement.getUsername(), user.getAuthToken(), user.getAuthRole());
        }
        return null;
    }
}