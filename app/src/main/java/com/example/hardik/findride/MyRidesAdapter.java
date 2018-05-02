package com.example.hardik.findride;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyRidesAdapter extends RecyclerView.Adapter<MyRidesAdapter.RideViewHolder> {

    Context context;

    public MyRidesAdapter(Context context, List<MyRideDetails> myRideDetails) {
        this.context = context;
        this.myRideDetails = myRideDetails;
    }

    List<MyRideDetails> myRideDetails;
    @Override
    public RideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_riderow,parent,false);
        RideViewHolder rideViewHolder=new RideViewHolder(view);
        return rideViewHolder;
    }

    @Override
    public void onBindViewHolder(RideViewHolder holder, int position) {
        MyRideDetails myRides=myRideDetails.get(position);
        holder.startAdd.setText(myRides.getStartAddress());
        holder.endAdd.setText(myRides.getEndAddress());
        holder.startTime.setText(myRides.getStarTime());
        holder.typeOfRide.setText(myRides.getTor());
    }

    @Override
    public int getItemCount() {
        return myRideDetails.size();
    }

    class  RideViewHolder extends RecyclerView.ViewHolder{

        TextView startAdd,endAdd,startTime,typeOfRide;
        public RideViewHolder(View itemView) {
            super(itemView);

            startAdd= (TextView) itemView.findViewById(R.id.pickupAdd);
            endAdd= (TextView) itemView.findViewById(R.id.dropAdd);
            startTime= (TextView) itemView.findViewById(R.id.startDate);
            typeOfRide= (TextView) itemView.findViewById(R.id.tor);

        }
    }
}
