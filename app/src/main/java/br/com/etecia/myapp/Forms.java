package br.com.etecia.myapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Forms extends AppCompatActivity {
    Button btnVoltar, btnAdd;
    TextInputEditText nome, autor, editora, genero;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private void createBook(String nome, String autor, String editora, String genero){
        HashMap<String, String> params = new HashMap<>();
        params.put("nome", nome);
        params.put("autor", autor);
        params.put("editora", (editora));
        params.put("genero", genero);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_HERO, params, CODE_POST_REQUEST);
        request.execute();
    }
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("API_RESPONSE", "Response: " + s);

            // Adicione verificação se a resposta é válida JSON
            if (s.startsWith("{")) {
                try {
                    JSONObject object = new JSONObject(s);
                    if (!object.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // Trate a resposta inválida ou exiba uma mensagem de erro
                Log.e("API_RESPONSE", "Invalid JSON response");
            }
        }


        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms);
        btnVoltar = findViewById(R.id.btnVoltar);
        btnAdd = findViewById(R.id.btnConfirmar);
        nome = findViewById(R.id.txtTitulo);
        editora = findViewById(R.id.txtEditora);
        autor = findViewById(R.id.txtAutora);
        genero = findViewById(R.id.txtGenero);

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
                String txtNome = nome.getText().toString(),
                        txtAutor = autor.getText().toString(),
                        txtEditora = editora.getText().toString(),
                        txtGenero = genero.getText().toString();
                createBook(txtNome, txtAutor, txtEditora, txtGenero);
                nome.getText().clear();
                autor.getText().clear();
                editora.getText().clear();
                genero.getText().clear();
            }
        });

    }


}