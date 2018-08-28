package com.jcramire.prueba;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jcramire.prueba.Utils.Configurations;
import com.jcramire.prueba.data.model.Token;
import com.jcramire.prueba.data.model.Usuario;
import com.jcramire.prueba.data.remote.ApiService;
import com.jcramire.prueba.data.remote.ApiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getName();
    private ApiService mApiService;
    private String tokenAutentificacion;

    @BindView(R.id.nombre_usuario) EditText nombreUsuario;
    @BindView(R.id.correo_usuario) EditText correoUsuario;
    @BindView(R.id.avatar_usuario) ImageView avatarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mApiService = ApiUtils.getAPIService();
    }

    @OnClick({R.id.btn_conectar, R.id.btn_guardar})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_conectar:
                autorizacion();
                break;
            case R.id.btn_guardar:
                if (tokenAutentificacion != null) {
                    String nuevoNombre = nombreUsuario.getText().toString();
                    String nuevoCorreo = correoUsuario.getText().toString();
                    actualizarUsuario(nuevoNombre, nuevoCorreo);
                }else{
                    Toast.makeText(this, "Para guardar debe conectarse al servidor", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void autorizacion(){
        mApiService.obtenerAutorizacion(Configurations.token_acceso).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(response.code() == 200) {
                    tokenAutentificacion = response.body().getToken();
                    cargarUsuario();
                } else {
                    Toast.makeText(getApplicationContext(), "No es posible obtener Autorización", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                if(call.isCanceled()) {
                    Log.e(TAG, "La solicitud fue abortada");
                }else {
                    Toast.makeText(getApplicationContext(), "No se puede enviar la solicitud a la API", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cargarUsuario(){
        mApiService.mostrarUsuario("Bearer "+tokenAutentificacion).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.code() == 200){
                    nombreUsuario.setText(response.body().getName());
                    correoUsuario.setText(response.body().getEmail());
                    Glide.with(getApplicationContext()).load(response.body().getAvatar()).into(avatarUsuario);
                    Toast.makeText(getApplicationContext(), "Usuario Cargado Exitosamente", Toast.LENGTH_LONG).show();
                }else{
                    Log.e(TAG,"No es posible cargar Usuario");
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                if(call.isCanceled()) {
                    Log.e(TAG, "La solicitud fue abortada");
                }else {
                    Log.e(TAG,"No se puede enviar la solicitud a la API");
                }
            }
        });
    }

    private void actualizarUsuario(String nombre, String correo){
        mApiService.actualizarUsuario("Bearer "+tokenAutentificacion, nombre, correo).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.code() == 200) {
                    nombreUsuario.setText(response.body().getName());
                    correoUsuario.setText(response.body().getEmail());
                    Toast.makeText(getApplicationContext(), "Actualizado exitosamente", Toast.LENGTH_LONG).show();
                }else{
                    Log.e(TAG,"No es posible actualizar Usuario");
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                if(call.isCanceled()) {
                    Log.e(TAG, "La solicitud fue abortada");
                }else {
                    Log.e(TAG,"No se puede enviar la solicitud a la API");
                }
            }
        });
    }
}
