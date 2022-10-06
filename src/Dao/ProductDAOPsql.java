package Dao;

import Domein.Adres;
import Domein.OVChipkaart;
import Domein.Product;
import org.postgresql.replication.fluent.physical.PhysicalReplicationOptions;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProductDAOPsql implements ProductDAO {


    private Connection conn;
    private OVChipkaartDAOPsql ovdao;
    private ReizigerDAOPsql rdao;

    public ProductDAOPsql(Connection conn) {
        this.conn = conn;
    }

    public void setOVDAO(OVChipkaartDAOPsql ovdao) {
        this.ovdao = ovdao;

    }

    public void setRDAO(ReizigerDAOPsql rdao) {
        this.rdao = rdao;
    }


    @Override
    public boolean save(Product product) throws SQLException {
        try {
            String query = "insert into product(product_nummer, naam , beschrijving, prijs) values(?,?,?,?)";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, product.getProduct_nummer());
            pt.setString(2, product.getNaam());
            pt.setString(3, product.getBeschrijving());
            pt.setDouble(4, product.getPrijs());

            pt.executeUpdate();
            pt.close();


            if (product.getOvchipkaarten() != null) {
                for (OVChipkaart ovChipkaart : product.getOvchipkaarten()) {

                    String queryOpslaanProducten = "insert into ov_chipkaart_product values(?,?,?,?)";
                    PreparedStatement ptOpslaanProducten = conn.prepareStatement(queryOpslaanProducten);

                    ptOpslaanProducten.setInt(1, ovChipkaart.getKaart_nummer());
                    ptOpslaanProducten.setInt(2, product.getProduct_nummer());
                    ptOpslaanProducten.setString(3, "hier");
                    ptOpslaanProducten.setDate(4, Date.valueOf("1900-1-1"));

                    ptOpslaanProducten.executeUpdate();
                    ptOpslaanProducten.close();

                }
            }


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
            pt.setDouble(4, product.getPrijs());

            pt.setInt(5, product.getProduct_nummer());

            pt.executeUpdate();
            pt.close();


            if (product.getOvchipkaarten() != null) {
                for (OVChipkaart ovChipkaart : product.getOvchipkaarten()) {

                    String queryUpdateProducten = "update ov_chipkaart_product set kaart_nummer = ? , product_nummer = ? , status = ?, last_update = ? where kaart_nummer = ?";
                    PreparedStatement ptUpdateProducten = conn.prepareStatement(queryUpdateProducten);

                    ptUpdateProducten.setInt(1, ovChipkaart.getKaart_nummer());
                    ptUpdateProducten.setInt(2, product.getProduct_nummer());
                    ptUpdateProducten.setString(3, "hooor");
                    ptUpdateProducten.setDate(4, Date.valueOf("1607-01-01"));

                    ptUpdateProducten.setInt(5, ovChipkaart.getKaart_nummer());


                    ptUpdateProducten.executeUpdate();
                    ptUpdateProducten.close();
                }
            }

            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(Product product) {
        try {


            if (product.getOvchipkaarten() != null) {
                for (OVChipkaart o : product.getOvchipkaarten()) {
                    String queryDeleteProducten = "delete from ov_chipkaart_product where kaart_nummer = ? and product_nummer = ?  and status = ? and last_update = ? ";
                    PreparedStatement ptDeleteProducten = conn.prepareStatement(queryDeleteProducten);

                    ptDeleteProducten.setInt(1, o.getKaart_nummer());
                    ptDeleteProducten.setInt(2, product.getProduct_nummer());
                    ptDeleteProducten.setString(3, "hier");
                    ptDeleteProducten.setDate(4, Date.valueOf("1900-01-01"));

                    ptDeleteProducten.executeUpdate();
                    ptDeleteProducten.close();


                }
            }


            String query = "delete from product where product_nummer = ? and naam = ? and beschrijving = ? and prijs = ?";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, product.getProduct_nummer());
            pt.setString(2, product.getNaam());
            pt.setString(3, product.getBeschrijving());
            pt.setDouble(4, product.getPrijs());

            pt.executeUpdate();
            pt.close();


            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException {
        String query =
                "select product.product_nummer, product.naam, product.beschrijving, product.prijs\n" +
                        "from ov_chipkaart\n" +
                        "inner join ov_chipkaart_product on ov_chipkaart.kaart_nummer = ov_chipkaart_product.Kaart_nummer\n" +
                        "inner join product on ov_chipkaart_product.product_nummer = product.product_nummer\n" +
                        "where ov_chipkaart.kaart_nummer = ?";


        PreparedStatement pt = conn.prepareStatement(query);

        pt.setInt(1, ovChipkaart.getKaart_nummer());
        ResultSet myRs = pt.executeQuery();

        List<Product> productenLijst = new ArrayList<>();


        if (myRs.next()) {
            int pn = myRs.getInt("product_nummer");
            String nm = myRs.getString("naam");
            String bs = myRs.getString("beschrijving");
            double pr = myRs.getDouble("prijs");

            Product product = new Product(pn, nm, bs, pr);
            productenLijst.add(product);

        }
        return productenLijst;
    }


    @Override
    public List<Product> findAll() throws SQLException {
        Statement myStmt = conn.createStatement();
        ResultSet myRs = myStmt.executeQuery("SELECT * FROM product");

        List<Product> resultaat = conveteerNaarAdresObject(myRs);

        myStmt.close();
        myRs.close();

        return resultaat;
    }


    public List<Product> conveteerNaarAdresObject(ResultSet myRs) throws SQLException {

        List<Product> productenLijst = new ArrayList<>();

        while (myRs.next()) {
            int pn = myRs.getInt("product_nummer");
            String nm = myRs.getString("naam");
            String bs = myRs.getString("beschrijving");
            double pr = myRs.getDouble("prijs");

            Product product = new Product(pn, nm, bs, pr);
            productenLijst.add(product);

        }
        myRs.close();

        return productenLijst;

    }
}

