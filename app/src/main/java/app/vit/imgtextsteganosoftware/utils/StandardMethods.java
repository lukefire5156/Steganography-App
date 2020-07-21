package app.vit.imgtextsteganosoftware.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class StandardMethods {

  public static void showToast(Context context, int message) {
    Toast.makeText(context, context.getString(message), Toast.LENGTH_LONG).show();
  }

  public static void showLog(String tag, Object message) {
    Log.d(tag, String.valueOf(message));
  }
}
