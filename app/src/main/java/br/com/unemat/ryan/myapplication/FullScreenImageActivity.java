package br.com.unemat.ryan.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Garante que está usando o layout correto
        setContentView(R.layout.activity_fullscreen_image);

        // Garante que está procurando o ID correto
        ImageView fullscreenImage = findViewById(R.id.fullscreen_image);

        int imageResId = getIntent().getIntExtra("image_res_id", 0);

        if (imageResId != 0) {
            fullscreenImage.setImageResource(imageResId);
        }

        fullscreenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Fecha esta tela ao clicar
            }
        });
    }
}