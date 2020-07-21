package app.vit.imgtextsteganosoftware.activities.decrypt;

import android.graphics.Bitmap;

import java.io.File;

interface DecryptView {


  Bitmap getStegoImage();

  //void initToolbar();

  void setStegoImage(File file);


  void showToast(int message);

  void chooseImage();

  void showProgressDialog();

  void stopProgressDialog();

  void startDecryptResultActivity(String secretMessage, String secretImagePath);
}
