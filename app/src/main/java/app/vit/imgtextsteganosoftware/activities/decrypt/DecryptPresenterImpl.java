package app.vit.imgtextsteganosoftware.activities.decrypt;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import app.vit.imgtextsteganosoftware.R;
import app.vit.imgtextsteganosoftware.utils.StandardMethods;


class DecryptPresenterImpl implements DecryptPresenter, DecryptInteractorImpl.DecryptInteractorListener {

  private DecryptView mView;
  private DecryptInteractor mInteractor;
  private String stegoImagePath = "";

  DecryptPresenterImpl(DecryptView decryptView) {

    this.mView = decryptView;
    this.mInteractor = new DecryptInteractorImpl(this);
  }

  @Override
  public void setStegiImgPath(String path) {
    stegoImagePath = path;

  }

  @Override
  public void selectImage(String path) {
    mView.showProgressDialog();

    File stegoFile = new File(path);

    stegoImagePath = path;

    mView.setStegoImage(stegoFile);
  }

  @Override
  public void decryptMessage() {
    if (stegoImagePath.isEmpty()) {
      mView.showToast(R.string.stego_image_not_selected);
    } else {
      mView.showProgressDialog();
      mInteractor.performDecryption(stegoImagePath);
    }
  }

  @Override
  public void onPerformDecryptionSuccessText(String text) {
    mView.stopProgressDialog();
    mView.showToast(R.string.decrypt_success);
    mView.startDecryptResultActivity(text, null);
  }

  @Override
  public void onPerformDecryptionSuccessImage(Bitmap secretImage) {
    mView.stopProgressDialog();
    mView.showToast(R.string.decrypt_success);
    String filePath = storeSecretImage(secretImage);
    mView.startDecryptResultActivity(null, filePath);
  }

  @Override
  public void onPerformDecryptionFailure(int message) {
    mView.stopProgressDialog();
    mView.showToast(message);
  }

  private String storeSecretImage(Bitmap secretImage) {
    String path = Environment.getExternalStorageDirectory() + File.separator + "CryptoMessenger";

    File folder = new File(path);
    File file = null;
    String filePath = "";

    if (!folder.exists()) {
      if (folder.mkdirs()) {
        file = new File(path, "SI_" + System.currentTimeMillis() + ".png");
      } else {
        mView.stopProgressDialog();
        mView.showToast(R.string.compress_error);
      }
    } else {
      file = new File(path, "SI_" + System.currentTimeMillis() + ".png");
    }

    if (file != null) {
      try {
        FileOutputStream fos = new FileOutputStream(file);
        secretImage.compress(Bitmap.CompressFormat.PNG, 100, fos);

        fos.flush();
        fos.close();

        filePath = file.getAbsolutePath();

      } catch (FileNotFoundException e1) {
        StandardMethods.showLog("EPI/Error", e1.getMessage());
      } catch (IOException e2) {
        StandardMethods.showLog("EPI/Error", e2.getMessage());
      }
    }

    return filePath;
  }
}
