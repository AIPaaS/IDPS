package test.com.ai.paas.ipaas.idps;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import com.ai.paas.ipaas.image.IImageClient;
import com.ai.paas.ipaas.image.ImageCmpFactory;

public class ImageTest {
	private static String imageUrl = "http://10.1.245.226:18000/iPaas-IDPS/";
	//private static String imageUrl = "http://127.0.0.1:8080/idps-web/";
	

	@Test
	public void uploadImageTest() {
		byte[] buffer = null;
		try {
			File file = new File("d:/003.jpg");
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

		try {
			IImageClient im = ImageCmpFactory.getClient(imageUrl, imageUrl);
			String id = im.upLoadImage(buffer, "666.jpg");
			System.out.println("========= " + id + "==========");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// @Test
	// public void getImageUrlTest() {
	// try {
	// ImageCmpFactory factory = new ImageCmpFactory(imageUrl);
	// IImageClient im = factory.getClient();
	// System.out.println(im.getImageUrl("57ce9335b1c3aa54ce707ef8", ".jpg"));
	// /**
	// *
	// http://127.0.0.1:8080/idps-web/image/57c7c9deb1c3aa4a297e30a1.jpg?needAuth=false&mongoInfo={"mongoServer":"10.1.245.226:37037","database":"image","userName":"idps","password":"idps","bucket":"fs"}
	// */
	// }catch(Exception ex) {
	// ex.printStackTrace();
	// }
	// }

	// @Test
	// public void deleteImageTest() {
	// try {
	// ImageCmpFactory factory = new ImageCmpFactory(imageUrl);
	// IImageClient im = factory.getClient();
	// System.out.println(im.deleteImage("57ce9335b1c3aa54ce707ef8"));
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// }

	// @Test
	// public void downloadTest() {
	// try {
	// ImageCmpFactory factory = new ImageCmpFactory(imageUrl);
	// IImageClient im = factory.getClient();
	// im.getImage("57ce9335b1c3aa54ce707ef8", ".jpg", "");
	// System.out.println(im.getImageUrl("57ce9335b1c3aa54ce707ef8", ".jpg"));
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// }

}
