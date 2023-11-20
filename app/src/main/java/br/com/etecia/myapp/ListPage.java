package br.com.etecia.myapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    RecyclerView recycleViewLivros;
    Button addLivro;
    Boolean isUpdating = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listpage);

        recycleViewLivros = findViewById(R.id.RecycleViewLivros);
        addLivro = findViewById(R.id.btnAddLivro);

        addLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Forms.class));
                finish();
                if (isUpdating) {

                }
            }
        });
        readBooks();

        listaLivros = new ArrayList<>();
        listaLivros.add(new Books(1, "Macaco", "Macumbeiraa", "macumbaiada", "Mandela"));


        recycleViewLivros.setLayoutManager(new
                GridLayoutManager(
                getApplicationContext(), 1));

        recycleViewLivros.setHasFixedSize(true);
    }
    class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
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
                    refreshBookList(object.getJSONArray("livros"));
                } else {
                    Log.e("ListPage", "Error in server response: " + object.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ListPage", "Error parsing JSON response: " + e.getMessage());
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
    private void readBooks() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_BOOKS, null, CODE_GET_REQUEST);
        request.execute();
    }
     private void refreshBookList(JSONArray livros) {
         try {
             listaLivros.clear(); // Limpa a lista existente antes de adicionar os novos livros
             for (int i = 0; i < livros.length(); i++) {
                 JSONObject obj = livros.getJSONObject(i);
                 listaLivros.add(new Books(
                         obj.getInt("id"),
                         obj.getString("nome"),
                         obj.getString("autor"),
                         obj.getString("editora"),
                         obj.getString("genero")
                 ));
             }

             // Notifica o adaptador que os dados foram alterados
             if (recycleViewLivros.getAdapter() != null) {
                 recycleViewLivros.getAdapter().notifyDataSetChanged();
             } else {
                 MyAdapter adapter = new MyAdapter(listaLivros, this);
                 recycleViewLivros.setAdapter(adapter);
             }
         } catch (JSONException e) {
             e.printStackTrace();
         }
     }


 }