package www.sydlinaonline.com.userpharmacy.RecycleAdapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import www.sydlinaonline.com.userpharmacy.Model.Medicine;
import www.sydlinaonline.com.userpharmacy.R;

public class MedicineCategoryAdapter extends RecyclerView.Adapter<MedicineCategoryAdapter.viewHolder> {

    private Context context;
    private ArrayList<Medicine> medList;

    public MedicineCategoryAdapter(Context context, ArrayList<Medicine> medList) {
        this.context = context;
        this.medList = medList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_medicine_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
            holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return medList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView medName;
        TextView medDes;
        TextView medPrice;
        ImageView medImage;

        public viewHolder(View itemView) {
            super(itemView);
            medName = (TextView) itemView.findViewById(R.id.tv_medicine_name);
            medDes = (TextView) itemView.findViewById(R.id.tv_medicine_description);
            medPrice = (TextView) itemView.findViewById(R.id.tv_medicine_price);
            medImage = (ImageView)itemView.findViewById(R.id.imv_medicine_image);
        }

        private void bind(int pos){
            medName.setText(medList.get(pos).getName());
            medDes.setText(medList.get(pos).getDescription());
            medPrice.setText(medList.get(pos).getPrice());
            //Picasso.with(getApplicationContext()).load(model.getImageUrl()).into(viewHolder.mMedicineImageView);
            Picasso.with(context).load(medList.get(pos).getImageUrl()).into(medImage);
        }
    }
}
