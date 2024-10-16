package com.example.mysql;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysql.activities.PrincipalActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText login;
    private EditText password;
    private Button buttonRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("UserLogin", MODE_PRIVATE);
        String savedUsuario = sharedPreferences.getString("usuario", "");
        String savedPassword = sharedPreferences.getString("password", "");

        login = findViewById(R.id.id_usuario);
        password = findViewById(R.id.id_clave);
        buttonRemember = findViewById(R.id.buttonRemember); // Inicializar el botón

        login.setText(savedUsuario);
        password.setText(savedPassword);
        validateUser();

        buttonRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rellenar campos de usuario y contraseña
                login.setText(savedUsuario);
                password.setText(savedPassword);
                Toast.makeText(MainActivity.this, "Campos rellenados", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUser();
            }
        });
    }

    private void validateUser(){
        String usuario = login.getText().toString().trim();
        String clave = password.getText().toString().trim();

//        String usuario = "asmith";
//        String clave = "mypassword";

        String url = "http://10.0.2.2/login/validar_usuario.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("error")) {
                                String errorMessage = jsonResponse.getString("error");
                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                saveCredentials(usuario, clave);
                                Toast.makeText(MainActivity.this, "Usuario Correcto", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);

                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            Log.e("Response", "Error al parsear la respuesta JSON", e);
                            Toast.makeText(MainActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("usuario", usuario);
                params.put("password", clave);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void saveCredentials(String usuario, String clave) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserLogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario", usuario);
        editor.putString("password", clave);
        editor.apply();
    }
}