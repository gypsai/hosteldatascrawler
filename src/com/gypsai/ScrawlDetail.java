package com.gypsai;

import java.io.IOException;
import java.net.URLStreamHandler;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScrawlDetail {
	
	
	public static ArrayList<String> urls;
	
	public static void main(String[] args) throws SQLException, IOException, InterruptedException
	{
		
		ScrawlDetail scd = new ScrawlDetail();
		urls = scd.getHostelUrls();
		/*
		for (String url:urls) {
			scd.getDetail(url);
		}*/
		for (int i = 0; i < urls.size(); i++) {
			
			HashMap<String, String> detail = new HashMap<String, String>();
			detail = scd.getDetail(urls.get(i));
			
			Thread.sleep(500);
			
			scd.store(detail);
		}
		
		
	}
	
	
	public void store(HashMap<String, String> a) throws SQLException
	{
		
		String sql = "insert into hostel_detail (name,address,tel,email," +
				"fax,postcode,website,brief_introduction,facilities,airport," +
				"trainsport,busstation,subtrain,pier) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		DB db = new DB(sql);
		db.ps.setString(1, a.get("name"));
		db.ps.setString(2, a.get("address"));
		db.ps.setString(3, a.get("tel"));
		db.ps.setString(4, a.get("email"));
		db.ps.setString(5, a.get("fox"));
		db.ps.setString(6, a.get("postcode"));
		db.ps.setString(7, a.get("website"));
		db.ps.setString(8, a.get("brief_introduction"));
		db.ps.setString(9, a.get("facilities"));
		db.ps.setString(10, a.get("airport"));
		db.ps.setString(11, a.get("train"));
		db.ps.setString(12, a.get("bus"));
		db.ps.setString(13, a.get("underground"));
		db.ps.setString(14, a.get("port"));
		
		db.ps.execute();
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
	
	   
	
	
	public HashMap<String, String> getDetail(String url) throws IOException
	{
		
		String ua = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.4) Gecko/2008102920 Firefox/3.0.4";
		HashMap<String, String> detail = new HashMap<String, String>();
		
		Document doc = Jsoup.connect(url).userAgent(ua).get();
		
		Elements elements =   doc.getElementsByAttributeValueContaining("src", "images/hostel/i_t2.gif");  //doc.getElementsByAttribute("target");
			
		Element e = elements.get(0);
		e = e.parent().parent();
		
		//获取所有的tr
		Elements trs = e.siblingElements();
		
		//名字
		detail.put("name", trs.get(0).text().replace(Jsoup.parse("&nbsp;").text(), " "));
		
		//地址
		detail.put("address", trs.get(2).text().replace(Jsoup.parse("&nbsp;").text(), " "));
		
		//tel
		detail.put("tel",trs.get(4).child(0).text().replace(Jsoup.parse("&nbsp;").text(), " "));
		
		
		//email
		detail.put("email", trs.get(4).child(1).text().replace(Jsoup.parse("&nbsp;").text(), " "));
		
		//fox
		detail.put("fox",trs.get(6).child(0).text().replace(Jsoup.parse("&nbsp;").text(), " "));
		
		
		//postcode
		detail.put("postcode",trs.get(6).child(1).text().replace(Jsoup.parse("&nbsp;").text(), " ") );
		
		//website
		detail.put("website", trs.get(8).child(0).child(0).text().replace(Jsoup.parse("&nbsp;").text(), " "));
		
		//简介
		Elements elements2 =   doc.getElementsByAttributeValueContaining("src", "images/hostel/i_t0.gif");  //doc.getElementsByAttribute("target");
		Element e2 = elements2.get(0).parent().parent().siblingElements().get(0);
		
		detail.put("brief_introduction", e2.text().replace(Jsoup.parse("&nbsp;").text(), " "));
		
		
		//facilities
		Elements elements3 =   doc.getElementsByAttributeValueContaining("src", "images/hostel/i_t1.gif");  //doc.getElementsByAttribute("target");
		Element e3 = elements3.get(0).parent().parent().siblingElements().get(0);
		detail.put("facilities", e3.text().replace(Jsoup.parse("&nbsp;").text(), " "));
		
		//airport
		Elements elements4 =   doc.getElementsByAttributeValueContaining("src", "images/hostel/i_airport.gif");  //doc.getElementsByAttribute("target");
		Element e4 = elements4.get(0).parent().siblingElements().get(0);
		detail.put("airport", e4.text().replace(Jsoup.parse("&nbsp;").text(), " "));
		
		//train
		Elements elements5 =   doc.getElementsByAttributeValueContaining("src", "images/hostel/i_train.gif");  //doc.getElementsByAttribute("target");
		Element e5 = elements5.get(0).parent().siblingElements().get(0);
		detail.put("train", e5.text().replace(Jsoup.parse("&nbsp;").text(), " "));
		
		
		//bus
		Elements elements6 =   doc.getElementsByAttributeValueContaining("src", "images/hostel/i_bus.gif");  //doc.getElementsByAttribute("target");
		Element e6 = elements6.get(0).parent().siblingElements().get(0);
		detail.put("bus", e6.text().replace(Jsoup.parse("&nbsp;").text(), " "));
		
		//port
		Elements elements7 =   doc.getElementsByAttributeValueContaining("src", "images/hostel/i_port.gif");  //doc.getElementsByAttribute("target");
		Element e7 = elements7.get(0).parent().siblingElements().get(0);
		detail.put("port", e7.text().replace(Jsoup.parse("&nbsp;").text(), " "));
		
		//subtrain
		Elements elements8 =   doc.getElementsByAttributeValueContaining("src", "images/hostel/i_underground.gif");  //doc.getElementsByAttribute("target");
		Element e8 = elements8.get(0).parent().siblingElements().get(0);
		detail.put("port", e8.text().replace(Jsoup.parse("&nbsp;").text(), " "));
		
		//System.out.println(e8.text());

		
		Iterator<String> iter = detail.keySet().iterator(); 
		while (iter.hasNext()) { 
		    Object key = iter.next(); 
		    Object val = detail.get(key); 
		    
		    System.out.println(key);
		    System.out.println(val);
		} 
		
		return detail;
	}
	

}
