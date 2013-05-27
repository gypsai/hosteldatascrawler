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

public class Scraw {
	
	
	
	
	public static void main(String[] arg) throws SQLException
	{
		
		String ua = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.4) Gecko/2008102920 Firefox/3.0.4";
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> url = new ArrayList<String>();
		
		
		try {
			
			Document doc = Jsoup.connect("http://www.yhachina.com/topic.php?channelID=10&topicID=32").userAgent(ua).timeout(100000000).get();
			Elements elements =   doc.getElementsByAttributeValueContaining("href", "ls.php");  //doc.getElementsByAttribute("target");
			
			for (Element element : elements) {
				String na =  element.text();
				String ul = element.attr("href");
				
				name.add(na);
				url.add(ul);	
			}
			Elements elements2 = doc.getElementsByAttribute("rowspan");
			ArrayList<HashMap<String, Integer>> province = new ArrayList<HashMap<String,Integer>>();
			
			System.out.println(elements2);
			for (int i=1;i<elements2.size();i++) {
				Element e = elements2.get(i);
				String num = e.attr("rowspan");
				String prov = e.text();
				HashMap<String, Integer> a = new HashMap<String, Integer>();
				a.put(prov, Integer.parseInt(num));
				province.add(a);
				
				//System.out.printf("update hostel_detail set province='香港' where id>225 and  id<=227;", prov);
			//	System.out.printf("%s 有 %s \n",prov,num);
			}
			
			int cousor = 0;
			HashMap<String, Integer> b = new HashMap<String, Integer>();
			b.put("湖北", 1);
			province.add(10, b);
			
			HashMap<String, Integer> c = new HashMap<String, Integer>();
			c.put("吉林", 1);
			province.add(12, c);
			
			for (int i = 0; i < province.size(); i++) {
				
				HashMap<String, Integer> p = new HashMap<String, Integer>();
				p=province.get(i);
				Iterator iter = p.entrySet().iterator(); 
				
				while (iter.hasNext()) { 
				    Map.Entry entry = (Map.Entry) iter.next(); 
				    Object key = entry.getKey(); 
				    Integer val = (Integer)entry.getValue(); 
				    System.out.printf("update hostel_detail set province='%s' where id>%d and  id<=%d;\n", key,cousor,cousor+val);
				    cousor = cousor+val;
				    System.out.printf("省份%d :%s 有 %s 总：%d \n",i+1,key,val,cousor);
				    
				}
			}
			
			//System.out.printf("%d",elements.size());
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		String insert = "insert into hostel(name,detail_url) values(?,?)";
		
		DB db = new DB(insert);
		//String[] sql;
		
		for (int i = 0; i < name.size(); i++) {
			
			try {
				//System.out.printf("datais: %s", name.get(i));
				
				db.ps.setString(1, name.get(i));
				db.ps.setString(2, url.get(i));
				//db.ps.execute();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	//	db.insert();
		
		// gethostel
		ResultSet rs = db.getHostel();
		while (rs.next()) {
			//System.out.println(rs.getString("name"));
		}
		db.ps.close();
		
		
		
	}
	
	
	


}
