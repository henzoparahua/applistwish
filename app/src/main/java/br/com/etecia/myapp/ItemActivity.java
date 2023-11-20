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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemActivity extends AppCompatActivity {
    private int bookId;
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private List<Books> listaLivros = new ArrayList<>();
    private TextInputEditText Titulo, Autora, Editora, Genero;
    private Button btLeave, btDelete, btSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        bookId = getIntent().getIntExtra("bookId", 0);

        Titulo = findViewById(R.id.txtTitulo);
        Autora = findViewById(R.id.txtAutora);
        Editora = findViewById(R.id.txtEditora);
        Genero = findViewById(R.id.txtGenero);
        btLeave = findViewById(R.id.btnVoltar);
        btDelete = findViewById(R.id.btnDeletar);
        btSave = findViewById(R.id.btnSalvar);
        readBooks();

        btLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ListPage.class));
                finish();
            }
        });
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBook();
            }
        });
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBook(bookId);
                startActivity(new Intent(getApplicationContext(),ListPage.class));
                finish();
            }
        });
    }

    private void readBooks() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_BOOKS, null, CODE_GET_REQUEST);
        request.execute();
    }
    private void deleteBook(int id) {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_BOOK + id, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshBookList(JSONArray livros, int targetBookId) {
        try {
            listaLivros.clear();

            if (livros.length() > 0) {
                for (int i = 0; i < livros.length(); i++) {
                    JSONObject obj = livros.getJSONObject(i);
                    int currentBookId = obj.getInt("id");

                    if (currentBookId == targetBookId) {
                        listaLivros.add(new Books(
                                obj.getInt("id"),
                                obj.getString("nome"),
                                obj.getString("autor"),
                                obj.getString("editora"),
                                obj.getString("genero")
                        ));
                        updateUI();
                        break;
                    }
                }
            } else {
                Log.e("ItemActivity", "Error: 'livros' array is empty.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

}
    private void updateBook() {
        String nome = Titulo.getText().toString().trim();
        String  autor = Autora.getText().toString().trim();
        String  genero = Genero.getText().toString().trim();
        String  editora = Editora.getText().toString().trim();
        String id = String.valueOf(bookId);

        HashMap<String, String> params = new HashMap<>();
        params.put("genero", genero);
        params.put("editora", editora);
        params.put("autor", autor);
        params.put("nome", nome);
        params.put("id", id);

        Toast.makeText(getApplicationContext(), params.toString(), Toast.LENGTH_SHORT).show();


        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_BOOKS, params, CODE_POST_REQUEST);
        request.execute();
    }

    private void updateUI() {
        if (!listaLivros.isEmpty()) {
            Books book = listaLivros.get(0);

            if (book != null) {
                Titulo.setText(book.getTitulo());
                Autora.setText(book.getAutor());
                Editora.setText(book.getEditora());
                Genero.setText(book.getGenero());
            }
        }
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        private String url;
        private HashMap<String, String> params;
        private int requestCode;

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
                if (s.startsWith("[") && s.endsWith("]")) {
                    // Handle JSON array
                    JSONArray jsonArray = new JSONArray(s);
                    if (jsonArray.length() > 0) {
                        // If the array is not empty, proceed with your logic
                        JSONObject object = jsonArray.getJSONObject(0);
                        if (!object.getBoolean("error")) {
                            if (object.has("livros") && object.get("livros") instanceof JSONArray) {
                                refreshBookList((JSONArray) object.get("livros"), bookId);
                            } else {
                                Log.e("ItemActivity", "Error: 'livros' key is missing or not a JSONArray.");
                            }
                        } else {
                            Log.e("ItemActivity", "Error in server response: " + object.getString("message"));
                        }
                    } else {
                        // Handle empty array case
                        Log.e("ItemActivity", "Received empty JSON array.");
                    }
                } else {
                    // Handle JSON object
                    JSONObject object = new JSONObject(s);
                    if (!object.getBoolean("error")) {
                        if (object.has("livros") && object.get("livros") instanceof JSONArray) {
                            refreshBookList((JSONArray) object.get("livros"), bookId);
                        } else {
                            Log.e("ItemActivity", "Error: 'livros' key is missing or not a JSONArray.");
                        }
                    } else {
                        Log.e("ItemActivity", "Error in server response: " + object.getString("message"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ItemActivity", "Error parsing JSON response: " + e.getMessage());
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
}
