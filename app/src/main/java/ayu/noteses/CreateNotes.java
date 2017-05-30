package ayu.noteses;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateNotes extends AppCompatActivity implements OnClickListener {

    private Button buttonSubmit;
    private EditText edTitle;
    private EditText edContent;
    private Intent iin;
    private Bundle b;

    private DBDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        iin = getIntent();
        b = iin.getExtras();


        buttonSubmit = (Button) findViewById(R.id.buttonSave);
        buttonSubmit.setOnClickListener(this);
        edTitle = (EditText) findViewById(R.id.editTextTitle);
        edContent = (EditText) findViewById(R.id.editTextContent);

        dataSource = new DBDataSource(this);
        if(b!=null)
        {
            String j =(String) b.get("title");
            edTitle.setText(j);
            edContent.setText((String)b.get("content"));
        }
        //membuat sambungan baru ke database
        dataSource.open();
    }
    @Override
    public void onClick(View v) {
        // Inisialisasi data barang
        String title = null;
        String content = null;

        @SuppressWarnings("unused")

        //inisialisasi barang baru (masih kosong)
        notes notes = null;
        if(edTitle.getText()!=null && edContent.getText()!=null )
        {
            /* jika field nama, merk, dan harga tidak kosong
             * maka masukkan ke dalam data barang*/
            title = edTitle.getText().toString();
            content = edContent.getText().toString();

        }

        switch(v.getId())
        {
            case R.id.buttonSave:
                if(b!=null)
                {
                    notes = new notes();
                    notes.setTitle(edTitle.getText().toString());
                    notes.setContent(edContent.getText().toString());
                    notes.setId((long)b.get("id"));
                    dataSource.updateNotes(notes);

                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    CreateNotes.this.finish();
                    Snackbar.make(getWindow().getDecorView(), "Successfull ", Snackbar.LENGTH_LONG).show();
                    dataSource.close();

                }
                else {
                    // insert data barang baru

                    notes = dataSource.createNotes(title, content);

                    //konfirmasi kesuksesan
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    CreateNotes.this.finish();
                    Snackbar.make(getWindow().getDecorView(), "Successfull ", Snackbar.LENGTH_LONG).show();
                }
                break;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            dataSource.deleteNotes(b.getLong("id"));
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            Long a = b.getLong("id");
            Log.v("delete", a.toString());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void initialize(){

    }
}
