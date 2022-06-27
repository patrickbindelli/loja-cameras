package br.edu.femass.dao;

import br.edu.femass.model.Cliente;
import br.edu.femass.testes.MainTeste;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao extends DaoPostgres implements DAO<Cliente>{
    private static final Logger logger = LoggerFactory.getLogger(MainTeste.class);
    @Override
    public void create(Cliente value) throws Exception {
        String sql = "INSERT INTO cliente (nome, sobrenome, cpf, telefone) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setString(1, value.getNome());
        ps.setString(2, value.getSobrenome());
        ps.setString(3, value.getCpf());
        ps.setString(4, value.getTelefone());
        ps.executeUpdate();
        logger.debug(String.valueOf(ps));

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong("id"));
    }

    @Override
    public List<Cliente> read() throws Exception {
        String sql = "SELECT * FROM cliente ORDER BY nome";
        PreparedStatement ps = getPreparedStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<Cliente> clientes = new ArrayList<>();

        while (rs.next()){
            Cliente cliente = new Cliente();
            cliente.setNome(rs.getString("nome"));
            cliente.setSobrenome(rs.getString("sobrenome"));
            cliente.setCpf(rs.getString("cpf"));
            cliente.setTelefone(rs.getString("telefone"));
            cliente.setId(rs.getLong("id"));
            clientes.add(cliente);
        }

        return clientes;
    }

    public Cliente getById(long id) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        PreparedStatement ps = getPreparedStatement(sql);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        rs.next();

        Cliente cliente = new Cliente();
        cliente.setId(rs.getLong("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setSobrenome(rs.getString("sobrenome"));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setTelefone(rs.getString("telefone"));
        return cliente;
    }

    @Override
    public void update(Cliente value) throws Exception {
        String sql = "UPDATE cliente SET nome = ?, sobrenome = ?, cpf = ?, telefone = ? WHERE id = ?";
        PreparedStatement ps = getPreparedStatement(sql);
        ps.setString(1, value.getNome());
        ps.setString(2, value.getSobrenome());
        ps.setString(3, value.getCpf());
        ps.setString(4, value.getTelefone());
        ps.setLong(5, value.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Cliente value) throws Exception {
        String sql = "DELETE FROM cliente WHERE id = ?";
        PreparedStatement ps = getPreparedStatement(sql);
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }
}
