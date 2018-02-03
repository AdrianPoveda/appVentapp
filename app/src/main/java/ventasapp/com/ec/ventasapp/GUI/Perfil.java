package ventasapp.com.ec.ventasapp.GUI;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ventasapp.com.ec.ventasapp.R;
import ventasapp.com.ec.ventasapp.data.DataBase;
import ventasapp.com.ec.ventasapp.utilidades.DisplayUtils;
import ventasapp.com.ec.ventasapp.utilidades.ResizeImage;

public class Perfil extends AppCompatActivity {

    private ImageView fotoperfil;
    private TextView txtTitulo;
    private TextView txtnombre;
    private TextView txtapellido;
    private TextView txtdireccion;
    private TextView txtemail;
    private TextView txtfecha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_perfil);
        createComponents();
    }


    public void createComponents() {

        fotoperfil = new ImageView(this);
        txtTitulo = new TextView(this);
        txtnombre = new TextView(this);
        txtapellido = new TextView(this);
        txtdireccion = new TextView(this);
        txtemail = new TextView(this);
        txtfecha = new TextView(this);


        DisplayUtils displayUtils = new DisplayUtils();
        ResizeImage resizeImage = new ResizeImage();
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.imgPerfil);
        int height = displayUtils.getHeight(getWindowManager());
        int width = displayUtils.getWidth(getWindowManager());

        int indetifier = getResources().getIdentifier("users","drawable",getPackageName());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100*width,20*height);
        layoutParams.setMargins(30*width,10*height,30*width,0);
        fotoperfil.setLayoutParams(layoutParams);
        relativeLayout.addView(fotoperfil);
        Bitmap bitmap = resizeImage.resizeImage(getApplicationContext(),indetifier,90*width,20*height);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        fotoperfil.setBackgroundDrawable(bitmapDrawable);

        layoutParams = new RelativeLayout.LayoutParams(100*width,7*height);
        txtTitulo.setSingleLine();
        txtTitulo.setText("Mi Perfil");
        txtTitulo.setTextColor(Color.BLACK);
        txtTitulo.setTypeface(null, Typeface.BOLD);
        txtTitulo.setGravity(Gravity.CENTER);
        txtTitulo.setPadding(20,0,20,0);
        txtTitulo.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(5*width,2*height,5*width,0);
        txtTitulo.setLayoutParams(layoutParams);
        relativeLayout.addView(txtTitulo);

        DataBase dataBase = new DataBase(getApplicationContext());

        layoutParams = new RelativeLayout.LayoutParams(100*width,7*height);
        txtnombre.setSingleLine();
        txtnombre.setText("Nombres : "+dataBase.getUser("1").getNombres());
        txtnombre.setTextColor(Color.BLACK);
        txtnombre.setTypeface(null, Typeface.BOLD);
        txtnombre.setGravity(Gravity.CENTER);
        txtnombre.setPadding(20,0,20,0);
        txtnombre.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(5*width,30*height,5*width,0);
        txtnombre.setLayoutParams(layoutParams);
        relativeLayout.addView(txtnombre);

        layoutParams = new RelativeLayout.LayoutParams(100*width,7*height);
        txtapellido.setSingleLine();
        txtapellido.setText("Apellidos : "+dataBase.getUser("1").getApellido());
        txtapellido.setTextColor(Color.BLACK);
        txtapellido.setTypeface(null, Typeface.BOLD);
        txtapellido.setGravity(Gravity.CENTER);
        txtapellido.setPadding(20,0,20,0);
        txtapellido.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(5*width,35*height,5*width,0);
        txtapellido.setLayoutParams(layoutParams);
        relativeLayout.addView(txtapellido);

        layoutParams = new RelativeLayout.LayoutParams(100*width,7*height);
        txtdireccion.setSingleLine();
        txtdireccion.setTypeface(null, Typeface.BOLD);
        txtdireccion.setText("Dirección : "+dataBase.getUser("1").getDireccion());
        txtdireccion.setTextColor(Color.BLACK);
        txtdireccion.setGravity(Gravity.CENTER);
        txtdireccion.setPadding(20,0,20,0);
        txtdireccion.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(5*width,40*height,5*width,0);
        txtdireccion.setLayoutParams(layoutParams);
        relativeLayout.addView(txtdireccion);

        layoutParams = new RelativeLayout.LayoutParams(100*width,7*height);
        txtemail.setSingleLine();
        txtemail.setTypeface(null, Typeface.BOLD);
        txtemail.setText("Email : "+dataBase.getUser("1").getEmail());
        txtemail.setTextColor(Color.BLACK);
        txtemail.setGravity(Gravity.CENTER);
        txtemail.setPadding(20,0,20,0);
        txtemail.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(5*width,45*height,5*width,0);
        txtemail.setLayoutParams(layoutParams);
        relativeLayout.addView(txtemail);

        layoutParams = new RelativeLayout.LayoutParams(100*width,7*height);
        txtfecha.setSingleLine();
        txtfecha.setTypeface(null, Typeface.BOLD);
        txtfecha.setText("Fecha Nacimiento : "+dataBase.getUser("1").getFecha());
        txtfecha.setTextColor(Color.BLACK);
        txtfecha.setGravity(Gravity.CENTER);
        txtfecha.setPadding(20,0,20,0);
        txtfecha.setTextSize(displayUtils.getFontSize(getResources().getDisplayMetrics().density));
        layoutParams.setMargins(5*width,50*height,5*width,0);
        txtfecha.setLayoutParams(layoutParams);
        relativeLayout.addView(txtfecha);

        dataBase.close();

    }
}
