package br.com.converter;

import java.io.Serializable;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;

import br.com.entidades.Estados;

@FacesConverter(forClass = Estados.class, value="estadosConverter")
public class EstadoConverter  implements Converter, Serializable{

	private static final long serialVersionUID = 1L;
	
	@Override /*retorna um objeto inteiro*/
	public Object getAsObject(FacesContext context, UIComponent component,
			String codigoEstado) {
		
		EntityManager entityManager = CDI.current().select(EntityManager.class).get();
		
		Estados estados = (Estados) entityManager.
				find(Estados.class,Long.parseLong(codigoEstado));
		
		return estados;
	}

	@Override /*retorna apenas o código em String*/
	public String getAsString(FacesContext context, UIComponent component,
			Object estado) {
		
		if (estado == null) {
			return null;
		}
		
		if (estado instanceof Estados) {
		   //faz a conversão de CAST para recupear os campos da Classe Estado
		   return ((Estados) estado).getId().toString();
     	}else {
	      return estado.toString();
	    }
   }
	
	
}
