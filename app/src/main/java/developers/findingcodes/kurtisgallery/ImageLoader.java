package developers.findingcodes.kurtisgallery;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;



public class ImageLoader extends AsyncTaskLoader<List<Image>> {

    private String mUrl;
    public ImageLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Image> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        List<Image> earthquakes = QueryUtils.fetchImageData(mUrl);
        return earthquakes;
    }
}
