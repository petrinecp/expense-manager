package cz.muni.expense.data;

import javax.ejb.Stateless;

import cz.muni.expense.model.Message;
import cz.muni.expense.model.Statistics;

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
