package www.sydlinaonline.com.userpharmacy.RecycleAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import www.sydlinaonline.com.userpharmacy.R;

public class PopupRecyclerViewAdapter extends RecyclerView.Adapter<PopupRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<String>data;

    public PopupRecyclerViewAdapter(Context mContext, ArrayList<String> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_pop_layout, parent,false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.medicineName.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView medicineName;

        public MyViewHolder(View itemView) {
            super(itemView);
            medicineName = (TextView)itemView.findViewById(R.id.tv_pop_item);

        }
    }
}
