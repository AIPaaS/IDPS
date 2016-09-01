package test.com.ai.paas.ipaas.idps;

import org.junit.Test;

import com.ai.paas.ipaas.image.IImageClient;
import com.ai.paas.ipaas.image.ImageCmpFactory;

public class ImageTest {
	private static String imageUrl = "http://127.0.0.1:8080/idps-web";
	private static String mongoInfo = "{\"mongoServer\":\"10.1.245.226:37037\",\"database\":\"image\",\"userName\":\"idps\",\"password\":\"idps\",\"bucket\":\"fs\"}";
//	private static IImageClient im = null;
	
//	@Test
//	public void deTest() {
//		// System.out.println(im.getUpImageUrl());
//		byte[] buffer = null;
//		try {
//			File file = new File("/Users/yuanman/Documents/111.png");
//			FileInputStream fis = new FileInputStream(file);
//			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
//			byte[] b = new byte[1000];
//			int n;
//			while ((n = fis.read(b)) != -1) {
//				bos.write(b, 0, n);
//			}
//			fis.close();
//			bos.close();
//			buffer = bos.toByteArray();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			ImageCmpFactory factory = new ImageCmpFactory(imageUrl, mongoInfo, false);
//			IImageClient im = factory.getClient();
//			String id = im.upLoadImage(buffer, "111.png");
//			System.out.println(id);
//		}catch(Exception ex) {
//			ex.printStackTrace();
//		}
//	}

//	@Test
//	public void seTest() {
//		try {
//			ImageCmpFactory factory = new ImageCmpFactory(imageUrl, mongoInfo);
//			IImageClient im = factory.getClient();
//			System.out.println(im.getImageUrl("57c55683b1c3aa3c8ccdaaca", ".jpg"));
//		}catch(Exception ex) {
//			ex.printStackTrace();
//		}
//	}
	
	@Test
	public void deleteTest() {
		try {
			ImageCmpFactory factory = new ImageCmpFactory(imageUrl, mongoInfo);
			IImageClient im = factory.getClient();
			System.out.println(im.deleteImage("57c55683b1c3aa3c8ccdaaca"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
//	@Test
//	public void downTest() {
//		im.getImage("56aa08347960b60009db8a3d", ".jpg", "");
//		System.out.println(im.getImageUrl("56aa08347960b60009db8a3d", ".jpg"));
//	}

}
