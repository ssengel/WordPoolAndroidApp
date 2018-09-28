package com.ssengel.wordpool;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ssengel.wordpool.helper.BottomNavigationBehavior;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        toolbar.setTitle("Pool");
        loadFragment(new PoolFragment());

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_random:
                    loadFragment(new RandomFragment());
                    toolbar.setTitle("Random");
                    return true;
                case R.id.navigation_pool:
                    loadFragment(new PoolFragment());
                    toolbar.setTitle("Pool");
                    return true;
                case R.id.navigation_library:
                    loadFragment(new LibraryFragment());
                    toolbar.setTitle("Library");
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_createWord) {
            startActivity(new Intent(this.getApplicationContext(), CreateWordActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//        builder.setMessage("Are you sure you want to exit ?")
//                .setCancelable(false)
//                .setPositiveButton("Yes",diaOnClickListener)
//                .setNegativeButton("No", diaOnClickListener);
//        builder.create().show();
    }
    //todo > duzenlenmeli
//    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//    DialogInterface.OnClickListener diaOnClickListener = new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialogInterface, int i) {
//            switch (i) {
//                case DialogInterface.BUTTON_POSITIVE:
//                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                    break;
//                case DialogInterface.BUTTON_NEGATIVE:
//                    break;
//            }
//        }
//    };

}
