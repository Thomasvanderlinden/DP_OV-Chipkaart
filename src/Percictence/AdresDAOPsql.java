package Percictence;

import Domein.Adres;
import Domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {

    private Connection conn;

    public AdresDAOPsql(Connection conn) {
        this.conn = conn;
    }


    @Override
    public boolean save(Adres adres){

        try {
            String query = "insert into adres(adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) values (?, ?, ?, ?, ?, ?)";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, adres.getAdres_id());
            pt.setString(2, adres.getPostcode());
            pt.setString(3, adres.getHuisnummer());
            pt.setString(4, adres.getStraat());
            pt.setString(5, adres.getWoonplaats());
            pt.setInt(6, adres.getReiziger_id());

            pt.executeUpdate();

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) {
        try {
            String query = "update adres set adres_id = ?, postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? where adres_id = ?";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, adres.getAdres_id());
            pt.setString(2, adres.getPostcode());
            pt.setString(3, adres.getHuisnummer());
            pt.setString(4, adres.getStraat());
            pt.setString(5, adres.getWoonplaats());
            pt.setInt(6, adres.getReiziger_id());

            pt.setInt(7, adres.getAdres_id());

            pt.executeUpdate();

            return true;

        } catch (Exception e) {
            return false;
        }

    }


    @Override
    public boolean delete(Adres adres) {
        try {
            String query = "delete from adres where adres_id = ? and postcode = ? and huisnummer = ? and straat = ? and woonplaats = ? and reiziger_id = ? ";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, adres.getAdres_id());
            pt.setString(2, adres.getPostcode());
            pt.setString(3, adres.getHuisnummer());
            pt.setString(4, adres.getStraat());
            pt.setString(5, adres.getWoonplaats());
            pt.setInt(6, adres.getAdres_id());

            pt.executeUpdate();

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {

        String query = "select * from adres where reiziger_id = ?";
        PreparedStatement pt = conn.prepareStatement(query);

        pt.setInt(1, reiziger.getReiziger_id());
        ResultSet myRs = pt.executeQuery();
        if(myRs.next()) {

            Adres adres = new Adres();
            adres.setAdres_id(Integer.parseInt(myRs.getString(1)));
            adres.setPostcode(myRs.getString(2));
            adres.setHuisnummer(myRs.getString(3));
            adres.setStraat(myRs.getString(4));
            adres.setWoonplaats(myRs.getString(5));
            adres.setReiziger_id(myRs.getInt(6));
            return adres;
        }
        return null;
    }


    @Override
    public Adres findAdresById(int id) throws SQLException {

        String query = "select * from adres where adres_id = ?";
        PreparedStatement pt = conn.prepareStatement(query);

        pt.setInt(1, id);
        ResultSet myRs = pt.executeQuery();


        Adres adres = new Adres();
        while (myRs.next()) {
            adres.setAdres_id(Integer.parseInt(myRs.getString(1)));
            adres.setPostcode(myRs.getString(2));
            adres.setHuisnummer(myRs.getString(3));
            adres.setStraat(myRs.getString(4));
            adres.setWoonplaats(myRs.getString(5));
            adres.setReiziger_id(myRs.getInt(6));
        }
        return adres;

    }


    @Override
    public List<Adres> findAll() throws SQLException {
        Statement myStmt = conn.createStatement();
        ResultSet myRs = myStmt.executeQuery("SELECT * FROM adres");


        List<Adres> resultaat = conveteerNaarAdresObject(myRs);

        return resultaat;
    }


    //deze klasse heb ik zelf toegevoegd zodat findAll & findByDatum er netter uitzien, anders staat het in de methodes zelf:
    public List<Adres> conveteerNaarAdresObject(ResultSet myRs) throws SQLException {

        List<Adres> adreslijst = new ArrayList<>();

        while (myRs.next()) {
            Adres a1 = new Adres();

            a1.setAdres_id(Integer.parseInt(myRs.getString(1)));
            a1.setPostcode(myRs.getString(2));
            a1.setHuisnummer(myRs.getString(3));
            a1.setStraat(myRs.getString(4));
            a1.setWoonplaats(myRs.getString(5));
            a1.setReiziger_id(myRs.getInt(6));

            adreslijst.add(a1);
        }
        return adreslijst;

    }
}
