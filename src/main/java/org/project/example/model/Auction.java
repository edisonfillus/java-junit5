package org.project.example.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Auction {

	@Id @GeneratedValue
	private int id;
	private String description;
	private Calendar data;
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="auction")
	private List<Bid> lances;
	private boolean finished;

	public Auction(String descricao) {
		this(descricao, Calendar.getInstance());
	}

	public Auction(String descricao, Calendar data) {
		this.description = descricao;
		this.data = data;
		this.lances = new ArrayList<>();
	}

	public Auction(String description, double d, Bidder mauricio, boolean b) {
		this.description = description;
		this.finished = b;
		this.lances = new ArrayList<Bid>();
		lances.add(new Bid(mauricio, d));
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
		return description;
	}

	public List<Bid> getLances() {
		return Collections.unmodifiableList(lances);
	}

	public Calendar getData() {
		return (Calendar) data.clone();
	}

	public void encerra() {
		this.finished = true;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
