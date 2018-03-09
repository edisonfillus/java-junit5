package org.project.example.service;

import java.util.Calendar;
import java.util.List;

import org.project.example.model.Auction;
import org.project.example.persistence.interfaces.AuctionDAO;

public class AuctionFinisher {

	private int finished;
	private final AuctionDAO dao;

	public AuctionFinisher(AuctionDAO dao) {
		this.dao = dao;
	}

	public void finishOpenAuctions() {
		List<Auction> todosLeiloesCorrentes = dao.openAuctions();

		for (Auction leilao : todosLeiloesCorrentes) {
			if (comecouSemanaPassada(leilao)) {
				finished++;
				leilao.encerra();
				dao.update(leilao);
			}
		}
	}

	private boolean comecouSemanaPassada(Auction leilao) {
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

	public Object getTotalFinished() {
		return finished;
	}

}
 	