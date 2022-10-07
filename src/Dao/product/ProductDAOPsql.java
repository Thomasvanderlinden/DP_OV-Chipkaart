package Dao.product;

import Dao.ovchipkaart.OVChipkaartDAOPsql;
import Dao.reiziger.ReizigerDAOPsql;
import Domein.OVChipkaart;
import Domein.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public boolean save(Product product) {
        try {
            String query = "insert into product(product_nummer, naam , beschrijving, prijs) values(?,?,?,?)";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, product.getProduct_nummer());
            pt.setString(2, product.getNaam());
            pt.setString(3, product.getBeschrijving());
            pt.setDouble(4, product.getPrijs());

            pt.executeUpdate();
            pt.close();

            //staat in aparte methode ivm update:
            saveOV_ChipKaart_Product(product);


            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }

    }


    public void saveOV_ChipKaart_Product(Product product) throws SQLException {
        if (product.getOvChipkaartenNummers() != null) {
            for (Integer kaartNummer : product.getOvChipkaartenNummers()) {

                String queryOpslaanProducten = "insert into ov_chipkaart_product values(?,?,?,?)";
                PreparedStatement ptOpslaanProducten = conn.prepareStatement(queryOpslaanProducten);

                ptOpslaanProducten.setInt(1, kaartNummer);
                ptOpslaanProducten.setInt(2, product.getProduct_nummer());
                ptOpslaanProducten.setString(3, "actief");
                ptOpslaanProducten.setDate(4, Date.valueOf("1900-1-1"));

                ptOpslaanProducten.executeUpdate();
                ptOpslaanProducten.close();

            }
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



            for(OVChipkaart o : ovdao.findByProduct(product)){
                if(!product.getOvChipkaartenNummers().contains(o)){
                    //...delete in table
                    deleteOV_ChipKaart_Product(product);

                }
            }

            for(Integer ovChipkaartNummer : product.getOvChipkaartenNummers()){
                if(!ovdao.findByProduct(product).contains(ovChipkaartNummer)){
                    //...insert into database
                    saveOV_ChipKaart_Product(product);
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

            //staat in aparte methode ivm update:
            deleteOV_ChipKaart_Product(product);


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



    public void deleteOV_ChipKaart_Product(Product product) throws SQLException {
        if (product.getOvChipkaartenNummers() != null) {
            for (Integer ovChipkaartNummer : product.getOvChipkaartenNummers()) {
                String queryDeleteProducten = "delete from ov_chipkaart_product where kaart_nummer = ? and product_nummer = ?  and status = ? and last_update = ? ";
                PreparedStatement ptDeleteProducten = conn.prepareStatement(queryDeleteProducten);

                ptDeleteProducten.setInt(1, ovChipkaartNummer);
                ptDeleteProducten.setInt(2, product.getProduct_nummer());
                ptDeleteProducten.setString(3, "actief");
                ptDeleteProducten.setDate(4, Date.valueOf("1900-01-01"));

                ptDeleteProducten.executeUpdate();
                ptDeleteProducten.close();


            }
        }
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException {
        String query =
                        "select product.product_nummer, product.naam, product.beschrijving, product.prijs\n" +
                        "from product\n" +
                        "inner join ov_chipkaart_product on  product.product_nummer = ov_chipkaart_product.product_nummer\n" +
                        "where ov_chipkaart_product.kaart_nummer = ? ";


        //todo: 1 join kan weg: done

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
            //todo: ovchipkaartNummer toevoegen aan product: + findAll


//            for(OVChipkaart o : ovdao.findByProduct(product)){
//                product.voegOvchipkaartNummerToeAanProduct(o.getKaart_nummer());
//            }
            product.voegOvchipkaartNummerToeAanProduct(ovChipkaart.getKaart_nummer());


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

            for(OVChipkaart o : ovdao.findByProduct(product)){
                product.voegOvchipkaartNummerToeAanProduct(o.getKaart_nummer());
            }

            productenLijst.add(product);

        }
        myRs.close();

        return productenLijst;

    }
}

