package com.example.viewpager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viewpager.fragment.FileListFragment;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {

    Context context;
    File[] filesAndFolders;
    RecyclerToFolderlistFragment mListener;

    public FileListAdapter(Context context, File[] filesAndFolders, RecyclerToFolderlistFragment listener) {
        this.mListener = listener;
        this.context = context;
        this.filesAndFolders = filesAndFolders;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view, mListener);
    }


    @Override
    public void onBindViewHolder(FileListAdapter.ViewHolder holder, int position) {


        File file = filesAndFolders[position];
        holder.textView.setText(file.getName());
        holder.imageView.setImageResource(R.drawable.ic_launcher_background);
        holder.file = file;

    }

    @Override
    public int getItemCount() {
        return filesAndFolders.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        File file;
        RecyclerToFolderlistFragment mLisener;

        public ViewHolder(View itemView, RecyclerToFolderlistFragment listenr) {
            super(itemView);
            textView = itemView.findViewById(R.id.file_name_text_view);
            imageView = itemView.findViewById(R.id.icon_view);
            mLisener = listenr;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenr.onRecyclerToFolderlistFragment(file.getName());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    PopupMenu popupMenu = new PopupMenu(context, v);
                    popupMenu.getMenu().add("DELETE");
                    popupMenu.getMenu().add("MOVE");
                    popupMenu.getMenu().add("RENAME");

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getTitle().equals("DELETE")) {
                                boolean deleted = file.delete();
                                if (deleted) {
                                    Toast.makeText(context.getApplicationContext(), "DELETED ", Toast.LENGTH_SHORT).show();
                                    v.setVisibility(View.GONE);
                                }
                            }
                            if (item.getTitle().equals("MOVE")) {
                                Toast.makeText(context.getApplicationContext(), "MOVED ", Toast.LENGTH_SHORT).show();

                            }
                            if (item.getTitle().equals("RENAME")) {
                                Toast.makeText(context.getApplicationContext(), "RENAME ", Toast.LENGTH_SHORT).show();

                            }
                            return true;
                        }
                    });

                    popupMenu.show();
                    return true;
                }
            });
        }
    }


    public interface RecyclerToFolderlistFragment {
        void onRecyclerToFolderlistFragment(String folder);
    }

}
