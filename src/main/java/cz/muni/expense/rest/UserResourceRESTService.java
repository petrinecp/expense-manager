package cz.muni.expense.rest;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.Path;

import cz.muni.expense.data.CategoryRepository;
import cz.muni.expense.model.User;

@Path("/user")
@RequestScoped
public class UserResourceRESTService extends GenericRESTService<User> {
//	public UserResourceRESTService() {
//		CategoryRepository repository = CDI.current()
//				.select(User.class).get();
//		this.setRepository(repository);
//	}
}
