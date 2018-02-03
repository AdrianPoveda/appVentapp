package ventasapp.com.ec.ventasapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ventasapp.com.ec.ventasapp.GUI.MainActivity;
import ventasapp.com.ec.ventasapp.R;
import ventasapp.com.ec.ventasapp.model.ItemPropiedad;
import ventasapp.com.ec.ventasapp.utilidades.DisplayUtils;

/**
 * Created by Jhonny on 22/1/18.
 */

public class ItemPropiedadAdapter extends BaseAdapter {

    Context context;
    List<ItemPropiedad> propiedadList;

    public ItemPropiedadAdapter(Context context, List<ItemPropiedad> propiedadList) {
        this.context = context;
        this.propiedadList = propiedadList;
    }

    @Override
    public int getCount() {
        return propiedadList.size();
    }

    @Override
    public Object getItem(int position) {
        return propiedadList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return propiedadList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = LayoutInflater.from(context);

        DisplayUtils displayUtils = new DisplayUtils();

        view = inflater.inflate(R.layout.item_list_propiedad,null);

        ImageView imgPropiedad = (ImageView)view.findViewById(R.id.imgpropiedad);
        TextView txtPrecio = (TextView)view.findViewById(R.id.txtprecio);
        TextView txtDescripcion = (TextView)view.findViewById(R.id.txtdescripcion);

        imgPropiedad.setImageBitmap(propiedadList.get(position).getImgPropiedad());
        txtPrecio.setText(propiedadList.get(position).getPrecio());
        txtDescripcion.setText(propiedadList.get(position).getDescripcion());


        return view;
    }


}
