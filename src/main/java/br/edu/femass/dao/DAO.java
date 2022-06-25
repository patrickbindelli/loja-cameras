package br.edu.femass.dao;

import java.util.List;

public interface DAO<T> {
    public void create(T value) throws Exception;
    public List<T> read() throws Exception;
    public void update(T value) throws Exception;
    public void delete(T value) throws Exception;
}
