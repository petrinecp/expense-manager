/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.tools.examples.data;

import javax.ejb.Stateless;
import org.jboss.tools.examples.model.Category;

/**
 *
 * @author Peter Petrinec
 */
@Stateless
public class CategoryRepository extends GenericRepository<Category> {

    public CategoryRepository() {
        super(Category.class);
    }
    
}
