package br.edu.femass.dao;

import br.edu.femass.model.Fornecedor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDao extends DaoPostgres implements DAO<Fornecedor> {

    private static final Logger logger = LoggerFactory.getLogger(FornecedorDao.class);
    @Override
    public void create(Fornecedor value) throws Exception {
        String sql = "INSERT INTO fornecedor (nome, cnpj, telefone) VALUES (?, ?, ?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setString(1, value.getNome());
        ps.setString(2, value.getCnpj());
        ps.setString(3, value.getTelefone());
        ps.executeUpdate();
        logger.debug(String.valueOf(ps));

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong("id"));
    }

    public Fornecedor getById(long id) throws SQLException {
        String sql = "SELECT * FROM fornecedor WHERE id = ?";
        PreparedStatement ps = getPreparedStatement(sql);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        rs.next();

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(rs.getLong("id"));
        fornecedor.setNome(rs.getString("nome"));
        fornecedor.setCnpj(rs.getString("cnpj"));
        fornecedor.setTelefone(rs.getString("telefone"));
        return fornecedor;
    }

    @Override
    public List<Fornecedor> read() throws Exception {
        String sql = "SELECT * FROM fornecedor ORDER BY nome";
        PreparedStatement ps = getPreparedStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Fornecedor> fornecedores = new ArrayList<>();

        while (rs.next()){
            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setNome(rs.getString("nome"));
            fornecedor.setCnpj(rs.getString("cnpj"));
            fornecedor.setId(rs.getLong("id"));
            fornecedor.setTelefone(rs.getString("telefone"));
            fornecedores.add(fornecedor);
        }

        return fornecedores;
    }

    @Override
    public void update(Fornecedor value) throws Exception {
        String sql = "UPDATE fornecedor SET nome = ?, cnpj = ?, telefone = ? WHERE id = ?";
        PreparedStatement ps = getPreparedStatement(sql);
        ps.setString(1, value.getNome());
        ps.setString(2, value.getCnpj());
        ps.setString(3, value.getTelefone());
        ps.setLong(4, value.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Fornecedor value) throws Exception {
        String sql = "DELETE FROM fornecedor WHERE id = ?";
        PreparedStatement ps = getPreparedStatement(sql);
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }
}
