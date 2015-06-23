package cz.muni.expense.model;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author Peter Petrinec 
 * @param <ID> 
 */
@MappedSuperclass
public abstract class BaseEntity<ID> implements Serializable {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;  
        if (object == null) return false;  
        if (getClass() != object.getClass()) return false;
        
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BaseEntity)) {
            return false;
        }
        BaseEntity other = (BaseEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
