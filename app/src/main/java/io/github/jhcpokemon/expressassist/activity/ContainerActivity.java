package io.github.jhcpokemon.expressassist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import io.github.jhcpokemon.expressassist.R;
import io.github.jhcpokemon.expressassist.fragment.ExpressItemFragment;
import io.github.jhcpokemon.expressassist.fragment.SearchFragment;
import io.github.jhcpokemon.expressassist.fragment.SettingFragment;
import io.github.jhcpokemon.expressassist.model.ExpressLogProvider;

public class ContainerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ExpressItemFragment.OnListItemClickListener {

    private FragmentManager manager;
    private ExpressItemFragment historyFragment;
    private SearchFragment searchFragment;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manager = getSupportFragmentManager();
        historyFragment = ExpressItemFragment.newInstance();
        searchFragment = new SearchFragment();
        settingFragment = new SettingFragment();
        historyFragment.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        if (intent.getStringExtra("from").equals("login")) {
            manager.beginTransaction().add(R.id.container, historyFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            manager.beginTransaction().replace(R.id.container, searchFragment).commit();
        } else if (id == R.id.nav_history) {
            manager.beginTransaction().replace(R.id.container, historyFragment).commit();
        } else if (id == R.id.nav_setting) {
            manager.beginTransaction().replace(R.id.container, settingFragment).commit();
        } else if (id == R.id.nav_share) {
            Intent mIntent = new Intent();
            mIntent.setAction(Intent.ACTION_SEND);
            mIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_message));
            mIntent.setType("text/plain");
            startActivity(Intent.createChooser(mIntent, getResources().getString(R.string.send_to)));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListItemClicked(ExpressLogProvider.ExpressItem item) {
        Toast.makeText(getApplicationContext(), item.order, Toast.LENGTH_SHORT).show();
    }
}
