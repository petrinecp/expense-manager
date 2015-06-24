/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.data;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import cz.muni.expense.model.User;
import cz.muni.expense.model.User_;
/**
 *
 * @author Lukáš Valach
 */
@Stateless	
public class UserRepository extends GenericRepository<User> {

    public UserRepository() {
        super(User.class);
    }
    
    public User findByUsername(String username) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);
        criteria.select(user).where(cb.equal(user.get(User_.username), username));
        try{
        	return em.createQuery(criteria).getSingleResult();
        } catch (NoResultException ex){
        	return null;
        }
    }
    
    public User findByUsernameAndPassword(String username, String password){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        //criteria.select(member).where(cb.equal(member.get(Member_.name), email));
        criteria.select(user).where(cb.equal(user.get(User_.username), username));
        try{
        	User userToReturn = em.createQuery(criteria).getSingleResult();
        	return userToReturn.getPasswd().equals(password) ? userToReturn : null;
        } catch (NoResultException ex){
        	return null;
        }
    }

    public User findByUsernameAndAuthToken(String authId, String authToken) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);
        criteria.select(user).where(cb.equal(user.get(User_.username), authId));
        try{
        	User userToReturn = em.createQuery(criteria).getSingleResult();
        	return userToReturn.getAuthToken().equals(authToken) ? userToReturn : null;
        } catch (NoResultException ex){
        	return null;
        }
    }
    	
}
