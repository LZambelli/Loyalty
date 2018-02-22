package joaopogiolli.com.br.loyalty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import joaopogiolli.com.br.loyalty.Firebase.FirebaseUtils;
import joaopogiolli.com.br.loyalty.Models.Usuario;
import joaopogiolli.com.br.loyalty.Utils.StaticUtils;

public class PrincipalUsuarioActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageDatabase;

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private Usuario usuario;
    private String pathImageView;

    private ImageView imageViewFotoActivityUsuarioPrincipal;
    private TextView textViewEmailActivityUsuarioPrincipal;
    private TextView textViewNomeUsuarioActivityUsuarioPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_usuario);
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseDatabase = FirebaseUtils.getFirebaseDatabase(this);
        databaseReference = FirebaseUtils.getDatabaseReference(firebaseDatabase);
        firebaseUser = FirebaseUtils.getFirebaseUser();
        firebaseStorage = FirebaseUtils.getFirebaseStorage();
        verificaUsuario();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawerLayoutActivityUsuarioPrincipal);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_exit_ActivityUsuarioPrincipal) {
            FirebaseUtils.logOut();
            StaticUtils.deleteOnSharedPreferences(this, StaticUtils.PUT_EXTRA_TIPO_USUARIO);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawerLayoutActivityUsuarioPrincipal);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbarActivityUsuarioPrincipal);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawerLayoutActivityUsuarioPrincipal);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigationViewActivityUsuarioPrincipal);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);

        imageViewFotoActivityUsuarioPrincipal = headerLayout.findViewById(R.id.imageViewFotoActivityUsuarioPrincipal);
        textViewNomeUsuarioActivityUsuarioPrincipal = headerLayout.findViewById(R.id.textViewNomeUsuarioActivityUsuarioPrincipal);
        textViewEmailActivityUsuarioPrincipal = headerLayout.findViewById(R.id.textViewEmailActivityUsuarioPrincipal);

        Intent thisIntent = getIntent();
        Bundle extras = thisIntent.getExtras();
        if (extras != null) {
            usuario = (Usuario) extras.get(StaticUtils.PUT_EXTRA_TIPO_USUARIO);
        }
    }

    private void verificaUsuario() {
        if (firebaseUser == null) {
            finish();
        } else {
            setDados();
        }
    }

    private void setDados() {
        storageDatabase = firebaseStorage.getReference(StaticUtils.PERFIL_IMAGES + "/" + usuario.getId() + ".png");
        if (storageDatabase != null) {
            storageDatabase.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(PrincipalUsuarioActivity.this)
                            .load(uri)
                            .into(imageViewFotoActivityUsuarioPrincipal);
                }
            });
        }
        textViewNomeUsuarioActivityUsuarioPrincipal.setText(usuario.getNome());
        textViewEmailActivityUsuarioPrincipal.setText(usuario.getEmail());
    }

}
