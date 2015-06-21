package cz.muni.expense.rest;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.Path;

import cz.muni.expense.data.CategoryRepository;
import cz.muni.expense.data.UserRepository;
import cz.muni.expense.model.Category;
import cz.muni.expense.model.User;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/user")
@RequestScoped
public class UserResourceRESTService extends GenericRESTService<User> {
    
    @Inject
    private CategoryRepository categoryRepository;
    
    public UserResourceRESTService() {
            UserRepository repository = CDI.current().select(UserRepository.class).get();
            this.setRepository(repository);
    }
    
    @GET
    @Path("/{id:[0-9][0-9]*}/category")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> listUserCategories(@PathParam("id") Long id) {
        return categoryRepository.listUserAndGlobalCategoriesByUserId(id);
    }
}
