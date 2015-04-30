package cz.muni.expense.parser;

import cz.muni.expense.enums.BankIdentifier;
import cz.muni.expense.exception.ParserException;

/**
 *
 * @author Peter Petrinec
 */
public interface ParserFactory {
    
    Parser getParser(BankIdentifier type) throws ParserException;
}
