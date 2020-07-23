package com.agenin.id.Fragment.ui;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.agenin.id.Activity.MainActivity;
import com.agenin.id.Activity.PaymentActivity;
import com.agenin.id.DBQueries;
import com.agenin.id.Interface.UserClient;
import com.agenin.id.Model.Api.UserAPI;
import com.agenin.id.Model.UserModel;
import com.agenin.id.Preference.UserPreference;
import com.agenin.id.Progress.ProgressRequestBody;
import com.agenin.id.R;
import com.agenin.id.Util.BitmapUtils;
import com.agenin.id.Util.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import info.hoang8f.widget.FButton;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class DaftarFragment extends Fragment  {


    private static final int REQUEST_CAMERA = 1002;
    private static final int SELECT_FILE = 1000;
    private Uri image_selected_uri=null;
    private Bitmap bitmap;
    private FButton uploadButton;
    private ImageView ivImage,bukti;
    private EditText tgl;
    private EditText an;
    public static ProgressBar progressBar;
    private String userChoosenTask;
    private DatePickerDialog picker;

    private FirebaseAuth firebaseAuth;

    private static Dialog loadingDialog;
    private static boolean isCamera;


    public DaftarFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_daftar, container, false);
        uploadButton =(FButton) itemView.findViewById(R.id.save_btn);
        ivImage = (ImageView)itemView.findViewById(R.id.image_add_payment);
        bukti = (ImageView)itemView.findViewById(R.id.imageView12);
        tgl = itemView.findViewById(R.id.tgl);
        an = itemView.findViewById(R.id.a_n);
        progressBar = itemView.findViewById(R.id.progress_bar);

        //loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //loading dialog


        firebaseAuth = FirebaseAuth.getInstance();
        uploadButton.setButtonColor(getResources().getColor(R.color.colorPrimary));
        uploadButton.setTextColor(getResources().getColor(R.color.colorAccent));

        progressBar.setMax(100);

        tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tgl.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        tgl.setFocusable(false);
        tgl.setFocusableInTouchMode(false);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user==null){
                    firebaseAuth.createUserWithEmailAndPassword((String) SignUpFragment.userdata.get("email"),(String) SignUpFragment.userdata.get("password"))
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseInstanceId.getInstance().getInstanceId()
                                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                        if(task.isSuccessful()){

                                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                                                  @Override
                                                                  public void onSuccess(InstanceIdResult instanceIdResult) {
                                                                      String newToken = instanceIdResult.getToken();
                                                                      SignUpFragment.userdata.put("token_fb",newToken);
                                                                      SignUpFragment.userdata.put("uid",user.getUid());
                                                                      SignUpFragment.userdata.put("bukti_tgl",tgl.getText().toString());
                                                                      SignUpFragment.userdata.put("bukti_an",an.getText().toString());
                                                                      uploadFile(image_selected_uri,bitmap,getContext(),SignUpFragment.userdata,loadingDialog,isCamera);

                                                                  }
                                                              });

                                                        }else {
                                                            Toast.makeText(getActivity(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }else {
                                        loadingDialog.dismiss();
                                        String error = task.getException().getMessage();
                                        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else {

                        FirebaseInstanceId.getInstance().getInstanceId()
                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                        if(task.isSuccessful()){
                                            String token = task.getResult().getToken();
                                            SignUpFragment.userdata.put("token_fb",token);
                                            SignUpFragment.userdata.put("uid",user.getUid());
                                            SignUpFragment.userdata.put("bukti_tgl",tgl.getText().toString());
                                            SignUpFragment.userdata.put("bukti_an",an.getText().toString());
                                            uploadFile(image_selected_uri,bitmap,getContext(),SignUpFragment.userdata,loadingDialog,isCamera);
                                        }else {
                                            Toast.makeText(getActivity(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                }


            }
        });



        return itemView;
    }

//    private void uploadFile() {
//
//        if (image_selected_uri!=null){
//            final String namefile =System.currentTimeMillis()+ "." + getFileExtension(image_selected_uri);
//        }else {
//            Toast.makeText(getContext(),"No file selected",Toast.LENGTH_SHORT).show();
//        }
//    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA) {


               bitmap = BitmapUtils.getBitmapFromGalerry(getContext(),image_selected_uri,800, 800);
//                bukti.setImageBitmap(bitmap);
//                bitmap = (Bitmap) data.getExtras().get("data");

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
//                image_selected_uri = getImageUri(getContext(), bitmap);
//                 bitmap = BitmapUtils.getBitmapFromGalerry(this,image_selected_uri,800, 800);
//                image_selected_uri = getImageUri(getApplicationContext(), bitmap);

                bukti.setImageBitmap(bitmap);

            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        bitmap=null;
        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getApplicationContext().getContentResolver(), data.getData());
                image_selected_uri = data.getData();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bukti.setImageBitmap(bitmap);
    }


    private void openCamera() {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){


                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE,"New Pictures");
                            values.put(MediaStore.Images.Media.DESCRIPTION,"From Camera");
                            image_selected_uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values);
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            if (cameraIntent.resolveActivity(getContext().getPackageManager()) != null) {
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_selected_uri);

                            }
                            startActivityForResult(cameraIntent,REQUEST_CAMERA);

                        }else {
                            Toast.makeText(getContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }


    private void selectImage() {
        final CharSequence[] items = { "Ambil Kamera", "Ambil Galeri",
                "Keluar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Ambil Foto Bukti");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getContext());
                if (items[item].equals("Ambil Kamera")) {
                    isCamera =true;
                    userChoosenTask="Ambil Kamera";
                    if(result)
                        openCamera();
                } else if (items[item].equals("Ambil Galeri")) {
                    isCamera = false;
                    userChoosenTask="Ambil Galeri";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void uploadFile(final Uri fileUri, final Bitmap bitmap, final Context context, Map<String,Object> userdata, final Dialog dialog, Boolean isCamera) {

        if (bitmap!=null){

            String token_fb,passwordori,name,email,name_refferal,password,handphone,bukti_tgl,bukti_bank,bukti_an;
            token_fb = (String)userdata.get("token_fb");
            email =(String)userdata.get("email");
            name =  (String)userdata.get("name");
            passwordori = (String)userdata.get("password");
            name_refferal = (String)userdata.get("name_refferal");
            password =  (String)userdata.get("uid");
            handphone = (String)userdata.get("handphone");
            bukti_tgl = (String)userdata.get("bukti_tgl");
            bukti_bank = (String)userdata.get("bukti_bank");
            bukti_an =  (String)userdata.get("bukti_an");

            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("token_fb", createPartFromString(token_fb));
            map.put("email", createPartFromString(email));
            map.put("name", createPartFromString(name));
            map.put("name_refferal", createPartFromString(name_refferal));
            map.put("password", createPartFromString(password));
            map.put("handphone", createPartFromString(handphone));
            map.put("bukti_tgl", createPartFromString(bukti_tgl));
            map.put("bukti_bank", createPartFromString(bukti_bank));
            map.put("bukti_an", createPartFromString(bukti_an));

            File file;
            if (!isCamera){

                InputStream inputStream = null;
                try {
                    inputStream = context.getContentResolver().openInputStream(fileUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                file = new File(getPath(context,fileUri));
                writeFile(inputStream, file);
            }else {
                InputStream inputStream = null;
                try {
                    inputStream = context.getContentResolver().openInputStream(fileUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                file = savebitmap(bitmap);
                writeFile(inputStream, file);
            }



            ProgressRequestBody fileBody = new ProgressRequestBody(file,context.getContentResolver().getType(fileUri), (ProgressRequestBody.UploadCallbacks) context);
//            RequestBody filePart = RequestBody.create(
//                    MediaType.parse(context.getContentResolver().getType(fileUri)),
//                    file
//            );
            MultipartBody.Part body = MultipartBody.Part.createFormData("bukti",file.getName(),fileBody);

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(DBQueries.url)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.build();

            UserClient client = retrofit.create(UserClient.class);

            Call<UserAPI> call = client.register(body,map);

            call.enqueue(new Callback<UserAPI>() {
                @Override
                public void onResponse(Call<UserAPI> call, Response<UserAPI> response) {
                    if (!response.isSuccessful()) {
                        try {

                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            Toast.makeText(context,jsonObject.get("mesagge").toString(),Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


//                     Toast.makeText(context,response.body().getMesagge(),Toast.LENGTH_SHORT).show();
//                        if (dialog!=null) {
//                            dialog.dismiss();
//                        }
//                        return;
                      final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        // Get auth credentials from the user for re-authentication. The example below shows
                        // email and password credentials but there are multiple possible providers,
                        // such as GoogleAuthProvider or FacebookAuthProvider.
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(email, passwordori);

                        // Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        user.delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            UserPreference userPreference = new UserPreference(context);
                                                            userPreference.setUserPreference("user", null);

                                                            FirebaseAuth.getInstance().signOut();

                                                            if (dialog!=null) {
                                                                dialog.dismiss();
                                                            }
                                                            return;
                                                        }
                                                    }
                                                });

                                    }
                                });

                    }else {
                        dialog.dismiss();
                        UserPreference userPreference = new UserPreference(context);
                        UserModel userModel = response.body().getUser();
                        userPreference.setUserPreference("user", userModel);
                        SignInFragment.isLogin = true;
                        Toast.makeText(context, "Selamat Datang di Agenin", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        loadingDialog.dismiss();
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }


                }

                @Override
                public void onFailure(Call<UserAPI> call, Throwable t) {
                 final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    // Get auth credentials from the user for re-authentication. The example below shows
                    // email and password credentials but there are multiple possible providers,
                    // such as GoogleAuthProvider or FacebookAuthProvider.
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email, passwordori);

                    // Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    user.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        dialog.dismiss();
                                                        FirebaseAuth.getInstance().signOut();
                                                        UserPreference userPreference = new UserPreference(context);
                                                        userPreference.setUserPreference("user", null);
                                                        Log.i("error",call.toString());
                                                        Log.i("trhrowable",t.toString());
                                                        Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                }
                            });

                }
            });


        }else {
            Toast.makeText(context,"Silahkan Pilih Foto",Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }


    private static void writeFile(InputStream in, File file) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if ( out != null ) {
                    out.close();
                }
                in.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }


    private static String getFileExtension(Uri uri,Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }



    private static File savebitmap(Bitmap bmp) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        // String temp = null;
        File file = new File(extStorageDirectory, "temp.png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, "temp.png");

        }

        try {
            outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }


    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    String docId = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        docId = DocumentsContract.getDocumentId(uri);
                    }
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    String id = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        id = DocumentsContract.getDocumentId(uri);
                    }
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
