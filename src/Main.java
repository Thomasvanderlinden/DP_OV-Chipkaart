import Percictence.*;
import Domein.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public Main() {
    }

    private static Connection connection;


    public static void main(String[] args) throws SQLException {

        connection = getConnection();

        AdresDAOPsql adao = new AdresDAOPsql(connection);
        OVChipkaartDAOPsql odao = new OVChipkaartDAOPsql(connection);
        ReizigerDAOPsql rdao = new ReizigerDAOPsql(connection, adao, odao);

        rdao.setAdresDAO(adao);
        rdao.setOVDAO(odao);

        testReizigerDAO(rdao);
        testAdresDAO(adao);
        testOVDAO(odao);

    }


    private static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/ovchip";
        Connection myConn = DriverManager.getConnection(url, "postgres", "Oempa.2000");
        return myConn;
    }


    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        System.out.println(rdao.findByGbDatum("1800-12-12"));
        System.out.println(rdao.findAll());

        //test rdao.findall()
        System.out.println("test alle reizigers");
        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();
        System.out.println();


        //test rdao.findByGbDatum()
        System.out.println("test reizigers zoeken op geboortedatum");
        //haal bepaalde reizigers uit database:
        List<Reiziger> reizigers1 = rdao.findByGbDatum("2002-12-03");
        System.out.println("[Test] ReizigerDAO.findByGbDatum() geeft de volgende reizigers:");
        for (Reiziger r : reizigers1) {
            System.out.println(r);
        }
        System.out.println();
        System.out.println();


        //test rdao.findById()
        System.out.println("test reizigers zoeken op id");
        //haal reizigers uit de database:
        Reiziger reiziger2 = rdao.findReizigerById(1);
        System.out.println("[Test] ReizigerDAO.findById() geeft de volgende reizigers:");
        System.out.println(reiziger2);
        System.out.println();
        System.out.println();


        //test rdao.save()
        System.out.println("test reiziger toevoegen aan tabel/safe");
        //Maak een nieuwe reiziger aan en persisteer deze in de database
        Reiziger r1 = new Reiziger(20, "test", "test", "test", Date.valueOf("1800-12-12"));

        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(r1);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        //verwijder hier meteen weer deze reiziger zodat de database geen duplicate fout geeft elke keer
        rdao.delete(r1);
        System.out.println();
        System.out.println();


        //test rdao.delete()
        System.out.println("reiziger verwijderen test");
        Reiziger r2 = new Reiziger(25, "test2", "test2", "test2", Date.valueOf("1800-12-12"));


        //voeg eerst de reiziger toe:
        rdao.save(r2);
        System.out.println("eerst waren er zoveel reizigers: " + rdao.findAll().size());
        rdao.delete(r2);
        System.out.println("nu zijn er zoveel reizigers: " + rdao.findAll().size());
        System.out.println();
        System.out.println();


        System.out.println("test de update functie reiziger");
        //maak reiziger aan
        rdao.save(r2);
        //print reizigerinformatie uit:
        System.out.println(rdao.findReizigerById(25));
        //maak de geupdate reiziger aan
        Reiziger r3 = new Reiziger(25, "test3", "test3", "test3", Date.valueOf("1800-12-12"));
        //update de reiziger
        rdao.update(r3);
        //print de geupdate versie uit
        System.out.println(rdao.findReizigerById(25));
        //verwijder reiziger 3 zodat het de volgende keer weer goed gaat
        rdao.delete(r3);


        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("reiziger opslaan & dan adres mee test");

        Reiziger r5 = new Reiziger(211, "test5", "test5", "test5", Date.valueOf("1800-12-12"));
        Adres a5 = new Adres(211, "test5", "test5", "test5", "test5", 211);

        r5.setAdres(a5);
        rdao.delete(r5);

        rdao.save(r5);


        System.out.println("hier moet je zijn");
        System.out.println();

//        Reiziger reiziger = rdao.findReizigerById(100);
//        System.out.println("zo ziet de reiziger er nu uit " + reiziger + reiziger.getAdres());


        Reiziger r6 = new Reiziger(100, "okeeee", "okeeee", "test44", Date.valueOf("1800-12-12"));
        Adres a6 = new Adres(100, "okeeee", "okeeee", "test44", "test44", 100);
        OVChipkaart o6 = new OVChipkaart(1234, Date.valueOf("1900-1-1"), 2, 0, 100);
        OVChipkaart o7 = new OVChipkaart(5678, Date.valueOf("1900-1-1"), 2, 0, 100);


        r6.ovkaartToevoegen(o6);
        r6.ovkaartToevoegen(o7);

        r6.setAdres(a6);
        r6.setOvChipkaarts_reiziger(r6.getOvChipkaarts_reiziger());
        rdao.save(r6);

        System.out.println(r6.getOvChipkaarts_reiziger());
//        System.out.println("zo ziet de reiziger er nu uit " + rdao.findReizigerById(100) + reiziger.getAdres());


        System.out.println(o6);
        rdao.delete(r6);


        System.out.println(r6);
        System.out.println(r5);


    }

    private static void testAdresDAO(AdresDAO adao) throws SQLException {

        System.out.println("\n---------- Test AdresDAO -------------");


        for (Adres a : adao.findAll()) {
            System.out.println(a);
        }
        //test adao.save()
        System.out.println("test adres toevoegen aan tabel/safe");
        //Maak een nieuwe reiziger aan en persisteer deze in de database

        Adres a1 = new Adres(13, "ewf", "ees", "ese", "ese", 13);


        System.out.println("eerst zijn er zoveel adressen: " + adao.findAll().size());
        adao.save(a1);
        System.out.println("nu zijn er zoveel adressen" + adao.findAll().size());

        //verwijder hier meteen weer deze reiziger zodat de database geen duplicate fout geeft elke keer
        adao.delete(a1);
        //hier is dus meteen de delete functie getest
        System.out.println();
        System.out.println();


        System.out.println("test de update functie adres");
        //maak adres aan
        adao.save(a1);
        //print reizigerinformatie uit:
        List<Adres> lijstMetAdressen = adao.findAll();
        System.out.println(lijstMetAdressen.get(7));

        //maak de geupdate reiziger aan
        Adres a2 = new Adres(13, "1234EE", "test3", "test3", "test3", 13);

        //update de reiziger
        adao.update(a2);
        //print de geupdate versie uit

        System.out.println(lijstMetAdressen.get(6));
        //verwijder reiziger 3 zodat het de volgende keer weer goed gaat
        adao.delete(a2);

        System.out.println();
        System.out.println();


        for (Adres s : adao.findAll()) {
            System.out.println(s);
        }


    }

    public static void testOVDAO(OVChipkaartDAOPsql odao) {

        System.out.println("testen");

    }


}
