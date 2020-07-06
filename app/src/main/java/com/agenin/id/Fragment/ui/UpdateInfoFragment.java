package com.agenin.id.Fragment.ui;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.agenin.id.DBQueries;
import com.agenin.id.Interface.UserClient;
import com.agenin.id.Model.UserModel;
import com.agenin.id.Preference.UserPreference;
import com.agenin.id.Progress.ProgressRequestBody;
import com.agenin.id.R;
import com.bumptech.glide.Glide;
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

         if (photo.equals("")){
            // Glide.with(getContext()).load(getResources().getDrawable(R.drawable.profil2)).into(circleImageView);
         }else {
             Glide.with(getContext()).load(photo).into(circleImageView);
         }
        nameField.setText(name);
        emailField.setText(email);

        chaangePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHapus = false;

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
                updateData.put("fullname",nameField.getText().toString());
                uploadFile(imageUri,bitmap,getContext(),updateData,loadingDialog);

            }else {

                Map<String, RequestBody> updateData = new HashMap<>();
                updateData.put("email",createPartFromString(emailField.getText().toString()));
                updateData.put("name",createPartFromString(nameField.getText().toString()));
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                UserPreference userPreference = new UserPreference(getContext());
                UserModel user = userPreference.getUserPreference("user");

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
                        loadingDialog.dismiss();

                        Glide.with(getContext()).load(response.body().getProfil()).circleCrop().into(circleImageView);

                        UserPreference userPreference = new UserPreference(getContext());
                        userPreference.setUserPreference("user", response.body());
                        ((Activity) getContext()).finish();
                        Toast.makeText((getContext()), "Update Berhasil!", Toast.LENGTH_SHORT).show();


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
            UserModel user = userPreference.getUserPreference("user");

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
                    loadingDialog.dismiss();


                    UserPreference userPreference = new UserPreference(getContext());
                    userPreference.setUserPreference("user", response.body());
                    ((Activity) getContext()).finish();
                    Toast.makeText((getContext()), "Update Berhasil!", Toast.LENGTH_SHORT).show();


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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadFile(final Uri fileUri, final Bitmap bitmap, final Context context, Map<String, Object> userdata, final Dialog dialog) {

        if (bitmap!=null){




            String name,email;

            email =(String)userdata.get("email");
            name =  (String)userdata.get("fullname");


            HashMap<String, RequestBody> map = new HashMap<>();

            map.put("email", createPartFromString(email));
            map.put("name", createPartFromString(name));


            File file;

            String id = DocumentsContract.getDocumentId(fileUri);
            InputStream inputStream = null;
            try {
                inputStream = context.getContentResolver().openInputStream(fileUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            file = new File(context.getCacheDir().getAbsolutePath()+"/"+id+"."+getFileExtension(fileUri,context));
            writeFile(inputStream, file);




//            ProgressRequestBody fileBody = new ProgressRequestBody(file,context.getContentResolver().getType(fileUri), (ProgressRequestBody.UploadCallbacks) context);
            RequestBody filePart = RequestBody.create(
                    MediaType.parse(context.getContentResolver().getType(fileUri)),
                    file
            );
            MultipartBody.Part body = MultipartBody.Part.createFormData("profil",file.getName(),filePart);

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            UserPreference userPreference = new UserPreference(context);
            UserModel user = userPreference.getUserPreference("user");

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
                    dialog.dismiss();


                    UserPreference userPreference = new UserPreference(context);
                    userPreference.setUserPreference("user", response.body());
                 //   ((Activity) getContext()).finish();
                    ((Activity) context).finish();
                    Toast.makeText((context), "Update Berhasil!", Toast.LENGTH_SHORT).show();


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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == 1000){

            if (resultCode ==getActivity().RESULT_OK){

                onSelectFromGalleryResult(data);

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        bitmap=null;
        if (data != null) {
            updatePhoto = true;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageUri = data.getData();
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
}
