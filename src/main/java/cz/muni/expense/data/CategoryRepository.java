package cz.muni.expense.data;

import javax.ejb.Stateless;
import cz.muni.expense.model.Category;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Peter Petrinec
 */
@Stateless
public class CategoryRepository extends GenericRepository<Category> {

    public CategoryRepository() {
        super(Category.class);
    }
    
    public List<Category> listUserAndGlobalCategoriesByUserId(Long id) {
        Query query = em.createQuery("SELECT c FROM Category c "
                + "WHERE c.user.id = :userId OR c.user.id IS NULL");
        query.setParameter("userId", id);
        return query.getResultList();
    }
}
