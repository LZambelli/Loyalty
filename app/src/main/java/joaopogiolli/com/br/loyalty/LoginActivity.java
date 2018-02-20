package joaopogiolli.com.br.loyalty;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        if (verificaConexao(this)) {
            Toast(getString(R.string.naoPossuiConexao));
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

            }
        });

        buttonActivityLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificarCampos()) {

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
        if ((netInfo != null) && (netInfo.isConnectedOrConnecting()) && (netInfo.isAvailable())) {
            return true;
        } else {
            return false;
        }
    }

    private void Toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
