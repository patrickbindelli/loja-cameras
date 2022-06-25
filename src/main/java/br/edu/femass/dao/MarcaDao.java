package br.edu.femass.dao;

import br.edu.femass.model.Marca;
import br.edu.femass.model.Tipo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarcaDao extends DaoPostgres implements DAO<Marca> {

    @Override
    public void create(Marca value) throws SQLException {
        String sql = "INSERT INTO marca (nome) VALUES (?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setString(1, value.getNome());;
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong("id"));
    }

    public Marca getById(long id) throws SQLException {
        String sql = "SELECT * FROM marca WHERE id = ?";
        PreparedStatement ps = getPreparedStatement(sql);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        rs.next();

        Marca marca = new Marca();
        marca.setNome(rs.getString("nome"));
        marca.setId(rs.getLong("id"));
        return marca;
    }

    @Override
    public List<Marca> read() throws SQLException {
        String sql = "SELECT * FROM marca ORDER BY nome";
        PreparedStatement ps = getPreparedStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Marca> marcas = new ArrayList<>();

        while (rs.next()){
            Marca marca = new Marca();
            marca.setNome(rs.getString("nome"));
            marca.setId(rs.getLong("id"));
            marcas.add(marca);
        }
        return marcas;
    }

    @Override
    public void update(Marca value) throws SQLException {
        String sql = "UPDATE marca SET nome = ? WHERE id = ?";
        PreparedStatement ps = getPreparedStatement(sql);
        ps.setString(1, value.getNome());
        ps.setLong(2, value.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Marca value) throws SQLException {
        String sql = "DELETE FROM marca WHERE id = ?";
        PreparedStatement ps = getPreparedStatement(sql);
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }
}
