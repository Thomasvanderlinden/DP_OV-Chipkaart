package lib;

import Domein.Product;

import java.sql.Date;
import java.sql.PreparedStatement;

public class temp {

    for(
    Product p : ovChipkaart.getProducten()){
        pdao.save(p);
    }

            if(ovChipkaart.getProducten() != null){
        for(Product product : ovChipkaart.getProducten()){

            String queryOpslaanProducten = "insert into ov_chipkaart_product values(?,?,?,?)";
            PreparedStatement ptOpslaanProducten = conn.prepareStatement(queryOpslaanProducten);

            ptOpslaanProducten.setInt(1, ovChipkaart.getKaart_nummer());
            ptOpslaanProducten.setInt(2, product.getProduct_nummer());
            ptOpslaanProducten.setString(3, "hier");
            ptOpslaanProducten.setDate(4, Date.valueOf("1900-1-1"));

            ptOpslaanProducten.executeUpdate();
            ptOpslaanProducten.close();

        }
    }
}
