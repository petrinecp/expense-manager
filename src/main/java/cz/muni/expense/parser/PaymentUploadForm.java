package cz.muni.expense.parser;

import cz.muni.expense.enums.BankIdentifier;
import java.util.LinkedList;
import java.util.List;

/**
 * POJO to automatically map payments history upload form into this class
 * 
 * @author Peter Petrinec
 */
public class PaymentUploadForm {
    
    private BankIdentifier bankIdentifier;
    private List<byte[]> sources = new LinkedList<>();

    public PaymentUploadForm(BankIdentifier bankIdentifier) {
        this.bankIdentifier = bankIdentifier;
    }

    public void addSource(byte[] source) {
        sources.add(source);
    }
    
    public BankIdentifier getBankIdentifier() {
        return bankIdentifier;
    }

    public List<byte[]> getSources() {
        return sources;
    }
}
