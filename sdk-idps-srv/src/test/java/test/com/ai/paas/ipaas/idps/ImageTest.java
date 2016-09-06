package test.com.ai.paas.ipaas.idps;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Test;

import com.ai.paas.ipaas.image.IImageClient;
import com.ai.paas.ipaas.image.ImageFactory;
import com.ai.paas.ipaas.uac.vo.AuthDescriptor;

public class ImageTest {

	private static final String AUTH_ADDR = "http://10.1.228.200:14105/service-portal-uac-web/service/auth";
	private static AuthDescriptor ad = null;
	private static IImageClient im = null;

	static {
		ad = new AuthDescriptor(AUTH_ADDR, "0A8111DB280044528DF309D501DFFF6A",
				"123456", "IDPS020");
		try {
			im = ImageFactory.getClient(ad);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deTest() {
		// System.out.println(im.getUpImageUrl());
		byte[] buffer = null;
		try {
			File file = new File("d:/234.png");
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
		String id = im.upLoadImage(buffer, "234.png");

		System.out.println(id);
	}

	@Test
	public void seTest() {
		System.out.println(im.getImageUrl("576b8c46c9e77c0007c34529", ".png"));

	}
	@Test
	public void deleteTest() {
		System.out.println(im.deleteImage("574835f0c9e77c000768f594"));

	}
	
	@Test
	public void downTest() {
		im.getImage("56aa08347960b60009db8a3d", ".jpg", "");
		System.out.println(im.getImageUrl("56aa08347960b60009db8a3d", ".jpg"));

	}

}
