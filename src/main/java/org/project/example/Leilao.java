package org.project.example;

import java.util.ArrayList;
import java.util.List;

public class Leilao {
	
	private String item;
	private List<Lance> lances;

	public Leilao (String item) {
		this.item = item;
		this.lances = new ArrayList<>();
	}
	
	public void propoe(Lance lance) {
		this.lances.add(lance);
	}

	public String getItem() {
		return item;
	}

	public List<Lance> getLances() {
		return lances;
	}
	
	
	
}
