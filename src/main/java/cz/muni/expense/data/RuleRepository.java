/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.data;

import cz.muni.expense.model.Rule;
import javax.ejb.Stateless;

/**
 *
 * @author majo
 */
@Stateless
public class RuleRepository extends GenericRepository<Rule> {

    public RuleRepository() {
        super(Rule.class);
    }
 
}
