package com.example.iket.ecellapp2k17.blogs.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.iket.ecellapp2k17.R;
import com.example.iket.ecellapp2k17.blogs.model.data.BlogData;
import com.example.iket.ecellapp2k17.helper.image_loaders.GlideImageLoader;
import com.example.iket.ecellapp2k17.helper.image_loaders.RoundedCornersTransformation;

import java.util.ArrayList;
import java.util.List;

public class VerticlePagerAdapter extends PagerAdapter {

    String mResources[] = {"Hello page 1","Hello page 2"};

    private List<BlogData> blogDataList = new ArrayList<>();
    Context mContext;
    LayoutInflater mLayoutInflater;

    public VerticlePagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.blogDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    void setBlogDataList(List<BlogData> blogDataList)
    {
        this.blogDataList=blogDataList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.blog_card, container, false);

        BlogData data=blogDataList.get(position);
        TextView title = (TextView) itemView.findViewById(R.id.blog_title);
        TextView owner=(TextView)itemView.findViewById(R.id.blog_owner);
        TextView date=(TextView)itemView.findViewById(R.id.blog_date);
        TextView body=(TextView)itemView.findViewById(R.id.blog_body);
        ImageView blogImage= (ImageView) itemView.findViewById(R.id.blog_image);
//        ImageView swipe_bottom=(ImageView)itemView.findViewById(R.id.swipe_blog);

//        if(position==blogDataList.size()-1)
//            swipe_bottom.setVisibility(View.INVISIBLE);
        Glide.with(mContext).load(data.getBlogImage()).bitmapTransform(new RoundedCornersTransformation( mContext,20,0)).into(blogImage);
        title.setText(data.getBlogTitle());
        owner.setText(data.getBlogOwner());
        date.setText(data.getBlogDate());
        body.setText(data.getBlogBody());

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
