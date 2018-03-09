package org.project.example.persistence.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.project.example.model.Bid;
import org.project.example.model.Auction;
import org.project.example.model.Bidder;
import org.project.example.persistence.interfaces.AuctionDAO;

public class MySQLAuctionDAO implements AuctionDAO {
	private Connection conexao;

	public MySQLAuctionDAO() {
		try {
			this.conexao = DriverManager.getConnection(
					"jdbc:mysql://localhost/mocks", "root", "");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Calendar data(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}

	/* (non-Javadoc)
	 * @see org.project.example.persistence.impl.AuctionDAO#save(org.project.example.model.Auction)
	 */
	@Override
	public void create(Auction leilao) {
		try {
			String sql = "INSERT INTO LEILAO (DESCRICAO, DATA, ENCERRADO) VALUES (?,?,?);";
			PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, leilao.getDescricao());
			ps.setDate(2, new java.sql.Date(leilao.getData().getTimeInMillis()));
			ps.setBoolean(3, leilao.isFinished());

			ps.execute();

			ResultSet generatedKeys = ps.getGeneratedKeys();
			if (generatedKeys.next()) {
				leilao.setId(generatedKeys.getInt(1));
			}

			for (Bid lance : leilao.getLances()) {
				sql = "INSERT INTO LANCES (LEILAO_ID, USUARIO_ID, VALOR) VALUES (?,?,?);";
				PreparedStatement ps2 = conexao.prepareStatement(sql);
				ps2.setInt(1, leilao.getId());
				ps2.setInt(2, lance.getBidder().getId());
				ps2.setDouble(3, lance.getValue());

				ps2.execute();
				ps2.close();

			}

			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	/* (non-Javadoc)
	 * @see org.project.example.persistence.impl.AuctionDAO#encerrados()
	 */
	@Override
	public List<Auction> encerrados() {
		return porEncerrado(true);
	}

	/* (non-Javadoc)
	 * @see org.project.example.persistence.impl.AuctionDAO#openAuctions()
	 */
	@Override
	public List<Auction> openAuctions() {
		return porEncerrado(false);
	}

	private List<Auction> porEncerrado(boolean status) {
		try {
			String sql = "SELECT * FROM LEILAO WHERE ENCERRADO = " + status + ";";

			PreparedStatement ps = conexao.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			List<Auction> leiloes = new ArrayList<Auction>();
			while (rs.next()) {
				Auction leilao = new Auction(rs.getString("descricao"), data(rs.getDate("data")));
				leilao.setId(rs.getInt("id"));
				if (rs.getBoolean("encerrado"))
					leilao.encerra();

				String sql2 = "SELECT VALOR, NOME, U.ID AS USUARIO_ID, L.ID AS LANCE_ID FROM LANCES L INNER JOIN USUARIO U ON U.ID = L.USUARIO_ID WHERE LEILAO_ID = "
						+ rs.getInt("id");
				PreparedStatement ps2 = conexao.prepareStatement(sql2);
				ResultSet rs2 = ps2.executeQuery();

				while (rs2.next()) {
					Bidder usuario = new Bidder(rs2.getInt("id"), rs2.getString("nome"));
					Bid lance = new Bid(usuario, rs2.getDouble("valor"));

					leilao.propoe(lance);
				}
				rs2.close();
				ps2.close();

				leiloes.add(leilao);

			}
			rs.close();
			ps.close();

			return leiloes;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.project.example.persistence.impl.AuctionDAO#atualiza(org.project.example.model.Auction)
	 */
	@Override
	public void update(Auction leilao) {

		try {
			String sql = "UPDATE LEILAO SET DESCRICAO=?, DATA=?, ENCERRADO=? WHERE ID = ?;";
			PreparedStatement ps = conexao.prepareStatement(sql);
			ps.setString(1, leilao.getDescricao());
			ps.setDate(2, new java.sql.Date(leilao.getData().getTimeInMillis()));
			ps.setBoolean(3, leilao.isFinished());
			ps.setInt(4, leilao.getId());

			ps.execute();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
