package joaopogiolli.com.br.loyalty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import joaopogiolli.com.br.loyalty.Models.Estabelecimento;
import joaopogiolli.com.br.loyalty.Utils.StaticUtils;

public class PrincipalEstabelecimentoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageDatabase;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private FloatingActionButton floatingActionButtonActivityEstabelecimentoPrincipal;

    private Estabelecimento estabelecimento;

    private ImageView imageViewFotoActivityEstabelecimentoPrincipal;
    private TextView textViewNomeActivityEstabelecimentoPrincipal;
    private TextView textViewEmailActivityEstabelecimentoPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_estabelecimento);
        initViews();
        initClicks();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseDatabase = FirebaseUtils.getFirebaseDatabase(this);
        databaseReference = FirebaseUtils.getDatabaseReference(firebaseDatabase);
        firebaseUser = FirebaseUtils.getFirebaseUser();
        firebaseStorage = FirebaseUtils.getFirebaseStorage();
        verificaEstabelecimento();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawerLayoutActivityEstabelecimentoPrincipal);
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

        if (id == R.id.nav_exit_ActivityEstabelecimentoPrincipal) {
            FirebaseUtils.logOut();
            StaticUtils.deleteOnSharedPreferences(this, StaticUtils.PUT_EXTRA_TIPO_ESTABELECIMENTO);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawerLayoutActivityEstabelecimentoPrincipal);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbarActivityEstabelecimentoPrincipal);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawerLayoutActivityEstabelecimentoPrincipal);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigationViewActivityEstabelecimentoPrincipal);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);

        floatingActionButtonActivityEstabelecimentoPrincipal = findViewById(R.id.floatingActionButtonActivityEstabelecimentoPrincipal);

        imageViewFotoActivityEstabelecimentoPrincipal = headerLayout.findViewById(R.id.imageViewFotoActivityEstabelecimentoPrincipal);
        textViewNomeActivityEstabelecimentoPrincipal = headerLayout.findViewById(R.id.textViewNomeActivityEstabelecimentoPrincipal);
        textViewEmailActivityEstabelecimentoPrincipal = headerLayout.findViewById(R.id.textViewEmailActivityEstabelecimentoPrincipal);

        Intent thisIntent = getIntent();
        Bundle extras = thisIntent.getExtras();
        if (extras != null) {
            estabelecimento = (Estabelecimento) extras.get(StaticUtils.PUT_EXTRA_TIPO_USUARIO);
        }
    }

    private void initClicks(){
        floatingActionButtonActivityEstabelecimentoPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void verificaEstabelecimento() {
        if (firebaseUser == null) {
            finish();
        } else {
            setDados();
        }
    }

    private void setDados() {
        storageDatabase = firebaseStorage.getReference(StaticUtils.ESTABELECIMENTO_IMAGES + "/" + estabelecimento.getId() + ".png");
        if (storageDatabase != null) {
            storageDatabase.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(PrincipalEstabelecimentoActivity.this)
                            .load(uri)
                            .into(imageViewFotoActivityEstabelecimentoPrincipal);
                }
            });
        }
        textViewNomeActivityEstabelecimentoPrincipal.setText(estabelecimento.getNome());
        textViewEmailActivityEstabelecimentoPrincipal.setText(estabelecimento.getEmail());
    }

}
