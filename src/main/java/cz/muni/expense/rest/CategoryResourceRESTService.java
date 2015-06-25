package cz.muni.expense.rest;

import cz.muni.expense.auth.AuthAccessElement;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.Path;
import cz.muni.expense.data.CategoryRepository;
import cz.muni.expense.enums.UserRole;
import cz.muni.expense.model.Category;
import cz.muni.expense.model.User;
import java.net.URI;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Peter Petrinec
 */
@Path("/category")
@RequestScoped
public class CategoryResourceRESTService extends GenericRESTService<Category> {

    public CategoryResourceRESTService() {
        CategoryRepository repository = CDI.current().select(CategoryRepository.class).get();
        this.setRepository(repository);
    }

    @RolesAllowed({UserRole.BASIC_USER, UserRole.ADMIN, UserRole.PRIVILEGED_USER})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public List<Category> listAll() {
        User user = getUser();
        if (user.getAuthRole().equals(UserRole.ADMIN)) {
            return repository.findAll();
        } else {
            return ((CategoryRepository) repository).listUserAndGlobalCategoriesByUserId(user.getId());
        }
    }

    @RolesAllowed({UserRole.BASIC_USER, UserRole.ADMIN, UserRole.PRIVILEGED_USER})
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response create(Category entity) {
        if (!getUser().getAuthRole().equals(UserRole.ADMIN)) {
            setUser(entity);
        }
        Category e = repository.create(entity);
        URI createdUri = URI.create(uriInfo.getAbsolutePath() + e.getId().toString());
        return Response.created(createdUri).entity(e).build();
    }

    @RolesAllowed({UserRole.BASIC_USER, UserRole.ADMIN, UserRole.PRIVILEGED_USER})
    @POST
    @Path("/{id:[0-9][0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Category entity) {
        if (!getUser().getAuthRole().equals(UserRole.ADMIN)) {
            setUser(entity);
            if (!canEdit(entity)) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }

        Category e = repository.update(entity);
        URI createdUri = URI.create(uriInfo.getAbsolutePath() + e.getId().toString());
        return Response.created(createdUri).entity(e).build();
    }

    @Override
    protected void setUser(Category t) {
        t.setUser(getUser());
    }

    @Override
    protected boolean canEdit(Category entity) {
        return entity.getUser().equals(getUser());
    }

}
