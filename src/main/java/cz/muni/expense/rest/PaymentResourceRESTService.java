package cz.muni.expense.rest;

import cz.muni.expense.data.BankRepository;
import cz.muni.expense.data.PaymentRepository;
import cz.muni.expense.exception.ParserException;
import cz.muni.expense.model.Bank;
import cz.muni.expense.model.Payment;
import cz.muni.expense.parser.Parser;
import cz.muni.expense.parser.ParserFactory;
import cz.muni.expense.parser.PaymentUploadForm;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

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
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadPaymentsHistory(@MultipartForm PaymentUploadForm input) {
        Response.ResponseBuilder builder = null;
        Bank bank = bankRepository.findByIdentifier(input.getBankIdentifier());

        try (InputStream stream = new ByteArrayInputStream(input.getFile())) {
            Parser parser = parserFactory.getParser(input.getBankIdentifier());
            List<Payment> payments = parser.parse(stream);
            for (Payment p : payments) {
                p.setBank(bank);
                repository.create(p);
            }
            builder = Response.ok();
        } catch (IllegalArgumentException ex) {
            builder = Response.status(Response.Status.BAD_REQUEST);
        } catch (IOException ex) {
            builder = Response.status(Response.Status.BAD_REQUEST);
        } catch (ParserException ex) {
            builder = Response.status(Response.Status.BAD_REQUEST);
        }

        return builder.build();
    }
}
