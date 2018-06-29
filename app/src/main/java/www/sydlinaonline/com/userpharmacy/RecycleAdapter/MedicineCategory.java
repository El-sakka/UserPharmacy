package www.sydlinaonline.com.userpharmacy.RecycleAdapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import www.sydlinaonline.com.userpharmacy.Model.Medicine;
import www.sydlinaonline.com.userpharmacy.R;

public class MedicineCategory extends AppCompatActivity {

    private static final String MEDICINE_LIST = "medicine_list";

    //private ArrayList<String> medicineList = new ArrayList<>();
    private ArrayList<Medicine> medicineObjects= new ArrayList<>();
    private static final String TAG = "MedicineCategory";
    private String key;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_category);
        init();


    }

    private void init() {
        Intent intent = getIntent();
        key = intent.getStringExtra(MEDICINE_LIST);
        Log.d(TAG, "init: key:"+key);

        mRecyclerView = (RecyclerView)findViewById(R.id.rv_med_cat);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        getMedicineList(key);

    }

    private void getMedicineList(String key){
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("Category").child(key);

        final ArrayList<String>list = new ArrayList<>();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                    list.add(snapshot.getValue(String.class));
                getMedicines(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getMedicines(final ArrayList<String> list) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("Medicine");
        for (int i = 0; i < list.size(); i++) {
            final int count = i;
            Query query = mRef.orderByChild("name").startAt(list.get(i)).endAt(list.get(i) + "\uf8ff");
            Log.d(TAG, "getMedicines: queery: " + query.toString());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                        Medicine model = snapshot.getValue(Medicine.class);
                        medicineObjects.add(model);
                        Log.d(TAG, "onDataChange: koko ya7 "+model.getName());
                        //Log.d(TAG, "onDataChange: listSizesssssssss: "+medicineObjects.size());
                        if(count==1){
                            Log.d(TAG, "onDataChange: listSizesssssssss: "+medicineObjects.size());
                            setRecycleView(medicineObjects);
                        }
                    }
                    //Log.d(TAG, "onDataChange: listSizesssssssss: "+medicineObjects.size());
                    //setRecycleView(medicineObjects);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void setRecycleView(ArrayList<Medicine> obj){
        Log.d(TAG, "setRecycleView: SSSSSSSS: "+obj.size());
        MedicineCategoryAdapter adapter = new MedicineCategoryAdapter(this,obj);
        mRecyclerView.setAdapter(adapter);
    }
}
