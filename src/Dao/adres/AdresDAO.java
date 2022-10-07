package Dao.adres;

import Domein.Adres;
import Domein.Reiziger;
import java.sql.SQLException;
import java.util.List;

public interface AdresDAO {
    boolean save (Adres adres) throws SQLException;
    boolean update (Adres adres);
    boolean delete (Adres adres) throws SQLException;
    Adres findByReiziger(Reiziger reiziger) throws SQLException;
    List <Adres> findAll() throws SQLException;

}