package developers.findingcodes.kurtisgallery;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;



public class ImageLoader extends AsyncTaskLoader<List<Image>> {

    private String mUrl;
    List<Image> images;
    private List<Image> CachedData;

    public ImageLoader(Context context, String url){
        super(context);
        mUrl = url;
    }


    public ImageLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if(CachedData == null){

            forceLoad();
        }else{
            super.deliverResult(CachedData);
        }

    }

    @Override
    public List<Image> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        QueryUtils queryUtils = new QueryUtils();
        images  = queryUtils.fetchImageData(mUrl);
        return images;
    }

    @Override
    public void deliverResult(List<Image> data) {

        CachedData = data;

        super.deliverResult(data);
    }
}
