package com.gypsai;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.ResultSet;

public class DB {

	public java.sql.Statement statement;
	public java.sql.ResultSet rs ;
	public java.sql.PreparedStatement ps;
    public	Connection conn;
	public String sql;
	
	public DB(String sql)
	{
		
		String driver = "com.mysql.jdbc.Driver";
		
		String url = "jdbc:mysql://127.0.0.1:3306/hostel";
		
		String user = "root";
		String password = "";
		
		//System.out.println(sql);
		//sql = this.sql;
		
		try {
			
			
			Class.forName(driver);
			 conn = DriverManager.getConnection(url, user, password);

			if(!conn.isClosed())
			{
			
			//	System.out.println("Succeeded connecting to the Database!");

				
			}else {
				
			//	System.out.println("Failed connecting to the Database!");

			}
			
			
			
			ps = conn.prepareStatement(sql);
			
			// statement用来执行SQL语句

			//statement = conn.createStatement();
			//ps = conn.prepareStatement(sql);
			// 要执行的SQL语句

			
			
			/*
			String name = null;  
			while (rs.next()) {
				name = rs.getString("name");
				System.out.println(rs.getString("id") + "\t" + name);  
			}
			*/
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
		
		
	}	
		
	
	
	public boolean insert(String rt)
	{
		try {
			//return statement.execute(sql);
			
			ps = conn.prepareStatement(sql);
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public java.sql.ResultSet getHostel() throws SQLException
	{
		java.sql.ResultSet rs;
		String sql = "select * from hostel";
		ps=conn.prepareStatement(sql);
		rs=ps.executeQuery(); 
		
		
		
		return rs;
		
	}
	
public  static void main(String[] args) {
		
}
	
}
