package www.sydlinaonline.com.userpharmacy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import www.sydlinaonline.com.userpharmacy.Model.Medicine;
import www.sydlinaonline.com.userpharmacy.RecycleAdapter.MostPopularAdpater;

public class MostPopularActivity extends AppCompatActivity {

    private static final String TAG = "MostPopularActivity";


    private RecyclerView mRecyclerView;

    ArrayList<Medicine> medicines ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_popular);

        init();
        retriveData();
    }


    private void init(){
        mRecyclerView = (RecyclerView)findViewById(R.id.rv_most_popular);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicines = new ArrayList<>();
    }

    private void retriveData(){
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("Medicine");
        Query query = mRef.orderByChild("mostSales");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot :  dataSnapshot.getChildren()){
                    Medicine medicine = snapshot.getValue(Medicine.class);
                    medicines.add(medicine);
                }

                setUpAdapter(medicines);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setUpAdapter(ArrayList<Medicine> list){
        Log.d(TAG, "setUpAdapter: listSize"+list.size());
        Collections.sort(list, new Comparator<Medicine>() {
            @Override
            public int compare(Medicine o1, Medicine o2) {
                return o2.getMostSales().compareTo(o1.getMostSales());
            }
        });

        for(int i=0;i<list.size();i++){
            Log.d(TAG, "setUpAdapter: "+ list.get(i).getName());
        }

        MostPopularAdpater adpater = new MostPopularAdpater(MostPopularActivity.this,list);
        mRecyclerView.setAdapter(adpater);
    }





}
