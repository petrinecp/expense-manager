package cz.muni.expense.data;

import javax.ejb.Stateless;

import cz.muni.expense.model.Message;

/**
 * 
 * @author Drimal
 *
 */
@Stateless
public class MessagesRepository extends GenericRepository<Message>{

	public MessagesRepository() {
		super(Message.class);
	}

}
