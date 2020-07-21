package app.vit.imgtextsteganosoftware.activities.encrypt;

interface EncryptPresenter {

  /**
   * Selects Image from Internal Storage
   *
   * @param type which is either COVER or SECRET
   * @param path indicates path to selected image
   */
  void selectImage(int type, String path);

  /**
   * Selects Image from Camera Activity
   *
   * @param type which is either COVER or SECRET
   */
  void selectImageCamera(int type);

  /**
   * Encrypts text and uses listeners to perform actions
   */
  void encryptText();

  /**
   * Encrypts image and uses listeners to perform actions
   */
  void encryptImage();
}
