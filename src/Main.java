import Domein.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public Main() throws SQLException {
    }

    public static void main(String[] args) throws SQLException {

        ReizigerDAO rdao = new ReizigerDAOPsql(getConnection());
        testReizigerDAO(rdao);

        AdresDAO adao = new AdresDAOPsql(getConnection());
        testAdresDAO(adao);

    }


    private static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/ovchip";
        Connection myConn = DriverManager.getConnection(url, "postgres", "Oempa.2000");
        return myConn;
    }

//    private static Connection closeConnection(){
//
//    }


    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");


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
        for(Reiziger r : reizigers1){
            System.out.println(r);
        }
        System.out.println();
        System.out.println();



        //test rdao.findById()
        System.out.println("test reizigers zoeken op id");
        //haal reizigers uit de database:
        Reiziger reiziger2 = rdao.findById(1);
        System.out.println("[Test] ReizigerDAO.findById() geeft de volgende reizigers:");
        System.out.println(reiziger2);
        System.out.println();
        System.out.println();



        //test rdao.save()
        System.out.println("test reiziger toevoegen aan tabel/safe");
        //Maak een nieuwe reiziger aan en persisteer deze in de database
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



        //test rdao.delete()
        System.out.println("reiziger verwijderen test");
        //Reiziger r2 = new Reiziger(14, "d","e","d", Date.valueOf("1900-11-22"));
        Reiziger r2 = new Reiziger();
        r2.setAchternaam("test2");
        r2.setReiziger_id(25);
        r2.setVoorletters("test2");
        r2.setGeboortedatum(Date.valueOf("1800-12-12"));
        r2.setTussenvoegsels("test2");

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
        System.out.println(rdao.findById(25));

        //maak de geupdate reiziger aan
        Reiziger r3 = new Reiziger();
        r3.setAchternaam("test3");
        r3.setReiziger_id(25);
        r3.setVoorletters("test3");
        r3.setGeboortedatum(Date.valueOf("1800-12-12"));
        r3.setTussenvoegsels("test3");

        //update de reiziger
        rdao.update(r3);
        //print de geupdate versie uit
        System.out.println(rdao.findById(25));
        //verwijder reiziger 3 zodat het de volgende keer weer goed gaat
        rdao.delete(r3);


    }

    private static void testAdresDAO(AdresDAO adao) throws SQLException {

        System.out.println("\n---------- Test AdresDAO -------------");


        for(Adres a : adao.findAll()){
            System.out.println(a);
        }
        //test adao.save()
        System.out.println("test reiziger toevoegen aan tabel/safe");
        //Maak een nieuwe reiziger aan en persisteer deze in de database
        Adres a1 = new Adres();
        a1.setAdres_id(13);
        a1.setPostcode("1111ee");
        a1.setHuisnummer("test");
        a1.setStraat("test");
        a1.setWoonplaats("test");
        a1.setReiziger_id(13);


        System.out.println("eerst zijn er zoveel adressen: " + adao.findAll().size());
        adao.save(a1);
        System.out.println("nu zijn er zoveel adressen" + adao.findAll().size());

        //verwijder hier meteen weer deze reiziger zodat de database geen duplicate fout geeft elke keer
        adao.delete(a1);
        System.out.println();
        System.out.println();





        System.out.println("test de update functie adres");
        //maak adres aan
        adao.save(a1);
        //print reizigerinformatie uit:
        List <Adres> lijstMetAdressen = adao.findAll();
        System.out.println(lijstMetAdressen.get(7));

        //maak de geupdate reiziger aan
        Adres a2 = new Adres();
        a2.setStraat("test3");
        a2.setAdres_id(13);
        a2.setHuisnummer("test3");
        a2.setPostcode("1234EE");
        a2.setWoonplaats("test3");
        a2.setReiziger_id(13);

        //update de reiziger
        adao.update(a2);
        //print de geupdate versie uit

        System.out.println(lijstMetAdressen.get(6));
        //verwijder reiziger 3 zodat het de volgende keer weer goed gaat
        adao.delete(a2);




    }


}
