package com.example.quiziac;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EbookAdapter extends RecyclerView.Adapter<EbookAdapter.EbookViewHolder> {

    private Context context;
    private List<EbookData> list;

    public EbookAdapter(Context context, List<EbookData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EbookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdf_style,parent,false);
        return new EbookViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull EbookViewHolder holder, int position) {
            holder.pdf_name.setText(list.get(position).getName());

            holder.itemView.setOnClickListener(view -> {
                //Toast.makeText(context,list.get(position).getName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, PdfViewerActivity.class);
                intent.putExtra("Url",list.get(position).getUrl());
                context.startActivity(intent);
            });
            holder.pdf_download.setOnClickListener(view -> {
                //Toast.makeText(context,"Download",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(list.get(position).getUrl()));
                context.startActivity(intent);
            });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EbookViewHolder extends RecyclerView.ViewHolder {

        private TextView pdf_name;
        private ImageView pdf_download;

        public EbookViewHolder(@NonNull View itemView) {
            super(itemView);

            pdf_download = itemView.findViewById(R.id.pdf_download);
            pdf_name = itemView.findViewById(R.id.pdf_name);
        }
    }
}
