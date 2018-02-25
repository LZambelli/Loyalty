package joaopogiolli.com.br.loyalty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import joaopogiolli.com.br.loyalty.Fragments.BuscaUsuarioFragment;
import joaopogiolli.com.br.loyalty.Fragments.CadastroPromocaoFragment;
import joaopogiolli.com.br.loyalty.Fragments.ListaPromocoesFragment;
import joaopogiolli.com.br.loyalty.Models.Estabelecimento;
import joaopogiolli.com.br.loyalty.Models.Promocao;
import joaopogiolli.com.br.loyalty.Utils.StaticUtils;

public class PrincipalEstabelecimentoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageDatabase;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private FloatingActionButton floatingActionButtonActivityEstabelecimentoPrincipal;
    private ActionBar actionBar;
    private boolean mToolBarNavigationListenerIsRegistered = false;

    private Estabelecimento estabelecimento;

    private ImageView imageViewFotoActivityEstabelecimentoPrincipal;
    private TextView textViewNomeActivityEstabelecimentoPrincipal;
    private TextView textViewEmailActivityEstabelecimentoPrincipal;
    private ListView listViewFragmentListaPromocoes;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_estabelecimento);
        initViews();
        initClicks();
        if (savedInstanceState != null) {
            resolveUpButtonWithFragmentStack();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseDatabase = FirebaseUtils.getFirebaseDatabase(this);
        databaseReference = FirebaseUtils.getDatabaseReference(firebaseDatabase);
        firebaseUser = FirebaseUtils.getFirebaseUser();
        firebaseStorage = FirebaseUtils.getFirebaseStorage();
        verificaEstabelecimento();
        setFragment();
    }

    @Override
    protected void onResume() {
        Fragment ultimoFragment = getFragmentAtual();
        if (ultimoFragment != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.FrameLayoutActivityEstabelecimentoPrincipal, ultimoFragment);
            fragmentTransaction.commit();
            if (ultimoFragment.getTag() == StaticUtils.FRAGMENT_CADASTRO_PROMOCAO) {
                if (floatingActionButtonActivityEstabelecimentoPrincipal.getVisibility() == View.GONE) {
                    floatingActionButtonActivityEstabelecimentoPrincipal.setVisibility(View.GONE);
                }
            }
        }
        super.onResume();
        initListenners();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawerLayoutActivityEstabelecimentoPrincipal);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int backStackCount = getSupportFragmentManager().getBackStackEntryCount();

            if (backStackCount >= 1) {
                getSupportFragmentManager().popBackStack();
                if (backStackCount == 1) {
                    showUpButton(false);
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_exit_ActivityEstabelecimentoPrincipal:
                FirebaseUtils.logOut();
                StaticUtils.deleteOnSharedPreferences(this, StaticUtils.PUT_EXTRA_TIPO_ESTABELECIMENTO);
                finish();
                break;
            case R.id.nav_users_cards_ActivityEstabelecimentoPrincipal:

        }

        DrawerLayout drawer = findViewById(R.id.drawerLayoutActivityEstabelecimentoPrincipal);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbarActivityEstabelecimentoPrincipal);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

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

        fragmentManager = getSupportFragmentManager();
    }

    private void initListenners() {
        databaseReference
                .child(StaticUtils.TABELA_ESTABELECIMENTO)
                .orderByChild(StaticUtils.ID)
                .equalTo(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                estabelecimento = snapshot.getValue(Estabelecimento.class);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void initClicks() {
        floatingActionButtonActivityEstabelecimentoPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpButton(true);
                floatingActionButtonActivityEstabelecimentoPrincipal.setVisibility(View.GONE);
                CadastroPromocaoFragment cadastroPromocaoFragment = new CadastroPromocaoFragment();
                Bundle bundle = new Bundle();
                bundle.putString(StaticUtils.PUT_EXTRA_TIPO_ESTABELECIMENTO, new Gson().toJson(estabelecimento));
                cadastroPromocaoFragment.setArguments(bundle);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.FrameLayoutActivityEstabelecimentoPrincipal,
                        cadastroPromocaoFragment, StaticUtils.FRAGMENT_CADASTRO_PROMOCAO);
                fragmentTransaction.addToBackStack(StaticUtils.FRAGMENT_CADASTRO_PROMOCAO);
                fragmentTransaction.commit();
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

    private void setFragment() {
        ListaPromocoesFragment listaPromocoesFragment = new ListaPromocoesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(StaticUtils.PUT_EXTRA_TIPO_ESTABELECIMENTO, new Gson().toJson(estabelecimento));
        listaPromocoesFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayoutActivityEstabelecimentoPrincipal,
                listaPromocoesFragment, StaticUtils.FRAGMENT_LISTA_PROMOCOES);
        fragmentTransaction.commit();
    }

    private void resolveUpButtonWithFragmentStack() {
        showUpButton(getSupportFragmentManager().getBackStackEntryCount() > 0);
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

    private Fragment getFragmentAtual() {
        Fragment currentFragment = null;
        if (fragmentManager.getBackStackEntryCount() > 0) {
            String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        } else if(fragmentManager.getBackStackEntryCount() == 0){
            currentFragment = fragmentManager.findFragmentByTag(StaticUtils.FRAGMENT_LISTA_PROMOCOES);
        }
        return currentFragment;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, final ContextMenu.ContextMenuInfo menuInfo) {

        MenuItem deletar = menu.add(0, 1, 0, getString(R.string.excluir));
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        MenuItem editar = menu.add(0, 2, 0, getString(R.string.editar));
        editar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        MenuItem atribuirCarimbo = menu.add(0, 3, 0, getString(R.string.atribuirCarimbo));
        atribuirCarimbo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Fragment fragmentAtual = getFragmentAtual();
                View view = fragmentAtual.getView();
                if (view != null) {
                    listViewFragmentListaPromocoes = view.findViewById(R.id.listViewFragmentListaPromocoes);
                }
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                Promocao promocao = (Promocao) listViewFragmentListaPromocoes.getItemAtPosition(info.position);
                showUpButton(true);
                BuscaUsuarioFragment buscaUsuarioFragment = new BuscaUsuarioFragment();
                Bundle bundle = new Bundle();
                bundle.putString(StaticUtils.PUT_EXTRA_TIPO_PROMOCAO, new Gson().toJson(promocao));
                buscaUsuarioFragment.setArguments(bundle);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.FrameLayoutActivityEstabelecimentoPrincipal,
                        buscaUsuarioFragment, StaticUtils.FRAGMENT_BUSCA_USUARIO);
                fragmentTransaction.addToBackStack(StaticUtils.FRAGMENT_BUSCA_USUARIO);
                fragmentTransaction.commit();
                return true;
            }
        });
    }
}
