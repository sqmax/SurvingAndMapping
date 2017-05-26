package com.example.survingandmapping;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 */
public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder>{

    private List<Station> stationList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView stationId;
        TextView Jiao;
        TextView Spjl;
        View line;
        public ViewHolder(View view){
            super(view);
            stationId=(TextView)view.findViewById(R.id.stationId);
            Jiao=(TextView)view.findViewById(R.id.jiao);
            Spjl=(TextView)view.findViewById(R.id.spjl);
            line=(View)view.findViewById(R.id.myline);
        }
    }
    public StationAdapter(List<Station> stationList){
        this.stationList=stationList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.station_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(StationAdapter.ViewHolder holder, int position) {
        Station station=stationList.get(position);
        holder.stationId.setText("测站:"+station.getCeZhan());
        holder.Spjl.setText("水平距离："+station.getDis());
        holder.Jiao.setText("观测角："+station.getGuanCeJ().getDu()+"度"+station.getGuanCeJ().getFen()+"分"
                            +station.getGuanCeJ().getMiao()+"秒");
    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }
}
