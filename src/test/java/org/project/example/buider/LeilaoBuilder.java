package org.project.example.buider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.project.example.model.Lance;
import org.project.example.model.Leilao;
import org.project.example.model.Usuario;

/**
 * Test Data Builder para Leilao
 * @author Edison Klafke Fillus
 *
 */
public class LeilaoBuilder {
	private String descricao;
	private Calendar data;
	private List<Lance> lances;
	private boolean encerrado;

	public LeilaoBuilder() {
		this.data = Calendar.getInstance();
		lances = new ArrayList<Lance>();
	}
	
	public LeilaoBuilder para(String descricao) {
		this.descricao = descricao;
		return this;
	}
	
	public LeilaoBuilder naData(Calendar data) {
		this.data = data;
		return this;
	}

	public LeilaoBuilder lance(Usuario usuario, double valor) {
		lances.add(new Lance(usuario, valor));
		return this;
	}

	public LeilaoBuilder encerrado() {
		this.encerrado = true;
		return this;
	}

	public Leilao constroi() {
		Leilao leilao = new Leilao(descricao, data);
		for(Lance lanceDado : lances) leilao.propoe(lanceDado);
		if(encerrado) leilao.encerra();
				
		return leilao;
	}
	
}
