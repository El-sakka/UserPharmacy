package www.sydlinaonline.com.userpharmacy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import www.sydlinaonline.com.userpharmacy.Model.Medicine;

public class PharmacyActivity extends AppCompatActivity {

    private static final String TAG = "PharmacyActivity";

    //views
    private EditText mSearchEditText;
    private ImageView mSearchImageView;

    private RecyclerView mRecyclerView;

    //layout
    DrawerLayout drawerLayout ;

    //toolbar
    private android.support.v7.widget.Toolbar toolbar;

    FirebaseAuth mFirebaseAuth;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupFirebase();

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layour);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, drawerLayout, toolbar);


        // init Firebase database

        // init views
        mSearchEditText = (EditText) findViewById(R.id.edt_search);
        mSearchImageView = (ImageView) findViewById(R.id.img_ic_search);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_searchList);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mSearchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //getData("perfulma","villagel","congstal");

        String[] strArr = {"perfulma","villagel","congstal"};

        Arrays.sort(strArr);
        for(int i=0;i<strArr.length ;i++){
            Log.d(TAG, "onCreate: "+strArr[i]);
        }
    }





    private void searchButtonPressed(){
        String fullText = mSearchEditText.getText().toString();

    }


    private void getData(String item1,String item2,String item3){

        Log.d(TAG, "getData: ");
        mRef = FirebaseDatabase.getInstance().getReference().child("Database")
                .child("Medicine");
        mRef.keepSynced(true);

        //mRef.orderByChild("name").startAt(item2).endAt(item2+"\uf8ff");
        Query query = mRef.orderByChild("name").startAt(item1).endAt(item2+"\uf8ff",item3+"\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Medicine model = snapshot.getValue(Medicine.class);
                    Log.d(TAG, "onDataChange: medicince Name: "+model.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void setupFirebase(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        if(mFirebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(PharmacyActivity.this,MainActivity.class));
        }
    }



}
