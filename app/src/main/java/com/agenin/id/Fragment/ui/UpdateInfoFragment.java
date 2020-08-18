package com.agenin.id.Fragment.ui;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.agenin.id.Activity.MainActivity;
import com.agenin.id.Activity.UpdateUserInfoActivity;
import com.agenin.id.DBQueries;
import com.agenin.id.Interface.UserClient;
import com.agenin.id.Model.UserModel;
import com.agenin.id.Preference.UserPreference;
import com.agenin.id.Progress.ProgressRequestBody;
import com.agenin.id.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateInfoFragment extends Fragment {




    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    private CircleImageView circleImageView;
    private Button chaangePhotoBtn,removeBtn,updateBtn,doneBtn;
    private static EditText nameField;
    private static EditText emailField;
    private EditText password;
    private Dialog loadingDialog,passwordDialog;
    private String name;
    private String email;
    private String photo;
    private Uri imageUri;
    private Bitmap bitmap;
    private Boolean isHapus = true;
    private  boolean updatePhoto = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_info, container, false);

        circleImageView = view.findViewById(R.id.profile_image);
        chaangePhotoBtn = view.findViewById(R.id.change_photo_btn);
        removeBtn = view.findViewById(R.id.remove_photo_btn);
        updateBtn = view.findViewById(R.id.update);
        nameField = view.findViewById(R.id.name);
        emailField = view.findViewById(R.id.email);

        //loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //loading dialog

        //password dialog
       passwordDialog = new Dialog(getContext());
       passwordDialog.setContentView(R.layout.password_confirm_dialog);
       passwordDialog.setCancelable(true);
       passwordDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
       passwordDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

       password = passwordDialog.findViewById(R.id.password);
       doneBtn = passwordDialog.findViewById(R.id.done_btn);


       //password dialog

         name = getArguments().getString("Name");
         email = getArguments().getString("Email");
         photo = getArguments().getString("Photo");


         if (photo.isEmpty()){
             Glide.with(getContext()).load(getResources().getDrawable(R.drawable.profil2)).into(circleImageView);
         }else {
             Glide.with(getContext()).load(photo).apply(new RequestOptions().placeholder(R.drawable.profil2)).into(circleImageView);
         }
        nameField.setText(name);
        emailField.setText(email);

        chaangePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHapus = false;
                updatePhoto = true;
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);//
                        startActivityForResult(Intent.createChooser(intent, "Select File"),1000);
                    }else {
                        getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
                    }
                }else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);//
                    startActivityForResult(Intent.createChooser(intent, "Select File"),1000);
                }
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri = null;
                updatePhoto = true;
                isHapus = true;
                Glide.with(getContext()).load(R.drawable.profil2).into(circleImageView);
            }
        });

        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });

        return view;
    }

    private void checkInputs()  {
        if(!TextUtils.isEmpty(emailField.getText())){
            if(!TextUtils.isEmpty(nameField.getText())){
                updateBtn.setEnabled(true);
            }
            else{
                updateBtn.setEnabled(false);
            }
        }else {
            updateBtn.setEnabled(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void checkEmailAndPassword() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

        Drawable customErrorIcon = getResources().getDrawable(R.mipmap.error);
        customErrorIcon.setBounds(0,0,customErrorIcon.getIntrinsicWidth(),customErrorIcon.getIntrinsicHeight());


        if(emailField.getText().toString().matches(emailPattern)){

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (emailField.getText().toString().toLowerCase().trim().equals(email.trim())){
               //same email
                loadingDialog.show();
                updatePhoto();

            }else {
                //update email
                passwordDialog.show();
                doneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        
                        loadingDialog.show();
                        String userPassword = password.getText().toString();

                        passwordDialog.dismiss();

                        AuthCredential credential = EmailAuthProvider
                                .getCredential(email, userPassword);

                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                            user.updateEmail(emailField.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        updatePhoto();
                                                    }else {
                                                        loadingDialog.dismiss();
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        }else {
                                            loadingDialog.dismiss();
                                            String error = task.getException().getMessage();
                                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
            }

        }else {
            emailField.setError("Invalid Email!",customErrorIcon);
        }
    }


    @NonNull
    private static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private  void  updatePhoto(){
        //update photo

        if (updatePhoto){

            if (imageUri != null){
                loadingDialog.show();
                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });

                Map<String, Object> updateData = new HashMap<>();
                updateData.put("email",emailField.getText().toString());
                updateData.put("name",nameField.getText().toString());
                uploadFile(imageUri,bitmap,getContext(),updateData,loadingDialog);

            }else {

                Map<String, RequestBody> updateData = new HashMap<>();
                updateData.put("email",createPartFromString(emailField.getText().toString()));
                updateData.put("name",createPartFromString(nameField.getText().toString()));
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                UserPreference userPreference = new UserPreference(getContext());
                UserModel user = UserPreference.getUserPreference("user");

                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                Request newRequest  = chain.request().newBuilder()
                                        .addHeader("Authorization", "Bearer " + user.getToken())
                                        .build();
                                return chain.proceed(newRequest);
                            }
                        })
                        .readTimeout(2, TimeUnit.MINUTES)
                        .connectTimeout(2, TimeUnit.MINUTES)
                        .build();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(DBQueries.url)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                UserClient client = retrofit.create(UserClient.class);

                Call<UserModel> call = client.updateHapusProfil(user.getId(),updateData);

                call.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if(!response.isSuccessful()){
                            try {
                                Log.i("response", response.errorBody().string());
                                Toast.makeText(getContext(), (CharSequence) response.errorBody().string(),Toast.LENGTH_SHORT).show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (loadingDialog!=null) {
                                loadingDialog.dismiss();
                            }
                            return;

                        }

                        Glide.with(getContext()).load(response.body().getProfil()).circleCrop().into(circleImageView);

                        UserPreference userPreference = new UserPreference(getContext());
                        UserPreference.setUserPreference("user", response.body());
                        if (!response.body().getProfil().equals("")) {
                            Glide.with(getContext()).load(DBQueries.url + response.body().getProfil()).apply(new RequestOptions().placeholder(R.drawable.profil2)).into(ProfilFragment.profileview);

                        } else {
                            ProfilFragment.profileview.setImageDrawable(getContext().getResources().getDrawable(R.drawable.profil2));

                        }
                        ((Activity) getContext()).finish();
                        Toast.makeText((getContext()), "Update Berhasil!", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();


                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        loadingDialog.dismiss();
                        Log.i("error",call.toString());
                        Log.i("trhrowable",t.toString());
                        ((Activity) getContext()).finish();
             //           Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }else {

            Map<String, RequestBody> updateData = new HashMap<>();
            updateData.put("email",createPartFromString(emailField.getText().toString()));
            updateData.put("name",createPartFromString(nameField.getText().toString()));

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            UserPreference userPreference = new UserPreference(getContext());
            UserModel user = UserPreference.getUserPreference("user");

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request newRequest  = chain.request().newBuilder()
                                    .addHeader("Authorization", "Bearer " + user.getToken())
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    })
                    .readTimeout(2, TimeUnit.MINUTES)
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(DBQueries.url)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();


            UserClient client = retrofit.create(UserClient.class);

            Call<UserModel> call = client.updateProfil(user.getId(),updateData);

            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                    if(!response.isSuccessful()){
                        try {
                            Log.i("response", response.errorBody().string());
                            Toast.makeText(getContext(), (CharSequence) response.errorBody().string(),Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                       if (loadingDialog!=null) {
                            loadingDialog.dismiss();
                        }
                        return;

                    }


                    UserPreference userPreference = new UserPreference(getContext());
                    UserPreference.setUserPreference("user", response.body());
                    ((Activity) getContext()).finish();
                    Toast.makeText((getContext()), "Update Berhasil!", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();


                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    loadingDialog.dismiss();
                    Log.i("error",call.toString());
                    Log.i("trhrowable",t.toString());
                    ((Activity) getContext()).finish();
         //           Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();
                }
            });


        }

        //update photo
    }


    public void uploadFile(final Uri fileUri, final Bitmap bitmap, final Context context, Map<String, Object> userdata, final Dialog dialog) {

      if (bitmap!=null){




            String name,email;

            email =(String)userdata.get("email");
            name =  (String)userdata.get("name");


            HashMap<String, RequestBody> map = new HashMap<>();

            map.put("email", createPartFromString(email));
            map.put("name", createPartFromString(name));


            File file;



//            String id = DocumentsContract.getDocumentId(fileUri);
            InputStream inputStream = null;
            try {
                inputStream = context.getContentResolver().openInputStream(fileUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            file = new File(getPath(context,fileUri));
            writeFile(inputStream, file);


            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

//            ProgressRequestBody fileBody = new ProgressRequestBody(file,context.getContentResolver().getType(fileUri), (ProgressRequestBody.UploadCallbacks) context);
            RequestBody filePart = RequestBody.create(
                    MediaType.parse(context.getContentResolver().getType(fileUri)),
                    file
            );
            MultipartBody.Part body = MultipartBody.Part.createFormData("profil",file.getName(),filePart);


            UserPreference userPreference = new UserPreference(context);
            UserModel user = UserPreference.getUserPreference("user");

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request newRequest  = chain.request().newBuilder()
                                    .addHeader("Authorization", "Bearer " + user.getToken())
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    })
                    .readTimeout(2, TimeUnit.MINUTES)
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .build();
          Retrofit retrofit = new Retrofit.Builder()
                  .baseUrl(DBQueries.url)
                  .client(okHttpClient)
                  .addConverterFactory(GsonConverterFactory.create(gson))
                  .build();
//            Retrofit.Builder builder = new Retrofit.Builder()
//                    .baseUrl(DBQueries.url)
//                    .client(okHttpClient)
//                    .addConverterFactory(GsonConverterFactory.create());




            UserClient client = retrofit.create(UserClient.class);

            Call<UserModel> call = client.updateProfilPhoto(user.getId(),body,map);

            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if(!response.isSuccessful()){
                        try {
                            Log.i("response", response.errorBody().string());
                            Toast.makeText(getContext(), (CharSequence) response.errorBody().string(),Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (dialog!=null) {
                            dialog.dismiss();
                        }
                        return;

                    }



                    UserPreference userPreference = new UserPreference(context);
                    UserPreference.setUserPreference("user", response.body());
                   if (!response.body().getProfil().equals("")) {
                     Glide.with(UpdateUserInfoActivity.profilFragment.getContext()).load(DBQueries.url + response.body().getProfil()).apply(new RequestOptions().placeholder(R.drawable.profil2)).into(ProfilFragment.profileview);

                    } else {
                        ProfilFragment.profileview.setImageDrawable(UpdateUserInfoActivity.profilFragment.getContext().getResources().getDrawable(R.drawable.profil2));

                    }

                    //   ((Activity) getContext()).finish();
                    ((Activity) context).finish();
                    Toast.makeText((context), "Update Berhasil!", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    dialog.dismiss();
                    Log.i("error",call.toString());
                    Log.i("trhrowable",t.toString());
           //         Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
                }
            });


        }else {
            Toast.makeText(context,"No file selected",Toast.LENGTH_SHORT).show();
        }
    }

    private static String getFileExtension(Uri uri,Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
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

//    private  void updateField(FirebaseUser user, final Map<String, Object> updateData){
//        FirebaseFirestore.getInstance().collection("USERS").document(user.getUid()).update(updateData)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//                            if (updateData.size()>1){
//                                DBqueries.email = emailField.getText().toString().trim();
//                                DBqueries.fullname = nameField.getText().toString().trim();
//                            }else {
//                                DBqueries.fullname = nameField.getText().toString().trim();
//                            }
//                            getActivity().finish();
//                            Toast.makeText(getContext(), "Update Berhasil!", Toast.LENGTH_SHORT).show();
//
//
//                        }else {
//                            String error = task.getException().getMessage();
//                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
//                        }
//                        loadingDialog.dismiss();
//
//                    }
//                });
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == 1000){

            if (resultCode ==getActivity().RESULT_OK){

                onSelectFromGalleryResult(data);

            }
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        bitmap=null;
        if (data != null) {
            updatePhoto = true;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getApplicationContext().getContentResolver(), data.getData());
                imageUri = Objects.requireNonNull(data).getData();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Glide.with(getContext()).load(imageUri).into(circleImageView);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"),1000);
            }else {
                Toast.makeText(getContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }

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
