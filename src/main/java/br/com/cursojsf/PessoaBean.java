package br.com.cursojsf;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

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
	
	private Part arquivoFoto;
	
	public String salvar() throws IOException{
		
        /*Processar imagem*/
		
	
		
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
    	
    	/*String codigoEstado = (String) event.getComponent().
    			getAttributes().get("submittedValue");*/
    	
    	Estados estado = (Estados) ((HtmlSelectOneMenu) event.getSource()).getValue();
    	
           if(estado != null) {
        	   pessoa.setEstados(estado);
        	   
       		   mostrarCidades(estado);

           }
    }
    
    public void mostrarCidades(Estados estado) {
    	
  	   List<Cidades> cidades = JPAUtil.getEntityManager().
			   createQuery("from Cidades where estados.id = " 
	          + estado.getId()).getResultList();
	   
	   List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();

	   for(Cidades cidade : cidades) {        		   
		   selectItemsCidade.add(new SelectItem(cidade,cidade.getNome()));
	   }
	   
	   setCidades(selectItemsCidade);
    }
    
    public void editar() {
    
    	if (pessoa.getCidades() != null) {
    		Estados estado = pessoa.getCidades().getEstados();
    		pessoa.setEstados(estado);

    		mostrarCidades(estado);
    	}
    
    }
     
    public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}
    
    public List<SelectItem> getCidades() {
		return cidades;
	}
    
    public void setArquivoFoto(Part arquivoFoto) {
		this.arquivoFoto = arquivoFoto;
	}
    
    public Part getArquivoFoto() {
		return arquivoFoto;
	}
     
    //Metodo que converte um InputStream para Byte
    private byte[] getByte(InputStream is) throws IOException{
    	
    	int length;
    	int size = 1024;
    	byte[] buf = null;
    	
    	if (is instanceof ByteArrayInputStream) {
    		size = is.available();
    		buf = new byte[size];
    		length = is.read(buf, 0, size);
    	}else {
    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		buf = new byte[size];
    		
    		while ((length = is.read(buf,0,size)) != -1) {
    			bos.write(buf,0,length);
    		}
    		
    		buf = bos.toByteArray();
    	}
    	
    	return buf;
    	
    }
    
    public void download() throws IOException {
    	
    	Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    	String fileDownloadId = params.get("fileDownloadId");
		Pessoa pessoa = daoGeneric.consultar(Pessoa.class, fileDownloadId);
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
									.getExternalContext().getResponse();
		response.addHeader("Content-Disposition", "attachment; filename=download." + pessoa.getExtensao());
		response.setContentType("application/octet-stream");
		response.setContentLength(pessoa.getFotoIconBase64Original().length);
		response.getOutputStream().write(pessoa.getFotoIconBase64Original());
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
    }
    
    
}

