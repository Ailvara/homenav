package database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseHandler 
{
    static String SQL  = "CREATE TABLE IF NOT EXISTS devices (id varchar(10) primary key, ip varchar(20), token varchar(20))";

    String url = "jdbc:sqlite:devices.db";
    String jdbc="org.sqlite.JDBC";
    String username = "admin";
    String password = "admin";
        
    private Connection connect(String jdbc, String url,String username,String password){
        Connection c=null;
        try {
            Class.forName(jdbc);
        } catch (Exception e) {
            return null;
        }

        try {
            c = DriverManager.getConnection (url, username, password);
        } catch (Exception e) {
        }
        return c;
    }

    public void prepareDatabase() 
    {
        Connection c=connect(jdbc,url,username,password);
        if(c==null) return;
        
        try {
            Statement s=c.createStatement();
            s.executeUpdate(SQL);
            s.close();
            c.close();
        } catch (Exception e) {
        }
    }
    
    public String insertToDatabase(String id, String ip, String token) 
    {
        try {
            Connection c=connect(jdbc,url,username,password);
            if(c==null) return "Connection failed";
            Statement s = c.createStatement();
            ResultSet r=s.executeQuery("SELECT token FROM devices WHERE id="+id);
            boolean result;
            
            if(r.next()){
                if(!r.getString(1).equals(token)) return "Token invalid";
                else result = update(c, id, ip);
            }
            else result = insert(c, id, ip, token);
            c.close();
            return ""+result;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return "SQL exception";
        }
    }
    
    private boolean insert(Connection c, String id, String ip, String token)
    {
        try {
            PreparedStatement ps = c.prepareStatement("INSERT INTO devices(id,ip,token) VALUES (?,?,?)");
            ps.setString(1,id);
            ps.setString(2,ip);
            ps.setString(3,token);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    private boolean update(Connection c, String id, String ip)
    {
        try {
            Statement s = c.createStatement();
            System.out.println("UPDATE devices SET ip = \""+ip+"\" WHERE id=\""+id+"\"");
            s.executeUpdate("UPDATE devices SET ip = \""+ip+"\" WHERE id=\""+id+"\"");
            s.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public String getFromDatabase(String id, String token)
    {
        try {
            Connection c=connect(jdbc,url,username,password);
            Statement s = c.createStatement();
            ResultSet r=s.executeQuery("SELECT ip, token FROM devices WHERE id="+id);
            
            if(r.next()){
                if(!r.getString(2).equals(token)) return "Token invalid";
                return(r.getString(1));
            }
            return "Not found";
        } catch (SQLException ex) {
            return "SQL exception";
        }
    }
}