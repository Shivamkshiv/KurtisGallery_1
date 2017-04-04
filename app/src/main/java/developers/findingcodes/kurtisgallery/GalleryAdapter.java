package developers.findingcodes.kurtisgallery;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;



public class GalleryAdapter extends ArrayAdapter<Image> {

    public GalleryAdapter(@NonNull Context context,  @NonNull List<Image> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.gallery_thumbnail,parent,false);
        }

        Image image = getItem(position);

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.thumbnail);
        Picasso.with(getContext()).load(image.getSmall()).into(imageView);

        return listItemView;
    }

}