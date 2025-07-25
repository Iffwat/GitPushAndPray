package com.example.lab_rest.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.lab_rest.R;
import com.example.lab_rest.model.RequestModel;

import java.util.List;
import java.util.Map;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private List<RequestModel> requestList;
    private Map<Integer, String> itemNameMap; // ✅ Mapping from item_id to item_name

    public RequestAdapter(List<RequestModel> requestList, Map<Integer, String> itemNameMap) {
        this.requestList = requestList;
        this.itemNameMap = itemNameMap;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvAddress, tvStatus, tvPrice;
        Button btnCancel;

        public ViewHolder(View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }

    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RequestAdapter.ViewHolder holder, int position) {
        RequestModel request = requestList.get(position);

        // ✅ Lookup the item name from map using item_id
        String itemName = itemNameMap != null ? itemNameMap.getOrDefault(request.getItemId(), "Unknown") : "Unknown";
        holder.tvItemName.setText("Item: " + itemName);

        holder.tvAddress.setText("Address: " + request.getAddress());
        holder.tvStatus.setText("Status: " + request.getStatus());

        if ("Completed".equalsIgnoreCase(request.getStatus())) {
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
            holder.tvPrice.setText("Total: RM " + request.getTotalPrice());
            holder.btnCancel.setVisibility(View.GONE);
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#FF9800"));
            holder.tvPrice.setText("Total: -");
            holder.btnCancel.setVisibility(View.VISIBLE);

            holder.btnCancel.setOnClickListener(v -> {
                if (cancelListener != null) {
                    cancelListener.onCancelClicked(request.getRequestId(), holder.getAdapterPosition());
                }
            });
        }
    }

    public interface CancelClickListener {
        void onCancelClicked(int requestId, int position);
    }

    private CancelClickListener cancelListener;

    public void setCancelClickListener(CancelClickListener listener) {
        this.cancelListener = listener;
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }
}
