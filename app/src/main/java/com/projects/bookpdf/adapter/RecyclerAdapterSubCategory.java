package com.projects.bookpdf.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.projects.bookpdf.R;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapterSubCategory extends RecyclerView.Adapter<RecyclerAdapterSubCategory.ViewHolder> {
    private Context context;
    private LinkedHashMap<String,String> subCategoryNames;
    private SubCategorySelectedNotifier subCategorySelectedNotifier=new SubCategorySelectedNotifier();
    private static final String tag="SubCategoryAdapter";
    private int lastPos=-1;
    public RecyclerAdapterSubCategory(Context context, LinkedHashMap<String, String> subCategoryNames) {
        this.context = context;
        this.subCategoryNames = subCategoryNames;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_sub_category_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int i=0;
        String subCatName=null;
        String imgUrl=null;
        for(Map.Entry<String,String> entry : subCategoryNames.entrySet())
        {
            if(i==position)
            {
                subCatName=entry.getKey();
                imgUrl=entry.getValue();
                break;
            }
            i++;
        }
        Log.e(tag,"inside onBindViewHolder() : position "+position);
        Log.e(tag,"inside onBindViewHolder() : subCatName "+subCatName);
        holder.txtSubCategoryName.setText(subCatName);
        Glide.with(context)
                .load(imgUrl)
                .into(holder.imgSubCategory);

        String finalSubCatName = subCatName;
        holder.cardSubCategory.setOnClickListener(v -> {
            //TODO: set color for selected item and de select color for rest of items
            select(holder);
            subCategorySelectedNotifier.notifyCategoryViewModel(finalSubCatName);
            lastPos=position;
            notifyDataSetChanged();
        });
        if(position==lastPos)
            select(holder);
        else
            deSelect(holder);
    }
    private void deSelect(ViewHolder holder) {
        holder.cardSubCategory.setCardBackgroundColor(context.getResources().getColor(R.color.text_color_white));
        holder.txtSubCategoryName.setTextColor(context.getResources().getColor(R.color.text_color_black));
    }

    private void select(ViewHolder holder) {
        holder.cardSubCategory.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        holder.txtSubCategoryName.setTextColor(context.getResources().getColor(R.color.text_color_white));
    }
    @Override
    public int getItemCount() {
        return subCategoryNames.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardSubCategory;
        CircleImageView imgSubCategory;
        TextView txtSubCategoryName;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardSubCategory=itemView.findViewById(R.id.card_view_sub_category);
            imgSubCategory=itemView.findViewById(R.id.img_sub_category);
            txtSubCategoryName=itemView.findViewById(R.id.txt_sub_category);
        }
    }
    public static class SubCategorySelectedNotifier extends Observable
    {
        void notifyCategoryViewModel(String subCategoryName)
        {
            setChanged();
            notifyObservers(subCategoryName);
        }
    }

    public SubCategorySelectedNotifier getSubCategorySelectedNotifier() {
        return subCategorySelectedNotifier;
    }
}
