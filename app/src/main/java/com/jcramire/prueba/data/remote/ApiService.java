package com.jcramire.prueba.data.remote;

import com.jcramire.prueba.data.model.Token;
import com.jcramire.prueba.data.model.Usuario;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface ApiService {

    @POST("facebook")
    @FormUrlEncoded
    Call<Token> obtenerAutorizacion(@Field("token") String token);

    @GET("profile")
    Call<Usuario> mostrarUsuario(@Header("Authorization") String authToken);

    @PUT("profile")
    @FormUrlEncoded
    Call<Usuario> actualizarUsuario(@Header("Authorization") String authToken, @Field("name") String nombre, @Field("email") String email);

}
