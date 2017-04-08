package developers.findingcodes.kurtisgallery;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class AKPremiumOneFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Image>> {

    private String TAG = MainActivity.class.getSimpleName();
    private static final String IMAGE_URL = "https://developers.kurtisgallery.in/example_api/kurtisone/id/7";
    private ArrayList<Image> lists;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    ImageView downloadimg;
    ImageView whatsappShare;
    private ProgressBar mProgressBar;
    private static final int IMAGE_LOADER_ID = 1;




    public AKPremiumOneFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootView = inflater.inflate(R.layout.fragment_akpremium_one, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        whatsappShare= (ImageView) rootView.findViewById(R.id.whatsappsharebtn);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);


        //setSupportActionBar(toolbar);

        //setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);


        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getActivity(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", lists);
                bundle.putInt("position", position);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        downloadimg = (ImageView) rootView.findViewById(R.id.downloadall);

        downloadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] urls = new String[50];

                    for (int i = 0; i < lists.size(); i++) {
                        urls[i] = lists.get(i).getSmall();

                    }


                DownloadImages downloadImages= new DownloadImages();
                downloadImages.downloadImage(getActivity(),urls);


                Toast.makeText(getActivity(),"Images Saved successfully",Toast.LENGTH_LONG).show();


            }
        });
        whatsappShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareImages();

            }
        });




        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(IMAGE_LOADER_ID, null, this);

        }
    }

    private void ShareImages(){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
        intent.setType("image/*"); /* This example is sharing jpeg images. */
        intent.setPackage("com.whatsapp");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        final ArrayList<Uri> files = new ArrayList<>();



        for(int i=0;i<lists.size();i++){

            String img_url = lists.get(i).getLarge();

            Picasso.with(getContext()).load(img_url).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    Uri uri = getImageUri(getContext(),bitmap);
                    files.add(uri);

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        // startActivity(Intent.createChooser(intent, "Share Image"));
        startActivity(intent);




    }






//    public static Bitmap getBitmapFromURL(String url) {
//        try {
//            URL ur = new URL(url);
//            HttpURLConnection connection = (HttpURLConnection) ur.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap bitmapFrmUrl = BitmapFactory.decodeStream(input);
//            return bitmapFrmUrl;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private  class MyTask extends AsyncTask<String,Void,Bitmap>{
//
//
//        @Override
//        protected Bitmap doInBackground(String... strings) {
//            return null;
//        }
//    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public Loader<List<Image>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new ImageLoader(getContext(), IMAGE_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Image>> loader, List<Image> images) {



        lists = new ArrayList<>();

        try{


            for(int i=0;i<images.size();i++){

                String small = images.get(i).getSmall();
                String large = images.get(i).getLarge();

                Image image = new Image(small,large);

                lists.add(image);

            }

        }catch (NullPointerException e){
            Log.e(TAG,"ASDFG",e);
        }



        mProgressBar.setVisibility(View.INVISIBLE);


        mAdapter = new GalleryAdapter(getActivity(), lists);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onLoaderReset(Loader<List<Image>> loader) {
        // Loader reset, so we can clear out our existing data.


        recyclerView.setAdapter(null);


    }



}
