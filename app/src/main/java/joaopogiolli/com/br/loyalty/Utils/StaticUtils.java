package joaopogiolli.com.br.loyalty.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;

import joaopogiolli.com.br.loyalty.Models.Estabelecimento;
import joaopogiolli.com.br.loyalty.Models.Usuario;

/**
 * Created by jlago on 20/02/2018.
 */

public class StaticUtils {

    public static final String PACK = "joaopogiolli.com.br.loyalty.Utils";
    public static final int COD_PHONE = 123;
    public static final int COD_ACCESS_COARSE_LOCATION = 234;
    public static final int COD_ACCESS_FINE_LOCATION = 345;
    public static final int COD_CAMERA = 456;
    public static final int COD_CROP = 567;
    public static final String TABELA_USUARIO = "Usuarios";
    public static final String TABELA_ESTABELECIMENTO = "Estabelecimentos";
    public static final String TABELA_CARTAOES = "Cartoes";
    public static final String TABELA_PROMOCAO = "Promocoes";
    public static final String PERFIL_IMAGES = "fotosPerfil";
    public static final String ESTABELECIMENTO_IMAGES = "fotosEstabelecimento";
    public static final String FIREBASE_AUTH_ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE";
    public static final String FIREBASE_AUTH_ERROR_WEAK_PASSWORD = "ERROR_WEAK_PASSWORD";
    public static final String FIREBASE_AUTH_ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL";
    public static final String PUT_EXTRA_TIPO_USUARIO = "TIPO_USUARIO";
    public static final String PUT_EXTRA_TIPO_ESTABELECIMENTO = "TIPO_ESTABELECIMENTO";
    public static final String IMAGEM_PERFIL = "IMAGEM_PERFIL";

    public static void Toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void salvarEstabelecimentoOnSharedPreferences(Context context, Estabelecimento estabelecimento) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PACK,
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String EstabelecimentoJson = gson.toJson(estabelecimento);
        sharedPreferences
                .edit()
                .putString(PUT_EXTRA_TIPO_ESTABELECIMENTO, EstabelecimentoJson)
                .commit();
    }

    public static Estabelecimento getEstabelecimentoOnSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PACK,
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
        return gson
                .fromJson(sharedPreferences.getString(PUT_EXTRA_TIPO_ESTABELECIMENTO, ""),
                        Estabelecimento.class);
    }

    public static void salvarUsuarioOnSharedPreferences(Context context, Usuario usuario) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PACK,
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String UsuarioJson = gson.toJson(usuario);
        sharedPreferences
                .edit()
                .putString(PUT_EXTRA_TIPO_USUARIO, UsuarioJson)
                .commit();
    }

    public static Usuario getUsuarioOnSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PACK,
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
        return gson
                .fromJson(sharedPreferences.getString(PUT_EXTRA_TIPO_USUARIO, ""),
                        Usuario.class);
    }

    public static void deleteOnSharedPreferences(Context context, String chave) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PACK,
                Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(chave).commit();
        deleteImagemOnSharedPreferences(context);
    }

    public static void deleteImagemOnSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PACK,
                Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(IMAGEM_PERFIL).commit();
    }

}
