package cz.muni.expense.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;

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

    @Pattern(regexp = "[0-9]*/[0-9]{4}")
    private String accountNumber;
    
    @Temporal(TemporalType.DATE)
    private Date paymentDate;

    private BigDecimal amount;
    private String additionalInfo;

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

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
//</editor-fold>
}
