/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.parser;

import cz.muni.expense.exception.ParserException;
import cz.muni.expense.model.Payment;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author Peter Petrinec
 */
public interface Parser {
    List<Payment> parse(InputStream stream) throws ParserException;
}
