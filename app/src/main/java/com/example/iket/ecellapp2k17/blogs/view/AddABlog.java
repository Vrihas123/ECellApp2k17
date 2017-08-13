package com.example.iket.ecellapp2k17.blogs.view;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.iket.ecellapp2k17.R;
import com.example.iket.ecellapp2k17.blogs.model.RetrofitAddBlogsProvider;
import com.example.iket.ecellapp2k17.blogs.model.RetrofitBlogsProvider;
import com.example.iket.ecellapp2k17.blogs.model.data.AddBlogsData;
import com.example.iket.ecellapp2k17.blogs.model.data.BlogData;
import com.example.iket.ecellapp2k17.blogs.presenter.AddBlogsPresenter;
import com.example.iket.ecellapp2k17.blogs.presenter.AddBlogsPresenterImpl;
import com.example.iket.ecellapp2k17.blogs.presenter.BlogsPresenterImpl;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddABlog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddABlog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddABlog extends android.support.v4.app.DialogFragment implements AddABlogView{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int RESULT_LOAD_IMAGE = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private EditText editText_blogTitle,editText_blogBody;
    private Button btn_insertImage,btn_post;
    private ImageView addABlog_bg,uploaded_img;
    private String blogTitle,blogBody;
    private AddBlogsPresenter addBlogsPresenter;
    private ProgressBar progressBar;
    private Bitmap bitmap;
    private Uri file_image;
    private static final int STORAGE_PERMISSION_CODE = 123;

    public AddABlog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddABlog.
     */
    // TODO: Rename and change types and number of parameters
    public static AddABlog newInstance(String param1, String param2) {
        AddABlog fragment = new AddABlog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_theme);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_ablog, container, false);
        editText_blogTitle = (EditText) view.findViewById(R.id.etxt_title);
        editText_blogBody = (EditText) view.findViewById(R.id.etxt_blogBody);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_blogs);
        uploaded_img=(ImageView)view.findViewById(R.id.uploaded_img);

        getDialog().setTitle("Add A Blog");
        btn_insertImage = (Button)  view.findViewById(R.id.insert_img);
        btn_insertImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
            }
        });

        btn_post = (Button) view.findViewById(R.id.post_blog);
        btn_post.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                blogTitle = editText_blogTitle.getText().toString();
                blogBody = editText_blogBody.getText().toString();
                if(blogTitle.isEmpty() || blogBody.isEmpty()){
                    Toast.makeText(getContext(),"Fields cannot be Empty..!",Toast.LENGTH_SHORT).show();
                }
                else{
                    addBlogsPresenter = new AddBlogsPresenterImpl(AddABlog.this,new RetrofitAddBlogsProvider());
                    addBlogsPresenter.addBlogsData(blogTitle,blogBody,file_image.toString());
                }

            }
        });

        addABlog_bg = (ImageView) view.findViewById(R.id.addBlog_bg);
        Glide.with(this).load(R.drawable.add_blog_bg_white).into(addABlog_bg);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            file_image = data.getData();
            Log.d("Blogs",file_image.toString());
            
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), file_image);
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                Bitmap conv_bm = getRoundedRectBitmap(resized, 100);
                uploaded_img.setImageBitmap(conv_bm);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(getContext(),"Your Image has been selected",Toast.LENGTH_SHORT).show();
            //load_blogImage.setImageURI(selected_image);
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show){
            progressBar.setVisibility(View.INVISIBLE);
        }
        else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void AddBlogsStatus(AddBlogsData addBlogsData) {
        Toast.makeText(getContext()," Your Blog has been Submitted Successfully ",Toast.LENGTH_SHORT).show();
    }

    public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(99, 99, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, 150, 150);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(50, 50, 50, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

        } catch (NullPointerException e) {
        } catch (OutOfMemoryError o) {
        }
        return result;
    }
}
