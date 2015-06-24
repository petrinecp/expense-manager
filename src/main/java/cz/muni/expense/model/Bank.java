package cz.muni.expense.model;

import cz.muni.expense.enums.BankIdentifier;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Peter Petrinec
 */
@Entity
@Table(name = "Banks")
public class Bank extends BaseEntity<Long> {
    
    @NotNull
    @Size(min = 1, max = 50)
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
