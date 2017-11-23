package ua.com.juja.sqlcmd;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


import java.sql.Connection;
import java.util.Arrays;

public class DataBaseManagerTest {

    private DatabaseManager manager;
    @Before
    public void setup(){
        manager = new DatabaseManager();
        manager.connect("postgres", "postgres", "postgres");
    }

    @Test
    public void testGetAllTableNames(){
        String[] tableNames = manager.getTableNames();
        assertEquals("[users]", Arrays.toString(tableNames));
    }

}
