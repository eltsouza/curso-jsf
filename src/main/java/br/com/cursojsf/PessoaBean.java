package br.com.cursojsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import br.com.dao.DaoGeneric;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoPessoa;
import br.com.repository.IDaoPessoaImpl;

@ViewScoped
@ManagedBean(name ="pessoaBean")
public class PessoaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	
	private IDaoPessoa iDaoPessoa = new IDaoPessoaImpl();
	
	public String salvar() {
		pessoa = daoGeneric.merge(pessoa);
		carregarPessoas();
		return "";
	}
	
	public String novo() {
		pessoa = new Pessoa();
		return "";
	}
	
	public String remove() {
		daoGeneric.deletePorId(pessoa);
		carregarPessoas();
		novo();
		return "";
	}
	
	@PostConstruct
	public void carregarPessoas() {
		pessoas = daoGeneric.getListEntity(Pessoa.class);
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DaoGeneric<Pessoa> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Pessoa> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}
	
	public List<Pessoa> getPessoas() {
		return pessoas;
	}
	
	public String logar() {
		
       Pessoa pessoaUser = iDaoPessoa.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());		
		
       if (pessoa != null) { //achou o usuario no banco
    	   
    	   //adiciona usuario na sessão
    	   FacesContext context = FacesContext.getCurrentInstance();
    	   ExternalContext externalContext = context.getExternalContext();
    	   externalContext.getSessionMap().put("usuarioLogado", pessoaUser.getLogin());
    	   
    	   return "primeiraPagina.jsf";
    	   
       }
       
	   return "index.jsf";
		
	}
	
	public boolean permiteAcesso(String acesso) {

		//adiciona usuario na sessão
 	   FacesContext context = FacesContext.getCurrentInstance();
 	   ExternalContext externalContext = context.getExternalContext();
 	   Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		
	   return pessoaUser.getPerfilUser().equals(acesso);
	}

}
