package com.example.bakalaurinis_v2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bakalaurinis_v2.Authentification.LoginActivity;
import com.example.bakalaurinis_v2.Fragment.FragmentFood;
import com.example.bakalaurinis_v2.Fragment.FragmentHome;
import com.example.bakalaurinis_v2.Fragment.FragmentRecipes;
import com.example.bakalaurinis_v2.Fragment.FragmentSleep;
import com.example.bakalaurinis_v2.Fragment.FragmentStatistics;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainWindowActivity extends AppCompatActivity {

    private String id;
    private String fragment;
    private Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        selectedFragment = null;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            fragment = extras.getString("fragment");
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        Bundle bundle = new Bundle();
        bundle.putString("id", id);

        switch (fragment) {
            case "recipes":
                bottomNavigationView.setSelectedItemId(R.id.nav_recipes);
                selectedFragment = new FragmentRecipes();
                break;
            case "food":
                bottomNavigationView.setSelectedItemId(R.id.nav_food);
                selectedFragment = new FragmentFood();
                break;
            case "home":
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
                selectedFragment = new FragmentHome();
                break;
            case "sleep":
                bottomNavigationView.setSelectedItemId(R.id.nav_sleep);
                selectedFragment = new FragmentSleep();
                break;
            case "statistics":
                bottomNavigationView.setSelectedItemId(R.id.nav_statistics);
                selectedFragment = new FragmentStatistics();
                break;
        }


        selectedFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                selectedFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectedFragment = null;

                Bundle bundle = new Bundle();
                bundle.putString("id", id);

                switch (menuItem.getItemId()) {
                    case R.id.nav_recipes:
                        selectedFragment = new FragmentRecipes();
                        break;
                    case R.id.nav_food:
                        selectedFragment = new FragmentFood();
                        break;
                    case R.id.nav_home:
                        selectedFragment = new FragmentHome();
                        break;
                    case R.id.nav_sleep:
                        selectedFragment = new FragmentSleep();
                        break;
                    case R.id.nav_statistics:
                        selectedFragment = new FragmentStatistics();
                        break;
                }

                selectedFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.usermenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Integer menuId = menuItem.getItemId();
        Intent intent;
        switch (menuId) {
            case R.id.nav_user:
                intent = new Intent(this, AccountActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                Toast.makeText(getBaseContext(), "Sėkmingai atsijungėte", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
