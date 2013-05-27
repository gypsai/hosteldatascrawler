package com.gypsai;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.print.Doc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class GetLoc {

	public static void main(String args[]) throws IOException, SQLException
	{
		
		
		getCover gc = new getCover();
		ArrayList<String> urls = gc.getHostelUrls();
		
		for (int i = 0; i < urls.size(); i++) {
			
			String mapurl = urls.get(i)+"&a=4"+"\n";
			System.out.print(mapurl);
			
			HashMap<String, String> gpsinfo = patternGPS(mapurl);
			
			if (gpsinfo!=null) {
				storeGPS(gpsinfo,i+1);
			}
			System.out.println("完成第"+i+"个");
			
		}
		System.out.println("Finish");
		
	}
	
	public static HashMap<String, String> patternGPS(String html) throws IOException {
		
		String url =  "http://www.yhachina.com/ls.php?id=178&a=4";
		String ua = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.4) Gecko/2008102920 Firefox/3.0.4";
		
		Document doc = Jsoup.connect(html).userAgent(ua).timeout(1000000).get();
		
		
		String reg = "GLatLng\\((\\d+\\.\\d+),(\\d+\\.\\d+)\\)";
		
		
		//String reg = "[a-zA-Z0-9_-]+@\\w+\\.[a-z]+(\\.[a-z]+)?"; 
		//String reg = "\\(\\d+\\)";
		Pattern pattern = Pattern.compile(reg);
		//String test = "map.setCenter(new GLatLng(39.934879,116.406375), 17);";
		//String test = "asdf(027) 123-1235asdf xxgypsai@foxmail.com)";
		
		Matcher matcher = pattern.matcher(doc.toString());
		
		if(matcher.find())
		{
		  System.out.println("rst:"+matcher.group(0)+matcher.group(1)+matcher.group(2));
		//  String[] b = matcher.group().split(",");
		  HashMap<String, String> gpsinfo  = new HashMap<String, String>();
		  gpsinfo.put("lat", matcher.group(1));
		  gpsinfo.put("lng", matcher.group(2));
		  return gpsinfo;
		//  System.out.print(b);
		}else {
			System.out.println("Not Found GPS info");
		}
		return null;
		
	}
	
	public static void storeGPS(HashMap<String, String> gpsinfo,int id) throws NumberFormatException, SQLException {
		
			
			String sql = "update hostel_detail set gps_lat=?,gps_lng=? where id=?";
			
			DB db = new DB(sql);
			
			db.ps.setFloat(1, Float.parseFloat(gpsinfo.get("lat")));
			db.ps.setFloat(2, Float.parseFloat(gpsinfo.get("lng")));
			db.ps.setInt(3, id);
	
		    db.ps.executeUpdate();
			
			db.ps.close();
			
		
	}
	}
