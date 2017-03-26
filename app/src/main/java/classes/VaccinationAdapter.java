package classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ebada.vaccinationtracking.R;

import java.util.ArrayList;

/**
 * Created by ebada on 06/03/2017.
 */

public class VaccinationAdapter extends BaseAdapter {
    Context context;
    ArrayList<kidsVaccination> vac ;
    public VaccinationAdapter (Context context ,ArrayList<kidsVaccination> vac)
    {
        this.context = context;
        this.vac = vac;
    }
    @Override
    public int getCount() {
        return vac.size();
    }

    @Override
    public Object getItem(int position) {
        return vac.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(R.layout.vac_listview_item,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.status.setText(vac.get(position).getStatus());
        holder.name.setText(vac.get(position).getName());
        holder.description.setText(vac.get(position).getDescription());
        return convertView;
    }

    static  class ViewHolder
    {
        TextView name,status,description;
        public ViewHolder(View view)
        {
            name = (TextView) view.findViewById(R.id.vacName);
            status = (TextView) view.findViewById(R.id.status);
            description = (TextView) view.findViewById(R.id.desc);
        }
    }
}
