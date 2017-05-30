package ayu.noteses;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.app.ListActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemClickListener {
    View view;
    private FloatingActionButton fab;
    private DBDataSource dataSource;
    private ArrayList<String> values;
    private ListView lvNotes;
    public ListActivity lAc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //men set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataSource = new DBDataSource(this);
        // buka kontroller
        dataSource.open();

        // ambil semua data barang
        values = dataSource.getAllNotes();
        // masukkan data barang ke array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        // mengambil listview untuk diset onItemLongClickListener
        lvNotes = (ListView) findViewById(android.R.id.list);
        lvNotes.setAdapter(adapter);


        lvNotes.setOnItemClickListener(this);

        // mengambil listview untuk diset onItemLongClickListener

        fab = (FloatingActionButton) findViewById(R.id.fabTambah);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabTambah:
                Intent i = new Intent(this, CreateNotes.class);
                startActivity(i);
                finish();
                break;

        }
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
        if (id == R.id.action_add) {
            Intent i = new Intent(this, CreateNotes.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //method yang dipanggil ketika edit data selesai
    public void finale() {
        MainActivity.this.finish();
        dataSource.close();
    }

    @Override
    public void onItemClick(final AdapterView<?> adapter, View v, int pos,
                            final long id) {

        notes b = dataSource.getNotes(id+1);
        Intent i = new Intent(this, CreateNotes.class);
        Bundle bun = new Bundle();
        bun.putLong("id", b.getId());
        bun.putString("action", "edit");
        bun.putString("title", b.getTitle());
        bun.putString("content", b.getContent());
        i.putExtras(bun);

        startActivity(i);
        finish();


    }


}
