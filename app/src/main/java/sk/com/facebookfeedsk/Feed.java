package sk.com.facebookfeedsk;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import sk.com.facebookfeedsk.Adapter.FeedAdapter;
import sk.com.facebookfeedsk.AppController.AppController;
import sk.com.facebookfeedsk.Model.FeedModel;

import static com.android.volley.Request.*;
import static com.android.volley.Request.Method.*;

public class Feed extends AppCompatActivity {
    String url = "http://api.androidhive.info/feed/feed.json";
    ProgressDialog mprgDialog;
    Dialog dia;
    ArrayList<FeedModel> mfeedBean;
    FeedAdapter mFeedAdapter;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        lv = (ListView) findViewById(R.id.list);
        mprgDialog = new ProgressDialog(this);
        dia = new Dialog(this);
        /* Async task calling method
        GetResponseFromServer getResponseFromServer = new GetResponseFromServer();
        getResponseFromServer.execute();
        */
        getResponse();
    }


    // Volley
    void getResponse() {
        mprgDialog.setTitle("Welcome to FaceBook Feed");
        mprgDialog.setMessage("Loading Movies...Please wait....");
        mprgDialog.setCancelable(false);
        mprgDialog.show();
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            mprgDialog.dismiss();
                try {
                    //JSONObject mJsonObjectParent = new JSONObject();
                    JSONArray mJsonArray = response.getJSONArray("feed");
                    int i;
                    mfeedBean = new ArrayList<>();
                    for (i = 0; i < mJsonArray.length();i++){
                        FeedModel mFeedModel = new FeedModel();
                        JSONObject mJsonObjectChild = mJsonArray.getJSONObject(i);
                        mFeedModel.setName(mJsonObjectChild.getString("name"));
                        mFeedModel.setImageUrl(mJsonObjectChild.getString("image"));
                        mFeedModel.setStatus(mJsonObjectChild.getString("status"));
                        mFeedModel.setProfilePic(mJsonObjectChild.getString("profilePic"));
                        mFeedModel.setTimeStamp(mJsonObjectChild.getInt("timeStamp"));
                        mFeedModel.setUrl(mJsonObjectChild.getString("url"));
                        mfeedBean.add(mFeedModel);
                    }
                    mFeedAdapter = new FeedAdapter(Feed.this,mfeedBean);
                    lv.setAdapter(mFeedAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mprgDialog.dismiss();
                dia.setContentView(R.layout.exit_layout);
                TextView dia_tittle = (TextView) dia.findViewById(R.id.dialog_tittle);
                TextView dia_content = (TextView) dia.findViewById(R.id.dialog_content);
                ImageView dia_img = (ImageView) dia.findViewById(R.id.dialog_image);
                Button dia_but = (Button) dia.findViewById(R.id.dialog_but);

                dia_tittle.setText("Welcome to FaceBook Feed");
                dia_content.setText("Please Check Internet Connection");
                dia_img.setImageResource(R.drawable.sadsmily);
                dia_but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                       // Feed.this.onDestroy();
                    }
                });
                dia.show();
                //mprgDialog.setMessage("No InterNet Connection");
                //mprgDialog.show();
            }});
        AppController.getInstance().addToRequestQueue(mJsonObjectRequest,"feed");
    }








    //Async Task
    public  class GetResponseFromServer extends AsyncTask<String,Void,String>{
        String response;
        @Override
        protected String doInBackground(String... params) {
            InputStream minputStreamReader = null ;
            BufferedReader mbufferIPStream = null;
            try {
                URL murl =new URL(url);
                HttpURLConnection mHttpUrlConnection = (HttpURLConnection) murl.openConnection();
                mHttpUrlConnection.setRequestMethod("GET");
                minputStreamReader = new BufferedInputStream(mHttpUrlConnection.getInputStream());
                mbufferIPStream = new BufferedReader(new InputStreamReader(minputStreamReader));
                StringBuilder stringBuilder = new StringBuilder();
                while ((response = mbufferIPStream.readLine()) != null) {
                    stringBuilder.append(response + "\n");
                }
                response = stringBuilder.toString();

            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mprgDialog.setMessage("Welcome.............Pls Wait data are Loading");
            mprgDialog.setCancelable(false);
            mprgDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mprgDialog.isShowing()){
                mprgDialog.dismiss();
            }
            try {
                JSONObject mJsonObjectParent = new JSONObject(s);
                JSONArray mJsonArray = mJsonObjectParent.getJSONArray("feed");
                int i;
                mfeedBean = new ArrayList<>();
                for (i = 0; i < mJsonArray.length();i++){
                    FeedModel mFeedModel = new FeedModel();
                    JSONObject mJsonObjectChild = mJsonArray.getJSONObject(i);
                    mFeedModel.setName(mJsonObjectChild.getString("name"));
                    mFeedModel.setImageUrl(mJsonObjectChild.getString("image"));
                    mFeedModel.setStatus(mJsonObjectChild.getString("status"));
                    mFeedModel.setProfilePic(mJsonObjectChild.getString("profilePic"));
                    mFeedModel.setTimeStamp(mJsonObjectChild.getInt("timeStamp"));
                    mFeedModel.setUrl(mJsonObjectChild.getString("url"));
                    mfeedBean.add(mFeedModel);
                }
                mFeedAdapter = new FeedAdapter(Feed.this,mfeedBean);
                lv.setAdapter(mFeedAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
