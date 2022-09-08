package Domein;

import java.sql.SQLException;
import java.util.List;

public interface ReizigerDAO {
    boolean save(Reiziger reiziger) throws SQLException;
    boolean update(Reiziger reiziger);
    boolean delete(Reiziger reiziger) throws SQLException;
    Reiziger findById(int id) throws SQLException;
    List<Reiziger> findByGbDatum(String datum) throws SQLException;
    List <Reiziger> findAll() throws SQLException;
}
