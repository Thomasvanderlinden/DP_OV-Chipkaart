package Domein;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class OVChipkaart {

    private int kaart_nummer;
    private Date geldig_tot;
    private int klasse;
    private double saldo;
    private Reiziger reiziger;
    public List<Product> producten = new ArrayList();


    public OVChipkaart(){}

    public OVChipkaart(int kaart_nummer, Date geldig_tot, int klasse, double saldo, Reiziger reiziger) {
        this.kaart_nummer = kaart_nummer;
        this.geldig_tot = geldig_tot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
        setProducten(producten);
    }

    public OVChipkaart(int kaart_nummer, Date geldig_tot, int klasse, double saldo) {
        this.kaart_nummer = kaart_nummer;
        this.geldig_tot = geldig_tot;
        this.klasse = klasse;
        this.saldo = saldo;
        setProducten(producten);
    }




    public void voegProductToeAanOVChipkaart(Product product){
        this.getProducten().add(product);
        product.ovchipkaarten.add(this);
    }

    public void verwijderProductVanOVChipkaart(Product product){
        producten.remove(product);
        product.ovchipkaarten.remove(this);
    }


    public List<Product> getProducten() {
        return producten;
    }

    public void setProducten(List<Product> producten) {
        this.producten = producten;
    }

    public int getKaart_nummer() {
        return kaart_nummer;
    }
    public void setKaart_nummer(int kaart_nummer) {
        this.kaart_nummer = kaart_nummer;
    }
    public Date getGeldig_tot() {
        return geldig_tot;
    }
    public void setGeldig_tot(Date geldig_tot) {
        this.geldig_tot = geldig_tot;
    }
    public int getKlasse() {
        return klasse;
    }
    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }
    public double getSaldo() {
        return saldo;
    }
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    public Reiziger getReiziger() {
        return reiziger;
    }
    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    @Override
    public String toString() {
        return "OVChipkaart{" +
                "kaart_nummer=" + kaart_nummer +
                ", geldig_tot=" + geldig_tot +
                ", klasse=" + klasse +
                ", saldo=" + +saldo +
                ", reiziger=" + reiziger +
                " product " + producten +
                '}';
        //todo : product informatie meegeven:
    }
}
