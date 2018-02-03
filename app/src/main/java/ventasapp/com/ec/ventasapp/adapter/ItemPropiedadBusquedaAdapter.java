package ventasapp.com.ec.ventasapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ventasapp.com.ec.ventasapp.R;
import ventasapp.com.ec.ventasapp.model.ItemPropiedad;
import ventasapp.com.ec.ventasapp.model.ItemPropiedadBusqueda;
import ventasapp.com.ec.ventasapp.utilidades.DisplayUtils;

/**
 * Created by Jhonny on 24/1/18.
 */

public class ItemPropiedadBusquedaAdapter extends ArrayAdapter<ItemPropiedadBusqueda> {

    Activity context;
    List<ItemPropiedadBusqueda> propiedadBusquedaList;

    public ItemPropiedadBusquedaAdapter(Activity context, List<ItemPropiedadBusqueda> propiedadBusquedaList) {
        super(context, R.layout.item_list_propiedad_busqueda, propiedadBusquedaList);
        this.context = context;
        this.propiedadBusquedaList = propiedadBusquedaList;
    }

    static class ViewHolder {
        protected ImageView imgID;
        protected TextView txtPrecio;
        protected TextView txtDescripcion;
        protected CheckBox checkBox;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.item_list_propiedad_busqueda, null);
            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.imgID = (ImageView)view.findViewById(R.id.imgpropiedad);
            viewHolder.txtPrecio = (TextView)view.findViewById(R.id.txtprecio);
            viewHolder.txtDescripcion = (TextView)view.findViewById(R.id.txtdescripcion);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkBox_propiedad);
            viewHolder.checkBox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            ItemPropiedadBusqueda element = (ItemPropiedadBusqueda) viewHolder.checkBox
                                    .getTag();
                            element.setCheckPropiedad(buttonView.isChecked());

                        }
                    });
            view.setTag(viewHolder);
            viewHolder.checkBox.setTag(propiedadBusquedaList.get(position));
            viewHolder.txtPrecio.setTag(propiedadBusquedaList.get(position));
            viewHolder.txtDescripcion.setTag(propiedadBusquedaList.get(position));


        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkBox.setTag(propiedadBusquedaList.get(position));
            ((ViewHolder) view.getTag()).txtPrecio.setTag(propiedadBusquedaList.get(position));
            ((ViewHolder) view.getTag()).txtDescripcion.setTag(propiedadBusquedaList.get(position));

        }
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.imgID.setImageBitmap(propiedadBusquedaList.get(position).getImgPropiedad()); //agregado

        holder.txtPrecio.setText(propiedadBusquedaList.get(position).getPrecio());
        holder.txtDescripcion.setText(propiedadBusquedaList.get(position).getDescripcion());
        holder.checkBox.setChecked(propiedadBusquedaList.get(position).isCheckPropiedad());
        return view;
    }



}
