package ventasapp.com.ec.ventasapp.utilidades;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import ventasapp.com.ec.ventasapp.R;



public class ComponentsAnimation {

    public void componentAnimation(Context context, ImageButton imageButton){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.btnanimation);
        animation.reset();
        imageButton.startAnimation(animation);
    }

    public void componentAnimation(Context context, ImageView imageView){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.btnanimation);
        animation.reset();
        imageView.startAnimation(animation);

    }


    public void componentAnimation(Context context, Button button){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.btnanimation);
        animation.reset();
        button.startAnimation(animation);

    }
}
