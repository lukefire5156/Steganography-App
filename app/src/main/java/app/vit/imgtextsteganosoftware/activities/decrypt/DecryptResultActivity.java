package app.vit.imgtextsteganosoftware.activities.decrypt;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.io.File;

import app.vit.imgtextsteganosoftware.R;
import app.vit.imgtextsteganosoftware.utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DecryptResultActivity extends AppCompatActivity {

  @BindView(R.id.tvSecretMessage)
  TextView tvSecretMessage;

  @BindView(R.id.ivSecretImage)
  ImageView ivSecretImage;


  private String secretImagePath;
  private String secretMessage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_decrypt_result);

    ActionBar actionBar = getSupportActionBar();
    actionBar.setTitle("Decrypted Result Image");

    ButterKnife.bind(this);

    //initToolbar();

    Intent intent = getIntent();

    if (intent != null) {
      Bundle bundle = intent.getExtras();
      secretMessage = bundle.getString(Constants.EXTRA_SECRET_TEXT_RESULT);
      secretImagePath = bundle.getString(Constants.EXTRA_SECRET_IMAGE_RESULT);
    }

    if (secretMessage != null) {
      tvSecretMessage.setText(secretMessage);
    } else if (secretImagePath != null) {
      ivSecretImage.setVisibility(View.VISIBLE);
      setSecretImage(secretImagePath);
    }
  }

/*  public void initToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setTitle("Decryption");
    }
  }*/

  public void setSecretImage(String path) {
    Picasso.with(this)
      .load(new File(path))
      .fit()
      .placeholder(R.drawable.ic_upload)
      .into(ivSecretImage);
  }
}
