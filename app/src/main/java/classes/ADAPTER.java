package classes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ebada.vaccinationtracking.EditKidActivity;
import com.example.ebada.vaccinationtracking.R;
import com.example.ebada.vaccinationtracking.ShowKidHistory;
import com.example.ebada.vaccinationtracking.ShowvaccinationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import custom_font.MyTextView;

/**
 * Created by ebada on 03/03/2017.
 */
//URL TO CHANGE
public class ADAPTER  extends BaseAdapter{

    Context context;
    ArrayList<Kids> kids ;
    public ADAPTER (Context context ,ArrayList<Kids> kids)
    {
        this.context = context;
        this.kids = kids;
    }
    @Override
    public int getCount() {
        return kids.size();
    }

    @Override
    public Object getItem(int position) {
        return kids.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(R.layout.show_kids_listview,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(kids.get(position).getName());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,EditKidActivity.class);
                intent.putExtra("kid",kids.get(position));
                context.startActivity(intent);
            }
        });
        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ShowKidHistory.class);
                intent.putExtra("kidId",kids.get(position).getID());
                context.startActivity(intent);

            }

        });
        holder.vaccination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ShowvaccinationActivity.class);
                intent.putExtra("kidId",kids.get(position).getID());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static  class ViewHolder
    {
        TextView name ;
        LinearLayout show,edit,vaccination;
        public ViewHolder(View view)
        {
            name = (TextView) view.findViewById(R.id.Fname);
            show = (LinearLayout) view.findViewById(R.id.hostory);
            edit = (LinearLayout) view.findViewById(R.id.Sedit);
            vaccination = (LinearLayout) view.findViewById(R.id.vaccination);
        }
    }
}
