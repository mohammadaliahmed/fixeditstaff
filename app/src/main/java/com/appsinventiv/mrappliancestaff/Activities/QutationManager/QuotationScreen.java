package com.appsinventiv.mrappliancestaff.Activities.QutationManager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.appsinventiv.mrappliancestaff.Activities.BookingSumary;
import com.appsinventiv.mrappliancestaff.Activities.Login;
import com.appsinventiv.mrappliancestaff.Models.LogsModel;
import com.appsinventiv.mrappliancestaff.R;
import com.appsinventiv.mrappliancestaff.Utils.CommonUtils;
import com.appsinventiv.mrappliancestaff.Utils.NotificationAsync;
import com.appsinventiv.mrappliancestaff.Utils.NotificationObserver;
import com.appsinventiv.mrappliancestaff.Utils.SharedPrefs;
import com.appsinventiv.mrappliancestaff.provider.GetAddress;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class QuotationScreen extends AppCompatActivity implements NotificationObserver {

    EditText clientName, email, address, city, phone, orderId, invoiceDate;
    TextView ticketNumber, date;
    Button calculate, send;

    LinearLayout wholeView;

    DatabaseReference mDatabase;
    RelativeLayout adasdsadas;


    EditText qty1, qty2, qty3, qty4, qty5, qty6, qty7;
    EditText price1, price2, price3, price4, price5, price6, price7;
    EditText gross1, gross2, gross3, gross4, gross5, gross6, gross7;
    EditText vat1, vat2, vat3, vat4, vat5, vat6, vat7;
    EditText total1, total2, total3, total4, total5, total6, total7;
    TextView totalValue, balance;
    EditText advance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);
        getPermissions();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        initUI();

        setupTopSection();

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showAlert();
                performCalculations();
            }
        });
    }

    private void performCalculations() {
        float grosss1 = 0;
        float totall1 = 0;
        float vatt1 = 0;
        if (qty1.getText().length() > 0 && price1.getText().length() > 0) {
            grosss1 = Float.parseFloat(qty1.getText().toString()) * Float.parseFloat(price1.getText().toString());
            vatt1 = Float.parseFloat((vat1.getText().toString().equalsIgnoreCase("")?"0":vat1.getText().toString()));
            totall1 = grosss1 + (grosss1*(vatt1/100));
            gross1.setText(String.format("%.2f", grosss1));
//            vat1.setText(String.format("%.2f", vatt1));
            total1.setText(String.format("%.2f", totall1));
        }
        float grosss2 = 0;
        float totall2 = 0;
        float vatt2 = 0;
        if (qty2.getText().length() > 0 && price2.getText().length() > 0) {
            grosss2 = Float.parseFloat(qty2.getText().toString()) * Float.parseFloat(price2.getText().toString());
            vatt2 =  Float.parseFloat((vat2.getText().toString().equalsIgnoreCase("")?"0":vat2.getText().toString()));
            totall2 = grosss2 +  (grosss2*(vatt2/100));
            gross2.setText(String.format("%.2f", grosss2));
//            vat2.setText(String.format("%.2f", vatt2));
            total2.setText(String.format("%.2f", totall2));
        }
        float grosss3 = 0;
        float totall3 = 0;
        float vatt3 = 0;
        if (qty3.getText().length() > 0 && price3.getText().length() > 0) {
            grosss3 = Float.parseFloat(qty3.getText().toString()) * Float.parseFloat(price3.getText().toString());
            vatt3 =  Float.parseFloat((vat3.getText().toString().equalsIgnoreCase("")?"0":vat3.getText().toString()));
            totall3 = grosss3 + (grosss3*(vatt3/100));
            gross3.setText(String.format("%.2f", grosss3));
//            vat3.setText(String.format("%.2f", vatt3));
            total3.setText(String.format("%.2f", totall3));
        }
        float grosss4 = 0;
        float totall4 = 0;
        float vatt4 = 0;
        if (qty4.getText().length() > 0 && price4.getText().length() > 0) {
            grosss4 = Float.parseFloat(qty4.getText().toString()) * Float.parseFloat(price4.getText().toString());
            vatt4 =Float.parseFloat((vat4.getText().toString().equalsIgnoreCase("")?"0":vat4.getText().toString()));
            totall4 = grosss4 +  (grosss4*(vatt4/100));
            gross4.setText(String.format("%.2f", grosss4));
//            vat4.setText(String.format("%.2f", vatt4));
            total4.setText(String.format("%.2f", totall4));
        }
        float grosss5 = 0;
        float totall5 = 0;
        float vatt5 = 0;
        if (qty5.getText().length() > 0 && price5.getText().length() > 0) {
            grosss5 = Float.parseFloat(qty5.getText().toString()) * Float.parseFloat(price5.getText().toString());
            vatt5 =  Float.parseFloat((vat5.getText().toString().equalsIgnoreCase("")?"0":vat5.getText().toString()));
            totall5 = grosss5 + (grosss5*(vatt5/100));
            gross5.setText(String.format("%.2f", grosss5));
//            vat5.setText(String.format("%.2f", vatt5));
            total5.setText(String.format("%.2f", totall5));
        }
        float grosss6 = 0;
        float totall6 = 0;
        float vatt6 = 0;
        if (qty6.getText().length() > 0 && price6.getText().length() > 0) {
            grosss6 = Float.parseFloat(qty6.getText().toString()) * Float.parseFloat(price6.getText().toString());
            vatt6 =  Float.parseFloat((vat6.getText().toString().equalsIgnoreCase("")?"0":vat6.getText().toString()));
            totall6 = grosss6 + (grosss6*(vatt6/100));
            gross6.setText(String.format("%.2f", grosss6));
//            vat6.setText(String.format("%.2f", vatt6));
            total6.setText(String.format("%.2f", totall6));
        }
        float grosss7 = 0;
        float totall7 = 0;
        float vatt7 = 0;
        if (qty7.getText().length() > 0 && price7.getText().length() > 0) {
            grosss7 = Float.parseFloat(qty7.getText().toString()) * Float.parseFloat(price7.getText().toString());
            vatt7 =  Float.parseFloat((vat7.getText().toString().equalsIgnoreCase("")?"0":vat7.getText().toString()));
            totall7 = grosss7 +  (grosss7*(vatt7/100));
            gross7.setText(String.format("%.2f", grosss7));
//            vat7.setText(String.format("%.2f", vatt7));
            total7.setText(String.format("%.2f", totall7));
        }

        float grandTotal = (totall1 + totall2 + totall3 + totall4 + totall5 + totall6 + totall7);
        totalValue.setText(String.format("%.2f", grandTotal));
        float advn = 0f;
        if (advance.getText().length() > 0) {
            advn = Float.parseFloat(advance.getText().toString());

        } else {
            advance.setText(0 + "");

        }
        balance.setText(String.format("%.2f", (grandTotal - advn)));
    }

    private void initUI() {


        qty1 = findViewById(R.id.qty1);
        qty2 = findViewById(R.id.qty2);
        qty3 = findViewById(R.id.qty3);
        qty4 = findViewById(R.id.qty4);
        qty5 = findViewById(R.id.qty5);
        qty6 = findViewById(R.id.qty6);
        qty7 = findViewById(R.id.qty7);

        price1 = findViewById(R.id.price1);
        price2 = findViewById(R.id.price2);
        price3 = findViewById(R.id.price3);
        price4 = findViewById(R.id.price4);
        price5 = findViewById(R.id.price5);
        price6 = findViewById(R.id.price6);
        price7 = findViewById(R.id.price7);


        gross1 = findViewById(R.id.gross1);
        gross2 = findViewById(R.id.gross2);
        gross3 = findViewById(R.id.gross3);
        gross4 = findViewById(R.id.gross4);
        gross5 = findViewById(R.id.gross5);
        gross6 = findViewById(R.id.gross6);
        gross7 = findViewById(R.id.gross7);


        vat1 = findViewById(R.id.vat1);
        vat2 = findViewById(R.id.vat2);
        vat3 = findViewById(R.id.vat3);
        vat4 = findViewById(R.id.vat4);
        vat5 = findViewById(R.id.vat5);
        vat6 = findViewById(R.id.vat6);
        vat7 = findViewById(R.id.vat7);

        total1 = findViewById(R.id.total1);
        total2 = findViewById(R.id.total2);
        total3 = findViewById(R.id.total3);
        total4 = findViewById(R.id.total4);
        total5 = findViewById(R.id.total5);
        total6 = findViewById(R.id.total6);
        total7 = findViewById(R.id.total7);


        wholeView = findViewById(R.id.wholeView);
        clientName = findViewById(R.id.clientName);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        phone = findViewById(R.id.phone);
        orderId = findViewById(R.id.orderId);
        invoiceDate = findViewById(R.id.invoiceDate);
        date = findViewById(R.id.date);
        ticketNumber = findViewById(R.id.ticketNumber);
        calculate = findViewById(R.id.calculate);
        send = findViewById(R.id.send);
        adasdsadas = findViewById(R.id.adasdsadas);
        totalValue = findViewById(R.id.totalValue);
        balance = findViewById(R.id.balance);
        advance = findViewById(R.id.advance);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Send Quotation?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                viewToBitmap(wholeView, wholeView.getWidth(), wholeView.getHeight());

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public Bitmap viewToBitmap(View view, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
//        bitmap = view.getDrawingCache();

        Uri ui = getImageUri(bitmap);
        uploadPicture(CommonUtils.getRealPathFromURI(ui));
        return bitmap;
    }


    private void uploadPicture(String path) {
        adasdsadas.setVisibility(View.VISIBLE);
        StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReference();

        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));


        Uri file = Uri.fromFile(new File(path));


        StorageReference riversRef = mStorageRef.child("Photos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        //createNewPost(imageUrl);
                                        String key = mDatabase.push().getKey();
                                        mDatabase.child("OrderLogs").child(BookingSumary.orderModel.getUser().getUsername()).child("" + BookingSumary.orderModel.getOrderId()).child(key).setValue(new LogsModel(
                                                key, "Quotation Created", System.currentTimeMillis()
                                        ));
                                        sendNotification();

                                        mDatabase.child("Quotations").child("" + BookingSumary.orderModel.getOrderId())
                                                .setValue(imageUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                CommonUtils.showToast("Saved");
                                                adasdsadas.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                });
                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        CommonUtils.showToast("" + exception.getMessage());
                    }
                });

    }

    private void sendNotification() {
        NotificationAsync notificationAsync = new NotificationAsync(this);
        String notification_title = "Please accept or reject the quotation";
        String notification_message = "Click to view";
        notificationAsync.execute("ali", BookingSumary.orderModel.getUser().getFcmKey(), notification_title, notification_message, "quotation", "" + BookingSumary.orderModel.getOrderId());


    }

    private void getPermissions() {


        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE

        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                } else {

                }
            }
        }
        return true;
    }

    private void setupTopSection() {
        date.setText("Date: " + CommonUtils.getDateOnly(System.currentTimeMillis()));
        ticketNumber.setText("Ticket no: " + CommonUtils.getFullDat(System.currentTimeMillis()));
        clientName.setText(BookingSumary.orderModel.getUser().getFullName());
        email.setText(BookingSumary.orderModel.getUser().getEmail());
        city.setText(GetAddress.getCity(this, BookingSumary.orderModel.getLat(), BookingSumary.orderModel.getLon()));
        address.setText(GetAddress.getAddress(this, BookingSumary.orderModel.getLat(), BookingSumary.orderModel.getLon()));
        orderId.setText("" + BookingSumary.orderModel.getOrderId());
        phone.setText("" + BookingSumary.orderModel.getUser().getMobile());
        invoiceDate.setText(CommonUtils.getDateOnly(BookingSumary.orderModel.getUser().getTime()));
    }

    @Override
    public void onSuccess(String chatId) {

    }

    @Override
    public void onFailure() {

    }
}
