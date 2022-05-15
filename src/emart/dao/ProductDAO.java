/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emart.dao;

import emart.dbutil.DBConnection;
import emart.pojo.ProductPojo;
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
public class ProductDAO {
    public static String getProductsId() throws SQLException{
        Connection conn  = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select max(productid) from products");
        rs.next();
        String productId = rs.getString(1); 
        if(productId == null) return "P101";
        int productNo = Integer.parseInt(productId.substring(1));
        productNo++;
        return "P" + productNo;
    }
    
    public static boolean addProduct(ProductPojo addProduct) throws SQLException{
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("insert into products values (?,?,?,?,?,?,?,'Y') ");
        ps.setString(1, addProduct.getProductId());
        ps.setString(2, addProduct.getProductName());
        ps.setString(3, addProduct.getProductCompanyName());
        ps.setDouble(4, addProduct.getProductPrice());
        ps.setDouble(5, addProduct.getOurPrice());
        ps.setInt(6, addProduct.getTax());
        ps.setInt(7, addProduct.getQuantity());
        return  ps.executeUpdate() ==1;
    }
    
    public static List<ProductPojo> getProductsDetail() throws SQLException{
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select * from products where status = 'Y' order by productid ");
        List<ProductPojo> productList = new ArrayList<>();
        while(rs.next()){
            ProductPojo p = new ProductPojo();
            p.setProductId(rs.getString(1));
            p.setProductName(rs.getString(2));
            p.setProductCompanyName(rs.getString(3));
            p.setProductPrice(rs.getDouble(4));
            p.setOurPrice(rs.getDouble(5));
            p.setTax(rs.getInt(6));
            p.setQuantity(rs.getInt(7));
            productList.add(p);
        }
        return productList;  
    }
    
    public static boolean deleteProduct(String pId) throws SQLException{
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("update products set status = 'N' where productid = ?");
        ps.setString(1, pId);
        return ps.executeUpdate() == 1;
    }
    
    public static boolean updateProduct(ProductPojo p) throws SQLException{
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("update products set productName= ?, productCompanyName = ?, productprice = ? , ourprice = ? , producttax = ?, quantity = ?  where productid = ?");
        ps.setString(1, p.getProductName());
        ps.setString(2, p.getProductCompanyName());
        ps.setDouble(3, p.getProductPrice());
        ps.setDouble(4, p.getOurPrice());
        ps.setInt(5, p.getTax());
        ps.setInt(6, p.getQuantity());
        ps.setString(7, p.getProductId());
        return ps.executeUpdate() ==1;         
    }
    
    public static ProductPojo getProductsDetail(String id)throws SQLException{
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("select * from products where productid = ? and status = 'Y' ");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        ProductPojo p = new ProductPojo();
        if(rs.next()){
            p.setProductId(rs.getString(1));
            p.setProductName(rs.getString(2));
            p.setProductCompanyName(rs.getString(3));
            p.setProductPrice(rs.getDouble(4));
            p.setOurPrice(rs.getDouble(5));
            p.setTax(rs.getInt(6));
            p.setQuantity(rs.getInt(7));
        }
        return p;
    }
    
    public static boolean updateStocks(List<ProductPojo> productsList)throws SQLException{
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("update products SET QUANTITY = QUANTITY - ? WHERE PRODUCTID = ? ");
        int rows = 0;
        for(ProductPojo p : productsList){
            ps.setInt(1, p.getQuantity());
            ps.setString(2, p.getProductId());
            int row = ps.executeUpdate();
            if(row != 0)
                rows++;
        }
        return rows == productsList.size();
        
    }
    
}
