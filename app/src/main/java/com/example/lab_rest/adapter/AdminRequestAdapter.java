package com.example.lab_rest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.AdminUpdateActivity;
import com.example.lab_rest.R;
import com.example.lab_rest.model.RecyclableItem;
import com.example.lab_rest.model.RequestModel;
import com.example.lab_rest.model.User;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRequestAdapter extends RecyclerView.Adapter<AdminRequestAdapter.ViewHolder> {

    private final Context context;
    private final List<RequestModel> requestList;
    private final Map<Integer, String> itemNameMap;
    private final Map<Integer, String> usernameMap;

    public AdminRequestAdapter(Context context, List<RequestModel> requestList,
                               Map<Integer, String> itemNameMap, Map<Integer, String> usernameMap) {
        this.context = context;
        this.requestList = requestList;
        this.itemNameMap = itemNameMap;
        this.usernameMap = usernameMap;
    }

    @NonNull
    @Override
    public AdminRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminRequestAdapter.ViewHolder holder, int position) {
        RequestModel request = requestList.get(position);

        String itemName = itemNameMap.get(request.getItemId());
        String username = usernameMap.get(request.getUserId());

        holder.tvUID.setText("UID: " + request.getUserId());
        holder.tvItemName.setText("Item: " + (itemName != null ? itemName : "null"));
        holder.tvUsername.setText("User: " + (username != null ? username : "null"));
        holder.tvAddress.setText("Address: " + request.getAddress());
        holder.tvStatus.setText("Status: " + request.getStatus());

        // ✅ Realtime dynamic calculation of total price via API
        int itemId = request.getItemId();
        double weight = request.getWeight(); // ensure this exists in RequestModel

        SharedPrefManager spm = new SharedPrefManager(context.getApplicationContext());
        User user = spm.getUser();
        String token = user.getToken();

        double total = weight * request.getItem().getPricePerKg();
        holder.tvTotalPrice.setText("Total Price: RM " + String.format("%.2f", total));
        /*ApiUtils.getUserService().getItemById(token, itemId).enqueue(new Callback<RecyclableItem>() {
            @Override
            public void onResponse(Call<RecyclableItem> call, Response<RecyclableItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    double pricePerKg = response.body().getPricePerKg();
                    double total = weight * pricePerKg;
                    holder.tvTotalPrice.setText("Total Price: RM " + String.format("%.2f", total));
                } else {
                    holder.tvTotalPrice.setText("Total Price: RM N/A");
                }
            }

            @Override
            public void onFailure(Call<RecyclableItem> call, Throwable t) {
                holder.tvTotalPrice.setText("Total Price: RM Error");
            }
        });*/

        holder.btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminUpdateActivity.class);
            intent.putExtra("request_id", request.getRequestId());
            intent.putExtra("item_id", request.getItemId());
            intent.putExtra("user_id", request.getUserId());
            intent.putExtra("address", request.getAddress());
            intent.putExtra("notes", request.getNotes());
            intent.putExtra("status", request.getStatus());
            intent.putExtra("price_per_kg", request.getPricePerKg()); // optional fallback
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUID, tvItemName, tvUsername, tvAddress, tvStatus, tvTotalPrice;
        Button btnUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUID = itemView.findViewById(R.id.tvUID);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice); // ✅ required in XML
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }
    }
}
