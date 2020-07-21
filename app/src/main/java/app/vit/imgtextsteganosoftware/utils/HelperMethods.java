package app.vit.imgtextsteganosoftware.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import androidx.annotation.NonNull;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;

public class HelperMethods {

  static byte[] bitmapToByteArray(Bitmap bitmap) {

    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

    return stream.toByteArray();
  }

  public static Bitmap byteArrayToBitmap(byte[] byteArray) {

    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
  }

  @NonNull
  public static String bitmapToBinaryStream(Bitmap bitmap) {
    byte[] bitmapInBytes = bitmapToByteArray(bitmap);

    return byteArrayToBitsStream(bitmapInBytes);
  }

  @NonNull
  public static String stringToBinaryStream(String string) {
    byte[] stringInBytes = string.getBytes(Charset.forName("UTF-8"));

    return byteArrayToBitsStream(stringInBytes);
  }

  @NonNull
  private static String byteArrayToBitsStream(byte[] array) {
    StringBuilder binary = new StringBuilder();

    for (byte b : array) {
      int val = b;
      for (int i = 0; i < 8; i++) {
        binary.append((val & 128) == 0 ? 0 : 1);
        val <<= 1;
      }
    }
    return binary.toString();
  }

  public static byte[] bitsStreamToByteArray(String stream) {
    Iterable iterable = Splitter.fixedLength(8).split(stream);
    byte[] resultBytes = new byte[Iterables.size(iterable)];
    Iterator iterator = iterable.iterator();
    int i = 0;
    while (iterator.hasNext()) {
      Integer byteAsInt = Integer.parseInt(iterator.next().toString(), 2);
      resultBytes[i] = byteAsInt.byteValue();
      i++;
    }

    return resultBytes;
  }
}
