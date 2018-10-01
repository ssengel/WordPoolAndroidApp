package com.ssengel.wordpool.helper;

import com.ssengel.wordpool.R;

public class CategoryToResorceId {
    public static int getImageResource(String imgName){
        if(imgName.equals("Health")){
            return R.drawable.img_health;
        }else if (imgName.equals("Science")){
            return  R.drawable.img_science;
        }
        else if (imgName.equals("Electronic")){
            return  R.drawable.img_electronic;
        }
        else if (imgName.equals("Art")){
            return R.drawable.img_art;
        }else{
            return R.drawable.img_computer;
        }
    }
}
