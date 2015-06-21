package cz.muni.expense.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Messages")
public class Message extends BaseEntity<Long>{

    private String name;
    private String text;

    public Message(){
    	super();
    }
    
    public Message(String name, String text) {
    	super();
        this.name = name;
        this.text = text;
    }

    public void setName(String name) {
		this.name = name;
	}
    
    public String getName() {
        return name;
    }

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
        return text;
    }
}
