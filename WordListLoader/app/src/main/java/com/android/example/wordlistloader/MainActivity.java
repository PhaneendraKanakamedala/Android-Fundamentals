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

package com.android.example.wordlistloader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import static com.android.example.wordlistloader.Contract.CONTENT_PATH;
import static com.android.example.wordlistloader.Contract.CONTENT_URI;
import static com.android.example.wordlistloader.Contract.WordList.KEY_ID;

/**
 * Implements a RecyclerView that displays a list of words from a SQL database.
 * - Clicking the fab button opens a second activity to add a word to the database.
 * - Clicking the Edit button opens an activity to edit the current word in the database.
 * - Clicking the Delete button deletes the current word from the database.
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Request code for editing and updating.
    public static final int WORD_EDIT = 1;
    // Value of id if it's a new word.
    public static final int WORD_ADD = -1;

    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportLoaderManager().initLoader(0, null, this);

        // Create recycler view.
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new WordListAdapter(this);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add a floating action button click handler for creating new entries.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EditWordActivity.class);
                startActivityForResult(intent, WORD_EDIT);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String queryUri = CONTENT_URI.toString();
        String[] projection = new String[] {CONTENT_PATH};
        return new CursorLoader(this, Uri.parse(queryUri), projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.setData(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.setData(null);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WORD_EDIT) {
            if (resultCode == RESULT_OK) {
                String word = data.getStringExtra(EditWordActivity.EXTRA_REPLY);

                // Update the database
                if (!TextUtils.isEmpty(word)) {
                    ContentValues values = new ContentValues();
                    values.put("word", word);
                    int id = data.getIntExtra(WordListAdapter.EXTRA_ID, -99);

                    if (id == WORD_ADD) {
                        getContentResolver().insert(CONTENT_URI, values);
                    } else if (id >= 0) {
                        String[] selectionArgs = {Integer.toString(id)};
                        getContentResolver().update(CONTENT_URI, values, KEY_ID, selectionArgs);
                    }
                    // Update the UI (The loader should do this for us, now, when the data is ready.
                    // But the loader does not get called. And so the data does not get set...
                    // This is a known issue, and restarting the loader is the solution.
                    // mAdapter.notifyDataSetChanged();
                    getSupportLoaderManager().restartLoader(0, null, this);
                } else {
                    Toast.makeText(
                            getApplicationContext(), R.string.empty_word_not_saved,
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}