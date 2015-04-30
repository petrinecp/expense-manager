package cz.muni.expense.enums;


import cz.muni.expense.parser.CsobXmlParser;
import cz.muni.expense.parser.Parser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Peter Petrinec
 */
public enum BankIdentifier {

    CSOB(CsobXmlParser.class), CSAS(CsobXmlParser.class);

    Class<? extends Parser> clazz;

    private BankIdentifier(Class<? extends Parser> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends Parser> getClazz() {
        return clazz;
    }
}
