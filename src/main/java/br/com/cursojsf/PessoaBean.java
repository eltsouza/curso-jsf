package br.com.cursojsf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

import br.com.dao.DaoGeneric;
import br.com.entidades.Cidades;
import br.com.entidades.Estados;
import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUtil;
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
	
	private List<SelectItem> estados;
	
	private List<SelectItem> cidades;
	
	public String salvar() {
		pessoa = daoGeneric.merge(pessoa);
		carregarPessoas();
		mostrarMsg("Registro cadastrado com sucesso.");
		return "";
	}
	
	private void mostrarMsg(String msg) {
  	   FacesContext context = FacesContext.getCurrentInstance();
       FacesMessage message = new FacesMessage(msg);
       context.addMessage(null, message);
	}

	public String novo() {
		pessoa = new Pessoa();
		return "";
	}
	
	public String remove() {
		daoGeneric.deletePorId(pessoa);
		carregarPessoas();
		novo();
		mostrarMsg("Registro removido com sucesso");
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
	
	public void pesquisaCep(AjaxBehaviorEvent event){

	   try {
		   URL url = new URL("https://viacep.com.br/ws/" + pessoa.getCep() + "/json/"); // retorna um JSon
		   URLConnection connection = url.openConnection(); // abre a conexao no WebService
		   InputStream is =  connection.getInputStream(); // Armazena o texto JSon no inputStrem
		   BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8")); // jogar para um buffer o inputStrem
		   
		   String cep = "";
		   StringBuilder jsonCep = new StringBuilder();
		   
		   while ((cep = br.readLine()) != null) { // enquanto existir linha
			  jsonCep.append(cep);
		   }

		   // biblioteca do google para converte objetos JSON
		   // converte o objeto JSON retornado do WebService para a classe pessoa.
		   Pessoa gsonAux = new Gson().fromJson(jsonCep.toString(), Pessoa.class); 
		   
		   pessoa.setCep(gsonAux.getCep());
		   pessoa.setLogradouro(gsonAux.getLogradouro());
		   pessoa.setComplemento(gsonAux.getComplemento());
		   pessoa.setBairro(gsonAux.getBairro());
		   pessoa.setLocalidade(gsonAux.getLocalidade());
		   pessoa.setUf(gsonAux.getUf());
		   pessoa.setUnidade(gsonAux.getUnidade());
		   pessoa.setIbge(gsonAux.getIbge());
		   pessoa.setGia(gsonAux.getGia());
		   
	   }catch(Exception e) {
		   e.printStackTrace();
		   mostrarMsg("Erro ao consultar o Cep.:" + pessoa.getCep());
	   }
		
		
	
	}
	
	public String logar() {
		
       Pessoa pessoaUser = iDaoPessoa.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());		
		
       if (pessoaUser != null) { //achou o usuario no banco
    	   //adiciona usuario na sessão
    	   FacesContext context = FacesContext.getCurrentInstance();
    	   ExternalContext externalContext = context.getExternalContext();
    	   externalContext.getSessionMap().put("usuarioLogado", pessoaUser);
    	   return "primeiraPagina.jsf";
       }else {
	      return "index.jsf";
       }		
	}
	
	public String deslogar() {
 	   FacesContext context = FacesContext.getCurrentInstance();
 	   ExternalContext externalContext = context.getExternalContext();
 	   externalContext.getSessionMap().remove("usuarioLogado");
 	   
 	   HttpServletRequest httpServletRequest = (HttpServletRequest)
 			   context.getCurrentInstance().
 			   getExternalContext().
 			   getRequest();
 	   
 	   httpServletRequest.getSession().invalidate();
	   return "index.jsf";
	}
	
	
	public String limparFormulario() {
	   pessoa = new Pessoa();
       return "";
	}
	
    public boolean permiteAcesso(String acesso) {
	    //recupera o usuario da sessao
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		
		return pessoaUser.getPerfilUser().equals(acesso);
	  }
    
    public List<SelectItem> getEstados() {
    	estados = iDaoPessoa.listaEstados();
		return estados;
	}
    
    //parametro obrigatorio, o JSF controla tudo
    public void carregaCidades(AjaxBehaviorEvent event) {
    	
    	String codigoEstado = (String) event.getComponent().getAttributes().get("submittedValue");
    	
    	if(codigoEstado != null) {
    	
           Estados estado = JPAUtil.getEntityManager().
        		   find(Estados.class, Long.parseLong(codigoEstado));
           
           if(estado != null) {
        	   pessoa.setEstados(estado);
        	   
        	   List<Cidades> cidades = JPAUtil.getEntityManager().
        			   createQuery("from Cidades where estados.id = " 
        	          + codigoEstado).getResultList();
        	   
        	   List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();

        	   for(Cidades cidade : cidades) {        		   
        		   selectItemsCidade.add(new SelectItem(cidade.getId(),cidade.getNome()));
        	   }
        	   
        	   setCidades(selectItemsCidade);
           }
    	
    	}
    	
    	
    }
     
    public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}
    
    public List<SelectItem> getCidades() {
		return cidades;
	}
     
    
    
}

