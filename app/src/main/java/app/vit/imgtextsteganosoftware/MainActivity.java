package app.vit.imgtextsteganosoftware;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;


import app.vit.imgtextsteganosoftware.activities.decrypt.DecryptActivity;
import app.vit.imgtextsteganosoftware.activities.encrypt.EncryptActivity;
import app.vit.imgtextsteganosoftware.activities.encrypt.EncryptImageActivity;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

  LinearLayout encrypt,decrypt,encryptImg;

/*  @OnClick({R.id.bAMEncrypt, R.id.bAMDecrypt})
  public void onButtonClick(View view) {
    if(view.getId() == R.id.bAMEncrypt) {
      Intent intent = new Intent(MainActivity.this, EncryptActivity.class);
      startActivity(intent);
    } else if(view.getId() == R.id.bAMDecrypt) {
      Intent intent = new Intent(MainActivity.this, DecryptActivity.class);
      startActivity(intent);
    }
  }*/

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);

    encrypt =findViewById(R.id.encodeButton);
    decrypt =findViewById(R.id.decodeButton);
    encryptImg =findViewById(R.id.encodeImageButton);


    encryptImg.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, EncryptImageActivity.class);
        startActivity(intent);
      }
    });

    encrypt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, EncryptActivity.class);
        startActivity(intent);
      }
    });
    decrypt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, DecryptActivity.class);
        startActivity(intent);
      }
    });

    //initToolbar();
  }

/*  public void initToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(false);
      actionBar.setTitle("Crypto Messenger");
    }
  }*/
public boolean onCreateOptionsMenu(Menu menu) {
  // Inflate the menu; this adds items to the action bar if it is present.
  getMenuInflater().inflate(R.menu.menu, menu);
  return true;
}

  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_name) {
      Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
      i.putExtra("check", "true");
      startActivity(i);

      Log.d("I", "In fb button");
      return true;

    }
    return true;
  }
}
