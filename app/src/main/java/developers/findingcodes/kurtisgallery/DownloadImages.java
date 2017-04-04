package developers.findingcodes.kurtisgallery;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class DownloadImages extends AppCompatActivity {

    public  void downloadImage(final Context ctx, String[]imgloc) {

        for (int i = 0; i < imgloc.length; i++) {

            Picasso.with(ctx)
                    .load(imgloc[i])
                    .into(new Target() {
                              @Override
                              public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                  try {
                                      String root = Environment.getExternalStorageDirectory().toString();
                                      File myDir = new File(root + "/~AK KurtisGallery");

                                      if (!myDir.exists()) {
                                          myDir.mkdirs();
                                      }

                                      String name = new Date().getTime() + ".jpg";
                                      myDir = new File(myDir, name);
                                      FileOutputStream out = new FileOutputStream(myDir);
                                      bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                      out.flush();
                                      out.close();
                                      Log.i("TAG", "scanning File " +myDir.getAbsolutePath());
                                      MediaScannerConnection.scanFile(getBaseContext(),
                                              new String[]{myDir.getAbsolutePath()}, null, null);


                                      Toast.makeText(ctx, "Download Completed", Toast.LENGTH_SHORT).show();
                                     // sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(myDir)));



                                      Log.i("TAG", "Agter scanning" +myDir.getAbsolutePath());

                                  } catch (Exception e) {
                                      // some action
                                  }
                                  //sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));

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

    }
    private void scanFile(String path) {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA,""+path);
        values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
        getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Log.i("TAG", "Agter scanning" +path);
    }



}
