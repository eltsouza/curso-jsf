package br.com.jpautil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
	
	private static EntityManagerFactory factory = null;
	
	//recurso estatico, se não existir um EntityManager, ele será criado
	static {
      if (factory == null) {
  		factory = Persistence.createEntityManagerFactory("curso-jsf"); // nome do persistence unit no arquivo persistence.xml
      }
	}
	
	public static EntityManager getEntityManager() {
		return factory.createEntityManager();
	}
   	

}
