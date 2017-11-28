package ua.com.juja.sqlcmd;

import java.sql.*;
import java.util.Arrays;
import java.util.Random;

public class JDBCDatabaseManager implements DatabaseManager {

    private Connection connection;

    @Override
    public String[] getTableNames(){
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT TABLE_NAME FROM information_schema.tables" +
                    " WHERE table_schema = 'public' AND table_type = 'BASE TABLE'");
            int index = 0;
            String[] tables = new String[100];
            while (rs.next()) {
                tables[index++] = rs.getString("table_name");
            }
            tables = Arrays.copyOf(tables, index, String[].class);
            rs.close();
            stmt.close();
            return tables;
        }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public DataSet[] getTableData(String tableName) {
        try {
            int size = getSize(tableName);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnSize = rsmd.getColumnCount();
            DataSet[]result = new DataSet[size];
            int index = 0;
            while (rs.next()) {
                DataSet dataSet = new DataSet();
                result[index++] = dataSet;
                for(int i = 1; i <= columnSize; i++) {
                    dataSet.put(rsmd.getColumnName(i), rs.getObject(i));
                }
            }
            rs.close();
            stmt.close();
            return result;
        }catch(SQLException ex){
            System.out.println("TableData problem...");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void create(DataSet input) {
        try {
            String tableNames = getNamesFormated(input, "%s,");
            String values = getValueFormated(input, "'%s',");

            Statement stmt = connection.createStatement();
            stmt.executeUpdate("INSERT INTO users(" + tableNames +
                    ") VALUES(" + values + ")");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("create method didn't work " );
            e.printStackTrace();
        }
    }

    private String getValueFormated(DataSet input, String format) {
        String values = "";
        for (Object value: input.getValues()){
            values += String.format(format, value);
        }
        values = values.substring(0, values.length()-1);
        return values;
    }

    private int getSize(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rsCount = stmt.executeQuery("SELECT COUNT (*) FROM " + tableName);
        rsCount.next();
        int size = rsCount.getInt(1);
        rsCount.close();
        return size;
    }

    private Connection getConnection() {
        return connection;
    }

    @Override
    public void connect(String databaseName, String login, String password){
        try{
            Class.forName("org.postgresql.Driver");
        }catch(ClassNotFoundException ex){
            System.out.println("connect your jdbc jar!");
            ex.printStackTrace();
        }try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + databaseName,
                    login, password);

        }catch(SQLException ex){
            System.out.println(String.format("Can`t get connection to databas: %s user:%s", databaseName, login, password));
            connection = null;
        }

    }

    @Override
    public void update(String tableName, int id, DataSet newValue){
        try {
            String tableNames = getNamesFormated(newValue, "%s = ?,");

            String sql = "UPDATE " + tableName + " SET " + tableNames + " WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            int index = 1;
            for (Object value: newValue.getValues()) {
                ps.setObject(index, value);
                index++;
            }
            ps.setObject(index, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println("update didn`t work");
            e.printStackTrace();
        }
    }

    private String getNamesFormated(DataSet newValue, String format) {
        String string = "";
        for (String name: newValue.getNames()) {
            string += String.format(format, name);
        }
        return string.substring(0, string.length() - 1);
    }

    @Override
    public void clear(String tableName) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM " +  tableName);
            stmt.close();
            //connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("problem with clearing table " + tableName);
        }
    }
}
