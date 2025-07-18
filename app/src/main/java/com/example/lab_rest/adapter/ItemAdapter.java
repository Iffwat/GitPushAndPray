// File: ItemAdapter.java
package com.example.lab_rest.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lab_rest.R;
import com.example.lab_rest.model.RecyclableItem;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private  Context context;
    private  List<RecyclableItem> itemList;


    private int currentPos;



    public ItemAdapter(Context context, List<RecyclableItem> itemList) {
        this.context = context;
        this.itemList = itemList;

    }
    private Context getContext(){ return context;}

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        TextView tvItemName, tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            // register context menu listener on itemView
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            currentPos = getAdapterPosition();
            return false;
        }
        @Override
        public boolean onLongClickUseDefaultHapticFeedback(@NonNull View v) {
            return View.OnLongClickListener.super.onLongClickUseDefaultHapticFeedback(v);
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate layout using the single item layout
        View view = inflater.inflate(R.layout.item_recyclable, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // bind data to the view holder instance
        RecyclableItem m = itemList.get(position);
        holder.tvItemName.setText(m.getItemName());
        holder.tvPrice.setText("Price: RM " + m.getPricePerKg() + " /kg");

    }
    public RecyclableItem getSelectedItem() {
        // return the book record if the current selected position/index is valid
        if(currentPos>=0 && itemList !=null && currentPos<itemList.size()) {
            return itemList.get(currentPos);
        }
        return null;
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
