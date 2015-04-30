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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof Payment)) {
//            return false;
//        }
//        Payment other = (Payment) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "cz.muni.expense.model.Payment[ id=" + id + " ]";
//    }
}
