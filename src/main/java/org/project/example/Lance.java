package org.project.example;

public class Lance {

	private Usuario usuario;
	private Double valor;


	public Lance(Usuario usuario, Double valor) {
		if (valor <= 0) 
			throw new IllegalArgumentException();
		this.usuario = usuario;
		this.valor = valor;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public Double getValor() {
		return valor;
	}

}
