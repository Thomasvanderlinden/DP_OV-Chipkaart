import Domein.Reiziger;
import Domein.ReizigerDAO;
import Domein.ReizigerDAOPsql;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public Main() throws SQLException {
    }

    public static void main(String[] args) throws SQLException {
        ReizigerDAO rdao = new ReizigerDAOPsql(getConnection());
        testReizigerDAO(rdao);
    }


    private static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/ovchip";
        Connection myConn = DriverManager.getConnection(url, "postgres", "Oempa.2000");
        return myConn;
    }

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");



        System.out.println("test alle reizigers");
        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }

        System.out.println();
        System.out.println();





        System.out.println("test reizigers zoeken op geboortedatum");
        //haal bepaalde reizigers uit database:
        List<Reiziger> reizigers1 = rdao.findByGbDatum("2002-12-03");
        System.out.println("[Test] ReizigerDAO.findByGbDatum() geeft de volgende reizigers:");
        for(Reiziger r : reizigers1){
            System.out.println(r);
        }

        System.out.println();
        System.out.println();




        System.out.println("test reizigers zoeken op id");
        //haal reizigers uit de database:
        Reiziger reiziger2 = rdao.findById(1);
        System.out.println("[Test] ReizigerDAO.findById() geeft de volgende reizigers:");
        System.out.println(reiziger2);


        System.out.println();
        System.out.println();




        System.out.println("test reiziger toevoegen aan tabel/safe");
        //Maak een nieuwe reiziger aan en persisteer deze in de database

        //Reiziger r1 = new Reiziger(14, "H","van","bemmel", Date.valueOf("1900-11-22"));

        Reiziger r1 = new Reiziger();
        r1.setAchternaam("test");
        r1.setReiziger_id(20);
        r1.setVoorletters("test");
        r1.setGeboortedatum(Date.valueOf("1800-12-12"));
        r1.setTussenvoegsels("test");


        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");

        rdao.save(r1);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        //verwijder hier meteen weer deze reiziger zodat de database geen duplicate fout geeft elke keer
        rdao.delete(r1);

        System.out.println();
        System.out.println();




        System.out.println("reiziger verwijderen test");


        //Reiziger r2 = new Reiziger(14, "d","e","d", Date.valueOf("1900-11-22"));
        Reiziger r2 = new Reiziger();
        r2.setAchternaam("test2");
        r2.setReiziger_id(21);
        r2.setVoorletters("test2");
        r2.setGeboortedatum(Date.valueOf("1800-12-12"));
        r2.setTussenvoegsels("test2");





        //voeg eerst de reiziger toe:
        rdao.save(r2);
        System.out.println("eerst waren er zoveel reizigers: " + rdao.findAll().size());
        rdao.delete(r2);
        System.out.println("nu zijn er zoveel reizigers: " + rdao.findAll().size());

        rdao.save(r2);
    }

}
