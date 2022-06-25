package br.edu.femass.dao;

import br.edu.femass.model.Camera;
import br.edu.femass.model.Cliente;
import br.edu.femass.model.Compra;
import br.edu.femass.model.ProdutoCompra;
import br.edu.femass.testes.MainTeste;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO extends DaoPostgres implements DAO<Compra> {

    private static final Logger logger = LoggerFactory.getLogger(CompraDAO.class);
    @Override
    public void create(Compra value) throws Exception {
        String sql = "INSERT INTO compra (preco, quantidade, data) VALUES (?, ?, ?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setDouble(1, value.getPreco());
        ps.setInt(2, value.getQuantidade());
        ps.setDate(3, Date.valueOf(value.getDataCompra()));
        ps.executeUpdate();
        logger.debug(String.valueOf(ps));

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong("id"));

        for(ProdutoCompra produtoCompra : value.getCameras()){
            String sql2 = "INSERT INTO compra_produto (id_produto, id_fornecedor, id_compra, preco_comprado) VALUES (?, ?, ?, ?)";
            ps = getPreparedStatement(sql2, true);
            ps.setLong(1, produtoCompra.getCamera().getId());
            ps.setLong(2, value.getFornecedor().getId());
            ps.setLong(3, value.getId());
            ps.setDouble(4, produtoCompra.getPrecoCompra());
            ps.executeUpdate();
            logger.debug(String.valueOf(ps));
        }
    }

    @Override
    public List<Compra> read() throws Exception {
        String sql = "SELECT * FROM compra_detalhes ORDER BY data";
        PreparedStatement ps = getPreparedStatement(sql);
        ResultSet rs = ps.executeQuery();
        logger.debug(String.valueOf(ps));

        List<Compra> compras = new ArrayList<>();

        while (rs.next()){
            Compra compra = new Compra();
            compra.setId(rs.getLong("id"));
            compra.setQuantidade(rs.getInt("quantidade"));
            compra.setPreco(rs.getDouble("preco"));
            compra.setDataCompra(rs.getDate("data").toLocalDate());
            compras.add(compra);

            String sql2 = "SELECT * FROM compra WHERE id_compra = ?";
            PreparedStatement ps2 = getPreparedStatement(sql2);
            ResultSet rs2 = ps2.executeQuery();
            logger.debug(String.valueOf(ps));

            List<Camera> cameras = new ArrayList<>();
            while (rs2.next()){

            }

        }

        return compras;
    }

    @Override
    public void update(Compra value) throws Exception {

    }

    @Override
    public void delete(Compra value) throws Exception {

    }
}
