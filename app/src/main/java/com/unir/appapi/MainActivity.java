package com.unir.appapi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;
    private TextView textResultado;
    private EditText editText;
    private Button btnConverter;
    private Retrofit retrofit;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spinner = findViewById(R.id.spinner);
        textResultado = findViewById(R.id.txtResultado);
        editText = findViewById(R.id.editTextNumberDecimal);
        btnConverter = findViewById(R.id.btnConverter);
        progressBar = findViewById(R.id.progressBar);
        String url = "https://blockchain.info/";
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        btnConverter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText.getText().toString().isEmpty()){
                    int valor = Integer.parseInt(editText.getText().toString());
                    String moeda = spinner.getSelectedItem().toString();
                    recuperarValor(moeda, valor);
                }else{
                    Toast.makeText(MainActivity.this, "Digite um valor", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void recuperarValor(String moeda, int valor){
        Bitcoin bitcoin = retrofit.create(Bitcoin.class);
        Call<Double> call = bitcoin.getCurrentValue(moeda, valor);
        progressBar.setVisibility(View.VISIBLE);
        textResultado.setVisibility(View.GONE);
        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                progressBar.setVisibility(View.GONE);
                textResultado.setVisibility(View.VISIBLE);
                if (response.isSuccessful()){
                    Double valorAtual = response.body();
                    textResultado.setText("Resultado: " +valorAtual.toString());
                }else{
                    Toast.makeText(MainActivity.this, "Erro na requisição", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                textResultado.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Erro na requisição", Toast.LENGTH_SHORT).show();
            }
        });
    }
}