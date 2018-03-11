package org.project.example.service;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.project.example.model.Auction;
import org.project.example.persistence.interfaces.AuctionDAO;

public class AuctionFinisher {

	private int finished;
	private final AuctionDAO dao;
	private final EmailSender sender;

	public AuctionFinisher(AuctionDAO dao, EmailSender sender) {
		this.dao = dao;
		this.sender = sender;
	}

	public void finishOpenAuctions() throws SQLException{
		List<Auction> todosLeiloesCorrentes = dao.findOpenAuctions();

		for (Auction leilao : todosLeiloesCorrentes) {
			if (comecouSemanaPassada(leilao)) {
				finished++;
				leilao.finish();
				try {
					dao.update(leilao);
				} catch (SQLException e) {
					LogManager.getLogger().error("Failed to finish auction",new SQLException(e));
					continue;
				}
				sender.send(leilao);
			}
		}
	}

	private boolean comecouSemanaPassada(Auction leilao) {
		return diasEntre(leilao.getDate(), Calendar.getInstance()) >= 7;
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
 	