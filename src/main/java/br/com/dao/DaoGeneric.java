package br.com.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import br.com.jpautil.JPAUtil;

//<E> inidica que o DAO Generico vai receber qualquer tipo de classe/Entidade

public class DaoGeneric<E> implements Serializable{

	private static final long serialVersionUID = 1L;

	public void salva(E entidade) {
   		EntityManager entityManager = JPAUtil.getEntityManager();
   	    EntityTransaction entityTransaction = entityManager.getTransaction();
   		entityTransaction.begin();
   		entityManager.persist(entidade); //salva os dados da entidade no banco
   		entityTransaction.commit();
   		entityManager.close();
   	}
   	public E merge(E entidade) {
   		EntityManager entityManager = JPAUtil.getEntityManager();
   	    EntityTransaction entityTransaction = entityManager.getTransaction();
   		entityTransaction.begin();
   		E retorno = entityManager.merge(entidade); //salva ou atualiza os dados da entidade no banco
   		entityTransaction.commit();
   		entityManager.close();
   		
   		return retorno;
   	}
	public void delete(E entidade) {
   		EntityManager entityManager = JPAUtil.getEntityManager();
   	    EntityTransaction entityTransaction = entityManager.getTransaction();
   		entityTransaction.begin();
   		entityManager.remove(entidade);
   		entityTransaction.commit();
   		entityManager.close();
	}   	
	public void deletePorId(E entidade) {
   		EntityManager entityManager = JPAUtil.getEntityManager();
   	    EntityTransaction entityTransaction = entityManager.getTransaction();
   		entityTransaction.begin();
   		Object id = JPAUtil.getPrimayKey(entidade);
   		entityManager.createQuery("delete from " + entidade.getClass().getCanonicalName() + " where id = " + id).executeUpdate();
   		entityTransaction.commit();
   		entityManager.close();
	}
	public List<E> getListEntity(Class<E> entidade){
   		EntityManager entityManager = JPAUtil.getEntityManager();
   	    EntityTransaction entityTransaction = entityManager.getTransaction();
   		entityTransaction.begin();
        
   		List<E> retorno = entityManager.createQuery("from " + entidade.getName()).getResultList();
   		
   		entityTransaction.commit();
   		entityManager.close();
   		
   		return retorno;
	}
	
}
