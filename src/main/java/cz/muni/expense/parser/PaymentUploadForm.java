package cz.muni.expense.parser;

import cz.muni.expense.enums.BankIdentifier;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

/**
 * POJO to automatically map payments history upload form into this class
 * 
 * @author Peter Petrinec
 */
public class PaymentUploadForm {
    
    @FormParam("bank")
    @PartType(MediaType.TEXT_PLAIN)
    private String bank;
    
    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private byte[] file;

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public BankIdentifier getBankIdentifier() {
        return BankIdentifier.valueOf(bank);
    }

    public void setBankIdentifier(String bank) {
        this.bank = bank;
    }
    
    
}
