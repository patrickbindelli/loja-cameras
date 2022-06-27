package br.edu.femass.dao;

import br.edu.femass.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraDao extends DaoPostgres implements DAO<Camera> {
    private static final Logger logger = LoggerFactory.getLogger(CameraDao.class);

    private static final Map<Long, Marca> mapMarcas = new HashMap<>();

    @Override
    public void create(Camera value) throws Exception {
        String sql = "INSERT INTO camera (nome, descricao, preco, estoque, tipo, marca) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setString(1, value.getNome());
        ps.setString(2, value.getDescricao());
        ps.setDouble(3, value.getPreco());
        ps.setInt(4, value.getEstoque());
        ps.setString(5, value.getTipo().toString());
        ps.setLong(6, value.getMarca().getId());
        ps.executeUpdate();
        logger.debug(String.valueOf(ps));

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong("id"));
    }

    @Override
    public List<Camera> read() throws Exception {
        String sql = "SELECT * FROM camera";
        PreparedStatement ps = getPreparedStatement(sql);
        ResultSet rs = ps.executeQuery();
        logger.debug(String.valueOf(ps));

        List<Camera> cameras = new ArrayList<>();

        List<Marca> marcasList = new MarcaDao().read();
        mapMarcas.clear();
        for(Marca marca : marcasList){
            mapMarcas.put(marca.getId(), marca);
        }

        while (rs.next()){
            Camera camera = getNewCamera(rs);
            cameras.add(camera);
        }

        mapMarcas.clear();
        return cameras;
    }

    private Camera getNewCamera(ResultSet rs) throws SQLException {
        Camera camera = new Camera();
        camera.setId(rs.getLong("id"));
        camera.setNome(rs.getString("nome"));
        camera.setDescricao(rs.getString("descricao"));
        camera.setPreco(rs.getDouble("preco"));
        camera.setEstoque(rs.getInt("estoque"));
        camera.setTipo(Tipo.valueOf(rs.getString("tipo")));
        camera.setMarca(mapMarcas.get(rs.getLong("marca")));
        return camera;
    }

    public Camera getById(long id) throws SQLException {
        String sql = "SELECT * FROM camera WHERE id = ?";
        PreparedStatement ps = getPreparedStatement(sql);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();
        rs.next();

        List<Marca> marcasList = new MarcaDao().read();
        mapMarcas.clear();
        for(Marca marca : marcasList){
            mapMarcas.put(marca.getId(), marca);
        }

        return getNewCamera(rs);
    }

    @Override
    public void update(Camera value) throws SQLException {
        String sql = "UPDATE camera SET nome = ?, descricao = ?, preco = ?, estoque = ?, tipo = ?, marca = ? WHERE id = ?";
        PreparedStatement ps = getPreparedStatement(sql);
        ps.setString(1, value.getNome());
        ps.setString(2, value.getDescricao());
        ps.setDouble(3, value.getPreco());
        ps.setInt(4, value.getEstoque());
        ps.setString(5, value.getTipo().toString());
        ps.setLong(6, value.getMarca().getId());
        ps.setLong(7, value.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Camera value) throws SQLException {
        String sql = "DELETE FROM camera WHERE id = ?";
        PreparedStatement ps = getPreparedStatement(sql);
        ps.setLong(1, value.getId());
        ps.executeUpdate();
    }
}
