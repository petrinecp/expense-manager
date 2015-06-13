package cz.muni.expense.data;

import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.expense.data.CategoryRepository;
import cz.muni.expense.model.Category;


/**
 * Test class for testing CategoryRepository
 * 
 * @author Martin Drimal
 *
 */
@RunWith(Arquillian.class)
public class CategoryRepositoryTest {

	@Deployment
    public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addPackages(true, "cz.muni.expense")
                .addAsResource("META-INF/our-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/test-import.sql", "import.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("test-ds.xml");
	}
	
	private Category defaultCategory;
	
	@Inject
	CategoryRepository repository;
	
	@Before
	public void setUp(){
		defaultCategory = new Category();
		defaultCategory.setTitle("testing");
		
		repository.create(defaultCategory);
	}
	
	@Test
	public void createCategoryTest(){
		Category category = new Category();
		category.setTitle("Workgroup");
		repository.create(category);
		
		Category categoryFromDB = repository.findById(category.getId());
		assertEquals("Bad category title", "Workgroup", categoryFromDB.getTitle());		
	}
	
	@Test
	public void deleteCategoryTest(){
		repository.delete(defaultCategory);
		List<Category>  categories = repository.findAll();
		assertEquals("Database should contains 3 category.", 3, categories.size());
	}
	
	@Test
	public void deleteByIdTest(){
		repository.deleteById(defaultCategory.getId());
		List<Category>  categories = repository.findAll();
		assertEquals("Database should contains 3 category.", 3, categories.size());
	}
	
	@Test
	public void findByIdTest(){
		Category category = repository.findById(defaultCategory.getId());
		assertEquals("Bad category title", "testing", category.getTitle());	
	}

	@Test
	public void findAllCategoriesTest(){
		List<Category> categories = repository.findAll();
		assertEquals("Database should contains two payments", 3, categories.size());
	}
	
}
