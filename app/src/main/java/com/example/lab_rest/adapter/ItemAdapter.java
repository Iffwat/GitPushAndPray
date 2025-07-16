package com.example.lab_rest.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.R;
import com.example.lab_rest.model.RecyclableItem;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final Context context;
    private final List<RecyclableItem> itemList;
    private final ItemActionListener listener;

    public interface ItemActionListener {
        void onEditClicked(RecyclableItem item);
        void onDeleteClicked(int itemId);
    }

    public ItemAdapter(Context context, List<RecyclableItem> itemList, ItemActionListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvPrice;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclable, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        RecyclableItem item = itemList.get(position);

        holder.tvItemName.setText(item.getItemName());
        holder.tvPrice.setText(String.format("Price: RM %.2f /kg", item.getPricePerKg()));

        holder.btnEdit.setOnClickListener(v -> listener.onEditClicked(item));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClicked(item.getItemId()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
