package joaopogiolli.com.br.loyalty;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class PrincipalEstabelecimentoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_estabelecimento);
        Toolbar toolbar = findViewById(R.id.toolbarActivityEstabelecimentoPrincipal);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawerLayoutActivityEstabelecimentoPrincipal);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigationViewActivityEstabelecimentoPrincipal);
        navigationView.setNavigationItemSelectedListener(this);
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
            // Handle the camera action
        }

        DrawerLayout drawer = findViewById(R.id.drawerLayoutActivityEstabelecimentoPrincipal);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
