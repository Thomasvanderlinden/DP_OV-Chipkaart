package Dao;


import Domein.Adres;
import Domein.OVChipkaart;
import Domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {

    private Connection conn;
    private AdresDAO adresDAO;
    private OVChipkaartDAO ovChipkaartDAO;


    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }


    public ReizigerDAOPsql(Connection conn, AdresDAO adresDAO, OVChipkaartDAO ovChipkaartDAO) {
        this.conn = conn;
        this.adresDAO = adresDAO;
        this.ovChipkaartDAO = ovChipkaartDAO;
    }

    public void setAdresDAO(AdresDAO adresDAO) {
        this.adresDAO = adresDAO;
    }

    public void setOVDAO(OVChipkaartDAO ovChipkaartDAO) {
        this.ovChipkaartDAO = ovChipkaartDAO;
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
            pt.close();
            if (reiziger.getAdres() != null) {
                adresDAO.save(reiziger.getAdres());

            }

            if (reiziger.getOvChipkaarts_reiziger() != null) {
                for (OVChipkaart o : reiziger.getOvChipkaarts_reiziger()) {
                    ovChipkaartDAO.save(o);
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }


    @Override
    public boolean update(Reiziger reiziger) {

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
            pt.close();

            if (reiziger.getAdres() != null) {
                adresDAO.update(reiziger.getAdres());

            }
            if (reiziger.getOvChipkaarts_reiziger() != null) {
                for (OVChipkaart o : reiziger.getOvChipkaarts_reiziger()) {
                    ovChipkaartDAO.update(o);
                }
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();

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


            if (reiziger.getAdres() != null) {
                adresDAO.delete(reiziger.getAdres());

            }
            if (reiziger.getOvChipkaarts_reiziger() != null) {
                for (OVChipkaart o : reiziger.getOvChipkaarts_reiziger()) {
                    ovChipkaartDAO.delete(o);
                }
            }
            //hier moet het adres eerst verwijderd wordem ivm foreign key constraint
            pt.executeUpdate();
            pt.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();

            return false;

        }

    }


    @Override
    public Reiziger findReizigerById(int id) throws SQLException {

        String query = "select * from reiziger where reiziger_id = ?";
        PreparedStatement pt = conn.prepareStatement(query);

        pt.setInt(1, id);
        ResultSet myRs = pt.executeQuery();


        Reiziger reiziger = new Reiziger();
        while (myRs.next()) {
            reiziger.setReiziger_id(myRs.getInt(1));
            reiziger.setVoorletters(myRs.getString(2));
            reiziger.setAchternaam(myRs.getString(4));
            reiziger.setGeboortedatum(myRs.getDate(5));

        }

        Adres reizigers_adres = adresDAO.findByReiziger(reiziger);
        if (reizigers_adres != null) {
            reiziger.setAdres(reizigers_adres);
        }

        List<OVChipkaart> o = ovChipkaartDAO.findByReiziger(reiziger);
        if (o != null) {
            reiziger.setOvChipkaarts_reiziger(o);
        }
        pt.close();
        myRs.close();

        return reiziger;

    }


    @Override
    public List<Reiziger> findByGbDatum(String datum) throws SQLException {

        String query = "select * from reiziger where geboortedatum = ?";

        PreparedStatement pt = conn.prepareStatement(query);
        pt.setDate(1, Date.valueOf(datum));
        ResultSet myRs = pt.executeQuery();

        List<Reiziger> resultaat = conveteerNaarReizigerObject(myRs);

        for (Reiziger r : resultaat) {
            r.setAdres(adresDAO.findByReiziger(r));
        }
        for (Reiziger r : resultaat) {
            r.setOvChipkaarts_reiziger(ovChipkaartDAO.findByReiziger(r));
        }


        pt.close();
        myRs.close();
        return resultaat;
    }


    @Override
    public List<Reiziger> findAll() throws SQLException {
        Statement myStmt = conn.createStatement();
        ResultSet myRs = myStmt.executeQuery("SELECT * FROM reiziger");

        List<Reiziger> resultaat = conveteerNaarReizigerObject(myRs);

        myStmt.close();

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

            Adres a = adresDAO.findByReiziger(r);
            r.setAdres(a);

            List<OVChipkaart> ov = ovChipkaartDAO.findByReiziger(r);
            r.setOvChipkaarts_reiziger(ov);

            reizigerLijst.add(r);
        }
        return reizigerLijst;
    }
}
