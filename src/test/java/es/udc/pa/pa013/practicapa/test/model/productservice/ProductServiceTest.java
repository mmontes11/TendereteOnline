package es.udc.pa.pa013.practicapa.test.model.productservice;

import static es.udc.pa.pa013.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa013.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa013.practicapa.model.category.Category;
import es.udc.pa.pa013.practicapa.model.category.CategoryDao;
import es.udc.pa.pa013.practicapa.model.product.Product;
import es.udc.pa.pa013.practicapa.model.product.ProductDao;
import es.udc.pa.pa013.practicapa.model.productservice.ProductService;
import es.udc.pa.pa013.practicapa.model.util.Block;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class ProductServiceTest {
	
	private final long NON_EXISTING_CATEGORY_ID = -1L;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private CategoryDao categoryDao;

	private Category createCategory(String name) {
		Category c = new Category(name);
		categoryDao.save(c);
		return c;
	}

	private Product createProduct(Category c, String name) {
		Product p = new Product(c, name, (float) 50.0);
		productDao.save(p);
		return p;
	}

	@Test
	public void testFindExistingProductsByKeywords() {
		Category mobiles = createCategory("Mobile Phones");
		Product nexus4 = createProduct(mobiles, "Nexus 4");
		Product s4 = createProduct(mobiles, "Galaxy S4");
		Category games = createCategory("Games");
		createProduct(games, "Crysis 3");
		createProduct(games, "Call of Duty MW3");
		List<Product> productList = new ArrayList<Product>(), resultList;
		productList.add(nexus4);
		productList.add(s4);

		resultList = productService.findProductsByKeywordsCategory("",
				mobiles.getCategoryId(), 0, 10).getList();
		assertEquals(resultList, productList);

		resultList = productService.findProductsByKeywordsCategory("gAlaxy",
				null, 0, 10).getList();
		assertEquals(resultList.size(), 1);
		assertEquals(resultList.get(0), s4);

		resultList = productService.findProductsByKeywordsCategory("4", null,
				0, 10).getList();
		assertEquals(resultList.size(), 2);
		assertEquals(resultList.get(0), nexus4);
		assertEquals(resultList.get(1), s4);

		resultList = productService.findProductsByKeywordsCategory("nExUs 4",
				mobiles.getCategoryId(), 0, 10).getList();
		assertEquals(resultList.size(), 1);
		assertEquals(resultList.get(0), nexus4);
	}

	@Test
	public void testFindNonExistingProductsByKeyWords() {
		Category mobiles = createCategory("Mobile Phones");
		createProduct(mobiles, "Nexus 4");
		createProduct(mobiles, "Galaxy S4");
		Category games = createCategory("Games");
		createProduct(games, "Crysis 3");
		createProduct(games, "Call of Duty MW3");
		List<Product> resultList;

		resultList = productService.findProductsByKeywordsCategory("Iphone 5",
				mobiles.getCategoryId(), 0, 10).getList();
		assertTrue(resultList.isEmpty());

		resultList = productService.findProductsByKeywordsCategory("gAlaxy",
				games.getCategoryId(), 0, 10).getList();
		assertTrue(resultList.isEmpty());
	}
	
	@Test
	public void testFindProductsWithNonExistingCategory (){
		Category c = createCategory("Category");
		categoryDao.save(c);
		Product p = createProduct(c, "Product");
		Block<Product> blockProduct = productService.findProductsByKeywordsCategory(p.getName(), NON_EXISTING_CATEGORY_ID, 0, 10);
		assertTrue(blockProduct.getList().isEmpty());
	}
	
	@Test
	public void testFindBlockProduct (){
		Category c = createCategory("Category");
		categoryDao.save(c);
		for (int i=0 ; i<11 ; i++){
			Product p = createProduct(c, "Product"+"V"+String.valueOf(i)+".0");
			productDao.save(p);
		}
		Block<Product> blockProduct = null;
		blockProduct = productService.findProductsByKeywordsCategory("Product", c.getCategoryId(), 0, 10); 
		assertEquals(blockProduct.getList().size(),10);
		assertTrue(blockProduct.getExistMore());
		blockProduct = productService.findProductsByKeywordsCategory("Product", c.getCategoryId(), 10, 10);
		assertEquals(blockProduct.getList().size(),1);
		assertFalse(blockProduct.getExistMore());
	}
	

	@Test
	public void testGetCategories() {
		Category games = createCategory("Games");
		Category mobiles = createCategory("Mobiles");
		assertEquals(productService.getCategories().size(), 2);
		assertEquals(productService.getCategories().get(0), games);
		assertEquals(productService.getCategories().get(1), mobiles);
	}

}
