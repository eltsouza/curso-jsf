package br.com.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Estados;
import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUtil;

public class IDaoPessoaImpl implements IDaoPessoa, Serializable {

	private static final long serialVersionUID = 1L;
	EntityManager entityManager = JPAUtil.getEntityManager();
	
	@Override
	public Pessoa consultarUsuario(String login, String senha) {
		
		Pessoa pessoa = null;
		
   	    EntityTransaction entityTransaction = entityManager.getTransaction();
   		entityTransaction.begin();
   		
		pessoa = (Pessoa) entityManager.createQuery("select p from Pessoa p where p.login = '" + login + "' and p.senha = '" + senha + "'").getSingleResult();

		entityTransaction.commit();
   		entityManager.close();
		
		return pessoa;
	}

	@Override
	public List<SelectItem> listaEstados() {

		List<SelectItem> selectItems = new ArrayList<SelectItem>();		
	//	EntityTransaction entityTransaction = entityManager.getTransaction();
//		entityTransaction.begin();
		List<Estados> estados = entityManager.createQuery("from Estados").getResultList(); 
		
		for (Estados estado : estados) {
			selectItems.add(new SelectItem(estado, estado.getNome()));
		}
		
		return selectItems;
	}


}
