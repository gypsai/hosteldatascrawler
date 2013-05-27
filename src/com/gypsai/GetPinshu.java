package com.gypsai;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetPinshu {

	public static void main(String[] args)
	{
		
		String baseUrl = "http://download.pingshu8.com:8000/3/ys/%E4%BA%94%E4%BB%A3%E5%8D%81%E5%9B%BD%E9%A3%8E%E4%BA%91%E5%BD%95/%E3%80%8A%E4%BA%94%E4%BB%A3%E5%8D%81%E5%9B%BD%E9%A3%8E%E4%BA%91%E5%BD%95%E3%80%8B%E5%8D%81%E5%9B%BD%E7%AF%8701.mp3?115021268194376x1367134161x115021273973118-1ac7d8f7052b5d32d7697922eab50a53";
	String reg = "\\d{2}.mp3";
		
	//01.mp3
		
	String[] strings = {"01","02","03","04","05","06","07","08","09","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",};
		Pattern pattern = Pattern.compile(reg);
		
		Matcher matcher = pattern.matcher(baseUrl);
		
		if(matcher.find())
		{
			for (int i = 1; i < 34; i++) {
				String str;
				if(i<=9)
				{
					str = "0"+i+".mp3";
				}else {
					
					str = i+".mp3";
				}
				System.out.println( matcher.replaceAll(str));	
			}
			
		 // System.out.println("rst:"+matcher.group(0));
		}else {
			System.out.println("Not Found  info");
		}
	}
	
}
