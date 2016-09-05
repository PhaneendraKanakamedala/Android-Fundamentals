/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.optionsmenuorderactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * This app shows an options menu with options as icons in the app bar
 * and more options in the overflow menu. It displays a toast message
 * showing you which option you selected.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Creates the content view, the toolbar, and
     * the floating action button.
     * This method is provided in the Basic Activity template.
     * @param savedInstanceState    Saved instance.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, R.string.snackbar_text, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
    }

    /**
     * Inflates the menu, and adds items to the action bar if it is present.
     * @param   menu    Menu to inflate.
     * @return          Returns true if the menu inflated.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handles app bar item clicks.
     * @param item  Item clicked.
     * @return      Returns true if one of the defined items was clicked.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_order:
                // Start OrderActivity if Order was clicked.
                // Intent to start OrderActivity.
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_status:
                displayToast(getString(R.string.action_status_message));
                return true;
            case R.id.action_favorites:
                displayToast(getString(R.string.action_favorites_message));
                return true;
            case R.id.action_contact:
                displayToast(getString(R.string.action_contact_message));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Shows a message that the donut was clicked.
     */
    public void showDonutOrder(View view) {
        displayToast(getString(R.string.donut_order_message));
    }

    /**
     * Shows a message that the ice cream sandwich was clicked.
     */
    public void showIceCreamOrder(View view) {
        displayToast(getString(R.string.ice_cream_order_message));
    }

    /**
     * Shows a message that the froyo was clicked.
     */
    public void showFroyoOrder(View view) {
        displayToast(getString(R.string.froyo_order_message));
    }

    /**
     * Displays the actual message in a toast message.
     * @param message   Message to display.
     */
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
