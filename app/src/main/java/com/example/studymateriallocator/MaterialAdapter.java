package com.example.studymateriallocator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder> {

    Context context;
    ArrayList<Material> materials;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MaterialAdapter(Context context, ArrayList<Material> materials){
        this.context = context;
        this.materials = materials;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleText, detailsText, downloadsText;
        ImageView downloadBtn, deleteBtn, editBtn;

        public ViewHolder(View view){
            super(view);
            titleText = view.findViewById(R.id.titleText);
            detailsText = view.findViewById(R.id.detailsText);
            downloadsText = view.findViewById(R.id.downloadsText);
            downloadBtn = view.findViewById(R.id.downloadBtn);
            deleteBtn = view.findViewById(R.id.deleteBtn);
            editBtn = view.findViewById(R.id.editBtn);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.material_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Material m = materials.get(position);

        holder.titleText.setText(m.title);
        holder.detailsText.setText("Uploaded by: " + m.uploadedBy);
        holder.downloadsText.setText(String.valueOf(m.downloads));

        DocumentReference ref = db.collection("materials").document(m.id);

        // Download button
        holder.downloadBtn.setOnClickListener(v -> {
            ref.update("downloads", FieldValue.increment(1));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(m.url));
            context.startActivity(intent);
        });

        // Delete button (only uploader)
        holder.deleteBtn.setOnClickListener(v -> {
            String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            if(m.uploadedBy.equals(currentUser)){
                ref.delete();
                Toast.makeText(context,"Material Deleted",Toast.LENGTH_SHORT).show();
                materials.remove(position);
                notifyItemRemoved(position);
            }else{
                Toast.makeText(context,"Only uploader can delete",Toast.LENGTH_SHORT).show();
            }
        });

        // Edit button (only uploader)
        holder.editBtn.setOnClickListener(v -> {
            String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();

            if (!m.uploadedBy.equals(currentUser)) {
                Toast.makeText(context, "Only uploader can edit", Toast.LENGTH_SHORT).show();
                return;
            }

            // Dialog to edit title and URL
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setTitle("Edit Material");

            android.widget.LinearLayout layout = new android.widget.LinearLayout(context);
            layout.setOrientation(android.widget.LinearLayout.VERTICAL);

            android.widget.EditText inputTitle = new android.widget.EditText(context);
            inputTitle.setText(m.title);
            layout.addView(inputTitle);

            android.widget.EditText inputUrl = new android.widget.EditText(context);
            inputUrl.setText(m.url);
            layout.addView(inputUrl);

            builder.setView(layout);

            builder.setPositiveButton("Save", (dialog, which) -> {
                String newTitle = inputTitle.getText().toString().trim();
                String newUrl = inputUrl.getText().toString().trim();

                if (!newTitle.isEmpty() && !newUrl.isEmpty()) {
                    ref.update("title", newTitle, "url", newUrl)
                            .addOnSuccessListener(aVoid -> {
                                m.title = newTitle;
                                m.url = newUrl;
                                notifyItemChanged(position);
                                Toast.makeText(context, "Material Updated", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show());
                }
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            builder.show();
        });
    }

    @Override
    public int getItemCount(){
        return materials.size();
    }
}