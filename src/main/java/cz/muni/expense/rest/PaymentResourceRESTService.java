package cz.muni.expense.rest;

import cz.muni.expense.data.BankRepository;
import cz.muni.expense.data.PaymentRepository;
import cz.muni.expense.data.RuleRepository;
import cz.muni.expense.enums.BankIdentifier;
import cz.muni.expense.enums.UserRole;
import cz.muni.expense.exception.ParserException;
import cz.muni.expense.model.Bank;
import cz.muni.expense.model.Category;
import cz.muni.expense.model.Payment;
import cz.muni.expense.model.Rule;
import cz.muni.expense.parser.Parser;
import cz.muni.expense.parser.ParserFactory;
import cz.muni.expense.parser.PaymentUploadForm;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.util.GenericType;

/**
 *
 * @author Peter Petrinec
 */
@Path("/payment")
@RequestScoped
public class PaymentResourceRESTService extends GenericRESTService<Payment> {

    @Inject
    private BankRepository bankRepository;

    @Inject
    private ParserFactory parserFactory;

    @Inject
    private RuleRepository ruleRepository;

    public PaymentResourceRESTService() {
        PaymentRepository repo = CDI.current().select(PaymentRepository.class).get();
        this.setRepository(repo);
    }

    /**
     * API for processing payment history file. API consumes form-data request
     * with TEXT parameter 'bank' and its value specifying the bank by the
     * BankIdentifier enumeration. Second parameter 'file' is of type FILE and
     * should contain the file including payments history provided by the bank.
     *
     * @param input Request is mapped into this parameter.
     * @return
     */
    @RolesAllowed({UserRole.PRIVILEGED_USER})
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadPaymentsHistory(MultipartFormDataInput input) {
        Response.ResponseBuilder builder = null;

        try {
            PaymentUploadForm form = processInput(input);
            BankIdentifier identifier = form.getBankIdentifier();
            Bank bank = bankRepository.findByIdentifier(identifier);
            Parser parser = parserFactory.getParser(identifier);

            List<byte[]> sources = form.getSources();
            List<Future<List<Payment>>> payments = new LinkedList<>();

            // Run ansynchronous parsing
            for (byte[] data : sources) {
                try (InputStream stream = new ByteArrayInputStream(data)) {
                    payments.add(parser.parse(stream));
                } catch (ParserException ex) {
                    // Failed to parse data
                    Logger.getLogger(PaymentResourceRESTService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            // Save collected payments from the files to database
            for (Future<List<Payment>> payment : payments) {
                for (Payment p : payment.get()) {
                    p.setBank(bank);
                    setUser(p);
                    try {
                        repository.create(p);
                    } catch (Exception e) {
                        int a = 2;
                    }
                }
            }

            builder = Response.ok();
        } catch (IOException ex) {
            Logger.getLogger(PaymentResourceRESTService.class.getName()).log(Level.SEVERE, null, ex);
            //wrong params or types
        } catch (ParserException ex) {
            Logger.getLogger(PaymentResourceRESTService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(PaymentResourceRESTService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(PaymentResourceRESTService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return builder.build();
    }

    private PaymentUploadForm processInput(MultipartFormDataInput input) throws IOException {
        String bank = input.getFormDataPart("bank", new GenericType<String>() {
        });
        BankIdentifier identifier = BankIdentifier.valueOf(bank);
        List<InputPart> fileInputParts = input.getFormDataMap().get("file");

        PaymentUploadForm form = new PaymentUploadForm(identifier);
        for (InputPart part : fileInputParts) {
            byte[] data = part.getBody(new GenericType<byte[]>() {
            });
            form.addSource(data);
        }

        return form;
    }
    
    @Override
    protected void setUser(Payment t){
        t.setUser(getUser());
    }
    
    @Override
    protected boolean canEdit(Payment entity){
        return entity.getUser().equals(getUser());
    }
}
