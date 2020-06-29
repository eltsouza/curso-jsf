package br.com.cursojsf;

import java.util.ArrayList;
import java.util.List;

//import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlCommandButton;
//import javax.faces.bean.SessionScoped;
//import javax.faces.bean.RequestScoped;

/*
 * A notação RequesScoped mantem os dados somente ate a retorno do servidor para requisição,
 * ou seja, após o usuário clicar no botão mais um vez o ManagedBean é morto e uma nova requisição é executada
 * Está notação é utilizada normalmente em cadastros, pois após submeter o formulário para salvar, os dados já serão gravados no banco.
 */
/*
 * A notação ViewScoped mantem os dados até o encerramento da sessão ou redirecionamento da tela, ou seja,após o usuário clicar no botão mais um vez o ManagedBean mantem os dados na tela
 * Está notação é utilizada normalmente durante a geração de um pedido para que os dados não sejam perdidos.
 */

/*A notação SessionScoped é cada usuário ou navegador que é aberto é iniciado uma sessão e os dados são mantidos de cada usuário em sua respecitiva sessão*/
 
/*A notação AplicationScoped mantém os dados na sessão, porém ele compartilha os dados de uma sessão com todos os usuarios */

//@RequestScoped
//@ViewScoped
//@SessionScoped
@ViewScoped
@ManagedBean(name ="pessoaBeanExemploScopes")
public class PessoaBeanExemploScopes {
	
	private String nome;
	
	private HtmlCommandButton commandButton;
	
	private List<String> nomes = new ArrayList<String>();
	
	public String addNome() {
		nomes.add(nome);
		
		if(nomes.size() > 3) {
		  commandButton.setDisabled(true); // Exemplo Binding componente pagina JSF(xhtml) -- Amarra o componente da pagina para trabalhar do lado do back-end        
		  return "paginaNavegada?faces-redirect=true";
		}
		return ""; //null ou vazio fica na mesma pagina -> outcome
	}
	
	public void setNomes(List<String> nomes) {
		this.nomes = nomes;
	}
	
	public List<String> getNomes() {
		return nomes;
	}
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setCommandButton(HtmlCommandButton commandButton) {
		this.commandButton = commandButton;
	}
	
	public HtmlCommandButton getCommandButton() {
		return commandButton;
	}
	
}
