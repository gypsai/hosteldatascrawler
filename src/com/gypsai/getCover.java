package com.gypsai;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class getCover {
	
public static ArrayList<String> urls;
	
	public static void main(String[] args) throws SQLException, IOException, InterruptedException
	{
		
		getCover gc = new getCover();
		urls = gc.getHostelUrls();
	
		
		for (int i = 176; i < urls.size(); i++) {
			
			String coverimage = new String();
			coverimage = gc.getDetail(urls.get(i));
			
			String filename = gc.downloadCover(coverimage);
			
			gc.updatecover(filename, i+1);
			
			System.out.println(filename+"  µÚ"+i+"¸ö");
			
			Thread.sleep(500);
			
		//	scd.store(detail);
		}
		
		
	}
	
	
	public void updatecover(String filename,int id) throws SQLException
	{
		
		
		String sql = "update hostel_detail set cover=? where id=?";
		
		DB db = new DB(sql);
		
		db.ps.setInt(2, id);
		db.ps.setString(1, filename);
		
	    db.ps.executeUpdate();
		
		db.ps.close();
		
	}
	
	
	public  ArrayList<String> getHostelUrls() throws SQLException
	{
		
		String insert = "insert into hostel(name,detail_url) values(?,?)";
		ArrayList<String> hostelurls = new ArrayList<String>(); 
		DB db = new DB(insert);
		
		ResultSet rs = db.getHostel();
		while (rs.next()) {
		//	System.out.println("http://www.yhachina.com"+rs.getString("detail_url"));
			hostelurls.add("http://www.yhachina.com"+rs.getString("detail_url"));
			
		}
		db.ps.close();
		
		return hostelurls;
	}
	
	   
	public String downloadCover(String picurl) throws MalformedURLException, IOException
	{
		
		 int count=0;
	        File file;
	        URLConnection uc;
	        
		uc=new URL(picurl).openConnection();
        //System.out.println(urls.get(i));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddss",Locale.SIMPLIFIED_CHINESE);
        String filename = sdf.format(new Date())+count;
        filename = MD5.MD5Encode(filename+picurl);
        
        BufferedOutputStream bout=new BufferedOutputStream(new FileOutputStream(new File("/Users/gypsai/Developer/hostelcover/"+filename+".jpg")));
        BufferedInputStream bin=new BufferedInputStream(uc.getInputStream());
        byte[] b=new byte[1];
        
        while(bin.read(b)!=-1)
        {
         bout.write(b);
        }
        
        
        bout.flush();
        bin.close();
        bout.close();          
     
        		
		return filename+".jpg";
	}
	
	public String getDetail(String url) throws IOException
	{
		
		String ua = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.4) Gecko/2008102920 Firefox/3.0.4";
		
		Document doc = Jsoup.connect(url).userAgent(ua).timeout(100000).get();
		
		Elements elements =   doc.getElementsByAttributeValueContaining("src", "home__");  //doc.getElementsByAttribute("target");
		System.out.println(url+"\n");
		System.out.println(elements);
		
		
		String picurl = elements.get(0).attr("src");
		picurl = "http://www.yhachina.com/"+picurl;
		
		
		return picurl;
	}
	

}
