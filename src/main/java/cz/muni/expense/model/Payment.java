package cz.muni.expense.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Peter Petrinec
 */
@Entity
@Table(name = "Payments")
public class Payment extends BaseEntity<Long> {

    @ManyToOne
    private User user;

    @ManyToOne
    private Bank bank;

    @ManyToOne
    private Category category;

    @Temporal(TemporalType.DATE)
    private Date paymentDate;

    private BigDecimal amount;
    private String infoForReceiver1;
    private String infoForReceiver2;

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getInfoForReceiver1() {
        return infoForReceiver1;
    }

    public void setInfoForReceiver1(String infoForReceiver1) {
        this.infoForReceiver1 = infoForReceiver1;
    }

    public String getInfoForReceiver2() {
        return infoForReceiver2;
    }

    public void setInfoForReceiver2(String infoForReceiver2) {
        this.infoForReceiver2 = infoForReceiver2;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
//</editor-fold>
}
