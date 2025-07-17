package com.example.lab_rest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.AdminUpdateActivity;
import com.example.lab_rest.R;
import com.example.lab_rest.model.AdminRequestModel;

import java.util.List;

public class AdminRequestAdapter extends RecyclerView.Adapter<AdminRequestAdapter.ViewHolder> {

    private List<AdminRequestModel> requestList;
    private Context context;

    public AdminRequestAdapter(Context context, List<AdminRequestModel> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvUsername, tvAddress, tvStatus, tvUID;
        Button btnUpdate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUID = itemView.findViewById(R.id.tvUID);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }
    }

    @Override
    public AdminRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdminRequestAdapter.ViewHolder holder, int position) {
        AdminRequestModel request = requestList.get(position);

        holder.tvUID.setText("UID: " + request.getUserId());
        holder.tvItemName.setText("Item: " + request.getItemName());
        holder.tvUsername.setText("User: " + request.getUsername());
        holder.tvAddress.setText("Address: " + request.getAddress());
        holder.tvStatus.setText("Status: " + request.getStatus());

        holder.btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminUpdateActivity.class);
            intent.putExtra("requestId", request.getRequestId());
            intent.putExtra("itemName", request.getItemName());
            intent.putExtra("notes", request.getNotes());
            intent.putExtra("pricePerKg", request.getPricePerKg());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }
}
