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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BookingSumary extends AppCompatActivity {

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

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        orderId = getIntent().getStringExtra("orderId");
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        adapter = new ServicesBookedAdapter(this, new ArrayList<ServiceCountModel>());
//        recyclerview.setAdapter(adapter);

        back = findViewById(R.id.back);
//        next = findViewById(R.id.next);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        buildingType = findViewById(R.id.buildingType);
        serviceType = findViewById(R.id.serviceType);
        start = findViewById(R.id.start);
        getOrderFromDB();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(BookingSumary.this,Assignemnt.class);
                i.putExtra("orderId",orderId);
                startActivity(i);
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

    private void getOrderFromDB() {
        mDatabase.child("Orders").child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    orderModel = dataSnapshot.getValue(OrderModel.class);
                    if (orderModel != null) {
                        serviceType.setText(orderModel.getServiceName());
                        date.setText(orderModel.getDate().replace("\n"," "));
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
}
