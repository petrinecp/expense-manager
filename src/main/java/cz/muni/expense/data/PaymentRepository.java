package cz.muni.expense.data;

import cz.muni.expense.model.Payment;
import javax.ejb.Stateless;

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
     * Creates a new payment in database and automatically
     * links it to a category defined by user's rules.
     * 
     * @param p New payment
     * @return Newly created payment
     */
    @Override
    public Payment create(Payment p) {
        return super.create(p);
    } 
}
