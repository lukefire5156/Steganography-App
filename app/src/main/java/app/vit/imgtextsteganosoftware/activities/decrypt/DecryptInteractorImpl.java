package app.vit.imgtextsteganosoftware.activities.decrypt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.util.Map;

import app.vit.imgtextsteganosoftware.R;
import app.vit.imgtextsteganosoftware.algorithms.Extracting;
import app.vit.imgtextsteganosoftware.utils.Constants;
import app.vit.imgtextsteganosoftware.utils.HelperMethods;

class DecryptInteractorImpl implements DecryptInteractor {

  DecryptInteractorListener mListener;

  DecryptInteractorImpl(DecryptInteractorListener listener) {
    this.mListener = listener;
  }

  @Override
  public void performDecryption(String path) {
    if (!path.isEmpty()) {
      new ExtractSecretMessage(path).execute();
    } else {
      mListener.onPerformDecryptionFailure(R.string.decrypt_fail);
    }
  }

  private class ExtractSecretMessage extends AsyncTask<Void, Void, Map> {

    String stegoImagePath;

    ExtractSecretMessage(String stegoImagePath) {
      this.stegoImagePath = stegoImagePath;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected Map doInBackground(Void... params) {
      Map map = null;
      Bitmap stegoImage = null;

      if (!stegoImagePath.isEmpty()) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inScaled = false;

        //Should be false so that set pixels are not pre-multiplied by alpha value
        options.inPremultiplied = false;

        stegoImage = BitmapFactory.decodeFile(stegoImagePath, options);
      }

      if (stegoImage != null) {
        map = Extracting.extractSecretMessage(stegoImage);
      }

      return map;
    }

    @Override
    protected void onPostExecute(Map map) {
      if (map != null) {

        //Either TEXT, IMAGE, or UNDEFINED
        int type = (int) map.get(Constants.MESSAGE_TYPE);

        if (type == Constants.TYPE_TEXT) {
          String bits = (String) map.get(Constants.MESSAGE_BITS);
          byte[] messageBytes = HelperMethods.bitsStreamToByteArray(bits);
          String message = new String(messageBytes);
          mListener.onPerformDecryptionSuccessText(message);
        } else if (type == Constants.TYPE_IMAGE) {

          String bits = (String) map.get(Constants.MESSAGE_BITS);
          byte[] imageBytes = HelperMethods.bitsStreamToByteArray(bits);
          Bitmap bitmap = HelperMethods.byteArrayToBitmap(imageBytes);
          mListener.onPerformDecryptionSuccessImage(bitmap);

        } else if (type == Constants.TYPE_UNDEFINED) {
          mListener.onPerformDecryptionFailure(R.string.non_stego_image_selected);
        }
      } else {
        mListener.onPerformDecryptionFailure(R.string.decrypt_fail);
      }
    }
  }

  interface DecryptInteractorListener {


    void onPerformDecryptionSuccessText(String text);

    void onPerformDecryptionSuccessImage(Bitmap bitmap);

    void onPerformDecryptionFailure(int message);
  }
}
