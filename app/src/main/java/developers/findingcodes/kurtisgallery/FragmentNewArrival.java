package developers.findingcodes.kurtisgallery;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;


public class FragmentNewArrival extends Fragment implements LoaderManager.LoaderCallbacks<List<Image>> {


    private String TAG = MainActivity.class.getSimpleName();
    private static final String IMAGE_URL = "https://developers.kurtisgallery.in/example_api/kurtisone/id/8";
    private ArrayList<Image> images;
    private GalleryAdapter mAdapter;
    private GridView gridView;
    private int IMAGE_LOADER = 1;

    public FragmentNewArrival() {
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
        //setSupportActionBar(toolbar);

        gridView = (GridView) rootView.findViewById(R.id.grid_View);



        mAdapter = new GalleryAdapter(getActivity(),images);

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

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(IMAGE_LOADER, null, this);
        } else {
            // Otherwise, display error
//            // First, hide loading indicator so error message will be visible
//            View loadingIndicator = findViewById(R.id.loading_indicator);
//            loadingIndicator.setVisibility(View.GONE);
//
//            // Update empty state with no connection error message
//            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        return  rootView;

    }

    @Override
    public Loader<List<Image>> onCreateLoader(int id, Bundle args) {
        return new ImageLoader(getContext(),IMAGE_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Image>> loader, List<Image> images) {
        // Hide loading indicator because the data has been loaded
//        View loadingIndicator = findViewById(R.id.loading_indicator);
//        loadingIndicator.setVisibility(View.GONE);
//
//        // Set empty state text to display "No earthquakes found."
//        mEmptyStateTextView.setText(R.string.no_earthquakes);

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


