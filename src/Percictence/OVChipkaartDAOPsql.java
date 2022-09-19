package Percictence;

import Domein.OVChipkaart;
import Domein.Reiziger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {

    private Connection conn;

    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
    }


    @Override
    public boolean save(OVChipkaart ovChipkaart){

        try {
            String query = "insert into ov_chipkaart values(?, ?, ?, ?, ?);";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, ovChipkaart.getKaart_nummer());
            pt.setDate(2, ovChipkaart.getGeldig_tot());
            pt.setInt(3, ovChipkaart.getKlasse());
            pt.setDouble(4, ovChipkaart.getSaldo());
            pt.setInt(5, ovChipkaart.getReiziger_id());

            pt.executeUpdate();
            pt.close();

            return true;
        } catch (Exception e) {
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
            pt.setInt(5, ovChipkaart.getReiziger_id());

            pt.setInt(6, ovChipkaart.getReiziger_id());

            pt.executeUpdate();
            pt.close();

            return true;


        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public boolean delete(OVChipkaart ovChipkaart){

        try {
            String query = "delete from ov_chipkaart where kaart_nummer = ?, geldig_tot = ? , klasse = ? , saldo = ? , reiziger_id = ?";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, ovChipkaart.getKaart_nummer());
            pt.setDate(2, ovChipkaart.getGeldig_tot());
            pt.setInt(3, ovChipkaart.getKlasse());
            pt.setDouble(4, ovChipkaart.getSaldo());
            pt.setInt(5, ovChipkaart.getReiziger_id());

            pt.executeUpdate();
            pt.close();

            return true;
        } catch (Exception e) {
            return false;
        }

    }


    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        String query = "select * from ov_chipkaart where reiziger = ?";
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
            ovChipkaart.setReiziger_id(myRs.getInt(5));
            lijstMetOVS.add(ovChipkaart);
        }
        myRs.close();
        pt.close();
        return lijstMetOVS;
    }

    @Override
    //todo: deze nog doen:
    public List<OVChipkaart> findAll() throws SQLException {
        return null;
    }
}
