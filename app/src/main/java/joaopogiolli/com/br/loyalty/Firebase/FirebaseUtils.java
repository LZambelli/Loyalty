package joaopogiolli.com.br.loyalty.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by jlago on 20/02/2018.
 */

public class FirebaseUtils {

    public static final String TABELA_USUARIO = "Usuarios";
    public static final String TABELA_ESTABELECIMENTO = "Estabelecimentos";
    public static final String TABELA_CARTAOES = "Cartoes";
    public static final String TABELA_PROMOCAO = "Promocoes";
    private static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;
    private static FirebaseUser firebaseUser;
    private static FirebaseDatabase firebaseDatabase;

    private FirebaseUtils() {
    }

    public static FirebaseDatabase getFirebaseDatabase(Context context) {
        if (firebaseDatabase == null) {
            FirebaseApp.initializeApp(context);
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
        }
        return firebaseDatabase;
    }

    public static DatabaseReference getDatabaseReference(FirebaseDatabase firebaseDatabase) {
        return firebaseDatabase.getReference();
    }

    public static FirebaseAuth getFirebaseAuth() {
        if (firebaseAuth == null) {
            initFirebase();
        }
        return firebaseAuth;
    }

    public static FirebaseStorage getFirebaseStorage() {
        return FirebaseStorage.getInstance();
    }

    private static void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    firebaseUser = user;
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public static FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public static void logOut() {
        firebaseAuth.signOut();
    }

}

