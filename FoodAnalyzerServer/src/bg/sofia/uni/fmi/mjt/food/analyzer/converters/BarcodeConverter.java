package bg.sofia.uni.fmi.mjt.food.analyzer.converters;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public final class BarcodeConverter {

    private BarcodeConverter() {

    }

    /**
     * Returns GtinUpc(Global Trade Item Number) from image
     *
     * @param fileName of the image
     * @return GtinUpc(Global Trade Item Number)
     * @throws IOException       if an error occurs during reading
     * @throws NotFoundException if file does not exists
     */
    public static String getConvertedGtinUpcFromImage(final String fileName) throws IOException, NotFoundException {
        final File file = new File(fileName);

        final BufferedImage barCodeBufferedImage = ImageIO.read(file);
        final LuminanceSource source = new BufferedImageLuminanceSource(barCodeBufferedImage);
        final BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        final Result result = new MultiFormatReader().decode(bitmap);

        return result.getText();
    }
}