/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emart.dao;

import emart.dbutil.DBConnection;
import emart.pojo.OrderPojo;
import emart.pojo.ProductPojo;
import emart.pojo.UserProfile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bhuwan Pandey
 */
public class OrderDAO {
    public static String getNextOrderId()throws SQLException{
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select max(orderid) from orders");
        rs.next();
        String id = rs.getString(1);
        if(id == null){
            return "O-101";
        }
        int orderNo = Integer.parseInt(id.substring(2));
        orderNo++;
        return "O-" + orderNo;
    }
    
    public static boolean addOrder(List<ProductPojo> al, String id)throws SQLException{
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("Insert into orders values (?,?,?,?) ");
        int count = 0;
        for(ProductPojo p : al){
            ps.setString(1, id);
            ps.setString(2, p.getProductId());
            ps.setInt(3, p.getQuantity());
            ps.setString(4, UserProfile.getUserId());
            count += ps.executeUpdate();
        }
        return count == al.size();
    }
    
    public static List<OrderPojo> viewAllOrders()throws SQLException{
        Connection conn  = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("Select * from orders");
        List<OrderPojo> al = new ArrayList<>();
        while(rs.next()){
            OrderPojo order= new OrderPojo();
            order.setOrderId(rs.getString(1));
            order.setProductId(rs.getString(2));
            order.setQuantity(rs.getInt(3));
            order.setUserId(rs.getString(4));
            al.add(order);
        }
        return al;
    }
    
    public static List<OrderPojo> viewUserOrders(String userId)throws SQLException{
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("select * from orders where userid = ? ");
        ps.setString(1, userId);
        ResultSet rs = ps.executeQuery();
        List<OrderPojo> al = new ArrayList<>();
        while(rs.next()){
            OrderPojo order = new OrderPojo();
            order.setOrderId(rs.getString(1));
            order.setProductId(rs.getString(2));
            order.setQuantity(rs.getInt(3));
            order.setUserId(rs.getString(4));
            al.add(order);
        }
        return al;  
    }
}
