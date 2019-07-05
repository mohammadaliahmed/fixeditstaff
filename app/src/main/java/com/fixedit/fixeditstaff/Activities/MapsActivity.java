package com.fixedit.fixeditstaff.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Path;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.fixedit.fixeditstaff.Models.OrderModel;
import com.fixedit.fixeditstaff.R;
import com.fixedit.fixeditstaff.Utils.CommonUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private double lng, lat;
    double origLat, origLon;
    FloatingActionButton takeMeToCurrent;
    Button arrived;
    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    LocationManager manager;
    String orderId;
    TextView orderIdText;
    double orderLat, orderLon;
    private LatLng origin, destination;
    OrderModel orderModel;

    ImageView dialPhone;
    TextView phoneNumber, customerName;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        orderIdText = findViewById(R.id.orderIdText);
        customerName = findViewById(R.id.customerName);
        phoneNumber = findViewById(R.id.phoneNumber);
        dialPhone = findViewById(R.id.dialPhone);
        takeMeToCurrent = findViewById(R.id.takeMeToCurrent);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        arrived = findViewById(R.id.arrived);
        orderId = getIntent().getStringExtra("orderId");
        orderLat = getIntent().getDoubleExtra("latitude", 0);
        orderLon = getIntent().getDoubleExtra("longitude", 0);
        orderIdText.setText("Order Id: " + orderId);
        getOrderFromDB();

        dialPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + orderModel.getUser().getMobile()));
                startActivity(i);
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            CommonUtils.showToast("Please turn on GPS");
            final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        } else {
            getPermissions();
        }

        takeMeToCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng newPosition = new LatLng(origLat, origLon);
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 16));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 16));

//                address.setText(lat == 0 ? "" : CommonUtils.getFullAddress(MapsActivity.this, origLat, origLon));
            }
        });

        arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent i=new Intent(MapsActivity.this,BookingSumary.class);
              i.putExtra("orderId",orderId);
              startActivity(i);
            }
        });

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Float a = 00f;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0.0f, new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    location.getLatitude();
                    location.getLongitude();
                    CommonUtils.showToast("" + location.getLatitude() + location.getLongitude());
                } else {
//                    CommonUtils.showToast("Null");
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
        createLocationRequest();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void getOrderFromDB() {
        mDatabase.child("Orders").child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    orderModel = dataSnapshot.getValue(OrderModel.class);
                    if (orderModel != null) {
                        customerName.setText("Customer name: " + orderModel.getUser().getFullName());
                        phoneNumber.setText("Phone number: " + orderModel.getUser().getMobile());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10 * 1000);
        mLocationRequest.setFastestInterval(1000);
    }


    @Override
    protected void onStart() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
//        progressBar.setVisibility(View.GONE);
        mGoogleApiClient.disconnect();
        super.onStop();

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
//                Intent intent = new Intent();
//                intent.putExtra("Longitude", mLastLocation.getLongitude());
//                intent.putExtra("Latitude", mLastLocation.getLatitude());
//                setResult(RESULT_OK,intent);
//
//                finish();

                lat = mLastLocation.getLatitude();
                lng = mLastLocation.getLongitude();
                origLat = lat;
                origLon = lng;


//                LatLng sydney = new LatLng(orderLat, orderLon);
//                mMap.addMarker(new MarkerOptions().position(sydney).title(orderId));
//
//                LatLng newPosition = new LatLng(lat, lng);
//                mMap.addMarker(new MarkerOptions().position(newPosition).title(orderId));


//                GoogleDirection.withServerKey("AIzaSyATb8hLtW0K9ViogKBgTTr9MeCHsCZOZlg")
//                        .from(new LatLng(lat, lng))
//                        .to(new LatLng(orderLat, orderLon))
//                        .avoid(AvoidType.FERRIES)
//                        .avoid(AvoidType.HIGHWAYS)
//                        .execute(new DirectionCallback() {
//
//                            @Override
//                            public void onDirectionSuccess(Direction direction, String rawBody) {
//                                if (direction.isOK()) {
//                                    // Do something
//                                    CommonUtils.showToast("Wow");
//                                } else {
//                                    // Do something
//                                    CommonUtils.showToast("Error");
//                                }
//                            }
//
//                            @Override
//                            public void onDirectionFailure(Throwable t) {
//                                // Do something
//                                CommonUtils.showToast(t.getMessage());
//                            }
//                        });
                origin = new LatLng(lat, lng);
                destination = new LatLng(orderLat, orderLon);
                GoogleDirection.withServerKey("AIzaSyD0ruQXUCNNB5y-bxLyyzy6Qcv9zLc-D_8")


                        .from(origin)
                        .to(destination)
                        .transportMode(TransportMode.DRIVING)
                        .execute(new DirectionCallback() {
                            @Override
                            public void onDirectionSuccess(Direction direction, String rawBody) {
                                Route route = direction.getRouteList().get(0);

                                mMap.addMarker(new MarkerOptions().position(origin));
                                mMap.addMarker(new MarkerOptions().position(destination));

                                ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                                mMap.addPolyline(DirectionConverter.createPolyline(MapsActivity.this, directionPositionList, 5, Color.BLUE));
//                                setCameraWithCoordinationBounds(route);

                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 14));
//                                btnRequestDirection.setVisibility(View.GONE);
                            }

                            @Override
                            public void onDirectionFailure(Throwable t) {

                            }
                        });

//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 14));


            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?"));
                startActivity(intent);
//                CommonUtils.showToast("Nullllll");
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
            }
        } catch (SecurityException e) {

        }

    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 2 ) {
//            getPlaceFromPicker(data);
//            LatLng newPosition = new LatLng(lat, lng);
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 16));
//
//            address.setText(lat == 0 ? "" : CommonUtils.getFullAddress(MapsActivity.this, lat, lng));
//        }
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            if (data != null) {
//                Bundle extras = data.getExtras();
//                lng = extras.getDouble("Longitude");
//                lat = extras.getDouble("Latitude");
//                origLat = lat;
//                origLon = lng;
//                LatLng newPosition = new LatLng(lat, lng);
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 16));
//
////                address.setText(lat == 0 ? "" : CommonUtils.getFullAddress(MapsActivity.this, lat, lng));
//
//
//            }
//
//        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(orderLat, orderLon);
        mMap.addMarker(new MarkerOptions().position(sydney).title(orderId));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void getPermissions() {


        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION

        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
//            Intent intent = new Intent(MapsActivity.this, GPSTrackerActivity.class);
//            startActivityForResult(intent, 1);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (permissions[0].equalsIgnoreCase("android.permission.ACCESS_FINE_LOCATION") && grantResults[0] == 0) {
//                Intent intent = new Intent(MapsActivity.this, GPSTrackerActivity.class);
//                startActivityForResult(intent, 1);

            }
        } catch (ArrayIndexOutOfBoundsException e) {
            getPermissions();
        }

    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
