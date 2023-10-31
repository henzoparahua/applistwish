package br.com.etecia.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class Forms extends AppCompatActivity {
    Button btnVoltar, btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms);
        btnVoltar = findViewById(R.id.btnVoltar);
        btnAdd = findViewById(R.id.btnConfirmar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ListPage.class));
                finish();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<>();
                params.put("name", nome);
                params.put("realname", realname);
                params.put("rating", String.valueOf(rating));
                params.put("teamaffiliation", team);

                PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_HERO, params, CODE_POST_REQUEST);
                request.execute();
            }
        });

    }

    private boolean createBook(){
        return false;
    }
}