package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JOptionPane;
 
public class DBMananger 
{ 
    private Connection connection;
    private Statement stmt;
    
   public void createConnection(String database, String user, String pswd)
   {
	System.out.println("-------- MySQL JDBC Connection Testing ------------");
        
	try 
        {
            Class.forName("com.mysql.jdbc.Driver");
	} 
        catch (ClassNotFoundException e) 
        {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return;
	}

	System.out.println("MySQL JDBC Driver Registered!");
	connection = null;

	try 
        {
            connection = DriverManager.getConnection(database,user,pswd);
        } 
        catch (SQLException e) 
        {
            JOptionPane.showMessageDialog(null, "Connection failed", "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
	}

	if (connection != null) 
        {
            System.out.println("You made it, take control your database now!");
	} 
        else 
        {
            System.out.println("Failed to make connection!");
            return;
	}
        
        createStatement();
   }
   
   private void createStatement()
   {
       System.out.println("Creating statement...");
       
       try 
       {
            stmt = connection.createStatement();
       }
       catch (SQLException e) 
       {
            System.out.println("Cannot create statement.");
            e.printStackTrace();
            return;
        }
   }
   
   public Vector getColumnNames(String query) 
   {
        Vector columnNames = new Vector();
       
        try 
        {
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();

            int max = rsmd.getColumnCount();
            for (int i=1;i<=max;i++)
            {
                columnNames.add(rsmd.getColumnName(i));
            }
        }
       catch (SQLException e) 
       {
           e.printStackTrace();
       }
        
       return columnNames;
   }
   
   
   public Vector getTable(String query) 
   {
        Vector columnNames = getColumnNames(query);
        Vector table = new Vector();
        
        try 
        {
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next())
            {
                Vector row = new Vector();
                for (int i=0; i<columnNames.size(); i++)
                {
                    String text  = rs.getString(columnNames.get(i).toString());
                    row.add(text);
                }
                table.add(row);
            }
        }
       catch (SQLException e) 
       {
           e.printStackTrace();
       }
        
       System.out.println(query);
        
       return table;
   }
   
   public void deleteRow(String tableName, String columnName, String id)
   {
       try
       {
            String text = "delete from "+tableName+" where "+columnName+"='"+id+"'";
            stmt.executeUpdate(text);
            
            System.out.println(text);
       }
       catch (SQLException e)
       {
            e.printStackTrace();
       }
   }

   public void insertRow(String tableName, Vector newValues)
   {
       try
       {
           String text="insert into "+tableName+" values('";
           
            for (Object newText: newValues)
            {
                if (newText == "") {
                    text = text.substring(0, text.length() - 1);
                    text += "NULL,'";
                }
                else {
                    text += newText + "','";
                }
            }
            text = text.substring(0, text.length()-2);
            text += ")";
            
            stmt.executeUpdate(text);
            
            System.out.println(text);
       }
       catch (SQLException e)
       {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cannot add new record! Please insert correct data.", "Error", JOptionPane.INFORMATION_MESSAGE);
       }
   }
   
    public String executeQuery(String query, String columnName) {
        String result = "";

        try {
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            result = rs.getString(columnName);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(query);
        
        return result;
    }

    public void startTransaction() {
        try {
            stmt.executeUpdate("BEGIN");
            System.out.println("BEGIN");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void endTransaction() {
        try {
            stmt.executeUpdate("COMMIT");
            System.out.println("COMMIT");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


         
