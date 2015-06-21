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
