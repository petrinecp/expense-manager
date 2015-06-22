/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.data;

import cz.muni.expense.model.Category;
import cz.muni.expense.model.Payment;
import cz.muni.expense.model.Rule;
import cz.muni.expense.model.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author majo
 */
@Stateless
public class RuleRepository extends GenericRepository<Rule> {

    public RuleRepository() {
        super(Rule.class);
    }

    public Category findCategory(Payment p) {
        if (p.getUser() == null) {
            throw new IllegalArgumentException("User can not be null");
        }
        List<Rule> rules = this.findRulesByUserId(p.getUser().getId());
        for (Rule rule : rules) {
            if (p.getInfoForReceiver1().contains(rule.getRuleString())
                    || p.getInfoForReceiver2().contains(rule.getRuleString())) {
                return rule.getCategory();
            }
        }
        return null;
    }

    public List<Rule> findRulesByUserId(Long userId) {
        Query query = em.createQuery("SELECT r FROM Rule r "
                + "WHERE r.user.id = :userId");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

}
