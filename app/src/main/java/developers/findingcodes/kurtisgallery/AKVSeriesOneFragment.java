package developers.findingcodes.kurtisgallery;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class AKVSeriesOneFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Image>> {

    private String TAG = MainActivity.class.getSimpleName();
    private static final String IMAGE_URL = "https://developers.kurtisgallery.in/example_api/kurtisone/id/10";
    private ArrayList<Image> images  = (ArrayList<Image>) QueryUtils.getImageArrayList();
    private GalleryAdapter mAdapter;
    private GridView gridView;
    ImageView downloadimg;
    ImageView whatsappShare;
    private static final int EARTHQUAKE_LOADER_ID = 1;



    public AKVSeriesOneFragment() {
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


        //setSupportActionBar(toolbar);

        gridView = (GridView) rootView.findViewById(R.id.grid_View);


        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getActivity(), images);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }
        });

        downloadimg = (ImageView) rootView.findViewById(R.id.downloadall);

        downloadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] urls = new String[50];
                for (int i = 0; i < images.size(); i++) {
                    urls[i] = images.get(i).getLarge();

                }
                DownloadImages downloadImages= new DownloadImages();
                downloadImages.downloadImage(getActivity(),urls);



            }
        });
        whatsappShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareImages();

            }
        });

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
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

        } else {


            //


        }



        return rootView;

    }

    private void ShareImages(){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
        intent.setType("image/*"); /* This example is sharing jpeg images. */
        intent.setPackage("com.whatsapp");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        final ArrayList<Uri> files = new ArrayList<>();



        for(int i=0;i<images.size();i++){


            String img_url = images.get(i).getSmall();

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
//        // Hide loading indicator because the data has been loaded
//        View loadingIndicator = findViewById(R.id.loading_indicator);
//        loadingIndicator.setVisibility(View.GONE);



        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (images != null && !images.isEmpty()) {
            mAdapter.addAll(images);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Image>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}
