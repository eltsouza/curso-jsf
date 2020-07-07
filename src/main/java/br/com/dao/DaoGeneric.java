package br.com.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.jpautil.JPAUtil;

//<E> inidica que o DAO Generico vai receber qualquer tipo de classe/Entidade

public class DaoGeneric<E> {

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
	
	
}
