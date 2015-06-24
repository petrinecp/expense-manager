/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.rest;

import cz.muni.expense.data.CategoryRepository;
import cz.muni.expense.data.RuleRepository;
import cz.muni.expense.enums.UserRole;
import cz.muni.expense.model.Category;
import cz.muni.expense.model.Rule;
import cz.muni.expense.model.User;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author majo
 */
@Path("/rule")
@RequestScoped
public class RuleResourceRESTService extends GenericRESTService<Rule> {

    public RuleResourceRESTService() {
        RuleRepository repository = CDI.current().select(RuleRepository.class).get();
        this.setRepository(repository);
    }

    @RolesAllowed({UserRole.BASIC_USER, UserRole.ADMIN, UserRole.PRIVILEGED_USER})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public List<Rule> listAll() {
        User user = getUser();
        if (user.getAuthRole().equals(UserRole.ADMIN)) {
            return repository.findAll();
        } else {
            return ((RuleRepository) repository).findRulesByUserId(user.getId());
        }
    }

    @Override
    protected void setUser(Rule r) {
        r.setUser(getUser());
    }
    
    @Override
    protected boolean canEdit(Rule entity){
        return entity.getUser().equals(getUser());
    }
}
