package test.com.ai.paas.ipaas.idps;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchType;
import org.junit.Test;

import com.ai.paas.ipaas.image.IImageClient;
import com.ai.paas.ipaas.image.ImageClientFactory;
import com.ai.paas.ipaas.uac.vo.AuthDescriptor;
import com.google.gson.Gson;

public class ImageTest {
	private static Gson gson = new Gson();
	
	private static final String AUTH_ADDR = "http://10.1.228.198:14821/iPaas-Auth/service/auth";
	private static AuthDescriptor ad = null;
	private static IImageClient im = null;
	
	
	
	static{
		ad =  new AuthDescriptor(AUTH_ADDR, "FFF49D0D518948D0AB28D7A8EEE25D03", "111111","IDPS023");
		try {
			im = ImageClientFactory.getSearchClient(ad);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void deTest() {
		
//		System.out.println(im.getUpImageUrl());
		byte[] buffer = null;  
        try {  
        	File file = new File("C:\\Users\\Administrator.5IVEXBVEXVTUU2T\\Desktop\\011ca5914f6fdd0d0db345e0.jpg");
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
		String id = im.upLoadImage(buffer, "011ca5914f6fdd0d0db345e0.jpg");
		
		System.out.println(id);
	}
	
	@Test
	public void seTest() {
		System.out.println(im.getImageUrl("56aa08347960b60009db8a3d", ".jpg"));
		
	}
	
	@Test
	public void downTest() {
		
	    im.downloadImage("56aa08347960b60009db8a3d", ".jpg", "");
		System.out.println(im.getImageUrl("56aa08347960b60009db8a3d", ".jpg"));
		
	}
	
}
