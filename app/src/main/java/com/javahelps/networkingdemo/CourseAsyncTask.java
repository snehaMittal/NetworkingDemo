package com.javahelps.networkingdemo;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class CourseAsyncTask extends AsyncTask<String , Void , ArrayList<Items>> {

    CoursesDownloadListener courseDownListener ;
    ProgressBar progressbar ;
    ListView listview ;

    public CourseAsyncTask(CoursesDownloadListener listener , ProgressBar progressbar , ListView listview) {
        this.progressbar = progressbar ;
        courseDownListener = listener ;
        this.listview = listview ;
    }

    public CourseAsyncTask(CoursesDownloadListener courseDownListener, ListView listview) {
        this.courseDownListener = courseDownListener;
        this.listview = listview;
    }

    public CourseAsyncTask(CoursesDownloadListener courseDownListener) {
        this.courseDownListener = courseDownListener;
    }

    @Override
    protected ArrayList<Items> doInBackground(String... params) {
        // code here is run on different thread

        String urlString = params[0];
        try {
            URL url = new URL(urlString);   //convert string to url
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();  //to form the connection
            urlConnection.setRequestMethod("GET");// we wont data from server that is why "get"
            Log.i("Course response", "Connection started: ");
            urlConnection.connect();         //cant be called on main thread so we have to use asyncTask() for multithreading
            Log.i("Courses Response:","Connection Complete");

            InputStream inputStream = urlConnection.getInputStream();      //api gives the input stream
            Scanner scanner = new Scanner(inputStream);
            String response = "" ;
            while (scanner.hasNext()){
                response += scanner.next();
            }
            Log.i("Courses Response:",response);
            ArrayList<Items> courses = parseCourses(response);
            return courses ;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Items> parseCourses(String response) throws JSONException{
        ArrayList<Items> posts = null;

        JSONArray items = new JSONArray(response);
        if(items!=null){
            posts = new ArrayList<>();
            for(int i = 0;i<items.length();i++){
                JSONObject courseObject = items.getJSONObject(i);
                String title = courseObject.getString("title");
                String body = courseObject.getString("body");
                Items itemsdata = new Items(title , body);
                posts.add(itemsdata);
            }
        }
        return posts ;
    }


    //runs before doinbackground
    // runs on main thread
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //runs after doinbackground
    // runs on main thread
    @Override
    protected void onPostExecute(ArrayList<Items> courses) {
        Log.i("Courses Array Size",courses.size() +"");
        progressbar.setVisibility(View.GONE);
        listview.setVisibility(View.VISIBLE);
        courseDownListener.onDownload(courses);
    }

    public interface CoursesDownloadListener{
        void onDownload(ArrayList<Items> courses);
    }

}
