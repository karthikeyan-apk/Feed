package sk.com.facebookfeedsk.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sk.com.facebookfeedsk.AppController.AppController;
import sk.com.facebookfeedsk.Feed;
import sk.com.facebookfeedsk.Model.FeedModel;
import sk.com.facebookfeedsk.R;

/**
 * Created by karthikeyan on 01-Apr-16.
 */
public class FeedAdapter extends BaseAdapter {
    Context cxt;
    ArrayList<FeedModel> mfeedBean;
    ImageLoader mImageLoader;
    public FeedAdapter(Context cxt, ArrayList<FeedModel> mfeedBean) {
        this.cxt = cxt;
        this.mfeedBean = mfeedBean;
    }

    @Override
    public int getCount() {
        return mfeedBean.size();
    }

    @Override
    public Object getItem(int position) {
        return mfeedBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = LayoutInflater.from(cxt).inflate(R.layout.activity_feed, parent, false);
        NetworkImageView profilePic = (NetworkImageView) mView.findViewById(R.id.profile_pic);
        TextView tittle = (TextView) mView.findViewById(R.id.tittle);
        TextView status = (TextView) mView.findViewById(R.id.status);
        TextView url = (TextView) mView.findViewById(R.id.url);
        TextView time = (TextView) mView.findViewById(R.id.timestamp);
        NetworkImageView descrptive = (NetworkImageView) mView.findViewById(R.id.descrptive);
        FeedModel mFeedModel = mfeedBean.get(position);
        tittle.setText(mFeedModel.getName());
        status.setText(mFeedModel.getStatus());
        mImageLoader = AppController.getInstance().getImageLoader();


            if(mFeedModel.getUrl().length()!=0 && mFeedModel.getUrl() !="null")
                url.setText(mFeedModel.getUrl());

        if(mFeedModel.getImageUrl()!="null"){
            descrptive.setImageUrl(mFeedModel.getImageUrl(), mImageLoader);
        }


        Date dt = new Date(mFeedModel.getTimeStamp());
        SimpleDateFormat st = new SimpleDateFormat("MM dd yyyy");
        time.setText(st.format(dt));

        profilePic.setImageUrl(mFeedModel.getProfilePic(),mImageLoader);
        return mView;
    }
}
