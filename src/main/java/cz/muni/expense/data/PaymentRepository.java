package cz.muni.expense.data;

import cz.muni.expense.model.Category;
import cz.muni.expense.model.Payment;
import cz.muni.expense.model.Rule;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Peter Petrinec
 */
@Stateless
public class PaymentRepository extends GenericRepository<Payment> {

    public PaymentRepository() {
        super(Payment.class);
    }

    /**
     * Creates a new payment in database and automatically links it to a
     * category defined by user's rules.
     *
     * @param p New payment
     * @return Newly created payment
     */
    @Override
    public Payment create(Payment p) {
        Category cat = this.findCategory(p);
        if (cat != null) {
            p.setCategory(cat);
        }
        return super.create(p);
    }
    
     public List<Payment> findPaymentsByUserId(Long userId) {
        Query query = em.createQuery("SELECT p FROM Payment p "
                + "WHERE p.user.id = :userId");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    private Category findCategory(Payment p) {
        if (p.getUser() == null) {
            throw new IllegalArgumentException("User can not be null");
        }
        List<Rule> rules = this.findRulesByUserId(p.getUser().getId());
        for (Rule rule : rules) {
            if (StringUtils.containsIgnoreCase(p.getAdditionalInfo(), rule.getRuleString())) {
                return rule.getCategory();
            }
        }
        return null;
    }

    private List<Rule> findRulesByUserId(Long userId) {
        Query query = em.createQuery("SELECT r FROM Rule r "
                + "WHERE r.user.id = :userId");
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
