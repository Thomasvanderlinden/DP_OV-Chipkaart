package Domein;

import java.util.ArrayList;
import java.util.List;


public class Product {
    private int product_nummer;
    private String naam;
    private String beschrijving;
    private double prijs;
    public List<Integer> ovChipkaartenNummers = new ArrayList<>();


    public Product(int product_nummer, String naam, String beschrijving, double prijs){
        this.product_nummer = product_nummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }


    public Product(){}


    public void voegOvchipkaartNummerToeAanProduct(int kaartNummer){
        if(!ovChipkaartenNummers.contains(kaartNummer)){
            ovChipkaartenNummers.add(kaartNummer);
        }
    }

    public void verwijderOVChipkaartVanProduct(Integer kaartNummer){
        ovChipkaartenNummers.remove(kaartNummer);
    }


    public int getProduct_nummer() {
        return product_nummer;
    }
    public void setProduct_nummer(int product_nummer) {
        this.product_nummer = product_nummer;
    }
    public String getNaam() {
        return naam;
    }
    public void setNaam(String naam) {
        this.naam = naam;
    }
    public String getBeschrijving() {
        return beschrijving;
    }
    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }
    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public void setPrijs(int prijs) {
        this.prijs = prijs;
    }

    public List<Integer> getOvChipkaartenNummers() {
        return ovChipkaartenNummers;
    }

    public void setOvChipkaartenNummers(List<Integer> kaartNummerLijst) {
        this.ovChipkaartenNummers = kaartNummerLijst;
    }

    @Override
    public String toString() {
        return "Product tostring {" +
                "product_nummer=" + product_nummer +
                ", naam='" + naam + '\'' +
                ", beschrijving='" + beschrijving + '\'' +
                ", prijs=" + prijs +
                '}';
    }
}
