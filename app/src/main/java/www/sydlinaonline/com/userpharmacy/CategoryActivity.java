package www.sydlinaonline.com.userpharmacy;

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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import www.sydlinaonline.com.userpharmacy.Model.Category;
import www.sydlinaonline.com.userpharmacy.Model.Medicine;
import www.sydlinaonline.com.userpharmacy.RecycleAdapter.myAdapter;

public class CategoryActivity extends AppCompatActivity {

    private static final String TAG = "CategoryActivity";
    RecyclerView mCategoryRecycleView;
    DatabaseReference mCategoryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        inti();
        getFirebaseCategory();

    }


    private void inti(){
        mCategoryRecycleView = (RecyclerView)findViewById(R.id.rv_category_list);
        mCategoryRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mCategoryRef = FirebaseDatabase.getInstance().getReference().child("Database")
                .child("Category");
    }

    private void getFirebaseCategory(){


        mCategoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String>val = new ArrayList<>();
                //mCategoryRecycleView.setAdapter(new myAdapter(val));
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    val.add(snapshot.getKey());

                mCategoryRecycleView.setAdapter(new myAdapter(getApplicationContext(),val));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


       // mCategoryRecycleView.setAdapter(adapter);
    }

    public static class categoryViewHolder extends RecyclerView.ViewHolder{

        public TextView mCategoryNameTextView;

        public categoryViewHolder(View itemView) {
            super(itemView);
            mCategoryNameTextView = (TextView)itemView.findViewById(R.id.tv_category_name);
        }

    }

}
