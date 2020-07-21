package app.vit.imgtextsteganosoftware.algorithms;

import android.graphics.Bitmap;
import android.graphics.Color;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;

import app.vit.imgtextsteganosoftware.utils.Constants;
import app.vit.imgtextsteganosoftware.utils.StandardMethods;

public class Extracting {

  public static Map extractSecretMessage(Bitmap stegoImage) {
    Map<String, Object> map = new HashMap<String, Object>();

    int width = stegoImage.getWidth();
    int height = stegoImage.getHeight();

    int key[] = new int[24];

    //Extract Key
    int keyPixel = stegoImage.getPixel(0, 0);

    int red = Color.red(keyPixel);
    int green = Color.green(keyPixel);
    int blue = Color.blue(keyPixel);

    StandardMethods.showLog("EXT", "Key2: " + red + " " + green + " " + blue);

    String red_bin = Integer.toBinaryString(red);
    red_bin = "00000000" + red_bin;
    red_bin = red_bin.substring(red_bin.length() - 8);

    for (int i = 0; i <= 7; i++) {
      key[i] = (red_bin.charAt(i) == '1' ? 1 : 0);
    }

    String green_bin = Integer.toBinaryString(green);
    green_bin = "00000000" + green_bin;
    green_bin = green_bin.substring(green_bin.length() - 8);

    for (int i = 0; i <= 7; i++) {
      key[i + 8] = (green_bin.charAt(i) == '1' ? 1 : 0);
    }

    String blue_bin = Integer.toBinaryString(blue);
    blue_bin = "00000000" + blue_bin;
    blue_bin = blue_bin.substring(blue_bin.length() - 8);

    for (int i = 0; i <= 7; i++) {
      key[i + 16] = (blue_bin.charAt(i) == '1' ? 1 : 0);
    }

    int typePixel = stegoImage.getPixel(0, 1);
    int tRed = Color.red(typePixel);
    int tGreen = Color.green(typePixel);
    int tBlue = Color.blue(typePixel);

    //Constants.COLOR_RGB_TEXT
    if (tRed == 135 && tGreen == 197 && tBlue == 245) {

      map.put(Constants.MESSAGE_TYPE, Constants.TYPE_TEXT);

    //Constants.COLOR_RGB_IMAGE
    } else if (tRed == 255 && tGreen == 105 && tBlue == 180) {

      map.put(Constants.MESSAGE_TYPE, Constants.TYPE_IMAGE);

    } else {

      map.put(Constants.MESSAGE_TYPE, Constants.TYPE_UNDEFINED);
      map.put(Constants.MESSAGE_BITS, "");
      return map;

    }

    StringBuilder sb = new StringBuilder();

    int keyPos = 0;
    outerloop:
    for (int x = 0; x < width; ++x) {
      for (int y = 2; y < height; ++y) {
        int pixel = stegoImage.getPixel(x, y);

        int colors[] = {Color.red(pixel), Color.green(pixel), Color.blue(pixel)};

        //Colors.COLOR_RGB_END
        if (colors[0] == 96 && colors[1] == 62 && colors[2] == 148) {
          break outerloop;
        } else {

          for (int c = 0; c < 3; c++) {

            if ((key[keyPos] ^ LSB2(colors[c])) == 1) {
              int lsb = LSB(colors[c]);
              sb.append(lsb);
              keyPos = (keyPos + 1) % key.length;
            }
          }
        }
      }
    }

    String sm = sb.toString();
    int sL = sm.length();

    //Cut unnecessary [0-7] pixels
    sm = sm.substring(0, sL - sL % 8);

    map.put(Constants.MESSAGE_BITS, sm);
    return map;
  }

  @Contract(pure = true)
  private static int LSB(int number) {
    return number & 1;
  }

  @Contract(pure = true)
  private static int LSB2(int number) {
    return (number >> 1) & 1;
  }
}
