package cz.muni.expense.data;

import javax.ejb.Stateless;

import cz.muni.expense.model.Message;

/**
 * 
 * @author Drimal
 *
 */
@Stateless
public class MessageRepository extends GenericRepository<Message>{

	public MessageRepository() {
		super(Message.class);
	}

}
