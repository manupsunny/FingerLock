package com.manusunny.fingerlock.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;

import com.manusunny.fingerlock.R;
import com.manusunny.fingerlock.elements.AppListAdapter;
import com.manusunny.fingerlock.utilities.AppListingUtility;

import java.util.ArrayList;

public class InstalledAppsActivity extends AppCompatActivity {
    private EditText searchBox;
    private AppListingUtility appListingUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installed_apps);
        try {
            getSupportActionBar().setTitle("Installed Apps");
        } catch (Exception e) {
            e.printStackTrace();
        }
        appListingUtility = AppListingUtility.getInstance(this);
        fillData(appListingUtility.installedAppInfos);
        setSearchBar();
    }

    public void fillData(final ArrayList<ApplicationInfo> appInfos) {
        AbsListView mListViewUnlocked = (AbsListView) findViewById(R.id.list_unlocked);
        mListViewUnlocked.setAdapter(new AppListAdapter(this, appInfos));
        mListViewUnlocked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent intent = new Intent(InstalledAppsActivity.this, AppDetailsActivity.class);
                intent.putExtra("package", appInfos.get(position).packageName);
                intent.putExtra("type", "installed");
                startActivityForResult(intent, 1);
            }
        });
    }

    private void setSearchBar() {
        searchBox = (EditText) findViewById(R.id.searchBox);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    fillData(appListingUtility.installedAppInfos);
                    return;
                }

                ArrayList<ApplicationInfo> appInfos = new ArrayList<>();
                for (ApplicationInfo info : appListingUtility.installedAppInfos) {
                    final String appName = getPackageManager().getApplicationLabel(info).toString().toLowerCase();
                    if (appName.contains(s.toString().toLowerCase())) {
                        appInfos.add(info);
                    }
                }
                fillData(appInfos);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        searchBox.setText("");
        fillData(appListingUtility.installedAppInfos);
    }
}
