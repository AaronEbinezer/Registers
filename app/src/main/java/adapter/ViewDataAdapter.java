package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import lucasnetwork.brainmagic.com.register.R;
import lucasnetwork.brainmagic.com.register.Users;

public class ViewDataAdapter extends ArrayAdapter {
    private Context context;
    private List<Users> list;
    public ViewDataAdapter(Context context, List<Users> list) {
        super(context, R.layout.viewdatadapter);
        this.context =context;
        this.list=list;
    }

    @SuppressLint("WrongConstant")
    public View getView(final int position,  View convertView, ViewGroup parent) {
        GetData holder;
        holder = new GetData();
        convertView = null;
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.viewdatadapter, null);
            holder.tex1 = (TextView) convertView.findViewById(R.id.nameadap);
            holder.tex2 = (TextView) convertView.findViewById(R.id.mobileadap);
            holder.tex3 = (TextView) convertView.findViewById(R.id.emailadap);

            holder.tex1.setText(list.get(position).getName());
            holder.tex2.setText(list.get(position).getPhonenumber());
            holder.tex3.setText(list.get(position).getEmail());
            convertView.setTag(holder);
        } else {
            holder = (GetData) convertView.getTag();
        }

        return convertView;
        }



    private class GetData{
        public TextView tex1;
        public TextView tex2;
        public TextView tex3;

    }

    @Override
    public int getCount() {
        return list.size();
    }
}
