package com.javahelps.networkingdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements CourseAsyncTask.CoursesDownloadListener{

    ArrayList<Items> posts = new ArrayList<>();
    ArrayAdapter<String> arrayadapter ;
    ArrayList<String> items = new ArrayList<>();
     ProgressBar progressBar ;
    static ListView listview ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ProgressBar progressBar =  (ProgressBar)findViewById(R.id.progressbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchCourse();
            }
        });

        listview = (ListView)findViewById(R.id.listview);
        arrayadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , items);
        listview.setAdapter(arrayadapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this , CommentActivity.class);
                intent.putExtra("position" , position);
                startActivity(intent);
            }
        });
    }



    public void fetchCourse(){
        String urlString = "https://jsonplaceholder.typicode.com/posts" ;            //url should be known
        CourseAsyncTask asyncTask = new CourseAsyncTask(this);//create an object of async task for multithreading

        progressBar.setVisibility(View.VISIBLE);
        listview.setVisibility(View.GONE);
        asyncTask.execute(urlString);
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
        // automatically handle clicks on the Home/Up button, so long..000000..
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDownload(ArrayList<Items> courses) {

        items.clear();
        for (Items item:courses){
            String T = item.title;
            String B = item.body ;
            String answer ="\nTitle:"+ T + "\n\n" +  "Body : " + B ;
            items.add(answer);
        }
        arrayadapter.notifyDataSetChanged();
    }
}
