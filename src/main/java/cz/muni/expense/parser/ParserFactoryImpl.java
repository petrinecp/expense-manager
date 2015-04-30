package cz.muni.expense.parser;

import cz.muni.expense.enums.BankIdentifier;
import cz.muni.expense.exception.ParserException;
import java.util.Objects;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author Peter Petrinec
 */
@RequestScoped
public class ParserFactoryImpl implements ParserFactory {

    @Inject
    private Instance<Parser> availableParsers;

    @Override
    public Parser getParser(BankIdentifier bank) throws ParserException {
        Objects.requireNonNull(bank);
        Parser parser = availableParsers.select(bank.getClazz()).get();
        if (parser == null) {
            throw new ParserException("Parser for bank '" + bank.toString() +"' was not provided.");
        }
        //Annotation[] annot = type.getClazz().getDeclaredAnnotations();
//        return availableParsers.select(, null);
        
//        Class<?> clazz = type.annotationType().getAnnotations(;
//        availableParsers.getClass();
//        for (Parser parser : availableParser) {
////            availableParser.select(type., antns)
////            if (report.getType().equals(type)) { //or whatever test you need
////                return report;
////            }
//        }
        return parser;
    }
}
