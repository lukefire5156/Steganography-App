package app.vit.imgtextsteganosoftware.activities.decrypt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import java.io.File;
import app.vit.imgtextsteganosoftware.R;
import app.vit.imgtextsteganosoftware.databinding.ActivityDecryptResultBinding;
import app.vit.imgtextsteganosoftware.utils.Constants;

public class DecryptResultActivity extends AppCompatActivity {
    private ActivityDecryptResultBinding binding;
    private String secretImagePath;
    private String secretMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDecryptResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Decrypted Result Image");
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            secretMessage = bundle.getString(Constants.EXTRA_SECRET_TEXT_RESULT);
            secretImagePath = bundle.getString(Constants.EXTRA_SECRET_IMAGE_RESULT);
        }

        if (secretMessage != null) {
            binding.tvSecretMessage.setText(secretMessage);
        } else if (secretImagePath != null) {
            binding.ivSecretImage.setVisibility(View.VISIBLE);
            setSecretImage(secretImagePath);
        }
    }

    public void setSecretImage(String path) {
        Picasso.with(this)
                .load(new File(path))
                .fit()
                .placeholder(R.drawable.ic_upload)
                .into(binding.ivSecretImage);
    }
}
