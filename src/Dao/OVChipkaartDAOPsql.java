package Dao;

import Domein.OVChipkaart;
import Domein.Product;
import Domein.Reiziger;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {

    private Connection conn;
    private ReizigerDAOPsql rdao;
    private ProductDAOPsql pdao;


    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
    }

    public void setPDAO(ProductDAOPsql pdao) {
        this.pdao = pdao;
    }

    public void setRDAO(ReizigerDAOPsql rdao) {
        this.rdao = rdao;
    }


    @Override
    public boolean save(OVChipkaart ovChipkaart) {

        try {
            String query = "insert into ov_chipkaart values(?, ?, ?, ?, ?);";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, ovChipkaart.getKaart_nummer());
            pt.setDate(2, ovChipkaart.getGeldig_tot());
            pt.setInt(3, ovChipkaart.getKlasse());
            pt.setDouble(4, ovChipkaart.getSaldo());
            pt.setInt(5, ovChipkaart.getReiziger().getReiziger_id());

            pt.executeUpdate();
            pt.close();

//            for (Product p : ovChipkaart.getProducten()) {
//                pdao.save(p);
//            }

            if (ovChipkaart.getProducten() != null) {
                for (Product product : ovChipkaart.getProducten()) {

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
    public boolean update(OVChipkaart ovChipkaart) {

        try {
            String query = "update ov_chipkaart set kaart_nummer = ?, geldig_tot = ? , klasse = ? , saldo = ? , reiziger_id = ? where reiziger_id = ? ";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, ovChipkaart.getKaart_nummer());
            pt.setDate(2, ovChipkaart.getGeldig_tot());
            pt.setInt(3, ovChipkaart.getKlasse());
            pt.setDouble(4, ovChipkaart.getSaldo());
            pt.setInt(5, ovChipkaart.getReiziger().getReiziger_id());

            pt.setInt(6, ovChipkaart.getReiziger().getReiziger_id());

            pt.executeUpdate();
            pt.close();


            if (ovChipkaart.getProducten() != null) {
                for (Product product: ovChipkaart.getProducten()) {

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
            e.printStackTrace();

            return false;
        }
    }


    @Override
    public boolean delete(OVChipkaart ovChipkaart) {

        try {


            if (ovChipkaart.getProducten() != null) {
                for (Product product : ovChipkaart.getProducten()) {
                    String queryDeleteProducten = "delete from ov_chipkaart_product where kaart_nummer = ? and product_nummer = ?  and status = ? and last_update = ? ";
                    PreparedStatement ptDeleteProducten = conn.prepareStatement(queryDeleteProducten);

                    ptDeleteProducten.setInt(1, ovChipkaart.getKaart_nummer());
                    ptDeleteProducten.setInt(2, product.getProduct_nummer());
                    ptDeleteProducten.setString(3, "hier");
                    ptDeleteProducten.setDate(4, Date.valueOf("1900-01-01"));

                    ptDeleteProducten.executeUpdate();
                    ptDeleteProducten.close();


                }
            }

            String query = "delete from ov_chipkaart where kaart_nummer = ? and geldig_tot = ? and klasse = ? and saldo = ? and reiziger_id = ?";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, ovChipkaart.getKaart_nummer());
            pt.setDate(2, ovChipkaart.getGeldig_tot());
            pt.setInt(3, ovChipkaart.getKlasse());
            pt.setDouble(4, ovChipkaart.getSaldo());
            pt.setInt(5, ovChipkaart.getReiziger().getReiziger_id());

            pt.executeUpdate();
            pt.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }

    }


    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        String query = "select * from ov_chipkaart where reiziger_id = ?";
        PreparedStatement pt = conn.prepareStatement(query);

        pt.setInt(1, reiziger.getReiziger_id());
        ResultSet myRs = pt.executeQuery();

        List<OVChipkaart> lijstMetOVS = new ArrayList<>();

        if (myRs.next()) {
            OVChipkaart ovChipkaart = new OVChipkaart();
            ovChipkaart.setKaart_nummer(myRs.getInt(1));
            ovChipkaart.setGeldig_tot(Date.valueOf(myRs.getString(2)));
            ovChipkaart.setKlasse(myRs.getInt(3));
            ovChipkaart.setSaldo(myRs.getDouble(4));
            ovChipkaart.setReiziger(reiziger);

            ovChipkaart.setReiziger(reiziger);
            lijstMetOVS.add(ovChipkaart);

        }

        myRs.close();
        pt.close();
        return lijstMetOVS;
    }

    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        Statement myStmt = conn.createStatement();
        ResultSet myRs = myStmt.executeQuery("SELECT * FROM ov_chipkaart");


        List<OVChipkaart> resultaat = conveteerNaarAdresObject(myRs);


        myStmt.close();
        myRs.close();
        return resultaat;
    }

    public List<OVChipkaart> conveteerNaarAdresObject(ResultSet myRs) throws SQLException {

        List<OVChipkaart> ovlijst = new ArrayList<>();

        while (myRs.next()) {

            int kn = myRs.getInt("kaart_nummer");
            Date gd = myRs.getDate("geldig_tot");
            int k = myRs.getInt("klasse");
            double sa = myRs.getDouble("saldo");
            int Rid = myRs.getInt("reiziger_id");

            OVChipkaart ov = new OVChipkaart(kn, gd, k, sa, rdao.findReizigerById(Rid));

            ovlijst.add(ov);

        }
        myRs.close();
        return ovlijst;

    }

}

