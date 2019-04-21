package project.itcs6166.com.expirynotify.main.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import project.itcs6166.com.expirynotify.R;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.MyViewHolder>{

    private static final String TAG = "ViewAdapter";
    private List<String> item_names;
    private List<String> expiry_dates;
    private Context context;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        clickListener = listener;
    }

    public ViewAdapter(List<String> item_names, List<String> expiry_dates, Context context) {
        this.item_names = item_names;
        this.expiry_dates = expiry_dates;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_text_view,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view, clickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder : called");
        holder.itemNameView.setText(item_names.get(position));
        holder.itemDateView.setText(expiry_dates.get(position));
    }

    @Override
    public int getItemCount() {
        return item_names.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameView;
        TextView itemDateView;
        RelativeLayout parentLayout;
        ImageView delete_image;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.item_name);
            itemDateView = itemView.findViewById(R.id.expiry_date);
            parentLayout = itemView.findViewById(R.id.list_parent_layout);
            delete_image = itemView.findViewById(R.id.image_delete);


            delete_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

}
