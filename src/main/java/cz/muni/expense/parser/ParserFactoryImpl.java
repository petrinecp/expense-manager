package cz.muni.expense.parser;

import cz.muni.expense.enums.BankIdentifier;
import cz.muni.expense.enums.FileFormat;
import cz.muni.expense.exception.ParserException;
import java.util.Objects;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

/**
 *
 * @author Peter Petrinec
 */
@RequestScoped
public class ParserFactoryImpl implements ParserFactory {

    @Inject @Any
    private Instance<Parser> availableParsers;

    @Override
    public Parser getParser(BankIdentifier bank) throws ParserException {
        Objects.requireNonNull(bank);
        Parser parser = availableParsers.select(new ParserTypeQualifier(bank, FileFormat.XML)).get();

        if (parser == null) {
            throw new ParserException("Parser for bank '" + bank.toString() +"' was not provided.");
        }

        return parser;
    }
}

class ParserTypeQualifier extends AnnotationLiteral<ParserType> implements ParserType {
    BankIdentifier identifier;
    FileFormat format;
    
    public ParserTypeQualifier(BankIdentifier identifier, FileFormat format) {
        this.identifier = identifier;
        this.format = format;
    }

    
    @Override
    public BankIdentifier bank() {
        return identifier;
    }

    @Override
    public FileFormat format() {
        return format;
    }
    
}
