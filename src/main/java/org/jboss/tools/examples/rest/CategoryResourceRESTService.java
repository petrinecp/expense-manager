/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.tools.examples.rest;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.Path;
import org.jboss.tools.examples.data.CategoryRepository;
import org.jboss.tools.examples.model.Category;

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
