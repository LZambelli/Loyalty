package joaopogiolli.com.br.loyalty;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import joaopogiolli.com.br.loyalty.Firebase.FirebaseUtils;
import joaopogiolli.com.br.loyalty.Models.Estabelecimento;
import joaopogiolli.com.br.loyalty.Models.Usuario;
import joaopogiolli.com.br.loyalty.Utils.StaticUtils;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textInputLayoutEmailActivityLogin;
    private EditText editTextEmail;
    private TextInputLayout textInputLayoutSenhaActivityLogin;
    private EditText editTextSenha;
    private TextView textViewEsqueceuSenhaActivityLogin;
    private Button buttonActivityLogin;
    private TextView textViewCadastrarActivityLogin;
    private String email;
    private String senha;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initClicks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!verificaConexao(this)) {
            StaticUtils.Toast(this, getString(R.string.naoPossuiConexao));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseUtils.getFirebaseAuth();
        firebaseDatabase = FirebaseUtils.getFirebaseDatabase(this);
        databaseReference = FirebaseUtils.getDatabaseReference(firebaseDatabase);
        if (firebaseAuth.getCurrentUser() != null) {
            escolheTela();
        }
    }

    private void initView() {
        textInputLayoutEmailActivityLogin = findViewById(R.id.textInputLayoutEmailActivityLogin);
        editTextEmail = findViewById(R.id.editTextEmail);
        textInputLayoutSenhaActivityLogin = findViewById(R.id.textInputLayoutSenhaActivityLogin);
        editTextSenha = findViewById(R.id.editTextSenha);
        textViewEsqueceuSenhaActivityLogin = findViewById(R.id.textViewEsqueceuSenhaActivityLogin);
        buttonActivityLogin = findViewById(R.id.buttonActivityLogin);
        textViewCadastrarActivityLogin = findViewById(R.id.textViewCadastrarActivityLogin);
    }

    private void initClicks() {
        textViewEsqueceuSenhaActivityLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                if (email.equals(null)) {
                    StaticUtils.Toast(getBaseContext(), getString(R.string.PreenchaEmail));
                } else {
                    if (!email.isEmpty()) {
                        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    StaticUtils.Toast(getBaseContext(), getString(R.string.RedefinicaoSenha));
                                }
                            }
                        });
                    } else {
                        StaticUtils.Toast(getBaseContext(), getString(R.string.PreenchaEmail));
                    }
                }
            }
        });

        buttonActivityLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificarCampos()) {
                    escolheTela();
                }
            }
        });

        textViewCadastrarActivityLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTelaCadastrar = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intentTelaCadastrar);
            }
        });
    }

    private boolean verificarCampos() {
        boolean retorno = true;
        email = editTextEmail.getText().toString();
        senha = editTextSenha.getText().toString();

        if (email.isEmpty() || email.equals(null)) {
            retorno = false;
            textInputLayoutEmailActivityLogin.setHintTextAppearance(R.style.TextLabelErrorHint);
            textInputLayoutEmailActivityLogin.setError(getString(R.string.erroPreenchimentoObrigatorio));
        } else {
            textInputLayoutEmailActivityLogin.setError(null);
            textInputLayoutEmailActivityLogin.setHintTextAppearance(R.style.TextLabelHint);
        }

        if (senha.isEmpty() || senha.equals(null)) {
            retorno = false;
            textInputLayoutSenhaActivityLogin.setHintTextAppearance(R.style.TextLabelErrorHint);
            textInputLayoutSenhaActivityLogin.setError(getString(R.string.erroPreenchimentoObrigatorio));
        } else {
            textInputLayoutSenhaActivityLogin.setError(null);
            textInputLayoutSenhaActivityLogin.setHintTextAppearance(R.style.TextLabelHint);
        }
        return retorno;
    }

    private boolean verificaConexao(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return ((netInfo != null) && (netInfo.isConnectedOrConnecting()) && (netInfo.isAvailable()));
    }

    private Usuario getUsuario(FirebaseUser firebaseUser) {
        final List<Usuario> listaUsuario = new ArrayList<>();
        Query query = databaseReference.child(StaticUtils.TABELA_USUARIO)
                .orderByChild("id")
                .equalTo(firebaseUser.getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listaUsuario.add(snapshot.getValue(Usuario.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return listaUsuario.get(0);
    }

    private Estabelecimento getEstabelecimento(FirebaseUser firebaseUser) {
        final List<Estabelecimento> listaEstabelecimento = new ArrayList<>();
        Query query = databaseReference.child(StaticUtils.TABELA_ESTABELECIMENTO)
                .orderByChild("id")
                .equalTo(firebaseUser.getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listaEstabelecimento.add(snapshot.getValue(Estabelecimento.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return listaEstabelecimento.get(0);
    }

    private void escolheTela(){
        Usuario usuario = getUsuario(firebaseAuth.getCurrentUser());
        Estabelecimento estabelecimento = getEstabelecimento(firebaseAuth.getCurrentUser());
        if (usuario != null) {

        } else if (estabelecimento != null) {

        }
    }

    private void login(String email, String senha) {
        firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(LoginActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (task.isComplete()) {
                                progressBar.setVisibility(View.GONE);
                                textViewAutenticando.setVisibility(View.GONE);
                                Intent intentListaCartoes = new Intent(LoginActivity.this, ListaCartoesActivity.class);
                                startActivity(intentListaCartoes);
                                finish();
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            textViewAutenticando.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, getString(R.string.erro_autenticacao),
                                    Toast.LENGTH_LONG).show();
                            textInputLayoutSenha.setHintTextAppearance(R.style.TextLabelErrorHint);
                            textInputLayoutSenha.setError(getString(R.string.erro_autenticacao));
                        }
                    }
                });
    }
}
