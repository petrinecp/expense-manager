/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.rest;

import cz.muni.expense.data.CategoryRepository;
import cz.muni.expense.data.RuleRepository;
import cz.muni.expense.model.Rule;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.Path;

/**
 *
 * @author majo
 */
@Path("/rule")
@RequestScoped
public class RuleResourceRESTService extends GenericRESTService<Rule>{
    public RuleResourceRESTService() {
        RuleRepository repository = CDI.current().select(RuleRepository.class).get();
        this.setRepository(repository);
    }
    
    @Override
    protected void setUser(Rule r){
        r.setUser(getUser());
    }
}
