/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.model;

import cz.muni.expense.enums.BankIdentifier;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Peter Petrinec
 */
@Entity
@Table(name = "Banks")
public class Bank extends BaseEntity<Long> {
    
    private String title;
       
//    @OneToMany(mappedBy = "bank")
//    private List<Payment> payments;
    
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

//    public List<Payment> getPayments() {
//        return payments;
//    }
//
//    public void setPayments(List<Payment> payments) {
//        this.payments = payments;
//    }
}
