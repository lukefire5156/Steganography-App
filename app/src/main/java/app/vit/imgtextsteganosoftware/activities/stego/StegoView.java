package app.vit.imgtextsteganosoftware.activities.stego;

import android.content.Intent;


interface StegoView {

  void showToast(int message);

  void showProgressDialog();

  void stopProgressDialog();

  //void initToolbar();

  void setStegoImage(String path);

  void saveToMedia(Intent intent);

  void shareStegoImage(String path);
}
