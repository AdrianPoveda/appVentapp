package ventasapp.com.ec.ventasapp.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ventasapp.com.ec.ventasapp.model.FirebaseDB;
import ventasapp.com.ec.ventasapp.model.Notificacion;



public class ManagementNotificacion {

    private DatabaseReference dbReference;
    private static final String LOG = ManagementNotificacion.class.getName();

    public ManagementNotificacion() {
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
        dbReference = firebaseDB.getReference(FirebaseDB.TBL_VENTAS);

    }

    public void insertar(Notificacion order) {

        dbReference.child(order.id).setValue(order);

    }

    public void delete(String order) {

        dbReference.child(order).removeValue();
    }



}
