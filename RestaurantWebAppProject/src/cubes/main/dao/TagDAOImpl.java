package cubes.main.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cubes.main.entity.Product;
import cubes.main.entity.Tag;

@Repository
public class TagDAOImpl implements TagDAO {
	
	@Autowired
	SessionFactory sessionFactory;

	@Transactional
	@Override
	public List<Tag> getTagList() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Tag> listTag = session.createQuery("from Tag", Tag.class);
		
		List<Tag> list = listTag.getResultList();
		
		return list;
	}

	@Transactional
	@Override
	public void saveTag(Tag tag) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.saveOrUpdate(tag);
		
	}

	@Transactional
	@Override
	public Tag updateTag(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Tag tag = session.get(Tag.class, id);
		
		return tag;
	}

	@Transactional
	@Override
	public void deleteTag(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("delete from Tag where id=:id");
		
		query.setParameter("id", id);
		
		query.executeUpdate();
		
	}

	@Transactional
	@Override
	public List<Tag> getTagsById(List<Integer> ids) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Tag> query = session.createQuery("select t from Tag t where t.id IN (:ids)", Tag.class);
		
		query.setParameter("ids", ids);
		
		List<Tag> tags = query.getResultList();
		
		return tags;
	}

	@Transactional
	@Override
	public List<Tag> getTagListByProducts() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query<Tag> query = session.createQuery("from Tag");
		
		List<Tag> list = query.getResultList();
		
		for(Tag tag: list) {
			Hibernate.initialize(tag.getProducts());
		}
		
		return list;
	}

	
	

}
