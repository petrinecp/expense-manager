/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.data;

import cz.muni.expense.enums.BankIdentifier;
import cz.muni.expense.model.Bank;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Peter Petrinec
 */
@Stateless
public class BankRepository extends GenericRepository<Bank> {

    public BankRepository() {
        super(Bank.class);
    }

    public Bank findByIdentifier(BankIdentifier identifier) {
        Query query = em.createQuery(
                "SELECT b FROM Bank b WHERE b.identifier = :identifier");
        query.setParameter("identifier", identifier);
        return (Bank)query.getSingleResult();
    }
}
