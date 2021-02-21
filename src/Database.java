import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {

    //name of database
    private String name;

    //DB credentials
    private String username;
    private String password;

    //connection to database
    private Connection conn = null;
    private Statement statement;



    Database(String name){
        this.name = name;
    }

    public void connectToDatabase(){
        try{
            this.conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + this.name);
            this.conn.setAutoCommit(false);
            this.statement = this.conn.createStatement();
        } catch (Exception e){
            e.printStackTrace();
            this.printErrors(e);
        }
        System.out.println("Opened Database Successfully");
    }

    public void createTable(String name){
        try{
            this.statement.executeUpdate("CREATE TABLE " + name + "(dummy INTEGER NOT NULL PRIMARY KEY);\n" +
            "INSERT INTO " + name + "(dummy) VALUES (1);\n" +
            "ALTER TABLE " + name + "\n" +
            "DROP COLUMN dummy;");
            this.conn.commit();
            System.out.println("Table " + name + " was created!");
        } catch(Exception e){
            this.printErrors(e);
        }
    }

    public void dropTable(String name){
        try{
            this.statement.executeUpdate("DROP TABLE " + name + ";");
            System.out.println("Table " + name + " was dropped!");
        } catch(Exception e){
            this.printErrors(e);
        }
    }

    public void closeConnection(){
        try{
            this.statement.close();
            this.conn.close();
            System.out.println("Database closed successfully");
        }catch(Exception e){
            this.printErrors(e);
        }
    }

    public String getTableList(){
        String tables = new String("");
        try {
            ResultSet results = this.statement.executeQuery("SELECT table_name\n" +
                    "FROM information_schema.tables\n" +
                    "WHERE table_schema = 'public'\n" +
                    "ORDER BY table_name;");
            if(!results.next()){
                return "No tables in database";
            }else{
                while (results.next()){
                    System.out.println(results.getString("table_name"));
                    tables += results.getString("table_name") + "\n";
                }
                return tables;
            }
        }catch (Exception e){
            this.printErrors(e);
            return "There was an error with the database";
        }
    }

    //private method to print relevant errors
    private void printErrors(Exception e){
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        System.exit(0);
    }

}
