package com.devcrawlers.wesselni;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private EditText editText;
    private Button buttonDone;
    private LatLng latLngchosen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        editText= findViewById(R.id.edit_text);
        buttonDone=findViewById(R.id.buttonDone);
        buttonDone.setOnClickListener(v -> {
            AddOffreFragment.editTextPosition.setText(latLngchosen.latitude+","+latLngchosen.longitude);
            finish();
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Places.initialize(getApplicationContext(),"AIzaSyBskA1YBZfvl9PQXmtFzRHMozuG9jCmZM0");
        //set Edit Text non focusable
        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialise place field list
                List<Place.Field> fieldList= Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,
                        Place.Field.NAME);
                //create intent
                Intent intent=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                        fieldList).build(MapsActivity.this);
                //start activity result
                startActivityForResult(intent,100);
            }
        });
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
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //create marker
                MarkerOptions markerOptions = new MarkerOptions();
                //set marker position
                markerOptions.position(latLng);

                //clear the previous click position
                mMap.clear();
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 1));
                //add marker on map
                mMap.addMarker(markerOptions);
                latLngchosen=latLng;
               // Toast.makeText(getApplicationContext(), String.valueOf(latLng.latitude+" "+latLng.longitude),
                 //       Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            //when success initialize place
            Place place = Autocomplete.getPlaceFromIntent(data);
            //setAddress on EditText
            editText.setText(place.getAddress());
            //setLocality name
            mMap.clear();
            LatLng sydney = place.getLatLng();
            mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    }

}
