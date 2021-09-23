package com.yk.player.data.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yk.player.R;
import com.yk.player.data.bean.Video;
import com.yk.player.ui.play.PlayActivity;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> implements Filterable {
    private Context context;

    private final List<Video> list;
    private final List<Video> filterList = new ArrayList<>();

    public VideoAdapter(List<Video> list) {
        this.list = list;
        filterList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                PlayActivity.startPlayActivity(context, filterList, position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Video video = filterList.get(position);
        Glide.with(context).load(video.getPath()).into(holder.ivCover);
        holder.tvName.setText(video.getName());
        holder.tvDuration.setText(String.valueOf(video.getDuration()));
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Video> curList = new ArrayList<>();
                if (TextUtils.isEmpty(constraint)) {
                    curList = list;
                } else {
                    for (Video video : list) {
                        if (!video.getName().contains(constraint)) {
                            continue;
                        }
                        curList.add(video);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = curList;
                filterResults.count = curList.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<Video> curList = (List<Video>) results.values;
                filterList.clear();
                filterList.addAll(curList);
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView ivCover;
        AppCompatTextView tvName;
        AppCompatTextView tvDuration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvName = itemView.findViewById(R.id.tvName);
            tvDuration = itemView.findViewById(R.id.tvDuration);
        }
    }
}
