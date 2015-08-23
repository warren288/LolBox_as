package com.warrren.introspection;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * @author:yangsheng
 * @date:2015/7/12
 */
public class SetterFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.set);
    }
}
