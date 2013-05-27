package com.gypsai;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetPics {
	
	public static void main(String[] args) throws Exception {
		
		GetPics gp = new GetPics();
		
        String url = "http://www.163.com";
        Document doc = Jsoup.connect(url).timeout(999999999).get();
        Elements links = doc.select("[src]");
        
        int count=0;
        File file;
        URLConnection uc;
        
        ArrayList<String> urls = gp.getPicUrls();
        
        for(int i=0;i<urls.size();i++)
        {
            
           count++;
           uc=new URL(urls.get(i)).openConnection();
           //System.out.println(urls.get(i));
           SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddss",Locale.SIMPLIFIED_CHINESE);
           String filename = sdf.format(new Date())+count;
           filename = MD5.MD5Encode(filename);
           
           gp.updatefilename(filename, count);
           
           BufferedOutputStream bout=new BufferedOutputStream(new FileOutputStream(new File("/Users/gypsai/Developer/hostelpics/"+filename+".jpg")));
           BufferedInputStream bin=new BufferedInputStream(uc.getInputStream());
           byte[] b=new byte[1];
           
           while(bin.read(b)!=-1)
           {
            bout.write(b);
           }
           
           System.out.println("已完成:"+count+"个.");
           bout.flush();
           bin.close();
           bout.close();          
          }
        
        System.out.println("完成！");
    }
	
	public void updatefilename(String filename,int imageid) throws SQLException {
		String sql = "update hostel_images set image_url=? where imageid=?";
		
		DB db = new DB(sql);
		
		db.ps.setInt(2, imageid);
		db.ps.setString(1, filename);
		
	    db.ps.executeUpdate();
		
		db.ps.close();
		
	}

	
	
	public  ArrayList<String> getPicUrls() throws SQLException
	{
		
		String sql = "select * from hostel_images order by imageid";
		ArrayList<String> hostelurls = new ArrayList<String>(); 
		DB db = new DB(sql);
		
		
		ResultSet rs = db.ps.executeQuery();
		
		while (rs.next()) {
		//	System.out.println("http://www.yhachina.com"+rs.getString("detail_url"));
			hostelurls.add("http://www.yhachina.com"+rs.getString("image_url_yha"));
			
		}
		
		db.ps.close();
		
		return hostelurls;
	}

}
