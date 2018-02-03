package ventasapp.com.ec.ventasapp.Servicios;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ventasapp.com.ec.ventasapp.GUI.MainActivity;
import ventasapp.com.ec.ventasapp.R;
import ventasapp.com.ec.ventasapp.data.DataBase;
import ventasapp.com.ec.ventasapp.model.FirebaseDB;
import ventasapp.com.ec.ventasapp.model.Notificacion;

public class ServicioNotificacion extends Service {

    private DatabaseReference dbReference;
    public static final int NOTIFICACION_ID=1;


    @Override
    public void onCreate() {
        super.onCreate();

        final FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();

        dbReference = firebaseDB.getReference(FirebaseDB.TBL_VENTAS);

        dbReference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Notificacion notificacion = dataSnapshot.getValue(Notificacion.class);
                notificacion.id = dataSnapshot.getKey();
                final DataBase dataBase = new DataBase(getApplicationContext());

                if(dataBase.getUser("1").getCodigoUser().equals(notificacion.getId())){

                    notificationAcept(notificacion.getDescripcion());

                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public ServicioNotificacion() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    public void notificationAcept(String descripcion){

        Intent intent = new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        builder.setSmallIcon(R.drawable.ic_stat_name);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        long[] pattern = new long[]{1000,500,1000,500};
        builder.setVibrate(pattern);
        builder.setLights(Color.BLUE, 1, 1);
        builder.setContentTitle("Venta App");
        builder.setContentText("Se realizado una nueva notificacion en tu propiedad "+descripcion);
        NotificationManager notificationManager= (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICACION_ID,builder.build());
    }
}
