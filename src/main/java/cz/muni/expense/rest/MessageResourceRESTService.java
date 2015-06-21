package cz.muni.expense.rest;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.Path;

import cz.muni.expense.data.MessageRepository;
import cz.muni.expense.model.Message;

@Path("/message")
@RequestScoped
public class MessageResourceRESTService extends GenericRESTService<Message>{

	public MessageResourceRESTService() {
        MessageRepository repository = CDI.current().select(MessageRepository.class).get();
        this.setRepository(repository);
    }
}
