package Dao.ovchipkaart;

import Dao.product.ProductDAOPsql;
import Dao.reiziger.ReizigerDAOPsql;
import Domein.OVChipkaart;
import Domein.Product;
import Domein.Reiziger;

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


            for(Product p : ovChipkaart.getProducten()){
                pdao.save(p);
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


            for(Product p : ovChipkaart.getProducten()){
                pdao.update(p);
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

            for(Product p : ovChipkaart.getProducten()){
                pdao.deleteOV_ChipKaart_Product(p);
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


            for(Product p : pdao.findByOVChipkaart(ovChipkaart)){
                ovChipkaart.voegProductToeAanOVChipkaart(p);
            }
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

        ArrayList<OVChipkaart> ovlijst = new ArrayList<>();

        while (myRs.next()) {

            int kn = myRs.getInt("kaart_nummer");
            Date gd = myRs.getDate("geldig_tot");
            int k = myRs.getInt("klasse");
            double sa = myRs.getDouble("saldo");
            int Rid = myRs.getInt("reiziger_id");

            OVChipkaart ovChipkaart = new OVChipkaart(kn, gd, k, sa, rdao.findReizigerById(Rid));

            for(Product p : pdao.findByOVChipkaart(ovChipkaart)){
                ovChipkaart.voegProductToeAanOVChipkaart(p);
            }

            ovlijst.add(ovChipkaart);

        }
        myRs.close();
        return ovlijst;

    }


    public List<OVChipkaart> findByProduct(Product product) throws SQLException {
        String query =
                    "select ov_chipkaart.kaart_nummer, ov_chipkaart.geldig_tot, ov_chipkaart.klasse, ov_chipkaart.saldo\n" +
                    "from ov_chipkaart\n" +
                    "inner join ov_chipkaart_product on ov_chipkaart.kaart_nummer = ov_chipkaart_product.Kaart_nummer\n" +
                    "where ov_chipkaart_product.product_nummer = ?" ;

        PreparedStatement pt = conn.prepareStatement(query);

        pt.setInt(1, product.getProduct_nummer());
        ResultSet myRs = pt.executeQuery();

        List<OVChipkaart> ovlijst = new ArrayList<>();


        if (myRs.next()) {
            int pn = myRs.getInt("kaart_nummer");
            Date nm = myRs.getDate("geldig_tot");
            int bs = myRs.getInt("klasse");
            double pr = myRs.getDouble("saldo");

            OVChipkaart ovChipkaart = new OVChipkaart(pn, nm, bs, pr);

            for(Product p : pdao.findByOVChipkaart(ovChipkaart)){
                ovChipkaart.voegProductToeAanOVChipkaart(p);
            }
            ovlijst.add(ovChipkaart);

        }
        return ovlijst;
    }

}

