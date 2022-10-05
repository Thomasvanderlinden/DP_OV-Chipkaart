package Dao;


import Domein.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    boolean save(Product product) throws SQLException;
    boolean update(Product product);
    boolean delete(Product product);
    Product findReizigerById(int id) throws SQLException;
    List<Product> findByGbDatum(String datum) throws SQLException;
    List<Product> findAll() throws SQLException;

}
