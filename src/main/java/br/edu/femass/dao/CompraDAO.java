package br.edu.femass.dao;

import br.edu.femass.model.Camera;
import br.edu.femass.model.Compra;
import br.edu.femass.model.ProdutoOperacao;
import br.edu.femass.model.Venda;
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

public class CompraDAO extends DaoPostgres implements DAO<Compra> {

    private static final Logger logger = LoggerFactory.getLogger(CompraDAO.class);
    @Override
    public void create(Compra value) throws Exception {
        String sql = "INSERT INTO compra (preco, quantidade, data, id_fornecedor) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setDouble(1, value.getPreco());
        ps.setInt(2, value.getQuantidade());
        ps.setDate(3, Date.valueOf(value.getDataCompra()));
        ps.setLong(4, value.getFornecedor().getId());
        ps.executeUpdate();
        logger.debug(String.valueOf(ps));

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong("id"));

        for(ProdutoOperacao produtoOperacao : value.getCameras().values()){
            String sql2 = "INSERT INTO compra_produto (id_produto, id_compra, preco_comprado, quantidade, subtotal) VALUES (?, ?, ?, ?, ?)";
            ps = getPreparedStatement(sql2, true);
            ps.setLong(1, produtoOperacao.getCamera().getId());
            ps.setLong(2, value.getId());
            ps.setDouble(3 , produtoOperacao.getValorUnitario());
            ps.setInt(4, produtoOperacao.getQuantidade());
            ps.setDouble(5, produtoOperacao.getSubtotal());
            ps.executeUpdate();

            Camera camera = produtoOperacao.getCamera();
            camera.setEstoque(camera.getEstoque() + produtoOperacao.getQuantidade());
            new CameraDao().update(camera);
        }
    }

    public List<Compra> getByDate(LocalDate date) throws Exception{
        String sql = "SELECT * FROM compra WHERE data = ?";
        PreparedStatement ps = getPreparedStatement(sql);
        ps.setDate(1, Date.valueOf(date));

        ResultSet rs = ps.executeQuery();
        logger.debug(String.valueOf(ps));

        List<Compra> compras = new ArrayList<>();
        Map<Long, Camera> longCameraHashMap = new HashMap<>();
        while (rs.next()) {
            Compra compra = new Compra();
            compra.setId(rs.getLong("id"));
            compra.setPreco(rs.getDouble("preco"));
            compra.setDataCompra(rs.getDate("data").toLocalDate());
            compra.setFornecedor(new FornecedorDao().getById(rs.getLong("id_fornecedor")));

            sql = "SELECT * FROM compra_produto WHERE id_compra = ?";
            ps = getPreparedStatement(sql);
            ps.setLong(1, compra.getId());
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
                produtoOperacao.setValorUnitario(rs2.getDouble("preco_comprado"));

                longProdutoVendaMap.put(produtoOperacao.getCamera().getId(), produtoOperacao);
            }

            compra.setCameras(longProdutoVendaMap);
            compra.setQuantidade(rs.getInt("quantidade"));
            compras.add(compra);
        }
        return compras;
    }

    @Override
    public List<Compra> read() throws Exception {
        String sql = "SELECT * FROM compra ORDER BY data";
        PreparedStatement ps = getPreparedStatement(sql);
        ResultSet rs = ps.executeQuery();
        logger.debug(String.valueOf(ps));

        List<Compra> compras = new ArrayList<>();
        Map<Long, Camera> longCameraHashMap = new HashMap<>();
        while (rs.next()) {
            Compra compra = new Compra();
            compra.setId(rs.getLong("id"));
            compra.setPreco(rs.getDouble("preco"));
            compra.setDataCompra(rs.getDate("data").toLocalDate());
            compra.setFornecedor(new FornecedorDao().getById(rs.getLong("id_fornecedor")));

            sql = "SELECT * FROM compra_produto WHERE id_compra = ?";
            ps = getPreparedStatement(sql);
            ps.setLong(1, compra.getId());
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
                produtoOperacao.setValorUnitario(rs2.getDouble("preco_comprado"));

                longProdutoVendaMap.put(produtoOperacao.getCamera().getId(), produtoOperacao);
            }

            compra.setCameras(longProdutoVendaMap);
            compra.setQuantidade(rs.getInt("quantidade"));
            compras.add(compra);
        }
        return compras;
    }

    @Override
    public void update(Compra value) throws Exception {

    }

    @Override
    public void delete(Compra value) throws Exception {
        String sql = "DELETE FROM compra_produto WHERE id_compra = ?";
        PreparedStatement ps = getPreparedStatement(sql);
        ps.setLong(1, value.getId());
        ps.executeUpdate();

        sql = "DELETE FROM compra WHERE id = ?";
        ps = getPreparedStatement(sql);
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }
}
