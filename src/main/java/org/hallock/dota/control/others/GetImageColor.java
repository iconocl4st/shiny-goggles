package org.hallock.dota.control.others;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GetImageColor {
    public static void getImageColor() throws IOException {
        BufferedImage read = ImageIO.read(new File("foo_0.36629573894124.png"));
        Color c = new Color(read.getRGB(14, 14));
        System.out.println(c.getRed() +", " + c.getGreen() + ", " + c.getBlue());
    }
}
