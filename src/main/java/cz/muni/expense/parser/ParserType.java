package cz.muni.expense.parser;

import cz.muni.expense.enums.FileFormat;
import cz.muni.expense.enums.BankIdentifier;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ FIELD, TYPE, METHOD })
public @interface ParserType {

	BankIdentifier bank();
        FileFormat format();
}