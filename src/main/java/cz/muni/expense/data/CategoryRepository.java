/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.data;

import javax.ejb.Stateless;
import cz.muni.expense.model.Category;

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
