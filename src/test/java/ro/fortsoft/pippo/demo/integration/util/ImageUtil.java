package ro.fortsoft.pippo.demo.integration.util;

import org.im4java.core.CompareCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * @author sbalamaci
 */
public class ImageUtil {

    /**
     * Compares two images by comparing the hash of their content
     *
     *
     * @param img1 Path to the first image for the comparison
     * @param img2 Path to the second image for the comparison
     * @return true if the images have the same
     */
    public static boolean isEqual(Path img1, Path img2) {
        MessageDigest md5Image1;
        MessageDigest md5Image2;
        try {
            md5Image1 = MessageDigest.getInstance("MD5");
            md5Image2 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e); //should never happen
        }

        try {
            md5Image1.update(Files.readAllBytes(img1));
            md5Image2.update(Files.readAllBytes(img2));
        } catch (IOException e) {
            throw new RuntimeException("Error reading images to compare", e);
        }
        return MessageDigest.isEqual(md5Image1.digest(), md5Image2.digest());
    }

    /**
     * Creates a diff from two images
     *
     * @param firstImage Path to the first source image
     * @param secondImage Path to the second source image
     */
    public static void createImageDiff(Path firstImage, Path secondImage, Path diffImage) {
        CompareCmd cmd = new CompareCmd();
        IMOperation op = new IMOperation();

        op.addImage(firstImage.toFile().getAbsolutePath());
        op.addImage(secondImage.toFile().getAbsolutePath());
        op.addImage(diffImage.toFile().getAbsolutePath());

        try {
            cmd.run(op);
        } catch (InterruptedException | IOException | IM4JavaException e) {
            throw new RuntimeException("Error executing IM4Java diff command", e);
        } finally {
            op.closeOperation();
        }
    }


}
