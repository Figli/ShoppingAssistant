package com.figli.shopingassistance;

import android.app.DialogFragment;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.figli.shopingassistance.adapter.TabAdapter;
import com.figli.shopingassistance.database.DBHelper;
import com.figli.shopingassistance.dialog.AddingTaskDialogFragment;
import com.figli.shopingassistance.dialog.EditTaskDialogFragment;
import com.figli.shopingassistance.fragment.CurrentTaskFragment;
import com.figli.shopingassistance.fragment.DoneTaskFragment;
import com.figli.shopingassistance.fragment.SplashFragment;
import com.figli.shopingassistance.fragment.TaskFragment;
import com.figli.shopingassistance.model.ModelTask;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AddingTaskDialogFragment.AddingTaskListener,
        CurrentTaskFragment.OnTaskDoneListener, DoneTaskFragment.OnTaskRestoreListener,
        EditTaskDialogFragment.EditingTaskListener{

    FragmentManager fragmentManager;

    PreferenceHelper preferenceHelper;

    TabAdapter tabAdapter;

    TaskFragment currentTaskFragment;
    TaskFragment doneTaskFragment;

//    SearchView searchView;

    public DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Ads.showBanner(this);

        PreferenceHelper.getInstance().init(getApplicationContext());
        preferenceHelper = PreferenceHelper.getInstance();

        dbHelper = new DBHelper(getApplicationContext());

        fragmentManager = getFragmentManager();

        runSplash();

        setUi();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        /*MenuItem splashItem = menu.findItem(R.id.action_splash);
        splashItem.setChecked(preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE));*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        /*if (id == R.id.action_splash) {
            item.setChecked(!item.isChecked());
            preferenceHelper.putBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE, item.isChecked());
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public void runSplash() {

        if (!preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE)) {

            SplashFragment splashFragment = new SplashFragment();

            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, splashFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private  void setUi() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            setSupportActionBar(toolbar);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.current_task));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.done_task));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        tabAdapter = new TabAdapter(fragmentManager, 2);

        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        currentTaskFragment = (CurrentTaskFragment) tabAdapter.getItem(TabAdapter.CURRENT_TASK_FRAGMENT_POSITION);
        doneTaskFragment = (DoneTaskFragment) tabAdapter.getItem(TabAdapter.DONE_TASK_FRAGMENT_POSITION);

        /*searchView = (SearchView) findViewById(R.id.serch_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentTaskFragment.findTask(newText);
                doneTaskFragment.findTask(newText);
                return false;
            }
        });*/


        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment addingTaskDialogFragment = new AddingTaskDialogFragment();
                addingTaskDialogFragment.show(fragmentManager, "AddingTaskDialogFragment");
            }
        });
    }

    @Override
    public void onTaskAdded(ModelTask newTask) {
       currentTaskFragment.addTask(newTask, true);
    }

    @Override
    public void onTaskAddingCancel() {
        Toast.makeText(this, "Product cancel", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskDone(ModelTask task) {
        doneTaskFragment.addTask(task, false);
    }

    @Override
    public void onTaskRestore(ModelTask task) {
        currentTaskFragment.addTask(task, false);
    }

    @Override
    public void onTaskEdited(ModelTask updateTask) {
        currentTaskFragment.updateTask(updateTask);
        dbHelper.update().task(updateTask);
    }
}
