package com.example.quiziac;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FeatureAdapter  extends RecyclerView.Adapter<FeatureAdapter.FeatureViewHolder>{



    Context context;
    ArrayList<FeatureModel> featureModels;
    public FeatureAdapter(Context context, ArrayList<FeatureModel> featureModels){
        this.context = context;
        this.featureModels = featureModels;

    }


    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, null);
        return new FeatureViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        FeatureModel model=featureModels.get(position);

        holder.textView.setText(model.getCategoryNameFeatures());
        Glide.with(context).load(model.getCategoryImageFeatures()).into(holder.imageView);

        holder.itemView.setOnClickListener(view -> {
            switch (model.getCatId()){
                case "EeLnosM5wNCwEykTMil5":
                    Intent intent = new Intent(context, EbookActivity.class);
                    intent.putExtra("catId",model.getCatId());
                    intent.putExtra("FeaturesId",model.getFeaturesId());
                    context.startActivity(intent);
                    break;

                case "cwSahstFugxJ1xYu6ppR":
                     intent = new Intent(context, quizactivity.class);
                    intent.putExtra("catId",model.getCatId());
                    intent.putExtra("FeaturesId",model.getFeaturesId());
                    context.startActivity(intent);
                    break;

                case "VbfooWhY7mEtoUBXZCBj":
                    intent = new Intent(context, VideoActivity.class);
                    intent.putExtra("catId",model.getCatId());
                    intent.putExtra("FeaturesId",model.getFeaturesId());
                    context.startActivity(intent);
                    break;

                case "iKxNex7Oft5UVEPiVSeu":
                    intent = new Intent(context, quizactivity.class);
                    intent.putExtra("catId",model.getCatId());
                    intent.putExtra("FeaturesId",model.getFeaturesId());
                    context.startActivity(intent);
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return featureModels.size();
    }

    public class FeatureViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.Cardimage);
            textView = itemView.findViewById(R.id.cardName);
        }
    }
}
