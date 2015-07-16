package jp.example.owner.jp.phoneappli;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//インテントの宣言
        Intent i;
        int id = item.getItemId();


        switch (id)
        {
          case R.id.action_PhoneBook :
              i=new Intent("android.intent.action.VIEW", Uri.parse("content://contacts/people/"));
              startActivity(i);
            break;
        }

        return super.onOptionsItemSelected(item);
    }
}
