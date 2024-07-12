package app.vit.imgtextsteganosoftware.activities.encrypt;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.squareup.picasso.Picasso;
import java.io.File;
import app.vit.imgtextsteganosoftware.R;
import app.vit.imgtextsteganosoftware.activities.stego.StegoActivity;
import app.vit.imgtextsteganosoftware.databinding.ActivityEncryptTextBinding;
import app.vit.imgtextsteganosoftware.utils.Constants;
import app.vit.imgtextsteganosoftware.utils.StandardMethods;

public class EncryptActivity extends AppCompatActivity implements EncryptView {
    private ActivityEncryptTextBinding binding;
    private ProgressDialog progressDialog;
    private EncryptPresenter mPresenter;
    private int whichImage = -1;
    private int secretMessageType = Constants.TYPE_TEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEncryptTextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bEncrypt.setOnClickListener(v -> {
            if (secretMessageType == Constants.TYPE_IMAGE) {
                mPresenter.encryptImage();
            } else if (secretMessageType == Constants.TYPE_TEXT) {
                String text = getSecretMessage();

                if (!text.isEmpty()) {
                    mPresenter.encryptText();
                } else {
                    showToast(R.string.secret_text_empty);
                }
            }
        });

        binding.ivCoverImage.setOnClickListener(v -> {
            final CharSequence[] items = {
                    getString(R.string.take_image_dialog),
                    getString(R.string.select_image_dialog)
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(EncryptActivity.this);
            builder.setTitle(getString(R.string.select_image_title));
            builder.setCancelable(false);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int item) {
                    if (items[item].equals(getString(R.string.take_image_dialog))) {

                        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(getApplicationContext(),
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(EncryptActivity.this,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    Constants.PERMISSIONS_CAMERA);

                        } else {
                            openCamera();
                        }
                    } else if (items[item].equals(getString(R.string.select_image_dialog))) {

                        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(EncryptActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    Constants.PERMISSIONS_EXTERNAL_STORAGE);

                        } else {
                            chooseImage();
                        }
                    }
                }
            });

            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            whichImage = Constants.COVER_IMAGE;
            builder.show();
        });
        binding.ivSecretImage.setOnClickListener(v -> {

            final CharSequence[] items = {
                    getString(R.string.take_image_dialog),
                    getString(R.string.select_image_dialog)
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(EncryptActivity.this);
            builder.setTitle(getString(R.string.select_image_title));
            builder.setCancelable(false);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int item) {
                    if (items[item].equals(getString(R.string.take_image_dialog))) {

                        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(getApplicationContext(),
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(EncryptActivity.this,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    Constants.PERMISSIONS_CAMERA);

                        } else {
                            openCamera();
                        }
                    } else if (items[item].equals(getString(R.string.select_image_dialog))) {

                        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(EncryptActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    Constants.PERMISSIONS_EXTERNAL_STORAGE);

                        } else {
                            chooseImage();
                        }
                    }
                }
            });

            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            whichImage = Constants.SECRET_IMAGE;

            builder.show();
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Encrypt Text into Image");
        progressDialog = new ProgressDialog(EncryptActivity.this);
        progressDialog.setMessage("Please wait...");
        mPresenter = new EncryptPresenterImpl(this);
        SharedPreferences sp = getSharedPrefs();
        String filePath = sp.getString(Constants.PREF_COVER_PATH, "");
        boolean isCoverSet = sp.getBoolean(Constants.PREF_COVER_IS_SET, false);
        if (isCoverSet) {
            setCoverImage(new File(filePath));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.PERMISSIONS_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }
                break;
            case Constants.PERMISSIONS_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImage();
                }
                break;
        }
    }

    @Override
    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(android.os.Environment
                .getExternalStorageDirectory(), "temp.png");

        Uri imageUri = FileProvider.getUriForFile(this, "alexparunov", file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, Constants.REQUEST_CAMERA);
    }

    @Override
    public void chooseImage() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, getString(R.string.choose_image)),
                Constants.SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_CAMERA) {
                mPresenter.selectImageCamera(whichImage);
            } else if (requestCode == Constants.SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String tempPath = getPath(selectedImageUri, EncryptActivity.this);
                mPresenter.selectImage(whichImage, tempPath);
            }
        }
    }


    public String getPath(Uri uri, AppCompatActivity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void startStegoActivity(String filePath) {
        Intent intent = new Intent(EncryptActivity.this, StegoActivity.class);
        intent.putExtra(Constants.EXTRA_STEGO_IMAGE_PATH, filePath);
        startActivity(intent);
    }

    @Override
    public Bitmap getCoverImage() {
        return ((BitmapDrawable) binding.ivCoverImage.getDrawable()).getBitmap();
    }

    @Override
    public void setCoverImage(File file) {
        showProgressDialog();
        Picasso.with(this)
                .load(file)
                .fit()
                .placeholder(R.drawable.ic_upload)
                .into(binding.ivCoverImage);
        stopProgressDialog();
        whichImage = -1;

        SharedPreferences.Editor editor = getSharedPrefs().edit();
        editor.putString(Constants.PREF_COVER_PATH, file.getAbsolutePath());
        editor.putBoolean(Constants.PREF_COVER_IS_SET, true);
        editor.apply();
    }

    @Override
    public Bitmap getSecretImage() {
        return ((BitmapDrawable) binding.ivSecretImage.getDrawable()).getBitmap();
    }

    @Override
    public void setSecretImage(File file) {
        showProgressDialog();
        Picasso.with(this)
                .load(file)
                .fit()
                .placeholder(R.drawable.ic_upload)
                .into(binding.ivSecretImage);
        stopProgressDialog();
        whichImage = -1;
    }

    @Override
    public String getSecretMessage() {
        return binding.etSecretMessage.getText().toString().trim();
    }

    @Override
    public void setSecretMessage(String secretMessage) {
        binding.etSecretMessage.setText(secretMessage);
    }

    @Override
    public void showToast(int message) {
        StandardMethods.showToast(this, message);
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void stopProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public SharedPreferences getSharedPrefs() {
        return getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }
}
