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
import com.projects.bookpdf.data.Category;
import com.projects.bookpdf.data.ObjectCollection;

import java.util.Map;
import java.util.Observable;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapterCategory extends RecyclerView.Adapter<RecyclerAdapterCategory.ViewHolder> {
    private Context context;
    private int lastPos=-1;
    private boolean selectedOnce=false;
    private static final String tag="AdapterCategory";
    private CategorySelectedNotifier categorySelectedNotifier=new CategorySelectedNotifier();
    public RecyclerAdapterCategory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_category_item, parent, false);
        ViewHolder vh=new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int i=0;
        String catName = null;
        String imgUrl=null;
        for(Map.Entry<String,Category> entry : ObjectCollection.category.entrySet())
        {
            if(i==position)
            {
                catName=entry.getValue().getCategoryTitle();
                imgUrl=entry.getValue().getCategoryImageUrl();
                break;
            }
            i++;
        }
        Log.e(tag,"inside onBindViewHolder() : "+position);
        Log.e(tag,"inside onBindVIewHolder() catName : "+catName);
        holder.txtCategoryName.setText(catName);
        Glide.with(context)
                .load(imgUrl)
                .into(holder.imgCategory);

        String finalCatName = catName;
        holder.cardCategory.setOnClickListener(v -> {
            //TODO: set color for selected item and de select color for rest of items
            select(holder);
            categorySelectedNotifier.notifyAboutCategorySelected(finalCatName);
            lastPos=position;
            if(!selectedOnce&&position==0)
                selectedOnce = true;
            else
                notifyDataSetChanged();
        });
        if(position==0&&!selectedOnce) {
            holder.cardCategory.callOnClick();
        }
        if(lastPos==position)
            select(holder);
        else
            deSelect(holder);
    }

    private void deSelect(ViewHolder holder) {
        holder.cardCategory.setCardBackgroundColor(context.getResources().getColor(R.color.text_color_white));
        holder.txtCategoryName.setTextColor(context.getResources().getColor(R.color.text_color_black));
    }

    private void select(ViewHolder holder) {
        holder.cardCategory.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        holder.txtCategoryName.setTextColor(context.getResources().getColor(R.color.text_color_white));
    }

    @Override
    public int getItemCount() {
        return ObjectCollection.category.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgCategory;
        TextView txtCategoryName;
        CardView cardCategory;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardCategory=itemView.findViewById(R.id.card_view_category);
            txtCategoryName=itemView.findViewById(R.id.txt_category_name);
            imgCategory=itemView.findViewById(R.id.circle_img_category);
        }
    }
    public static class CategorySelectedNotifier extends Observable
    {
        void notifyAboutCategorySelected(String catName)
        {
            setChanged();
            notifyObservers(catName);
        }
    }

    public CategorySelectedNotifier getCategorySelectedNotifier() {
        return categorySelectedNotifier;
    }
}
