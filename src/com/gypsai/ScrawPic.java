package com.gypsai;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScrawPic {

	public static ArrayList<String> urls;
	
	public static void main(String[] args) throws SQLException, IOException, InterruptedException
	{
		
		
		ScrawPic scp = new ScrawPic();
		urls = scp.getHostelUrls();
		
		String insert = "insert ignore into hostel_images(id,image_title,image_url_yha) values(?,?,?)";
		DB db = new DB(insert);
		
		
		
		for (int i = 0; i < urls.size(); i++) {
			
			Thread.sleep(1000);
			
			HashMap<String, String> images = new HashMap<String, String>();
			
			images = scp.getImages(urls.get(i));
			//存储返回的数据
			
			Iterator iter = images.entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry entry = (Map.Entry) iter.next(); 
			    Object key = entry.getKey(); 
			    Object val = entry.getValue(); 
			    
			    db.ps.setInt(1, i+1);
			    db.ps.setString(2, (String) key);
			    db.ps.setString(3, (String) val);
			    db.ps.execute();
			    
			} 
		}
		
		//db.ps.close();
		
	}
	
	//存储获取的数据
	public void store(HashMap<String, String> imageurl) throws SQLException
	{
		String insert = "insert into hostel(name,detail_url) values(?,?)";
		ArrayList<String> hostelurls = new ArrayList<String>(); 
		DB db = new DB(insert);
		
		ResultSet rs = db.getHostel();
		while (rs.next()) {
		//	System.out.println("http://www.yhachina.com"+rs.getString("detail_url"));
			hostelurls.add("http://www.yhachina.com"+rs.getString("detail_url")+"&a=3");
			
		}
		db.ps.close();
		
		
	}
	
	public HashMap<String, String> getImages(String url) throws IOException
	{
		String ua = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.4) Gecko/2008102920 Firefox/3.0.4";
		HashMap<String, String> images = new HashMap<String, String>();
		
		Boolean hasnext=true;
		int i=1;
		while(hasnext)
		{
			
			url=url+"&page="+i;
			Document doc = Jsoup.connect(url).userAgent(ua).get();
			
			Elements elements =   doc.getElementsByAttributeValueContaining("src", "160_120_pic");  //doc.getElementsByAttribute("target");
			
			
			//获取大图链接和题目
			for (Element e:elements) {
				
				String imagetitle = e.attr("title");
				//System.out.println(imagetitle);
				String imageurl = e.parent().getElementsByAttribute("href").get(0).attr("href");
				
				//System.out.println(e);
				
				imageurl = "http://www.yhachina.com/"+imageurl;
				Document doc2 = Jsoup.connect(imageurl).userAgent(ua).get();
				Elements elements2 = doc2.getElementsByAttributeValueContaining("title", imagetitle);
				
				String bigimage = elements2.get(0).attr("src");
				
				images.put(imagetitle, bigimage);
				
				System.out.println(imagetitle);
				System.out.println(bigimage);
			}
			
			i++;
			if(elements.size()==0)
			{
				hasnext=false;
				
			}
			
			
			
		}
		
		
		return images;
	}
	
	//先到数据库查
	public  ArrayList<String> getHostelUrls() throws SQLException
	{
		
		String insert = "insert into hostel_images(id,image_title,image_url_yha) values(?,?,?)";
		ArrayList<String> hostelurls = new ArrayList<String>(); 
		DB db = new DB(insert);
		ResultSet rs = db.getHostel();
		while (rs.next()) {
		//	System.out.println("http://www.yhachina.com"+rs.getString("detail_url"));
			hostelurls.add("http://www.yhachina.com"+rs.getString("detail_url")+"&a=3");
			
		}
		
		db.ps.close();
		
		return hostelurls;
	}
}
