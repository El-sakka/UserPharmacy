package www.sydlinaonline.com.userpharmacy.RecycleAdapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import www.sydlinaonline.com.userpharmacy.R;

public class myAdapter extends RecyclerView.Adapter<myAdapter.viewHolder> {


    private static final String TAG = "myAdapter";
    private static final String MEDICINE_LIST = "medicine_list";
    private ArrayList<String> cat;
    private ArrayList<String> medicineList= new ArrayList<>();
    private ArrayList<String> medicineList2= new ArrayList<>();
    private Context mContext;

    private int pos;

    public myAdapter(Context context,ArrayList<String> cat) {
        this.mContext = context;
        this.cat = cat;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       // return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false));
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_category_layout,parent,false));
        }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
            pos = position;
            holder.categoryName.setText(cat.get(position));
            holder.bind(position);
           // getKeys(holder,position);
    }

    @Override
    public int getItemCount() {
        return cat.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        private TextView categoryName;
        public viewHolder(View itemView) {
            super(itemView);

            categoryName = (TextView)itemView.findViewById(R.id.tv_category_name);

        }
        void bind(final int position){
            categoryName.setText(cat.get(position));
            categoryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"item #"+position,Toast.LENGTH_SHORT).show();
                   // getKeys(categoryName.getText().toString());
                    Log.d(TAG, "onClick: list2: "+medicineList2.size());
                    Intent intent = new Intent(mContext,MedicineCategory.class);
                    intent.putExtra(MEDICINE_LIST,categoryName.getText().toString());
                    Log.d(TAG, "onClick: LIIIIIIIII"+medicineList.size());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Log.d(TAG, "onClick: LIIIIIIIII2222"+medicineList.size());
                    mContext.startActivity(intent);
                }
            });
        }
    }

   /* public void getKeys(viewHolder holder,int position){
        holder.categoryName.setText(cat.get(position));
        String key = holder.categoryName.getText().toString();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("Category").child(key);
        mRef.keepSynced(false);
        Log.d(TAG, "getKeys: "+ key);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for(DataSnapshot snapshot: dataSnapshot.getChildren())
                   medicineList.add(snapshot.getValue(String.class));
                Log.d(TAG, "onDataChange: A7a: "+medicineList.size());
                medicineList2 = medicineList;

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d(TAG, "onDataChange: A7a2: "+medicineList.size());
    }*/

}
