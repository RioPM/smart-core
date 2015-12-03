/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartcore;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 *
 * @author rio
 */

@Stateless
@Path("/devicesstate")
public class devicestate {
    
    @GET
    public Integer getdevicestate(@QueryParam("user") String user, @QueryParam("password") String password, @QueryParam("deviceID") Integer deviceID) throws SQLException {
        return $getdevicestate(user, password, deviceID);
    }
    Integer $getdevicestate(String user, String password, Integer deviceID) throws SQLException{
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Integer state = -1;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:/home/rio/Downloads/pti.sqlite");
            stmt = conn.createStatement();
            while(rs.next()){
                rs = stmt.executeQuery("Select state from users u, devices d where u.username = "+user+" and u.password = "+password+" and u.id = d.userid and d.id = "+deviceID+";"); // Verificar la validesa del token
                
                //Retrieve by column name
                state = rs.getInt("state");
            }
            conn.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(devicestate.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (SQLException ex) {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
        //state = 2;
        return state;
    }
    
    @POST
    public Integer setdevicestate(@FormParam("user") String user, @FormParam("password") String password, @FormParam("deviceID") Integer deviceID, @FormParam("newstate") Integer newstate) {
        return $setdevicestate(user, password, deviceID, newstate);
    }
    Integer $setdevicestate(String user, String password, Integer deviceID, Integer newstate){
        Connection conn = null;
        Statement stmt = null;
        
        Integer state = -1;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:/home/rio/Downloads/pti.sqlite");
            stmt = conn.createStatement();
           
            //rs = stmt.executeQuery("update from users u, devices d SET state = \""+ newstate +"\" where u.username = "+user+" and u.password = "+password+" and u.id = d.userid and d.id = "+deviceID+";"); // Verificar la validesa del token
            stmt.executeUpdate("UPDATE devices SET state = \""+ newstate +"\" where userid = 1 and id = "+deviceID+";");
                //Retrieve by column name
                
            ResultSet rs = stmt.executeQuery("select state as state from devices where id = \""+ deviceID +"\"");
            state = rs.getInt("state");
            conn.close();
             
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(devicestate.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (SQLException ex) {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return state;
    }
}
