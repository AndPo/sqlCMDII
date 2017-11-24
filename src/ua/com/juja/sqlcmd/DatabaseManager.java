package ua.com.juja.sqlcmd;

import java.sql.*;
import java.util.Arrays;

public class DatabaseManager {

    private Connection connection;

    public static void main(String[] args) throws  ClassNotFoundException, SQLException{

        String database = "postgres";
        String login = "postgres";
        String password = "postgres";

        DatabaseManager manager = new DatabaseManager();

        manager.connect(database, login, password);

        Connection connection = manager.getConnection(database, login, password);

        //insert
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO users(name, surname, id) VALUES ('New', 'Friend', 101);");
        stmt.close();

        //select
        //ResultSet rs = manager.getTableNames();



        //select tableNames
        String[] tables = manager.getTableNames();

        //delete
        stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM users4delete WHERE id > 50 and id < 5");
        stmt.close();

        connection.close();

    }

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
                for(int i = 0; i < size; i++) {
                  dataSet.put(rsmd.getColumnName(i), rs.getObject(i));
                }
            }
            rs.close();
            stmt.close();

            return result;

        }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }

    private int getSize(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rsCount = stmt.executeQuery("SELECT COUNT (*) FROM public." + tableName);
        rsCount.next();
        int size = rsCount.getInt(1);
        rsCount.close();
        return size;
    }

    private Connection getConnection(String database, String login, String password) {
        return connection;
    }

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

}
