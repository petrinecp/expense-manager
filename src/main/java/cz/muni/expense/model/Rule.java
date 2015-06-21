/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author majo
 */
@Entity
@Table(name = "Payments")
public class Rule extends BaseEntity<Long> {
    
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "userId")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "categoryId")
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
