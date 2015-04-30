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
    
}
