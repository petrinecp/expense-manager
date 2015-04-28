/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.rest;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.Path;
import cz.muni.expense.data.CategoryRepository;
import cz.muni.expense.model.Category;

/**
 *
 * @author Peter Petrinec
 */
@Path("/category")
@RequestScoped
public class CategoryResourceRESTService extends GenericRESTService<Category>{
    
    public CategoryResourceRESTService() {
        CategoryRepository repository = CDI.current().select(CategoryRepository.class).get();
        this.setRepository(repository);
    }    
}
