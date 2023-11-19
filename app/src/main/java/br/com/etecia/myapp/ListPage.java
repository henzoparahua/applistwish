package br.com.etecia.myapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListPage extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    List<Books> listaLivros;
    RecyclerView idRecyclerView;
    Button addLivro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listpage);

        idRecyclerView = findViewById(R.id.listaLivros);
        addLivro = findViewById(R.id.btnAddLivro);

        addLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Forms.class));
                finish();
            }
        });

        listaLivros = new ArrayList<>();
        listaLivros.add(new Books("Macaco", "Macumbeiraa", "macumbaiada", "Mandela"));


        idRecyclerView.setLayoutManager(new
                GridLayoutManager(
                getApplicationContext(), 2));

        idRecyclerView.setHasFixedSize(true);
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
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    Log.d("ListPage", "Received JSON: " + object.toString());  // Add this line for debugging
                    refreshBookList(object.getJSONArray("livros"));
                } else {
                    Log.e("ListPage", "Error in server response: " + object.getString("message"));  // Add this line for debugging
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ListPage", "Error parsing JSON response: " + e.getMessage());  // Add this line for debugging
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
    private void refreshBookList(JSONArray livros) throws JSONException {
        listaLivros.clear();

        for (int i = 0; i < livros.length(); i++){
            JSONObject obj = livros.getJSONObject(i);
            listaLivros.add(new Books(
                    obj.getString("titulo"),
                    obj.getString("editora"),
                    obj.getString("genero"),
                    obj.getString("autor")
            ));
        }
        MyAdapter adapter = new MyAdapter(getApplicationContext(), listaLivros);
        idRecyclerView.setAdapter(adapter);
    }
}