package app.vit.imgtextsteganosoftware.activities.encrypt;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import app.vit.imgtextsteganosoftware.algorithms.Embedding;

class EncryptInteractorImpl implements EncryptInteractor {

  private EncryptInteractorListener listener;

  EncryptInteractorImpl(EncryptInteractorListener listener) {
    this.listener = listener;
  }

  @Override
  public void performSteganography(String message, Bitmap coverImage, Bitmap secretImage) {
    if (secretImage == null && message != null) {
      new EmbedSecretMessage(message, coverImage, null).execute();
    } else if(secretImage != null){
      new EmbedSecretMessage(null, coverImage, secretImage).execute();
    } else {
      listener.onPerformSteganographyFailure();
    }
  }


  private class EmbedSecretMessage extends AsyncTask<Void, Void, Bitmap> {
    String message;
    Bitmap coverImage, secretImage;

    EmbedSecretMessage(String message, Bitmap coverImage, Bitmap secretImage) {
      this.message = message;
      this.coverImage = coverImage;
      this.secretImage = secretImage;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
      Bitmap stegoImage = null;

      if (message != null && secretImage == null && coverImage != null) {
        stegoImage = Embedding.embedSecretText(this.message, this.coverImage);
      } else if (message == null && secretImage != null && coverImage != null) {
        stegoImage = Embedding.embedSecretImage(this.coverImage, this.secretImage);
      }

      return stegoImage;
    }

    @Override
    protected void onPostExecute(Bitmap stegoImage) {
      if (stegoImage != null) {
        listener.onPerformSteganographySuccessful(stegoImage);
      } else {
        listener.onPerformSteganographyFailure();
      }
    }
  }

  interface EncryptInteractorListener {


    void onPerformSteganographySuccessful(Bitmap stegoImage);

    void onPerformSteganographyFailure();
  }
}
