/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.model;

import cz.muni.expense.enums.BankIdentifier;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 *
 * @author Peter Petrinec
 */
@Entity
@Table(name = "Banks")
public class Bank extends BaseEntity<Long> {
    
    private String title;
    
    @Enumerated(EnumType.STRING)
    private BankIdentifier identifier;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BankIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(BankIdentifier identifier) {
        this.identifier = identifier;
    }
}
