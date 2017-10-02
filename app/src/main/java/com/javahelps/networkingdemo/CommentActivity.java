package com.javahelps.networkingdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity implements CommentAsyncTask.CommentDownloadListener{

    ListView commentlist ;

    ArrayAdapter<String> arrayadapter ;
    ArrayList<String> items = new ArrayList<>();
    int position ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        commentlist = (ListView) findViewById(R.id.commentListview);

        arrayadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , items);
        commentlist.setAdapter(arrayadapter);

        Intent intent = getIntent();
        position = intent.getIntExtra("position" , 0);

        fetchComments(position);
    }

    private void fetchComments(int position) {
        String url_part1 = "https://jsonplaceholder.typicode.com/posts/" ;
        String url_part2 = "/comments";
        String final_url = url_part1 + (position+1) + url_part2 ;
        CommentAsyncTask asyncTask = new CommentAsyncTask(this);
        asyncTask.execute(final_url);

    }

    @Override
    public void onDownload(ArrayList<Comments> courses) {
        items.clear();
        for (Comments comment:courses){
            String name = comment.name;
            String email = comment.email ;
            String body = comment.body ;

            String answer = "\n Name : " + name + " ( " + email + " ) \n\n" + " Body :" + body;
            items.add(answer);
        }
        arrayadapter.notifyDataSetChanged();
    }
}
