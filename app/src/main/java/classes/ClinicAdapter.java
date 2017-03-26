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

public class ClinicAdapter extends BaseAdapter {

    Context context;
    ArrayList<Clinic> clinics ;
    public ClinicAdapter (Context context ,ArrayList<Clinic> clinics)
    {
        this.context = context;
        this.clinics = clinics;
    }
    @Override
    public int getCount() {
        return clinics.size();
    }

    @Override
    public Object getItem(int position) {
        return clinics.get(position);
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
            convertView= inflater.inflate(R.layout.clinic_listview,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ClinicName.setText(clinics.get(position).getName());
        holder.ClinicAddress.setText(clinics.get(position).getAddress());
        holder.ClinicPhone.setText(clinics.get(position).getPhone());
        return convertView;
    }

    static  class ViewHolder
    {
        TextView ClinicName,ClinicAddress,ClinicPhone;
        public ViewHolder(View view)
        {
            ClinicName = (TextView) view.findViewById(R.id.clinicName);
            ClinicAddress = (TextView) view.findViewById(R.id.clinicAddress);
            ClinicPhone = (TextView) view.findViewById(R.id.clinicNumber);
         }
    }
}
