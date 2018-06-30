package www.sydlinaonline.com.userpharmacy.RecycleAdapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import www.sydlinaonline.com.userpharmacy.MedicineActivity;
import www.sydlinaonline.com.userpharmacy.Model.Medicine;
import www.sydlinaonline.com.userpharmacy.R;



public class MedicineCategoryAdapter extends RecyclerView.Adapter<MedicineCategoryAdapter.viewHolder> {

    private Context context;
    private ArrayList<Medicine> medList;

    private final static String NAME_KEY="NAME";
    private final static String PRICE_KEY="PRICE";
    private final static String IMAGE_KEY="IMAGE";
    private final static String DES_KEY="DES";
    private static final String BUNDLE_KEY = "bundle";



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
        private View mView;
        String imageUrl;

        public viewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            medName = (TextView) itemView.findViewById(R.id.tv_medicine_name);
            medDes = (TextView) itemView.findViewById(R.id.tv_medicine_description);
            medPrice = (TextView) itemView.findViewById(R.id.tv_medicine_price);
            medImage = (ImageView)itemView.findViewById(R.id.imv_medicine_image);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MedicineActivity.class);
                   // Bundle bundle = new Bundle();
                    intent.putExtra("Class","B");
                    intent.putExtra(NAME_KEY,medName.getText().toString());
                    intent.putExtra(PRICE_KEY,medPrice.getText().toString());
                    intent.putExtra(DES_KEY,medDes.getText().toString());
                    intent.putExtra(IMAGE_KEY,imageUrl);
                    //intent.putExtra(BUNDLE_KEY,bundle);
                    context.startActivity(intent);
                }
            });
        }

        private void bind(int pos){
            medName.setText(medList.get(pos).getName());
            medDes.setText(medList.get(pos).getDescription());
            medPrice.setText(medList.get(pos).getPrice());
            //Picasso.with(getApplicationContext()).load(model.getImageUrl()).into(viewHolder.mMedicineImageView);
            imageUrl = medList.get(pos).getImageUrl();
            if(!imageUrl.equals(""))
                Picasso.with(context).load(imageUrl).into(medImage);
        }
    }


}
