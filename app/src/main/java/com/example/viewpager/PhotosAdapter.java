//package com.example.viewpager;
//import android.provider.Contacts;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
//
//    private List<Contacts.Photos> = mList;
//
//
//    // Create new views (invoked by the layout manager)
//    @Override
//    public PhotosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
//                                                       int viewType) {
//        // create a new view
//        View itemLayoutView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.each_item_album, null);
//
//        // create ViewHolder
//
//        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
//        return viewHolder;
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, int position) {
//
//        // - get data from your itemsData at this position
//        // - replace the contents of the view with that itemsData
//
//            }
//
//    // inner class to hold a reference to each item of RecyclerView
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView txtViewTitle;
//        public ImageView imgViewIcon;
//
//        public ViewHolder(View itemLayoutView) {
//            super(itemLayoutView);
//            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.);
//        }
//    }
//
//
//    // Return the size of your itemsData (invoked by the layout manager)
//    @Override
//    public int getItemCount() {
//        return itemsData.length;
//    }
//}
