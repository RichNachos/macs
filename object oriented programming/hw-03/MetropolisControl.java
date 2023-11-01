import java.sql.SQLException;

public interface MetropolisControl {
    public void add(String metropolis, String continent, String population) throws SQLException;
    public void search(String metropolis, String continent, String population, boolean largerThan, boolean exactMatch) throws SQLException;
}
