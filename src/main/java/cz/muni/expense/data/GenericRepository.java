/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.data;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import cz.muni.expense.model.BaseEntity;

/**
 *
 * @author Peter Petrinec
 */
public abstract class GenericRepository<T extends BaseEntity> {
    
    protected Class<T> entityClass;
    
    @Inject
    protected EntityManager em;

    public GenericRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T create(T t) {
        em.persist(t);
        return t;
    }

    public T findById(final Object id) {
        return em.find(entityClass, id);
    }

    public T update(T t) {
        return em.merge(t);
    }

    public void delete(T t) {
        t = em.merge(t);
        em.remove(t);
    }
    
    public void deleteById(Object id) {
        em.remove(em.getReference(entityClass, id));
    }
    
    public List<T> findAll() {
        StringBuilder queryString = new StringBuilder("SELECT e FROM ");
        queryString.append(entityClass.getSimpleName()).append(" e");
        Query query = em.createQuery(queryString.toString()); 
        return query.getResultList();
    }
}
