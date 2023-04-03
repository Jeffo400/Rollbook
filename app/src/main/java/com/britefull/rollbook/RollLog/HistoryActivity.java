package com.britefull.rollbook.RollLog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.britefull.rollbook.MainActivity;
import com.britefull.rollbook.R;

//import org.w3c.dom.Text;


public class HistoryActivity extends AppCompatActivity {

    TextView textViewHistory;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        intent = getIntent();

        textViewHistory = findViewById(R.id.textViewHistory);
        textViewHistory.setText(intent.getStringExtra("HISTORY"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.email_history:
                emailHistory();
                return true;
            case R.id.clear_history:
                clearHistory();
                return true;
            default:
                return false;
        }
    }


    public void emailHistory(){
        if(textViewHistory.getText()!=null) {

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sonlight Attendance Raw Data");
            emailIntent.putExtra(Intent.EXTRA_TEXT, textViewHistory.getText());

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                finish();
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There is no email client installed", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "There is no history", Toast.LENGTH_LONG).show();
        }
    }

    public void clearHistory(){
        Intent clearIntent = new Intent();
        setResult(MainActivity.HISTORY_DELETED, clearIntent);
        finish();
    }

}
