package br.edu.femass.dao;

import br.edu.femass.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendaDao extends DaoPostgres implements DAO<Venda>{
    private static final Logger logger = LoggerFactory.getLogger(VendaDao.class);
    @Override
    public void create(Venda value) throws Exception {
        String sql = "INSERT INTO venda (preco, quantidade, data, id_cliente) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setDouble(1, value.getPreco());
        ps.setInt(2, value.getQuantidade());
        ps.setDate(3, Date.valueOf(value.getDataVenda()));
        ps.setLong(4, value.getCliente().getId());
        ps.executeUpdate();
        logger.debug(String.valueOf(ps));

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong("id"));

        for(ProdutoOperacao produtoOperacao : value.getCameras().values()){
            String sql2 = "INSERT INTO venda_produto (id_produto, id_venda, preco_vendido, quantidade, subtotal) VALUES (?, ?, ?, ?, ?)";
            ps = getPreparedStatement(sql2, true);
            ps.setLong(1, produtoOperacao.getCamera().getId());
            ps.setLong(2, value.getId());
            ps.setDouble(3 , produtoOperacao.getCamera().getPreco());
            ps.setInt(4, produtoOperacao.getQuantidade());
            ps.setDouble(5, produtoOperacao.getSubtotal());
            ps.executeUpdate();

            Camera camera = produtoOperacao.getCamera();
            camera.setEstoque(camera.getEstoque() - produtoOperacao.getQuantidade());
            new CameraDao().update(camera);
        }
    }

    public List<Venda> getByDate(LocalDate date) throws Exception {
        String sql = "SELECT * FROM venda WHERE data = ?";
        PreparedStatement ps = getPreparedStatement(sql);
        ps.setDate(1, Date.valueOf(date));

        ResultSet rs = ps.executeQuery();
        logger.debug(String.valueOf(ps));

        List<Venda> vendas = new ArrayList<>();
        Map<Long, Camera> longCameraHashMap = new HashMap<>();
        while (rs.next()) {
            Venda venda = new Venda();
            venda.setId(rs.getLong("id"));
            venda.setPreco(rs.getDouble("preco"));
            venda.setDataVenda(rs.getDate("data").toLocalDate());
            venda.setCliente(new ClienteDao().getById(rs.getLong("id_cliente")));

            sql = "SELECT * FROM venda_produto WHERE id_venda = ?";
            ps = getPreparedStatement(sql);
            ps.setLong(1, venda.getId());
            ResultSet rs2 = ps.executeQuery();
            logger.debug(String.valueOf(ps));

            Map <Long, ProdutoOperacao> longProdutoVendaMap = new HashMap<>();
            while (rs2.next()) {

                if(!longCameraHashMap.containsKey(rs2.getLong("id_produto"))){
                    Camera camera = new CameraDao().getById(rs2.getLong("id_produto"));
                    longCameraHashMap.put(camera.getId(), camera);
                }

                ProdutoOperacao produtoOperacao = new ProdutoOperacao();
                produtoOperacao.setCamera(longCameraHashMap.get(rs2.getLong("id_produto")));

                produtoOperacao.setQuantidade(rs2.getInt("quantidade"));
                produtoOperacao.setSubtotal(rs2.getDouble("subtotal"));
                produtoOperacao.setValorUnitario(rs2.getDouble("preco_vendido"));

                longProdutoVendaMap.put(produtoOperacao.getCamera().getId(), produtoOperacao);
            }

            venda.setCameras(longProdutoVendaMap);
            venda.setQuantidade(rs.getInt("quantidade"));
            vendas.add(venda);
        }
        return vendas;
    }

    @Override
    public List<Venda> read() throws Exception {
        String sql = "SELECT * FROM venda ORDER BY data";
        PreparedStatement ps = getPreparedStatement(sql);
        ResultSet rs = ps.executeQuery();
        logger.debug(String.valueOf(ps));

        List<Venda> vendas = new ArrayList<>();
        Map<Long, Camera> longCameraHashMap = new HashMap<>();
        while (rs.next()) {
            Venda venda = new Venda();
            venda.setId(rs.getLong("id"));
            venda.setPreco(rs.getDouble("preco"));
            venda.setDataVenda(rs.getDate("data").toLocalDate());
            venda.setCliente(new ClienteDao().getById(rs.getLong("id_cliente")));

            sql = "SELECT * FROM venda_produto WHERE id_venda = ?";
            ps = getPreparedStatement(sql);
            ps.setLong(1, venda.getId());
            ResultSet rs2 = ps.executeQuery();
            logger.debug(String.valueOf(ps));

            Map<Long, ProdutoOperacao> longProdutoVendaMap = new HashMap<>();
            while (rs2.next()) {

                if (!longCameraHashMap.containsKey(rs2.getLong("id_produto"))) {
                    Camera camera = new CameraDao().getById(rs2.getLong("id_produto"));
                    longCameraHashMap.put(camera.getId(), camera);
                }

                ProdutoOperacao produtoOperacao = new ProdutoOperacao();
                produtoOperacao.setCamera(longCameraHashMap.get(rs2.getLong("id_produto")));

                produtoOperacao.setQuantidade(rs2.getInt("quantidade"));
                produtoOperacao.setSubtotal(rs2.getDouble("subtotal"));
                produtoOperacao.setValorUnitario(rs2.getDouble("preco_vendido"));

                longProdutoVendaMap.put(produtoOperacao.getCamera().getId(), produtoOperacao);
            }

            venda.setCameras(longProdutoVendaMap);
            venda.setQuantidade(rs.getInt("quantidade"));
            vendas.add(venda);
        }
        return vendas;
    }

    @Override
    public void update(Venda value) throws Exception {

    }

    @Override
    public void delete(Venda value) throws Exception {
        String sql = "DELETE FROM venda_produto WHERE id_venda = ?";
        PreparedStatement ps = getPreparedStatement(sql);
        ps.setLong(1, value.getId());
        ps.executeUpdate();

        sql = "DELETE FROM venda WHERE id = ?";
        ps = getPreparedStatement(sql);
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }
}
