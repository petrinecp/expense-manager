/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.model;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author Peter Petrinec 
 * @param <ID> 
 */
@MappedSuperclass
public abstract class BaseEntity<ID> implements Serializable {
 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ID id;
 
    public ID getId() {
        return id;
    }
 
    public void setId(ID id) {
        this.id = id;
    }
 
}
