package classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ebada.vaccinationtracking.R;

import java.util.ArrayList;

import custom_font.MyTextView;

/**
 * Created by ebada on 12/03/2017.
 */

public class KidHistoryAdapter extends BaseAdapter {
    Context context;
    ArrayList<VacHistory> vacHistories ;
    public  KidHistoryAdapter (Context context , ArrayList<VacHistory> vacHistories)
    {
        this.context = context;
        this.vacHistories = vacHistories;
    }
    @Override
    public int getCount() {
        return vacHistories.size();
    }

    @Override
    public Object getItem(int position) {
        return vacHistories.get(position);
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
            convertView= inflater.inflate(R.layout.show_history_dialog,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.symptoms.setText(vacHistories.get(position).getSymptom());
        holder.pharmaceutical.setText(vacHistories.get(position).getPharmaceutical());
        return convertView;
    }

    static  class ViewHolder
    {
        MyTextView symptoms;
        MyTextView pharmaceutical;
        public ViewHolder(View view)
        {
            symptoms = (MyTextView) view.findViewById(R.id.symptoms);
            pharmaceutical = (MyTextView) view.findViewById(R.id.pharmaceutical);
        }
    }
}
