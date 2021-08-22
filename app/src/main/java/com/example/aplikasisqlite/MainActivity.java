package com.example.aplikasisqlite;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.aplikasisqlite.helper.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.aplikasisqlite.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    AlertDialog.Builder dialog;
    List<com.example.aplikasisqlite.model.Data> itemlist = new ArrayList<com.example.aplikasisqlite.model.Data>();
    com.example.aplikasisqlite.adapter.Adapter adapter;
    DBHelper SQLite = new DBHelper(this);

    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_ADDRESS = "address";

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Tambah SQLite
        SQLite = new DBHelper(getApplicationContext());

        FloatingActionButton fab = findViewById(R.id.fab);

        // Tambah List View
        listView = (ListView) findViewById(R.id.list_View);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Tambah Intent untuk pindah ke halaman Add dan Edit
                Intent intent = new Intent(MainActivity.this, AddEdit.class);
                startActivity(intent);
            }
        });
        //Tambah adapter dan listview
        adapter = new com.example.aplikasisqlite.adapter.Adapter(MainActivity.this, itemlist);
        listView.setAdapter(adapter);

        //tekan lama daftar listview untuk menampilkan edit dan hapus
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent , View view,
                                           final int position, long id) {
                // TODO Auto-generated method stub
                final String idx = itemlist.get(position).getId();
                final String name = itemlist.get(position).getName();
                final String address = itemlist.get(position).getAddress();

                final CharSequence[] dialogitem = {"Edit", "Delete"};
                dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                        @Override
                                public void onClick(DialogInterface dialog, int which) {
                            //TODO Auto-generated method stub
                            switch (which) {
                                case 0:
                                    Intent intent = new Intent(MainActivity.this, AddEdit.class);
                                    intent.putExtra(TAG_ID, idx);
                                    intent.putExtra(TAG_NAME, name);
                                    intent.putExtra(TAG_ADDRESS, address);
                                    startActivity(intent);
                                    break;
                                case 1:
                                    SQLite.delete(Integer.parseInt(idx));
                                    itemlist.clear();
                                    SQLite.getAllData();
                                    break;
                            }
                        }
                    }) .show();
                    return false;
                }
            });
        getAllData();
    }

    private void getAllData() {
            ArrayList<HashMap<String, String>> row = SQLite.getAllData();

            for (int i = 0; i < row.size(); i++) {
                String id = row.get(i).get(TAG_ID);
                String poster = row.get(i).get(TAG_NAME);
                String title = row.get(i).get(TAG_ADDRESS);

                com.example.aplikasisqlite.model.Data data = new com.example.aplikasisqlite.model.Data();

                data.setId(id);
                data.setName(poster);
                data.setAddress(title);

                itemlist.add(data);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onResume() {
            super.onResume();
            itemlist.clear();
            getAllData();
        }
}