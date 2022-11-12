package com.example.demoproject.FindMatchView;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.demoproject.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.Holder>{

    Bitmap[] images;

    public SliderAdapter(Bitmap[] images){

        this.images = images;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slider_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {

        viewHolder.imageView.setImageBitmap(images[position]);
        viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //viewHolder.imageView.setImageResource(images[position]);

    }

    @Override
    public int getCount() {
        return images.length;
    }

    public class Holder extends  SliderViewAdapter.ViewHolder{

        ImageView imageView;

        public Holder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.slider_image_view);

        }
    }

}
