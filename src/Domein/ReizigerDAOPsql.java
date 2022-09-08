package Domein;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {

    private Connection conn;

    public ReizigerDAOPsql(Connection conn) throws SQLException {
        this.conn = conn;
    }


    @Override
    public boolean save(Reiziger reiziger) throws SQLException {


        String query = "insert into reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) values (?, ?, ?, ?, ?)";
        PreparedStatement pt =  conn.prepareStatement(query);

        pt.setInt(1, reiziger.getReiziger_id());
        pt.setString(2, reiziger.getVoorletters());
        pt.setString(3, reiziger.getTussenvoegsels());
        pt.setString(4, reiziger.getAchternaam());
        pt.setDate(5, reiziger.getGeboortedatum());


        pt.executeUpdate();

        //todo moet hier nog iets bouwen om juiste boolean te returnen:
        //todo exceptions moeten nog beter worden afgehandeld
        return true;
    }

    @Override
    public boolean update(Reiziger reiziger) {


        //todo beetje raar dit er is geen oude en nieuwe:



        return true;
    }





    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {

        String query = "delete from reiziger where reiziger_id = ? and voorletters = ? and tussenvoegsel = ? and achternaam = ? and geboortedatum = ? ";
        PreparedStatement pt = conn.prepareStatement(query);

        pt.setInt(1, reiziger.getReiziger_id());
        pt.setString(2, reiziger.getVoorletters());
        pt.setString(3, reiziger.getTussenvoegsels());
        pt.setString(4, reiziger.getAchternaam());
        pt.setDate(5, reiziger.getGeboortedatum());

        pt.executeUpdate();

        //todo boolean goed returnen
        return true;

    }





    @Override
    public Reiziger findById(int id) throws SQLException {

        String query = "select * from reiziger where reiziger_id = ?";
        PreparedStatement pt = conn.prepareStatement(query);

        pt.setInt(1, id);
        ResultSet myRs = pt.executeQuery();


        Reiziger r = new Reiziger();
        while (myRs.next()) {
            r.setReiziger_id(Integer.parseInt(myRs.getString(1)));
            r.setVoorletters(myRs.getString(2));
            r.setAchternaam(myRs.getString(4));
            r.setGeboortedatum(Date.valueOf(myRs.getString(5)));

        }
        return r;

    }


    @Override
    public List<Reiziger> findByGbDatum(String datum) throws SQLException {

        String query = "select * from reiziger where geboortedatum = ?";

        PreparedStatement pt = conn.prepareStatement(query);
        pt.setDate(1, Date.valueOf(datum));
        ResultSet myRs = pt.executeQuery();

        List<Reiziger> resultaat = conveteerNaarObject(myRs);

        return resultaat;
    }


    @Override
    public List<Reiziger> findAll() throws SQLException {
        Statement myStmt = conn.createStatement();
        ResultSet myRs = myStmt.executeQuery("SELECT * FROM reiziger");

        List<Reiziger> resultaat = conveteerNaarObject(myRs);

        return resultaat;

    }


    public List<Reiziger> conveteerNaarObject(ResultSet myRs) throws SQLException {

        List<Reiziger> testlijst = new ArrayList<>();

        while (myRs.next()) {
            Reiziger r = new Reiziger();

            r.setReiziger_id(Integer.parseInt(myRs.getString(1)));
            r.setVoorletters(myRs.getString(2));
            r.setAchternaam(myRs.getString(4));
            r.setGeboortedatum(Date.valueOf(myRs.getString(5)));

            testlijst.add(r);
        }
        return testlijst;

    }

}
