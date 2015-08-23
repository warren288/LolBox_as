package com.vivo.performance.trackphone;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toolbar;

public class MainActivity extends Activity  {

    private DrawerLayout mDlMain;
    private ListView mLvNavigation;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mTb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTb = (Toolbar) findViewById(R.id.tb);
        setActionBar(mTb);
        mDlMain = (DrawerLayout) findViewById(R.id.dl_main_drawer);
        mLvNavigation = (ListView) findViewById(R.id.lv_navigation);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDlMain,
                R.drawable.abc_ic_ab_back_mtrl_am_alpha, R.string.abc_action_bar_home_description,
                R.string.abc_action_bar_home_description_format)
        {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };
        mDlMain.setDrawerListener(mDrawerToggle);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
