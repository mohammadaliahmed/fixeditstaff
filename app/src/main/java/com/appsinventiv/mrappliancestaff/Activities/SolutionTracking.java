package com.appsinventiv.mrappliancestaff.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.mrappliancestaff.Adapters.SolutionListAdapter;
import com.appsinventiv.mrappliancestaff.Models.OrderModel;
import com.appsinventiv.mrappliancestaff.Models.SolutionTrackingModel;
import com.appsinventiv.mrappliancestaff.R;
import com.appsinventiv.mrappliancestaff.Utils.CommonUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SolutionTracking extends AppCompatActivity {
    String orderId;
    EditText issue, solution, whatDid;
    Button save, saveSolution, saveIssue;
    DatabaseReference mDatabase;
    ArrayList<SolutionTrackingModel> itemList = new ArrayList<>();
    SolutionListAdapter adapter;
    RecyclerView recyclerview;
    TextView serviceName;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_tracking);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        serviceName = findViewById(R.id.serviceName);

        orderId = getIntent().getStringExtra("orderId");
        serviceName.setText("Solution Tracking for: " + orderId);
        whatDid = findViewById(R.id.whatDid);

        saveSolution = findViewById(R.id.saveSolution);

        saveIssue = findViewById(R.id.saveIssue);
        back = findViewById(R.id.back);
        issue = findViewById(R.id.issue);
        recyclerview = findViewById(R.id.recyclerview);
        solution = findViewById(R.id.solution);
        save = findViewById(R.id.save);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        adapter = new SolutionListAdapter(this, itemList);

        recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerview.setAdapter(adapter);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getCommentsFromDB();

        saveIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("SolutionTracking").child(orderId).child("issue").setValue(issue.getText().toString());
                CommonUtils.showToast("Saved");


            }
        });


        saveSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("SolutionTracking").child(orderId).child("solution").setValue(solution.getText().toString());
                CommonUtils.showToast("Saved");

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = mDatabase.push().getKey();
                mDatabase.child("SolutionTracking").child(orderId).child("comments").child(key).setValue(new SolutionTrackingModel(
                        key, whatDid.getText().toString(), System.currentTimeMillis()
                )).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        whatDid.setText("");
                    }
                });
            }
        });
    }

    private void getCommentsFromDB() {
        mDatabase.child("SolutionTracking").child(orderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    String issu = dataSnapshot.child("issue").getValue(String.class);
                    String soluti = dataSnapshot.child("solution").getValue(String.class);

                    solution.setText(soluti);
                    issue.setText(issu);


                    for (DataSnapshot snapshot : dataSnapshot.child("comments").getChildren()) {
                        SolutionTrackingModel model = snapshot.getValue(SolutionTrackingModel.class);
                        if (model != null) {
                            itemList.add(model);
                        }
                    }
                    Collections.sort(itemList, new Comparator<SolutionTrackingModel>() {
                        @Override
                        public int compare(SolutionTrackingModel listData, SolutionTrackingModel t1) {
                            Long ob1 = listData.getTime();
                            Long ob2 = t1.getTime();

                            return ob1.compareTo(ob2);

                        }
                    });
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
