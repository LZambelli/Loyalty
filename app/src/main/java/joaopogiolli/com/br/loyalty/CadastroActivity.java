package joaopogiolli.com.br.loyalty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import de.hdodenhof.circleimageview.CircleImageView;
import joaopogiolli.com.br.loyalty.Firebase.FirebaseUtils;
import joaopogiolli.com.br.loyalty.Models.Estabelecimento;
import joaopogiolli.com.br.loyalty.Models.Usuario;
import joaopogiolli.com.br.loyalty.Utils.StaticUtils;

public class CadastroActivity extends AppCompatActivity {

    private CircleImageView imageViewActivityCadastroUsuario;
    private FloatingActionButton FloatingActionButtonFotoActivityCadastro;
    private TextInputLayout textInputLayoutNomeActivityCadastroUsuario;
    private EditText editTextNomeActivityCadastroUsuario;
    private TextInputLayout textInputLayoutEmailActivityCadastroUsuario;
    private EditText editTextEmailActivityCadastroUsuario;
    private TextInputLayout textInputLayoutSenhaActivityCadastroUsuario;
    private EditText editTextSenhaActivityCadastroUsuario;
    private TextInputLayout textInputLayoutCelularActivityCadastroUsuario;
    private EditText editTextCelularActivityCadastroUsuario;
    private TextInputLayout textInputLayoutEnderecoActivityCadastroUsuario;
    private EditText editTextEnderecoActivityCadastroUsuario;
    private TextInputLayout textInputLayoutDescricaoActivityCadastroUsuario;
    private EditText editTextDescricaoActivityCadastroUsuario;
    private Switch switchIsEstabelecimento;
    private Button buttonActivityCadastroUsuario;
    private TextView textViewJaPossuiCadastroActivityCadastroUsuario;
    private LinearLayout linearLayoutEstabelecimento;
    private ProgressDialog progressDialog;
    private boolean ehEstabelecimento;
    private String nome;
    private String email;
    private String senha;
    private String celular;
    private String endereco;
    private String descricao;
    private Uri resultUri;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        initView();
        initClicks();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case StaticUtils.COD_CROP:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    resultUri = result.getUri();
                    try {
                        Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                        imageViewActivityCadastroUsuario.setImageBitmap(photo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseDatabase = FirebaseUtils.getFirebaseDatabase(this);
        databaseReference = FirebaseUtils.getDatabaseReference(firebaseDatabase);
        firebaseAuth = FirebaseUtils.getFirebaseAuth();
        firebaseStorage = FirebaseUtils.getFirebaseStorage();
    }

    private void initView() {
        imageViewActivityCadastroUsuario = findViewById(R.id.imageViewActivityCadastroUsuario);
        FloatingActionButtonFotoActivityCadastro = findViewById(R.id.FloatingActionButtonFotoActivityCadastro);
        textInputLayoutNomeActivityCadastroUsuario = findViewById(R.id.textInputLayoutNomeActivityCadastroUsuario);
        editTextNomeActivityCadastroUsuario = findViewById(R.id.editTextNomeActivityCadastroUsuario);
        textInputLayoutEmailActivityCadastroUsuario = findViewById(R.id.textInputLayoutEmailActivityCadastroUsuario);
        editTextEmailActivityCadastroUsuario = findViewById(R.id.editTextEmailActivityCadastroUsuario);
        textInputLayoutSenhaActivityCadastroUsuario = findViewById(R.id.textInputLayoutSenhaActivityCadastroUsuario);
        editTextSenhaActivityCadastroUsuario = findViewById(R.id.editTextSenhaActivityCadastroUsuario);

        textInputLayoutCelularActivityCadastroUsuario = findViewById(R.id.textInputLayoutCelularActivityCadastroUsuario);
        editTextCelularActivityCadastroUsuario = findViewById(R.id.editTextCelularActivityCadastroUsuario);
        MaskEditTextChangedListener maskCelular = new MaskEditTextChangedListener("(##) #####-####",
                editTextCelularActivityCadastroUsuario);
        editTextCelularActivityCadastroUsuario.addTextChangedListener(maskCelular);

        textInputLayoutEnderecoActivityCadastroUsuario = findViewById(R.id.textInputLayoutEnderecoActivityCadastroUsuario);
        editTextEnderecoActivityCadastroUsuario = findViewById(R.id.editTextEnderecoActivityCadastroUsuario);
        textInputLayoutDescricaoActivityCadastroUsuario = findViewById(R.id.textInputLayoutDescricaoActivityCadastroUsuario);
        editTextDescricaoActivityCadastroUsuario = findViewById(R.id.editTextDescricaoActivityCadastroUsuario);
        switchIsEstabelecimento = findViewById(R.id.switchIsEstabelecimento);
        buttonActivityCadastroUsuario = findViewById(R.id.buttonActivityCadastroUsuario);
        textViewJaPossuiCadastroActivityCadastroUsuario = findViewById(R.id.textViewJaPossuiCadastroActivityCadastroUsuario);
        linearLayoutEstabelecimento = findViewById(R.id.linearLayoutEstabelecimento);
        progressDialog = new ProgressDialog(this);
        ehEstabelecimento = false;
    }

    private void initClicks() {

        FloatingActionButtonFotoActivityCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CadastroActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CadastroActivity.this, new String[]{android.Manifest.permission.CAMERA}, StaticUtils.COD_CAMERA);
                } else {
                    camera();
                }
            }
        });

        switchIsEstabelecimento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ehEstabelecimento = !ehEstabelecimento;
                if (ehEstabelecimento) {
                    linearLayoutEstabelecimento.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutEstabelecimento.setVisibility(View.GONE);
                }
            }
        });

        buttonActivityCadastroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaCampos()) {
                    progressDialog.setTitle(getString(R.string.aguarde));
                    progressDialog.setMessage(getString(R.string.criando_perfil));
                    progressDialog.show();
                    if (ehEstabelecimento) {
                        createUserEstabelecimento();
                    } else {
                        createUserUsuario();
                    }
                }
            }
        });

        textViewJaPossuiCadastroActivityCadastroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void camera() {
        Intent intent = CropImage.activity(null).getIntent(this);
        startActivityForResult(intent, StaticUtils.COD_CROP);
    }

    private void Toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void createUserUsuario() {
        firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(CadastroActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()) {
                            if (task.isSuccessful()) {
                                Usuario usuario = initUsuario(task.getResult().getUser().getUid());
                                databaseReference.child(StaticUtils.TABELA_USUARIO)
                                        .child(usuario.getId()).setValue(usuario);
                                saveImage(null, usuario);
                            } else {
                                String erro = ((FirebaseAuthException) task.getException()).getErrorCode();
                                getErrorFirebaseAuth(erro);
                            }
                        }
                    }
                });
    }

    private void createUserEstabelecimento() {
        firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(CadastroActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()) {
                            if (task.isSuccessful()) {
                                Estabelecimento estabelecimento = initEstabelecimento(task.getResult().getUser().getUid());
                                databaseReference.child(StaticUtils.TABELA_ESTABELECIMENTO)
                                        .child(estabelecimento.getId()).setValue(estabelecimento);
                                saveImage(estabelecimento, null);
                            } else {
                                String erro = ((FirebaseAuthException) task.getException()).getErrorCode();
                                getErrorFirebaseAuth(erro);
                            }
                        }
                    }
                });
    }

    private void saveImage(Estabelecimento estabelecimento, Usuario usuario) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
        } catch (IOException e) {
            Toast(getString(R.string.ErroGravarImagem));
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        String path = null;
        if (ehEstabelecimento) {
            path = StaticUtils.ESTABELECIMENTO_IMAGES + "/" + estabelecimento.getId() + ".png";
        } else {
            path = StaticUtils.PERFIL_IMAGES + "/" + usuario.getId() + ".png";
        }
        StorageReference storageReference = firebaseStorage.getReference(path);
        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnSuccessListener(CadastroActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                if (ehEstabelecimento) {
                    Toast(getString(R.string.estabelecimentoCriadoSucesso));
                } else {
                    Toast(getString(R.string.usuarioCriadoSucesso));
                }
                CadastroActivity.this.finish();
            }
        });
    }

    private void getErrorFirebaseAuth(String error) {
        switch (error) {
            case StaticUtils.FIREBASE_AUTH_ERROR_EMAIL_ALREADY_IN_USE:
                progressDialog.dismiss();
                Toast(getString(R.string.EmailSendoUsado));
                break;
            case StaticUtils.FIREBASE_AUTH_ERROR_WEAK_PASSWORD:
                progressDialog.dismiss();
                Toast(getString(R.string.SenhaFraca));
                break;
            case StaticUtils.FIREBASE_AUTH_ERROR_INVALID_EMAIL:
                progressDialog.dismiss();
                Toast(getString(R.string.EmailInvalido));
                break;
            default:
                Toast(getString(R.string.ErroCriarUsuario));
                progressDialog.dismiss();
                break;
        }
    }

    private boolean verificaCampos() {
        boolean retorno;
        nome = editTextNomeActivityCadastroUsuario.getText().toString();
        email = editTextEmailActivityCadastroUsuario.getText().toString();
        senha = editTextSenhaActivityCadastroUsuario.getText().toString();
        celular = editTextCelularActivityCadastroUsuario.getText().toString();
        endereco = editTextEnderecoActivityCadastroUsuario.getText().toString();
        descricao = editTextDescricaoActivityCadastroUsuario.getText().toString();
        retorno = verificaCamposUsuario();
        if (ehEstabelecimento) {
            retorno = verificaCamposEstabelecimento();
        }
        return retorno;
    }

    private boolean verificaCamposUsuario() {
        boolean retorno = true;
        if (resultUri == null) {
            retorno = false;
            Toast(getString(R.string.InsiraFotoPerfil));
        }
        if (nome.isEmpty() || nome.equals(null)) {
            retorno = false;
            textInputLayoutNomeActivityCadastroUsuario.setHintTextAppearance(R.style.TextLabelErrorHint);
            textInputLayoutNomeActivityCadastroUsuario.setError(getString(R.string.erroPreenchimentoObrigatorio));
        } else {
            textInputLayoutNomeActivityCadastroUsuario.setError(null);
            textInputLayoutNomeActivityCadastroUsuario.setHintTextAppearance(R.style.TextLabelHint);
        }

        if (email.isEmpty() || email.equals(null)) {
            retorno = false;
            textInputLayoutEmailActivityCadastroUsuario.setHintTextAppearance(R.style.TextLabelErrorHint);
            textInputLayoutEmailActivityCadastroUsuario.setError(getString(R.string.erroPreenchimentoObrigatorio));
        } else {
            textInputLayoutEmailActivityCadastroUsuario.setError(null);
            textInputLayoutEmailActivityCadastroUsuario.setHintTextAppearance(R.style.TextLabelHint);
        }

        if (senha.isEmpty() || senha.equals(null)) {
            retorno = false;
            textInputLayoutSenhaActivityCadastroUsuario.setHintTextAppearance(R.style.TextLabelErrorHint);
            textInputLayoutSenhaActivityCadastroUsuario.setError(getString(R.string.erroPreenchimentoObrigatorio));
        } else {
            textInputLayoutSenhaActivityCadastroUsuario.setError(null);
            textInputLayoutSenhaActivityCadastroUsuario.setHintTextAppearance(R.style.TextLabelHint);
        }

        if (celular.isEmpty() || celular.equals(null)) {
            retorno = false;
            textInputLayoutCelularActivityCadastroUsuario.setHintTextAppearance(R.style.TextLabelErrorHint);
            textInputLayoutCelularActivityCadastroUsuario.setError(getString(R.string.erroPreenchimentoObrigatorio));
        } else {
            textInputLayoutCelularActivityCadastroUsuario.setError(null);
            textInputLayoutCelularActivityCadastroUsuario.setHintTextAppearance(R.style.TextLabelHint);
        }

        return retorno;
    }

    private boolean verificaCamposEstabelecimento() {
        boolean retorno = true;
        if (endereco.isEmpty() || endereco.equals(null)) {
            retorno = false;
            textInputLayoutEnderecoActivityCadastroUsuario.setHintTextAppearance(R.style.TextLabelErrorHint);
            textInputLayoutEnderecoActivityCadastroUsuario.setError(getString(R.string.erroPreenchimentoObrigatorio));
        } else {
            textInputLayoutEnderecoActivityCadastroUsuario.setError(null);
            textInputLayoutEnderecoActivityCadastroUsuario.setHintTextAppearance(R.style.TextLabelHint);
        }

        if (descricao.isEmpty() || descricao.equals(null)) {
            retorno = false;
            textInputLayoutDescricaoActivityCadastroUsuario.setHintTextAppearance(R.style.TextLabelErrorHint);
            textInputLayoutDescricaoActivityCadastroUsuario.setError(getString(R.string.erroPreenchimentoObrigatorio));
        } else {
            textInputLayoutDescricaoActivityCadastroUsuario.setError(null);
            textInputLayoutDescricaoActivityCadastroUsuario.setHintTextAppearance(R.style.TextLabelHint);
        }

        return retorno;
    }

    private Estabelecimento initEstabelecimento(String id) {
        Estabelecimento estabelecimento = new Estabelecimento();
        estabelecimento.setId(id);
        estabelecimento.setNome(nome);
        estabelecimento.setEmail(email);
        estabelecimento.setSenha(null);
        estabelecimento.setCelular(celular);
        estabelecimento.setEndereco(endereco);
        estabelecimento.setDescricao(descricao);
        return estabelecimento;
    }

    private Usuario initUsuario(String id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(null);
        usuario.setCelular(celular);
        return usuario;
    }
}
