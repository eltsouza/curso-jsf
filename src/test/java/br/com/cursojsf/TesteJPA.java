package br.com.cursojsf;

import javax.persistence.Persistence;

public class TesteJPA {
	
	public static void main(String[] args) {
		Persistence.createEntityManagerFactory("curso-jsf"); // nome do persistence unit no arquivo persistence.xml
	}

}
