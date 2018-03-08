package org.project.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Avaliador {

	private double maiorDeTodos = Double.NEGATIVE_INFINITY;
	private double menorDeTodos = Double.POSITIVE_INFINITY;
	private List<Lance> maiores;

	public void avalia(Leilao leilao) {
		if (leilao.getLances().size() == 0)
			throw new RuntimeException("Não é possível avaliar um leilão sem lances");

		for (Lance lance : leilao.getLances()) {
			if (lance.getValor() > maiorDeTodos)
				maiorDeTodos = lance.getValor();
			if (lance.getValor() < menorDeTodos)
				menorDeTodos = lance.getValor();
		}

		pegaOsMaioresNo(leilao);
	}

	private void pegaOsMaioresNo(Leilao leilao) {
		maiores = new ArrayList<>(leilao.getLances());
		Collections.sort(maiores, (Lance o1, Lance o2) -> o2.getValor().compareTo(o1.getValor()));
		maiores = maiores.subList(0, maiores.size() > 3 ? 3 : maiores.size());
	}

	public List<Lance> getTresMaiores() {
		return this.maiores;
	}

	public double getMaiorLance() {
		return maiorDeTodos;
	}

	public double getMenorLance() {
		return menorDeTodos;
	}
}
