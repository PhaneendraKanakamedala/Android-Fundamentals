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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static com.android.example.wordlistloader.Contract.CONTENT_PATH;
import static com.android.example.wordlistloader.Contract.CONTENT_URI;
import static com.android.example.wordlistloader.R.id.word;

/**
 * Simple Adapter for a RecyclerView with click handler for each item in the ViewHolder.
 */
public class WordListAdapter
        extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    class WordViewHolder extends RecyclerView.ViewHolder {
        public final TextView wordItemView;
        Button delete_button;
        Button edit_button;

        public WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = (TextView) itemView.findViewById(word);
            delete_button = (Button)itemView.findViewById(R.id.delete_button);
            edit_button = (Button)itemView.findViewById(R.id.edit_button);
        }
    }

    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_WORD = "WORD";
    public static final String EXTRA_POSITION = "POSITION";

    private Cursor mCursor = null;

    private static final String TAG = WordListAdapter.class.getSimpleName();

    // Query parameters are very similar to SQL queries.
    private String queryUri = CONTENT_URI.toString();
    private static final String[] projection = new String[] {CONTENT_PATH};
    private String selectionClause = null;
    private String selectionArgs[] = null;
    private String sortOrder = "ASC";

    private final LayoutInflater mInflater;
    private Context mContext;

    public WordListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setData(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.wordlist_item, parent, false);
        return new WordViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        String word = "";
        int id = -1;
        if (mCursor != null) {
            int count = mCursor.getCount();
            if (position < count) {
                mCursor.moveToPosition(position);
                int indexWord = mCursor.getColumnIndex(Contract.WordList.KEY_WORD);
                word = mCursor.getString(indexWord);
                holder.wordItemView.setText(word);
                int indexId = mCursor.getColumnIndex(Contract.WordList.KEY_ID);
                id = mCursor.getInt(indexId);
            } else {
                holder.wordItemView.setText(R.string.waiting);
            }
        } else {
            holder.wordItemView.setText(R.string.no_word);
        }
        // Attach a click listener to the DELETE button.
        holder.delete_button.setOnClickListener(new MyButtonOnClickListener(position, id, null) {

            @Override
            public void onClick(View v) {
                selectionArgs = new String[]{Integer.toString(id)};
                int deleted = mContext.getContentResolver().delete(
                        CONTENT_URI, CONTENT_PATH, selectionArgs);
                if (deleted > 0) {
                    notifyItemRemoved(position);
                } else {
                    Log.d(TAG, mContext.getString(R.string.not_deleted) + deleted);
                }
            }

        });

        // Attach a click listener to the EDIT button.
        holder.edit_button.setOnClickListener(new MyButtonOnClickListener(position, id, word) {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditWordActivity.class);

                intent.putExtra(EXTRA_ID, id);
                intent.putExtra(EXTRA_POSITION, position);
                intent.putExtra(EXTRA_WORD, word);

                ((Activity) mContext).startActivityForResult(intent, MainActivity.WORD_EDIT);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        } else {
            return -1;
        }
    }
}


