package Domein;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {

    private Connection conn;
    private ReizigerDAO rdao;


    public ReizigerDAOPsql(Connection conn) throws SQLException {
        this.conn = conn;
    }


    @Override
    public boolean save(Reiziger reiziger) {


        try {
            String query = "insert into reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) values (?, ?, ?, ?, ?)";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, reiziger.getReiziger_id());
            pt.setString(2, reiziger.getVoorletters());
            pt.setString(3, reiziger.getTussenvoegsels());
            pt.setString(4, reiziger.getAchternaam());
            pt.setDate(5, reiziger.getGeboortedatum());

            pt.executeUpdate();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger){


        try {
            String query = "update reiziger set reiziger_id = ?, voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? where reiziger_id = ?";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, reiziger.getReiziger_id());
            pt.setString(2, reiziger.getVoorletters());
            pt.setString(3, reiziger.getTussenvoegsels());
            pt.setString(4, reiziger.getAchternaam());
            pt.setDate(5, reiziger.getGeboortedatum());

            pt.setInt(6, reiziger.getReiziger_id());

            pt.executeUpdate();

            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public boolean delete(Reiziger reiziger) {

        try {
            String query = "delete from reiziger where reiziger_id = ? and voorletters = ? and tussenvoegsel = ? and achternaam = ? and geboortedatum = ? ";
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setInt(1, reiziger.getReiziger_id());
            pt.setString(2, reiziger.getVoorletters());
            pt.setString(3, reiziger.getTussenvoegsels());
            pt.setString(4, reiziger.getAchternaam());
            pt.setDate(5, reiziger.getGeboortedatum());

            pt.executeUpdate();
            return true;

        } catch (Exception e) {
            return false;
        }
        //todo boolean goed returnen
    }


    @Override
    public Reiziger findById(int id) throws SQLException {

        String query = "select * from reiziger where reiziger_id = ?";
        PreparedStatement pt = conn.prepareStatement(query);

        pt.setInt(1, id);
        ResultSet myRs = pt.executeQuery();


        Reiziger reiziger = new Reiziger();
        while (myRs.next()) {
            reiziger.setReiziger_id(Integer.parseInt(myRs.getString(1)));
            reiziger.setVoorletters(myRs.getString(2));
            reiziger.setAchternaam(myRs.getString(4));
            reiziger.setGeboortedatum(Date.valueOf(myRs.getString(5)));

        }
        return reiziger;

    }


    @Override
    public List<Reiziger> findByGbDatum(String datum) throws SQLException {

        String query = "select * from reiziger where geboortedatum = ?";

        PreparedStatement pt = conn.prepareStatement(query);
        pt.setDate(1, Date.valueOf(datum));
        ResultSet myRs = pt.executeQuery();

        List<Reiziger> resultaat = conveteerNaarReizigerObject(myRs);

        return resultaat;
    }


    @Override
    public List<Reiziger> findAll() throws SQLException {
        Statement myStmt = conn.createStatement();
        ResultSet myRs = myStmt.executeQuery("SELECT * FROM reiziger");

        List<Reiziger> resultaat = conveteerNaarReizigerObject(myRs);

        return resultaat;

    }


    //deze klasse heb ik zelf toegevoegd zodat findAll & findByDatum er netter uitzien:
    public List<Reiziger> conveteerNaarReizigerObject(ResultSet myRs) throws SQLException {

        List<Reiziger> reizigerLijst = new ArrayList<>();

        while (myRs.next()) {
            Reiziger r = new Reiziger();

            r.setReiziger_id(Integer.parseInt(myRs.getString(1)));
            r.setVoorletters(myRs.getString(2));
            r.setAchternaam(myRs.getString(4));
            r.setGeboortedatum(Date.valueOf(myRs.getString(5)));

            reizigerLijst.add(r);
        }
        return reizigerLijst;

    }

}
