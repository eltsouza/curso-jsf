package br.com.repository;

import br.com.entidades.Pessoa;
import javax.faces.model.SelectItem;
import java.util.List;

public interface IDaoPessoa  {

	Pessoa consultarUsuario(String login, String senha);
	
	List<SelectItem> listaEstados();
	
}
