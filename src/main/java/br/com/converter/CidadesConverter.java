package br.com.converter;

import java.io.Serializable;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;

import br.com.entidades.Cidades;

@FacesConverter(forClass = Cidades.class, value="cidadesConverter")
public class CidadesConverter  implements Converter, Serializable{

	private static final long serialVersionUID = 1L;

	
	@Override /*retorna um objeto inteiro*/
	public Object getAsObject(FacesContext context, UIComponent component,
			String codigoCidade) {
		
		EntityManager entityManager = CDI.current().select(EntityManager.class).get();
		
		Cidades cidades = (Cidades) entityManager.
				find(Cidades.class,Long.parseLong(codigoCidade));
		
		return cidades;
	}

	@Override /*retorna apenas o código em String*/
	public String getAsString(FacesContext context, UIComponent component,
			Object cidade) {
		
		if (cidade == null) {
			return null;
		}
		
		if (cidade instanceof Cidades) {
		   //faz a conversão de CAST para recupear os campos da Classe Cidades
			return ((Cidades) cidade).getId().toString();
     	}else {
	      return cidade.toString();
	    }
   }		
		
}
