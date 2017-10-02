package com.javahelps.networkingdemo;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Sneha on 01-10-2017.
 */

public class CommentAsyncTask extends AsyncTask<String , Void , ArrayList<Comments>> {

    CommentDownloadListener commentDownloadListener ;

    public CommentAsyncTask(CommentDownloadListener commentDownloadListener) {
        this.commentDownloadListener = commentDownloadListener;
    }

    @Override
    protected ArrayList<Comments> doInBackground(String... params) {
        Log.i("Course AsyncTask","Inside Async Task ");
        String urlString = params[0];
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            Log.i("Courses Response:","Connection Started");
            urlConnection.connect();
            Log.i("Courses Response:","Connection Complete");
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            String response = "";
            while (scanner.hasNext()){
                response += scanner.next();
            }
            Log.i("Courses Response:",response);
            ArrayList<Comments> courses = parseCourses(response);
            return  courses;



        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public ArrayList<Comments> parseCourses(String response) throws JSONException{
        ArrayList<Comments> posts = null;

        JSONArray items = new JSONArray(response);
        if(items!=null){
            posts = new ArrayList<>();
            for(int i = 0;i<items.length();i++){
                JSONObject courseObject = items.getJSONObject(i);
                String name = courseObject.getString("name");
                String email = courseObject.getString("email");
                String body = courseObject.getString("body");

                Comments comments = new Comments(name , email , body);
                posts.add(comments);
            }
        }
        return posts ;
    }

    @Override
    protected void onPostExecute(ArrayList<Comments> commentses) {
        commentDownloadListener.onDownload(commentses);
    }

    public static interface CommentDownloadListener{
        void onDownload(ArrayList<Comments> courses);
    }

}
