package Domein;

import java.util.ArrayList;
import java.util.List;


public class Product {
    private int product_nummer;
    private String naam;
    private String beschrijving;
    private double prijs;
//todo: integer van maken:
    public List<OVChipkaart> ovchipkaarten = new ArrayList<>();


    public Product(int product_nummer, String naam, String beschrijving, double prijs){
        this.product_nummer = product_nummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
        setOvchipkaarten(ovchipkaarten);
    }
    public Product(){}


    public void voegOVChipkaartToeAanProduct(OVChipkaart ovChipkaart){
        getOvchipkaarten().add(ovChipkaart);
        ovChipkaart.producten.add(this);
    }

    public void verwijderOVChipkaartVanProduct(OVChipkaart ovChipkaart){
        getOvchipkaarten().remove(ovChipkaart);
        ovChipkaart.producten.remove(this);
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
    public void setPrijs(int prijs) {
        this.prijs = prijs;
    }

    public List<OVChipkaart> getOvchipkaarten() {
        return ovchipkaarten;
    }

    public void setOvchipkaarten(List<OVChipkaart> ovchipkaarten) {
        this.ovchipkaarten = ovchipkaarten;
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
