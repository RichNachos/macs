import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.sql.*;

public class MetropolisTableModel extends AbstractTableModel implements MetropolisControl{
    private int rowCount = 0;
    private int colCount = 3;
    private String[] columnNames = {"Metropolis", "Continent", "Population"};
    private String[][] tableData;
    private DBConnection connection;

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return colCount;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return tableData[row][col];
    }
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public void add(String metropolis, String continent, String population) throws SQLException {
        // IMPLEMENT QUERY
        DBConnection dbConn = new DBConnection();
        Connection conn = dbConn.getConnection();

        if (metropolis.length() != 0 && continent.length() != 0 && population.length() != 0) {
            String query = "INSERT INTO metropolises VALUES(?,?,?);";
            PreparedStatement preparedQuery = conn.prepareStatement(query);
            preparedQuery.setString(1, metropolis);
            preparedQuery.setString(2, continent);
            preparedQuery.setString(3, population);
            preparedQuery.execute();

            search(metropolis, continent, "-1", true, true);
        }
        dbConn.stopConnection();
    }

    public void search(String metropolis, String continent, String population, boolean largerThan, boolean exactMatch) throws SQLException{
        // IMPLEMENT QUERY
        DBConnection dbConn = new DBConnection();
        Connection conn = dbConn.getConnection();

        String query = "SELECT * FROM metropolises";
        if (metropolis.length() != 0 || continent.length() != 0 || population.length() != 0) {
            query += " WHERE";
            String largerOperator;
            String exactOperator;
            if (largerThan) { largerOperator = ">"; }
            else { largerOperator = "<"; }
            if (exactMatch) { exactOperator = "="; }
            else {
                exactOperator = "LIKE";
                metropolis = "%" + metropolis + "%";
                continent = "%" + continent + "%";
            }

            int conds = 0;
            if (metropolis.length() != 0) {
                conds++;
                query += " metropolis " + exactOperator + " ?";
            }
            if (continent.length() != 0) {
                if (conds != 0) { query += " AND"; }
                conds++;
                query += " continent " + exactOperator + " ?";
            }
            if (population.length() != 0) {
                if (conds != 0) { query += " AND"; }
                query += " population " + largerOperator + " ?";
            }
        }

        query += ";";
        int index = 1;
        PreparedStatement preparedQuery = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        if (metropolis.length() != 0) { preparedQuery.setString(index++, metropolis); }
        if (continent.length() != 0) { preparedQuery.setString(index++, continent); }
        if (population.length() != 0) { preparedQuery.setString(index++, population); }
        ResultSet result = preparedQuery.executeQuery();
        changeTableData(result);
        fireTableDataChanged();

        dbConn.stopConnection();
    }

    private void changeTableData(ResultSet newData) throws SQLException {
        ResultSetMetaData meta = newData.getMetaData();
        colCount = meta.getColumnCount();
        newData.last();
        rowCount = newData.getRow();
        newData.beforeFirst();

        tableData = new String[rowCount][colCount];
        while (newData.next()) {
            for (int i = 0; i < colCount; i++) {
                tableData[newData.getRow() - 1][i] = newData.getString(i + 1);
            }
        }
    }
}
