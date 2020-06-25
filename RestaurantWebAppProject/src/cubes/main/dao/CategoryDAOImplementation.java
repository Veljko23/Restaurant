package cubes.main.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cubes.main.entity.Category;
import cubes.main.entity.Product;

@Repository
public class CategoryDAOImplementation implements CategoryDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	@Override
	public List<Category> getCategoryList() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Category> query = session.createQuery("from Category", Category.class);
		
		List<Category> categoryList = query.getResultList();
		
		
		return categoryList;
	}

	@Transactional
	@Override
	public void saveCategory(Category category) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(category);
	}

	@Transactional
	@Override
	public Category getCategory(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Category category = session.get(Category.class, id);
		
		return category;
	}

	@Transactional
	@Override
	public void deleteCategory(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery(" delete from Category where id=:id");
		
		query.setParameter("id", id);
		
		query.executeUpdate();
		
	}

	@Transactional
	@Override
	public List<Category> getCategoriesOnMainPage() {

		Session session = sessionFactory.getCurrentSession();
		
		Query<Category> query = session.createQuery("select c from Category c where c.isOnMainPage=1");
		List<Category> ca = new ArrayList<Category>();
		List<Category> list = query.getResultList();
		
		for(Category cat: list) {
			
			Query<Product> productQuery = session.createQuery("select p from Product p where p.isOnMainPage=1 AND p.category.id=:id");
			productQuery.setParameter("id", cat.getId());
			cat.setProductsOnMainPage(productQuery.getResultList());
			
			ca.add(cat);
			
			Iterator<Category> iter = ca.iterator();
			
			while(iter.hasNext()) {
				Category categ = iter.next();
				if(categ.getProductsOnMainPage().isEmpty())
					iter.remove();
			}
		}
		
		return ca;
	}
	
	@Transactional
	@Override
	public List<Category> getCategoriesOnMenuPage() {

		Session session = sessionFactory.getCurrentSession();
		
		Query<Category> query = session.createQuery("from Category");
		
		List<Category> list = query.getResultList();
		List<Category> ca = new ArrayList<Category>();
		for(Category cat: list) {
	
			Query<Product> productQuery = session.createQuery("select p from Product p where p.isOnMenu=1 AND p.category.id=:id");
			productQuery.setParameter("id", cat.getId());
			cat.setProductsOnMainPage(productQuery.getResultList());
			//list.remove(cat.getProductsOnMainPage().size()-1==0);
			//if(cat.getProductsOnMainPage().size()<1) {
				//list.remove(cat);
			//}
			
			
			ca.add(cat);
			
			Iterator<Category> iter = ca.iterator();
			
			while(iter.hasNext()) {
				Category categ = iter.next();
				if(categ.getProductsOnMainPage().isEmpty())
					iter.remove();
			}
			
			//for (Iterator<Category> iterator = ca.iterator(); iterator.hasNext(); ) {
			  //  Category value = iterator.next();
			    //if (value.getProductsOnMainPage().isEmpty()) {
			      //  iterator.remove();
			    //}
			//}
			
			//System.out.println(cat.getName() + " ima" + cat.getProductsOnMainPage().size() + " proizvoda");
		}
		
		return ca;
	}

	@Transactional
	@Override
	public List<Category> getCategoriesForFilter() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Category> query = session.createQuery("from Category");
		
		List<Category> list = query.getResultList();
		
		for(Category cat: list) {
			
			Query queryCount = session.createQuery("select count(product.id) from Product product where product.category.id=:id");
			queryCount.setParameter("id", cat.getId());
			cat.setCount((long) queryCount.uniqueResult());
		}
		
		return list;
	}

}
