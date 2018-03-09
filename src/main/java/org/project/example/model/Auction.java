package org.project.example.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Auction {

	private String descricao;
	private Calendar data;
	private List<Bid> lances;
	private boolean encerrado;
	private int id;

	public Auction(String descricao) {
		this(descricao, Calendar.getInstance());
	}

	public Auction(String descricao, Calendar data) {
		this.descricao = descricao;
		this.data = data;
		this.lances = new ArrayList<Bid>();
	}

	public void propoe(Bid lance) {
		if (lances.isEmpty() || podeDarLance(lance.getBidder())) {
			lances.add(lance);
		}
	}

	private boolean podeDarLance(Bidder usuario) {
		return !ultimoLanceDado().getBidder().equals(usuario) && qtdDeLancesDo(usuario) < 5;
	}

	private int qtdDeLancesDo(Bidder usuario) {
		int total = 0;
		for (Bid l : lances) {
			if (l.getBidder().equals(usuario))
				total++;
		}
		return total;
	}

	private Bid ultimoLanceDado() {
		return lances.get(lances.size() - 1);
	}

	public String getDescricao() {
		return descricao;
	}

	public List<Bid> getLances() {
		return Collections.unmodifiableList(lances);
	}

	public Calendar getData() {
		return (Calendar) data.clone();
	}

	public void encerra() {
		this.encerrado = true;
	}

	public boolean isFinished() {
		return encerrado;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
