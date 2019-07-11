package com.fixedit.fixeditstaff.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.fixedit.fixeditstaff.Adapters.ServicesBookedAdapter;
import com.fixedit.fixeditstaff.Models.OrderModel;
import com.fixedit.fixeditstaff.Models.ServiceCountModel;
import com.fixedit.fixeditstaff.R;
import com.fixedit.fixeditstaff.Utils.CommonUtils;
import com.fixedit.fixeditstaff.Utils.NotificationAsync;
import com.fixedit.fixeditstaff.Utils.NotificationObserver;
import com.fixedit.fixeditstaff.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BookingSumary extends AppCompatActivity implements NotificationObserver {

    RecyclerView recyclerview;

    ServicesBookedAdapter adapter;
    TextView date, time, buildingType;
    RelativeLayout next;
    TextView serviceType;
    ImageView back;
    String orderId;
    DatabaseReference mDatabase;
    private OrderModel orderModel;
    Button start;
    Button invoiceOk, invoiceModify;
    private String adminFcmKey;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        orderId = getIntent().getStringExtra("orderId");
        invoiceOk = findViewById(R.id.invoiceOk);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        adapter = new ServicesBookedAdapter(this, new ArrayList<ServiceCountModel>());
//        recyclerview.setAdapter(adapter);

        back = findViewById(R.id.back);
//        next = findViewById(R.id.next);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        invoiceModify = findViewById(R.id.invoiceModify);
        buildingType = findViewById(R.id.buildingType);
        serviceType = findViewById(R.id.serviceType);
        start = findViewById(R.id.start);
        getOrderFromDB();
        getAdminFCMkey();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BookingSumary.this, Assignemnt.class);
                i.putExtra("orderId", orderId);
                startActivity(i);
            }
        });

        invoiceOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BookingSumary.this, FinishJob.class);
                i.putExtra("orderId", orderId);
                startActivity(i);
            }
        });

        invoiceModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationAsync notificationAsync = new NotificationAsync(BookingSumary.this);
                String notification_title = "Order Change request";
                String notification_message = "Click to view";
                notificationAsync.execute("ali",adminFcmKey , notification_title, notification_message, "Modify", "" + orderId);
                CommonUtils.showToast("Request sent to admin for order change");

            }
        });

//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(BookingSumary.this, ChooseAddress.class));
//            }
//        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void getAdminFCMkey() {
        mDatabase.child("Admin").child("fcmKey").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    adminFcmKey = dataSnapshot.getValue(String.class);
//                    SharedPrefs.setAdminFcmKey(adminFcmKey);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getOrderFromDB() {
        mDatabase.child("Orders").child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    orderModel = dataSnapshot.getValue(OrderModel.class);
                    if (orderModel != null) {
                        serviceType.setText(orderModel.getServiceName());
                        date.setText(orderModel.getDate().replace("\n", " "));
                        time.setText(orderModel.getChosenTime());
                        buildingType.setText(orderModel.getBuildingType());
                        adapter = new ServicesBookedAdapter(BookingSumary.this, orderModel.getCountModelArrayList());
                        recyclerview.setAdapter(adapter);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onSuccess(String chatId) {

    }

    @Override
    public void onFailure() {

    }
}
