import static org.lwjgl.opengl.GL11.*;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;
import javax.swing.ImageIcon;


public class TextureLoaderCustom {
	public  void getTexture(String resourceName) throws IOException {
	    glBindTexture(GL_TEXTURE_2D, 1);

	    BufferedImage bufferedImage = loadImage(resourceName);
	    ByteBuffer textureBuffer = convertImageData(bufferedImage);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

	    // produce a texture from the byte buffer
	    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, bufferedImage.getWidth(),
	            bufferedImage.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
	            textureBuffer);
	}

	/**
	 * Convert the buffered image to a texture
	 */
	private  ByteBuffer convertImageData(BufferedImage bufferedImage) {
	    ByteBuffer imageBuffer;
	    WritableRaster raster;
	    BufferedImage texImage;

	    ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace
	            .getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 },
	            true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);

	    raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
	            bufferedImage.getWidth(), bufferedImage.getHeight(), 4, null);
	    texImage = new BufferedImage(glAlphaColorModel, raster, true,
	            new Hashtable());

	    // copy the source image into the produced image
	    Graphics g = texImage.getGraphics();
	    g.setColor(new Color(0f, 0f, 0f, 0f));
	    g.fillRect(0, 0, 256, 256);
	    g.drawImage(bufferedImage, 0, 0, null);

	    // build a byte buffer from the temporary image
	    // that be used by OpenGL to produce a texture.
	    byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer())
	            .getData();

	    imageBuffer = ByteBuffer.allocateDirect(data.length);
	    imageBuffer.order(ByteOrder.nativeOrder());
	    imageBuffer.put(data, 0, data.length);
	    imageBuffer.flip();

	    return imageBuffer;
	}

	/**
	 * Load a given resource as a buffered image
	 */
	private  BufferedImage loadImage(String ref) throws IOException {
	    URL url = getClass().getClassLoader().getResource(ref);

	    // due to an issue with ImageIO and mixed signed code
	    // we are now using good oldfashioned ImageIcon to load
	    // images and the paint it on top of a new BufferedImage
	    Image img = new ImageIcon(url).getImage();
	    BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img
	            .getHeight(null), BufferedImage.TYPE_INT_ARGB);
	    Graphics g = bufferedImage.getGraphics();
	    g.drawImage(img, 0, 0, null);
	    g.dispose();

	    return bufferedImage;
	}
}
