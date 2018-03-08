package org.project.example;

/**
 * Test Data Builder para Leilao
 * @author Edison Klafke Fillus
 *
 */
public class LeilaoBuilder {
	private Leilao leilao;

	public LeilaoBuilder() {
	}

	public LeilaoBuilder para(String descricao) {
		this.leilao = new Leilao(descricao);
		return this;
	}

	public LeilaoBuilder lance(Usuario usuario, double valor) {
		leilao.propoe(new Lance(usuario, valor));
		return this;
	}

	public Leilao constroi() {
		return leilao;
	}
}
