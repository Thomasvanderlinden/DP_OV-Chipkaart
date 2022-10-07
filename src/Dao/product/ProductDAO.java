package Dao.product;


import Domein.OVChipkaart;
import Domein.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    boolean save(Product product) throws SQLException;
    boolean update(Product product);
    boolean delete(Product product);

    List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException;
    List<Product> findAll() throws SQLException;

}
