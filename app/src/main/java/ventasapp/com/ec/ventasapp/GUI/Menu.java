package ventasapp.com.ec.ventasapp.GUI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ventasapp.com.ec.ventasapp.R;
import ventasapp.com.ec.ventasapp.Servicios.ServicioNotificacion;
import ventasapp.com.ec.ventasapp.data.DataBase;
import ventasapp.com.ec.ventasapp.utilidades.ComponentsAnimation;
import ventasapp.com.ec.ventasapp.utilidades.DisplayUtils;
import ventasapp.com.ec.ventasapp.utilidades.ResizeImage;

/**
 * Created by root on 24/01/18.
 */

public class Menu extends AppCompatActivity{

    private ImageView imgMain;
    private ImageView imgMenu;
    private ImageView imgPerfil;
    private ImageView imgPublicaciones;
    private ImageView imgCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);
        final ComponentsAnimation animation = new ComponentsAnimation();
        createComponents();

        imgMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animation.componentAnimation(getApplicationContext(),imgMain);
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animation.componentAnimation(getApplicationContext(),imgPerfil);
                Intent i = new Intent(getApplicationContext(), Perfil.class); //PonerPerfil
                startActivity(i);
            }
        });

        imgPublicaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animation.componentAnimation(getApplicationContext(),imgPublicaciones);
                Intent i = new Intent(getApplicationContext(), MainActivity.class); //PonerPublicacionesPorPerfil
                startActivity(i);
            }
        });

        imgCerrarSesion.setOnClickListener(new View.OnClickListener() { //Poner Cerrar Sesion
            @Override
            public void onClick(View view) {
                animation.componentAnimation(getApplicationContext(),imgCerrarSesion);
                stopService(new Intent(getBaseContext(), ServicioNotificacion.class));
                DataBase dataBase = new DataBase(getApplicationContext());
                dataBase.updateUser("1","","","","","","","");
                dataBase.close();
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });

    }

    public void createComponents() {
        imgMain = new ImageView(this);
        imgMenu = new ImageView(this);
        imgPublicaciones = new ImageView(this);
        imgPerfil = new ImageView(this);
        imgCerrarSesion = new ImageView(this);

        DisplayUtils displayUtils = new DisplayUtils();
        ResizeImage resizeImage = new ResizeImage();
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activ_menu);

        int height = displayUtils.getHeight(getWindowManager());
        int width = displayUtils.getWidth(getWindowManager());

        int indetifier = getResources().getIdentifier("btnmenuprincipal","drawable",getPackageName());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(30*width,20*height);
        layoutParams.setMargins(20*width,18*height,50*width,0);
        imgMain.setLayoutParams(layoutParams);
        relativeLayout.addView(imgMain);
        Bitmap bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,30*width,20*height);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        imgMain.setBackgroundDrawable(bitmapDrawable);

        indetifier = getResources().getIdentifier("logomenu","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(70*width,10*height);
        layoutParams.setMargins(15*width,4*height,15*width,0);
        imgMenu.setLayoutParams(layoutParams);
        relativeLayout.addView(imgMenu);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,80*width,10*height);
        bitmapDrawable = new BitmapDrawable(bitmap);
        imgMenu.setBackgroundDrawable(bitmapDrawable);

        indetifier = getResources().getIdentifier("btninicionuevo","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(32*width,18*height);
        layoutParams.setMargins(58*width,19*height,20*width,0);
        imgPerfil.setLayoutParams(layoutParams);
        relativeLayout.addView(imgPerfil);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,25*width,24*height);
        bitmapDrawable = new BitmapDrawable(bitmap);
        imgPerfil.setBackgroundDrawable(bitmapDrawable);

        indetifier = getResources().getIdentifier("btnpublicaciones","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(27*width,17*height);
        layoutParams.setMargins(20*width,42*height,46*width,0);
        imgPublicaciones.setLayoutParams(layoutParams);
        relativeLayout.addView(imgPublicaciones);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,27*width,17*height);
        bitmapDrawable = new BitmapDrawable(bitmap);
        imgPublicaciones.setBackgroundDrawable(bitmapDrawable);

        indetifier = getResources().getIdentifier("btnlogout","drawable",getPackageName());
        layoutParams = new RelativeLayout.LayoutParams(40*width,25*height);
        layoutParams.setMargins(60*width,38*height,20*width,0);
        imgCerrarSesion.setLayoutParams(layoutParams);
        relativeLayout.addView(imgCerrarSesion);
        bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,40*width,25*height);
        bitmapDrawable = new BitmapDrawable(bitmap);
        imgCerrarSesion.setBackgroundDrawable(bitmapDrawable);

    }


}
