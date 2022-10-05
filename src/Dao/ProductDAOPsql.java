package Dao;

import Domein.OVChipkaart;
import Domein.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {


    private Connection conn;
    private OVChipkaartDAOPsql ovdao;

    public ProductDAOPsql(Connection conn) {
        this.conn = conn;
    }

    public void setOVDAO(OVChipkaartDAOPsql ovdao) {
        this.ovdao = ovdao;

    }


    @Override
    public boolean save(Product product) throws SQLException {
        try {
            String query = "insert into product(product_nummer, naam , beschrijving, prijs) values(?,?,?,?)";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, product.getProduct_nummer());
            pt.setString(2, product.getNaam());
            pt.setString(3, product.getBeschrijving());
            pt.setInt(4, product.getPrijs());

            pt.executeUpdate();
            pt.close();


//            if (product.getOvchipkaarten() != null) {
//                for (OVChipkaart o : product.getOvchipkaarten()) {
//                    ovdao.save(o);
//                }
//            }




            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }

    }

    @Override
    public boolean update(Product product) {
        try {
            String query = "update product set product_nummer = ?, naam = ? , beschrijving = ?, prijs = ? where product_nummer = ?";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, product.getProduct_nummer());
            pt.setString(2, product.getNaam());
            pt.setString(3, product.getBeschrijving());
            pt.setInt(4, product.getPrijs());

            pt.setInt(5, product.getProduct_nummer());

            pt.executeUpdate();
            pt.close();

            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(Product product) {
        try {
            String query = "delete from product where product_nummer = ? and naam = ? and beschrijving = ? and prijs = ?";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, product.getProduct_nummer());
            pt.setString(2, product.getNaam());
            pt.setString(3, product.getBeschrijving());
            pt.setInt(4, product.getPrijs());

            pt.executeUpdate();
            pt.close();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Product findReizigerById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Product> findByGbDatum(String datum) throws SQLException {
        return null;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        return null;
    }
}
