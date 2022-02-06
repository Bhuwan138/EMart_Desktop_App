/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emart.dao;

import emart.dbutil.DBConnection;
import emart.pojo.EmployeesPojo;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Bhuwan Pandey
 */
public class EmployeesDoa {
    //to generate employeeId

    /**
     *
     * @return
     * @throws SQLException
     */
    public static String getEmployeeId() throws SQLException{
        Connection conn  = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select max(empid) from employees");
        rs.next();
        String empId = rs.getString(1); 
        int empNo = Integer.parseInt(empId.substring(1));
        empNo++;
        return "E" + empNo;
    }
    
     //to add employee
    public static boolean addEmployee(EmployeesPojo addEmp) throws SQLException{
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("insert into employees values(?,?,?,?)");
        ps.setString(1, addEmp.getEmployeeId());
        ps.setString(2, addEmp.getEmployeeName());
        ps.setString(3, addEmp.getJob());
        ps.setDouble(4, addEmp.getSalary());
        int result = ps.executeUpdate();
        return result == 1;
    }
    
    public static List<EmployeesPojo> getAllEmployee()throws SQLException{
        Connection conn  = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs  =  st.executeQuery("select * from employees");
        List<EmployeesPojo> emp = new ArrayList<>();
        while(rs.next()){
            EmployeesPojo empList = new EmployeesPojo();
            empList.setEmployeeId(rs.getString(1));
            empList.setEmployeeName(rs.getString(2));
            empList.setJob(rs.getString(3));
            empList.setSalary(rs.getDouble(4));
            emp.add(empList);
        }    
        return emp;
    }
}
