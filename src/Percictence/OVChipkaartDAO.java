package Percictence;

import Domein.Adres;
import Domein.OVChipkaart;
import Domein.Reiziger;

import java.sql.SQLException;
import java.util.List;


public interface OVChipkaartDAO {
    List <OVChipkaart> findByReiziger (Reiziger reiziger) throws SQLException;
    boolean save (OVChipkaart ovChipkaart) throws SQLException;
    boolean update (OVChipkaart ovChipkaart);
    boolean delete (OVChipkaart ovChipkaart) throws SQLException;

    List <OVChipkaart> findAll() throws SQLException;
}
