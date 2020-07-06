package com.agenin.id.Util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class BitmapUtils {

    public static Bitmap getBitmapFromAssets(Context context, String fileName , int width, int height){
        AssetManager assetManager = context.getAssets();

        InputStream inputStream;
        Bitmap bitmap = null;
        try{
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            inputStream = assetManager.open(fileName);
            options.inSampleSize = calculateInSampleSize(options,width,height);
            options.inJustDecodeBounds=false;
            return BitmapFactory.decodeStream(inputStream,null, options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public  static Bitmap getBitmapFromGalerry(Context context, Uri uri, int width, int height ){
        String[] filePathColomn = {MediaStore.Images.Media.DATA};

        //context.grantUriPermission(f,uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Cursor cursor =context.getContentResolver().query(uri,filePathColomn,null,null,null);
        Objects.requireNonNull(cursor).moveToFirst();
        int colomnIndex = cursor.getColumnIndex(filePathColomn[0]);
        String picturePath = cursor.getString(colomnIndex);
        cursor.close();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);
        options.inSampleSize = calculateInSampleSize(options,width,height);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFile(picturePath,options);
    }
    public static Bitmap applyOverlay(Context context, Bitmap sourceImage, int overlayDrawableResourceId){
        Bitmap bitmap = null;
        try{
            int width = sourceImage.getWidth();
            int height = sourceImage.getHeight();
            Resources r = context.getResources();

            Drawable imageAsDrawable =  new BitmapDrawable(r, sourceImage);
            Drawable[] layers = new Drawable[2];

            layers[0] = imageAsDrawable;
            layers[1] = new BitmapDrawable(r, BitmapUtils.decodeSampledBitmapFromResource(r, overlayDrawableResourceId, width, height));
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            bitmap = BitmapUtils.drawableToBitmap(layerDrawable);
        }catch (Exception ex){}
        return bitmap;
    }

    private static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                          int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static String insertImage(ContentResolver cr,
                                     Bitmap source,
                                     String title,
                                     String description
                                     ){

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME,title);
        values.put(MediaStore.Images.Media.DESCRIPTION,description);
        values.put(MediaStore.Images.Media.MIME_TYPE,"image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri uri = null;
        String stringUri = null;
        try{
            uri = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
            if(source != null){
                OutputStream outputStream = cr.openOutputStream(Objects.requireNonNull(uri));

                try {
                    source.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                }finally
                    {
                        Objects.requireNonNull(outputStream).close();
                    }
                    //long id = ContentUris.parseId(uri);
                   // Bitmap miniTumb = MediaStore.Images.Thumbnails.getThumbnail(cr,id,MediaStore.Images.Thumbnails.MINI_KIND,null);
                    //storeThumnail(cr,source,id,source.getWidth(),source.getHeight(),MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);


                }
                else
            {
                cr.delete(Objects.requireNonNull(uri),null,null);
                uri = null;
            }
        }catch (IOException e){
            cr.delete(uri,null,null);
            uri=null;
        }
        if(uri != null)
            stringUri = uri.toString();
        return stringUri;








    }


}
