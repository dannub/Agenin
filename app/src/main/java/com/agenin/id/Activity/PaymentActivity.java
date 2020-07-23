package com.agenin.id.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agenin.id.Adapter.CartPaymentAdapter;
import com.agenin.id.DBQueries;
import com.agenin.id.Interface.NotaClient;
import com.agenin.id.Model.Api.MyOrderAPI;
import com.agenin.id.Model.Api.NotaItemAPI;
import com.agenin.id.Model.CartItemModel;
import com.agenin.id.Model.UserModel;
import com.agenin.id.Preference.UserPreference;
import com.agenin.id.Progress.ProgressRequestBody;
import com.agenin.id.R;
import com.agenin.id.Util.BitmapUtils;
import com.agenin.id.Util.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import info.hoang8f.widget.FButton;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentActivity extends AppCompatActivity  implements  ProgressRequestBody.UploadCallbacks  {

    private static final int REQUEST_CAMERA = 1002;
    private static final int SELECT_FILE = 1000;
    private ImageView ivImage,bukti;
    public  static List<CartItemModel> cartItemModelList;
    private String userChoosenTask;

    private Uri image_selected_uri=null;

    private boolean isbukti;
    public static boolean isfree;

    private static EditText tgl;
    private static EditText an;
    private FButton Save;
    private ProgressBar progressBar;
    private String[] bankList;
    private  int bank;
    private static String bank_str;
    private String selectedBank;
    private DatePickerDialog picker;
    private FirebaseUser currentUser;
    private StorageReference storageReference;
    private Dialog loadingDialog;

    private ConstraintLayout orderConfirmationLayout;
    private static ConstraintLayout notaPaymentLayout;
    private ConstraintLayout input;

    private static ImageButton continueShoppingBtn;
    private static TextView orderID;
    private static Button downloadNota;

    private static TextView idPayment;
    private static TextView totalBayar;
    private static TextView waktuPesan;
    private static TextView alamat;
    private static TextView bank_tv;
    private static TextView status;

    public static CartPaymentAdapter cartPaymentAdapter;
    public static RecyclerView recyclerViewItem;


    private boolean successResponse = false;
    public static boolean fromCart;

    private String dirpath;
    private String now;
    private static final int REQUEST = 112;

    public  static LinearLayoutManager linearLayoutManager;

    private FirebaseFirestore firebaseFirestore;
    private Map<String,Object> notadata;
    private static String fullname,fullAddress,pincode;

    private Bitmap bitmap;
    private static boolean isCamera;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //loading dialog
        loadingDialog = new Dialog(PaymentActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(PaymentActivity.this.getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //loading dialog

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Upload Bukti");



        ivImage = (ImageView)findViewById(R.id.image_add_payment);
        bukti = (ImageView)findViewById(R.id.imageView12);
        tgl = findViewById(R.id.tgl);
        an = findViewById(R.id.a_n);
        progressBar = findViewById(R.id.progress_bar);
        Save = findViewById(R.id.save_btn);
        orderConfirmationLayout = findViewById(R.id.order_confirmation_layout);
        continueShoppingBtn = findViewById(R.id.continue_shopping_btn);
        downloadNota = findViewById(R.id.download);
        orderID = findViewById(R.id.order_id);
        notaPaymentLayout = findViewById(R.id.notalayout);
        downloadNota = findViewById(R.id.download);
        recyclerViewItem = findViewById(R.id.item_recycleview);

        idPayment = findViewById(R.id.id);
        totalBayar = findViewById(R.id.totalbayar);
        waktuPesan = findViewById(R.id.waktupesan);
        alamat = findViewById(R.id.alamat);
        bank_tv = findViewById(R.id.bank);
        status = findViewById(R.id.status);

        input = findViewById(R.id.input);

        Save.setButtonColor(getResources().getColor(R.color.colorPrimary));
        Save.setTextColor(getResources().getColor(R.color.colorAccent));




        cartPaymentAdapter = new CartPaymentAdapter(cartItemModelList);


        recyclerViewItem.setAdapter(cartPaymentAdapter);

        linearLayoutManager = new LinearLayoutManager(PaymentActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewItem.setLayoutManager(linearLayoutManager);

        cartPaymentAdapter.notifyDataSetChanged();


        notadata = new HashMap<>();


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                bank = 1;
            } else {
                bank= extras.getInt("bank");
            }
        } else {
            bank= (int) savedInstanceState.getSerializable("bank");
        }

        if (bank==1){
           bank_str = "BRI";
        }else if (bank == 2){
            bank_str = "BNI";
        }else if(bank==3){
            bank_str = "BCA";
        }else {
            bank_str = "Mandiri";
        }


        isbukti = false;

        storageReference = FirebaseStorage.getInstance().getReference("bukti");
        firebaseFirestore = FirebaseFirestore.getInstance();


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });
                uploadFile(image_selected_uri,bitmap,PaymentActivity.this,loadingDialog,isCamera);

            }
        });



        String name = DBQueries.addressModelList.get(DBQueries.selectedAddress).getNama();
        String mobileNo = DBQueries.addressModelList.get(DBQueries.selectedAddress).getNo_telepon();
        if (DBQueries.addressModelList.get(DBQueries.selectedAddress).getNo_alternatif().equals("")){
            fullname = name + " | "+mobileNo;
        }else {
            fullname =name + " | "+mobileNo+" atau "+DBQueries.addressModelList.get(DBQueries.selectedAddress).getNo_alternatif();
        }
        String flatNo = DBQueries.addressModelList.get(DBQueries.selectedAddress).getProvinsi();
        String locality = DBQueries.addressModelList.get(DBQueries.selectedAddress).getKabupaten();
        String landmark = DBQueries.addressModelList.get(DBQueries.selectedAddress).getAlamat();
        String city = DBQueries.addressModelList.get(DBQueries.selectedAddress).getKecamatan();
        String state = DBQueries.addressModelList.get(DBQueries.selectedAddress).getNegara();
        if (landmark.equals("")) {
            fullAddress =city+" "+locality + " "+flatNo+" "+" "+state;
        }else {
            fullAddress=landmark+" "+city+" "+locality + " "+flatNo+" "+state;
        }
        pincode=DBQueries.addressModelList.get(DBQueries.selectedAddress).getKodepos();







        tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(PaymentActivity.this,
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



    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadFile(final Uri fileUri, final Bitmap bitmap, final Context context, final Dialog dialog, Boolean isCamera) {

        if (bitmap!=null){


            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("atas_nama", createPartFromString(an.getText().toString()));
            map.put("bank", createPartFromString(bank_str));
            map.put("tgl_transfer", createPartFromString(tgl.getText().toString().trim()));
            map.put("full_address", createPartFromString(fullAddress));
            map.put("phone", createPartFromString(fullname));
            map.put("detail_address", createPartFromString(fullname+" "+fullAddress));
            map.put("kode_pos", createPartFromString(pincode));
            map.put("total_ongkir", createPartFromString(cartItemModelList.get(cartItemModelList.size()-1).getDeliveryPrice()));
            map.put("total_item_price", createPartFromString(String.valueOf(cartItemModelList.get(cartItemModelList.size()-1).getTotalItemsPrice())));
            map.put("total_amount", createPartFromString(String.valueOf(cartItemModelList.get(cartItemModelList.size()-1).getTotalAmount())));
            map.put("save_ongkir", createPartFromString(String.valueOf(cartItemModelList.get(cartItemModelList.size()-1).getSavedAmount())));

            Gson gson = new Gson();


            for (int i = 0; i < cartItemModelList.size()-1; i++) {


                    NotaItemAPI notaItemAPI = new NotaItemAPI(cartItemModelList.get(i).getProductID(), cartItemModelList.get(i).getProductQuantity(), cartItemModelList.get(i).getOngkir());
                    String json = gson.toJson(notaItemAPI);
                    map.put("items[" + i + "]", createPartFromString(json));



            }


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
//                String id = DocumentsContract.getDocumentId(fileUri);
                InputStream inputStream = null;
                try {
                    inputStream = context.getContentResolver().openInputStream(fileUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                file = savebitmap(bitmap);
                writeFile(inputStream, file);
//                InputStream inputStream = null;
//                try {
//                    inputStream = context.getContentResolver().openInputStream(fileUri);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                file = new File(getRealPathFromURI(image_selected_uri));
////                file = new File(getRealPathFromURI(context,fileUri));
//                writeFile(inputStream, file);

            }




            gson = new GsonBuilder()
                    .setLenient()
                    .create();

            ProgressRequestBody fileBody = new ProgressRequestBody(file,context.getContentResolver().getType(fileUri), (ProgressRequestBody.UploadCallbacks) context);
//            RequestBody filePart = RequestBody.create(
//                    MediaType.parse(context.getContentResolver().getType(fileUri)),
//                    file
//            );
            MultipartBody.Part body = MultipartBody.Part.createFormData("bukti",file.getName(),fileBody);

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

            NotaClient client = retrofit.create(NotaClient.class);

            Call<MyOrderAPI> call = null;
            if(MainActivity.showCart){
                call= client.setNota(user.getId(),body,map);
            }else {
                call= client.setDelivNota(user.getId(),true,body,map);
            }


            call.enqueue(new Callback<MyOrderAPI>() {
                @Override
                public void onResponse(Call<MyOrderAPI> call, Response<MyOrderAPI> response) {
                    if (!response.isSuccessful()) {
                        Log.i("response", String.valueOf(response.body()));
                        Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();
                        if (dialog!=null) {
                            dialog.dismiss();
                        }
                        return;
                    }

                    MyOrderAPI myOrderAPI = response.body();

                    idPayment.setText("Kode.Pesanan: " + myOrderAPI.get_id());
                    totalBayar.setText("Rp "+DBQueries.currencyFormatter(String.valueOf(cartItemModelList.get(cartItemModelList.size()-1).getTotalAmount())));
                    String format = "dd-MM-yyyy HH:mm:ss";
                    final SimpleDateFormat sdf = new SimpleDateFormat(format);

                    final String dateString = sdf.format(DBQueries.StringtoDate(myOrderAPI.getOrdered_date()));
                    waktuPesan.setText(dateString);
                    status.setText("Belum Dikonfirmasi");
                    alamat.setText(fullname + " " + fullAddress+" "+pincode);
                    bank_tv.setText("Bank " + bank_str);

                    if (MainActivity.showCart) {


                        final List<Integer> indexList = new ArrayList<>();
                        for (int x = 0; x < DBQueries.cartlist.size() ; x++) {

                            if (DBQueries.cartItemModelList.get(x).getInStock()) {
                                indexList.add(x);
                            }

                        }

                        for (int x = indexList.size() - 1; x >= 0; x--) {

                            DBQueries.cartlist.remove(indexList.get(x).intValue());
                            DBQueries.cartItemModelList.remove(indexList.get(x).intValue());
                            //DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size()-1);
                        }

                        if (DBQueries.cartlist.size() != 0) {


                            MainActivity.badgeCount.setVisibility(View.VISIBLE);
                           MainActivity.mainActivity.loadCart();
                        } else {
                            MainActivity.badgeCount.setVisibility(View.INVISIBLE);
                        }




                    }





                    orderID.setText("Order ID " + myOrderAPI.get_id());
                    downloadNota.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                           SaveNota(PaymentActivity.this);
                        }
                    });

                    continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (fromCart) {
                                if ( CartActivity.mycartfragment != null) {
                                    if (!MainActivity.showCart) {
                                        CartActivity.mycartfragment.finish();
                                        Intent mainIntent = new Intent(PaymentActivity.this, MainActivity.class);
                                        startActivity(mainIntent);
                                    }
                                    MainActivity.showCart = false;
                                    CartActivity.mycartfragment = null;


                                }
                            }
                            finish();

                        }
                    });


                    Save.setEnabled(true);
                    notaPaymentLayout.setVisibility(View.VISIBLE);
                    orderConfirmationLayout.setVisibility(View.VISIBLE);

                    input.setVisibility(View.INVISIBLE);

                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(getCurrentFocus(), InputMethodManager.HIDE_IMPLICIT_ONLY);




                    if (!MainActivity.showCart) {
                        if (ProductDetailActivity.productDetailsActivity != null) {
                            ProductDetailActivity.productDetailsActivity.finish();
                            ProductDetailActivity.productDetailsActivity = null;
                        }
                    }
                    if (PaymentInfoActivity.paymentInfoActivity != null) {
                        PaymentInfoActivity.paymentInfoActivity.finish();
                        PaymentInfoActivity.paymentInfoActivity = null;
                    }
                    if (DeliveryActivity.deliveryActivity != null) {
                        DeliveryActivity.deliveryActivity.finish();
                        DeliveryActivity.deliveryActivity = null;
                    }
                    if (CartActivity.cartActivity != null) {
                        CartActivity.cartActivity .finish();
                        CartActivity.cartActivity  = null;
                    }

                    dialog.dismiss();

                }

                @Override
                public void onFailure(Call<MyOrderAPI> call, Throwable t) {
                    dialog.dismiss();
                    Log.i("error",call.toString());
                    Log.i("trhrowable",t.toString());
                    Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
                }
            });


        }else {
            Toast.makeText(context,"No file selected",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA) {
//                bitmap = (Bitmap) data.getExtras().get("data");

//                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
//                image_selected_uri = getImageUri(PaymentActivity.this, bitmap);
                 bitmap = BitmapUtils.getBitmapFromGalerry(this,image_selected_uri,800, 800);
//                image_selected_uri = getImageUri(getApplicationContext(), bitmap);
                isbukti = true;
                bukti.setImageBitmap(bitmap);
            }

        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
         bitmap=null;
        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                image_selected_uri= Objects.requireNonNull(data).getData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isbukti = true;
        bukti.setImageBitmap(bitmap);
    }


    private void openCamera() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){

                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE,"New Pictures");
                            values.put(MediaStore.Images.Media.DESCRIPTION,"From Camera");
                            image_selected_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values);
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_selected_uri);

                            }
                            startActivityForResult(cameraIntent,REQUEST_CAMERA);

                        }else {
                            Toast.makeText(PaymentActivity.this,"Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }


    private void selectImage() {
        final CharSequence[] items = { "Ambil Kamera", "Ambil Galeri",
                "Keluar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
        builder.setTitle("Ambil Foto Bukti");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(PaymentActivity.this);
                if (items[item].equals("Ambil Kamera")) {
                    isCamera =true;
                    userChoosenTask="Ambil Kamera";
                    if(result)
                        openCamera();
                } else if (items[item].equals("Ambil Galeri")) {
                    isCamera =false;
                    userChoosenTask="Ambil Galeri";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Batal")) {
                    isCamera =false;
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

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (successResponse){
            if (CartActivity.mycartfragment != null) {
                if (!MainActivity.showCart) {
                    CartActivity.mycartfragment.finish();
                    Intent mainIntent = new Intent(PaymentActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }
                MainActivity.showCart = false;
                CartActivity.mycartfragment = null;


            }
            finish();

            return;
        }
        super.onBackPressed();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do here
                    layoutToImage();
//                    try {
//                        imageToPDF();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }


                } else {
                    Toast.makeText(PaymentActivity.this, "The app was not allowed to read your store.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    private void SaveNota(final Context context){
        final ProgressDialog progress = new ProgressDialog(context);
        class SaveThisImage extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.setTitle("Download");
                progress.setMessage("Tunggu Sebentar...");
                progress.setCancelable(false);
                progress.show();
            }
            @Override
            protected Void doInBackground(Void... arg0) {
                layoutToImage();
//                try {
//                    imageToPDF();
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                return null;
            }
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                if(progress.isShowing()){
                    progress.dismiss();
                }
                Toast.makeText(context, "Nota Telah Tersimpan", Toast.LENGTH_SHORT).show();
            }
        }
        SaveThisImage shareimg = new SaveThisImage();
        shareimg.execute();
    }


    public void layoutToImage() {
        // get view group using reference
        // convert view group to bitmap
        notaPaymentLayout.setDrawingCacheEnabled(true);
        notaPaymentLayout.buildDrawingCache();


        Bitmap bm = Bitmap.createBitmap(notaPaymentLayout.getWidth(), notaPaymentLayout.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bm);
        notaPaymentLayout.layout(0, 0, notaPaymentLayout.getMeasuredWidth(), notaPaymentLayout.getMeasuredHeight());
        notaPaymentLayout.draw(c);

        // Bitmap bm = detail.getDrawingCache(true);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(PaymentActivity.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) PaymentActivity.this, PERMISSIONS, REQUEST );
            } else {
                //do here
                now = Long.toString(System.currentTimeMillis());
                File f = new File(android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/"+now+"nota.jpg");
                try {

                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                notaPaymentLayout.setDrawingCacheEnabled(false);
            }
        } else {
            //do here
            now = Long.toString(System.currentTimeMillis());
            File f = new File(android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/"+now+ "nota.jpg");
            try {

                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            notaPaymentLayout.setDrawingCacheEnabled(false);
        }


    }
//    public void imageToPDF() throws FileNotFoundException {
//        try {
//            Document document = new Document(PageSize.A4);
//            dirpath = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
//            PdfWriter.getInstance(document, new FileOutputStream(dirpath + "/"+now+"nota.pdf")); //  Change pdf's name.
//            document.open();
//            Image img = Image.getInstance(dirpath +"/Download/" + now+"nota.jpg");
//            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
//                    - document.rightMargin() - 0) / img.getHeight()) * 100;
//            img.scalePercent(scaler);
//            img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
//            document.add(img);
//            document.close();
//            Toast.makeText(this, "Silahkan Cek di Download", Toast.LENGTH_SHORT).show();
//           // print.setVisibility(View.VISIBLE);
//        } catch (Exception e) {
//
//        }
//    }

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






    private File savebitmap(Bitmap bmp) {
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



    @Override
    public void onProgressUpdate(int percentage) {

           progressBar.setProgress(percentage);

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void uploadStart() {

    }


}
