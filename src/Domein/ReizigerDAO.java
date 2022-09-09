package Domein;

import java.sql.SQLException;
import java.util.List;

public interface ReizigerDAO {
    boolean save(Reiziger reiziger);
    boolean update(Reiziger reiziger);
    boolean delete(Reiziger reiziger);
    Reiziger findById(int id) throws SQLException;
    List<Reiziger> findByGbDatum(String datum) throws SQLException;
    List <Reiziger> findAll() throws SQLException;
}
