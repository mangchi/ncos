package mil.ln.ncos;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import org.junit.Test;

public class ImageEnlargeTest {

	@Test
	public void enlareg() {
		try {
			String formatType = "PNG";
			File srcFile = new File("C:\\Users\\mangchi\\Pictures\\@img_051.png");

			int newWidth = 1000;
			int newHeight = 404;

			BufferedImage bi = ImageIO.read(srcFile); // 원본이미지

			Image resizeImage = bi.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
			BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
			Graphics graphics = newImage.getGraphics();
			graphics.drawImage(resizeImage, 0, 0, null);
			graphics.dispose();

			File destFile = new File("C:\\Users\\mangchi\\Pictures\\enlarge\\@img_051.png");

			ImageIO.write(newImage, formatType, destFile);
		} catch (IIOException iie) {
			iie.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
