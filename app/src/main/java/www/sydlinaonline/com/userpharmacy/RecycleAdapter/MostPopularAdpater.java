package www.sydlinaonline.com.userpharmacy.RecycleAdapter;

import android.content.Context;
import android.content.Intent;
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
import www.sydlinaonline.com.userpharmacy.MostPopularActivity;
import www.sydlinaonline.com.userpharmacy.R;

public class MostPopularAdpater extends RecyclerView.Adapter<MostPopularAdpater.viewHolder> {

    private Context context;
    private ArrayList<Medicine> medicineArrayList;

    private static final String IMAGE3_KEY = "image3_key";
    private static final String DES3_KEY = "des3_key";
    private static final String PRICE3_KEY = "price3_key";
    private static final String NAME3_KEY = "name3_key";

    public MostPopularAdpater(Context context, ArrayList<Medicine> medicineArrayList) {
        this.context = context;
        this.medicineArrayList = medicineArrayList;
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
        return medicineArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        public View view;
        public TextView mMedicineNameTextView;
        public TextView mMedicinePriceTextView;
        public TextView mMedicineDescriptionTextView;
        public ImageView mMedicineImageView;
        String imageUrl;

        public viewHolder(View itemView) {
            super(itemView);
            view = itemView;
            mMedicineNameTextView = (TextView)itemView.findViewById(R.id.tv_medicine_name);
            mMedicinePriceTextView = (TextView)itemView.findViewById(R.id.tv_medicine_price);
            mMedicineDescriptionTextView = (TextView)itemView.findViewById(R.id.tv_medicine_description);
            mMedicineImageView = (ImageView) itemView.findViewById(R.id.imv_medicine_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(context,MedicineActivity.class);
                    intent.putExtra("Class","C");
                    intent.putExtra(NAME3_KEY,mMedicineNameTextView.getText().toString());
                    intent.putExtra(PRICE3_KEY,mMedicinePriceTextView.getText().toString());
                    intent.putExtra(DES3_KEY,mMedicineDescriptionTextView.getText().toString());
                    intent.putExtra(IMAGE3_KEY,imageUrl);
                    //intent.putExtra(CLICKED_KEY,bundle);
                    context.startActivity(intent);
                }
            });
        }
        private void bind(int pos){
            mMedicineNameTextView.setText(medicineArrayList.get(pos).getName());
            mMedicineDescriptionTextView.setText(medicineArrayList.get(pos).getDescription());
            mMedicinePriceTextView.setText(medicineArrayList.get(pos).getPrice());
            //Picasso.with(getApplicationContext()).load(model.getImageUrl()).into(viewHolder.mMedicineImageView);
            imageUrl = medicineArrayList.get(pos).getImageUrl();
            if(!imageUrl.equals(""))
                Picasso.with(context).load(imageUrl).into(mMedicineImageView);
        }
    }
}
