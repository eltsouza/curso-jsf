package br.com.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import br.com.jpautil.JPAUtil;

//<E> inidica que o DAO Generico vai receber qualquer tipo de classe/Entidade

@Named
public class DaoGeneric<E> implements Serializable{

	private static final long serialVersionUID = 1L;
   
	@Inject
	private EntityManager entityManager;
	
	@Inject
	private JPAUtil jpaUtil;
	
	public void salva(E entidade) {

		EntityTransaction entityTransaction = entityManager.getTransaction();
   		entityTransaction.begin();
   		
   		entityManager.persist(entidade); //salva os dados da entidade no banco
   		
   		entityTransaction.commit();
   	}
   	public E merge(E entidade) {
   	    EntityTransaction entityTransaction = entityManager.getTransaction();
   		entityTransaction.begin();

   		E retorno = entityManager.merge(entidade); //salva ou atualiza os dados da entidade no banco
   		entityTransaction.commit();
   		
   		return retorno;
   	}
	public void delete(E entidade) {
   	    EntityTransaction entityTransaction = entityManager.getTransaction();
   		entityTransaction.begin();

   		entityManager.remove(entidade);
   		entityTransaction.commit();
	}   	
	public void deletePorId(E entidade) {
  	    EntityTransaction entityTransaction = entityManager.getTransaction();
   		entityTransaction.begin();

   		Object id = jpaUtil.getPrimayKey(entidade);
   		
   		entityManager.createQuery("delete from " + entidade.getClass().getCanonicalName() + " where id = " + id).executeUpdate();
   		entityTransaction.commit();
	}
	public List<E> getListEntity(Class<E> entidade){
        EntityTransaction entityTransaction = entityManager.getTransaction();
   		entityTransaction.begin();        

   		List<E> retorno = entityManager.createQuery("from " + entidade.getName()).getResultList();
   		entityTransaction.commit();
   		
   		return retorno;
	}
	public E consultar(Class<E> entidade, String codigo){
	 	EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		E objeto = (E) entityManager.find(entidade, Long.parseLong(codigo));
		entityTransaction.commit();
		return objeto;
		
	}	
	
}
