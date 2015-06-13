/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.rest;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.Path;
import cz.muni.expense.data.BankRepository;
import cz.muni.expense.model.Bank;

/**
 *
 * @author Lukáš Valach
 */
@Path("/bank")
@RequestScoped
public class BankResourceRESTService extends GenericRESTService<Bank>{
    
    public BankResourceRESTService() {
        BankRepository repository = CDI.current().select(BankRepository.class).get();
        this.setRepository(repository);
    }
}
