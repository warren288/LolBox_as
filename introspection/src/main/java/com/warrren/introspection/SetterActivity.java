package com.warrren.introspection;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * @author:yangsheng
 * @date:2015/7/12
 */
public class SetterActivity extends AppCompatActivity {

    private Toolbar mTb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setter);
        mTb = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(mTb);
        mTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        mTb.setTitle(R.string.title_set);
        getFragmentManager().beginTransaction().add(R.id.container, new SetterFragment()).commit();
//        getSupportFragmentManager().beginTransaction().add(R.id.container, new SetterFragment()).commit();
    }
}
