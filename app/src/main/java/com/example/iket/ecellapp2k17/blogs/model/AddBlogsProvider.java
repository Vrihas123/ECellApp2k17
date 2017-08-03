package com.example.iket.ecellapp2k17.blogs.model;

import com.example.iket.ecellapp2k17.blogs.view.AddABlogCallback;

/**
 * Created by vrihas on 1/8/17.
 */

public interface AddBlogsProvider {
    void getBlogResponse(String blogTitle, String blogType, String blogBody, AddABlogCallback addABlogCallback);
    void  onDestroy();
}