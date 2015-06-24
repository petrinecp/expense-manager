package cz.muni.expense.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author majo
 */
@Entity
@Table(name = "Rules")
public class Rule extends BaseEntity<Long> {
    
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    
    @ManyToOne
    private Category category;
    
    private String ruleString;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getRuleString() {
        return ruleString;
    }

    public void setRuleString(String ruleString) {
        this.ruleString = ruleString.toLowerCase();
    }
    
    //</editor-fold>
}
