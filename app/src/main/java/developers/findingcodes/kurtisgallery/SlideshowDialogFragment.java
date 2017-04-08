package developers.findingcodes.kurtisgallery;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

//import android.widget.Toolbar;


public class SlideshowDialogFragment extends DialogFragment {
    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<Image> images;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblDate;
    private int selectedPosition = 0;
    private ProgressBar progressBar;
    private ImageView imageView;
    private TextView downloadImage;
    private  TextView shareImage;

    private Toolbar toolbar;

    static SlideshowDialogFragment newInstance() {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        return f;
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        downloadImage = (TextView) v.findViewById(R.id.downloadone);

        shareImage = (TextView) v.findViewById(R.id.sharewhatsapp);







//
//        // set the listener for Navigation
//        Toolbar actionBar = (Toolbar) v.findViewById(R.id.fake_action_bar);
//        if (actionBar!=null) {
//            final SlideshowDialogFragment window = this;
//            actionBar.setTitle("knfkdnf");
//            actionBar.setNavigationOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    window.dismiss();
//                }
//            });
//        }


        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        lblCount = (TextView) v.findViewById(R.id.lbl_count);
        // lblTitle = (TextView) v.findViewById(R.id.title);
        //lblDate = (TextView) v.findViewById(R.id.date);

        images = (ArrayList<Image>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");

        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + images.size());

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " of " + images.size());

        Image image = images.get(position);
        // lblTitle.setText(image.getLarge());
        final String imgsrc =image.getLarge();
        downloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImage(imgsrc);
            }
        });



        //lblDate.setText(image.getTimestamp());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_DarkActionBar);

    }

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            imageView = (ImageView) view.findViewById(R.id.image_preview);



            Image image = images.get(position);

            Glide.with(getActivity()).load(image.getLarge())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

            progressBar.setVisibility(View.INVISIBLE);


            shareImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //shareImage();
                    // ImageView iv = (ImageView) view.findViewById(viewPager.getCurrentItem());

                    Log.e(TAG, "IMAGEs " +imageView);
                    //Uri bmpUri = getLocalBitmapUri(imageView);
                    Drawable drawable = imageView.getDrawable();
                    Bitmap bmp = null;
                    if (drawable instanceof BitmapDrawable)
                        bmp = ((BitmapDrawable)drawable).getBitmap();
                    else {
                        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        drawable.draw(canvas);
                        bmp = bitmap;
                        Log.e(TAG, "IMAGEs " +bmp);

                    }

                    Uri bmpUri = null;
                    try {
                        File file = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
                        file.getParentFile().mkdirs();
                        FileOutputStream out = new FileOutputStream(file);

                        Log.e(TAG, "popopo: " + file);
                        bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
                        out.close();
                        bmpUri = Uri.fromFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (bmpUri != null) {
                        // Construct a ShareIntent with link to image
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        shareIntent.setType("image/*");
                        shareIntent.setPackage("com.whatsapp");
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        // Launch sharing dialog for image
                        startActivity(Intent.createChooser(shareIntent, "Share Image"));
                    } else {
                        // ...sharing failed, handle error
                        Log.e(TAG, "BABA Parnam" + bmpUri);
                    }
                }

            });




            imageView.setId(position);
            container.addView(view);




            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }





    public void downloadImage(String imgloc) {

        String img_url = images.get(selectedPosition).getLarge();
        Log.e(TAG, "BABAB" + imgloc);

        Picasso.with(getActivity())
                .load(imgloc)
                .into(new Target() {
                          @Override
                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                              try {
                                  String root = Environment.getExternalStorageDirectory().toString();
                                  File myDir = new File(root + "/kurtis");

                                  if (!myDir.exists()) {
                                      myDir.mkdirs();
                                  }

                                  String name = new Date().toString() + ".jpg";
                                  myDir = new File(myDir, name);
                                  FileOutputStream out = new FileOutputStream(myDir);
                                  bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                  out.flush();
                                  out.close();
                                  Toast.makeText(getActivity(), "Download Completed", Toast.LENGTH_SHORT).show();

                                  getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));

                              } catch (Exception e) {
                                  // some action
                              }
                          }

                          @Override
                          public void onBitmapFailed(Drawable errorDrawable) {
                          }

                          @Override
                          public void onPrepareLoad(Drawable placeHolderDrawable) {
                          }
                      }
                );





    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable

        Log.v(TAG,"IMAGEJI "+imageView);
        //iv.setDrawingCacheEnabled(true);
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);

            Log.e(TAG, "popopo: " + file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


}