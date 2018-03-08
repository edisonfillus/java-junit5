package org.project.example.service;

import java.util.Calendar;
import java.util.List;

import org.project.example.dao.LeilaoDAO;
import org.project.example.model.Leilao;

public class EncerradorDeLeilao {

	private int encerrados;
	private final LeilaoDAO dao;

	public EncerradorDeLeilao(LeilaoDAO dao) {
		this.dao = dao;
	}

	public void encerra() {
		List<Leilao> todosLeiloesCorrentes = dao.correntes();

		for (Leilao leilao : todosLeiloesCorrentes) {
			if (comecouSemanaPassada(leilao)) {
				encerrados++;
				leilao.encerra();
				dao.salva(leilao);
			}
		}
	}

	private boolean comecouSemanaPassada(Leilao leilao) {
		return diasEntre(leilao.getData(), Calendar.getInstance()) >= 7;
	}

	private int diasEntre(Calendar inicio, Calendar fim) {
		Calendar data = (Calendar) inicio.clone();
		int diasNoIntervalo = 0;
		while (data.before(fim)) {
			data.add(Calendar.DAY_OF_MONTH, 1);
			diasNoIntervalo++;
		}
		return diasNoIntervalo;
	}

	public Object getTotalEncerrados() {
		return encerrados;
	}

}
