/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emart.dao;

import emart.dbutil.DBConnection;
import emart.pojo.ReceptionistPojo;
import emart.pojo.UserPojo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Bhuwan Pandey
 */
public class ReceptionistDoa {
    public static Map<String, String> getNonRegisteredReceptionist() throws SQLException{
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("Select empid,empname from employees where job = 'Receptionist' and empid not in (select empid from users where usertype='Receptionist') ");
        Map<String,String> nonRegId =new HashMap<>();
        while(rs.next()){
            String id = rs.getString(1);
            String name = rs.getString(2);
            nonRegId.put(id, name);
        }
        return nonRegId;
    }
    
    public static boolean addReceptionist(UserPojo user)throws SQLException{
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("insert into users values(?,?,?,?,?)");
        ps.setString(1, user.getUserId());
        ps.setString(2, user.getEmpId());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getUserType());
        ps.setString(5, user.getUserName());
        int result = ps.executeUpdate();
        return result == 1;
    } 
    
    public static List<ReceptionistPojo> findAllReceptionist() throws SQLException{
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select users.empid,empname,userid,job,salary from users,employees where usertype = 'Receptionist' and users.empid = employees.empid");
        List<ReceptionistPojo> allEmp = new ArrayList<>();
        while(rs.next()){
            ReceptionistPojo rp = new ReceptionistPojo();
            rp.setEmpId(rs.getString(1));
            rp.setEmpName(rs.getString(2));
            rp.setUserId(rs.getString(3));
            rp.setJob(rs.getString(4));
            rp.setSalary(rs.getDouble(5));
            allEmp.add(rp);
        }
        return allEmp;
    }
}
