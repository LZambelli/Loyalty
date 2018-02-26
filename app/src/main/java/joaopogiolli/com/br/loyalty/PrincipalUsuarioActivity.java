package joaopogiolli.com.br.loyalty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import joaopogiolli.com.br.loyalty.Firebase.FirebaseUtils;
import joaopogiolli.com.br.loyalty.Fragments.ListaCartoesFragment;
import joaopogiolli.com.br.loyalty.Fragments.ListaPromocoesFragment;
import joaopogiolli.com.br.loyalty.Models.Estabelecimento;
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
    private ActionBar actionBar;
    private NavigationView navigationView;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private Usuario usuario;

    private ImageView imageViewFotoActivityUsuarioPrincipal;
    private TextView textViewEmailActivityUsuarioPrincipal;
    private TextView textViewNomeUsuarioActivityUsuarioPrincipal;
    private boolean mToolBarNavigationListenerIsRegistered = false;

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
        setFragment();
    }

    @Override
    protected void onResume() {
        Fragment ultimoFragment = getFragmentAtual();
        if (ultimoFragment != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.FrameLayoutActivityEstabelecimentoPrincipal, ultimoFragment);
            fragmentTransaction.commit();
        }
        super.onResume();
        initListenners();
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
        actionBar = getSupportActionBar();

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

        fragmentManager = getSupportFragmentManager();
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

    private void setFragment() {
        ListaCartoesFragment listaCartoesFragment = new ListaCartoesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(StaticUtils.PUT_EXTRA_TIPO_USUARIO, new Gson().toJson(usuario));
        listaCartoesFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayoutActivityUsuarioPrincipal,
                listaCartoesFragment, StaticUtils.FRAGMENT_LISTA_CARTOES);
        fragmentTransaction.commit();
    }

    private Fragment getFragmentAtual() {
        Fragment currentFragment = null;
        if (fragmentManager.getBackStackEntryCount() > 0) {
            String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        }
        return currentFragment;
    }

    private void showUpButton(boolean show) {
        if (show) {
            toggle.setDrawerIndicatorEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            actionBar.setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }
    }

    private void initListenners() {
        databaseReference
                .child(StaticUtils.TABELA_USUARIO)
                .orderByChild(StaticUtils.ID)
                .equalTo(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                usuario = snapshot.getValue(Usuario.class);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}
