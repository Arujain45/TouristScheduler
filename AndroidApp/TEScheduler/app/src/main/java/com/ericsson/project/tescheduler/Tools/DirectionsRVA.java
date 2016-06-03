package com.ericsson.project.tescheduler.Tools;

import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ericsson.project.tescheduler.Objects.AppData;
import com.ericsson.project.tescheduler.R;

public class DirectionsRVA extends RecyclerView.Adapter<DirectionsRVA.DirectionsViewHolder> {
    public static final int VIEWTYPE_INDEX_SEGMENT = 0;
    public static final int VIEWTYPE_INDEX_POI = 1;
    public static final int MOBILITY_INDEX_PUBLIC = 0;
    public static final int MOBILITY_INDEX_CAR = 1;
    public static final int MOBILITY_INDEX_WALKING = 2;
    public static final int MOBILITY_INDEX_BIKE = 3;
    Resources resources;

    public class DirectionsViewHolder extends RecyclerView.ViewHolder{
        ImageView iconMobility;
        TextView textDescription;
        TextView textTime;

        DirectionsViewHolder(final View itemView){
            super(itemView);
            iconMobility = (ImageView) itemView.findViewById(R.id.directions_segmentmobility);
            textDescription = (TextView) itemView.findViewById(R.id.directions_description);
            textTime = (TextView) itemView.findViewById(R.id.directions_time);
        }
    }

    public int getItemViewType(int position) {
        if(AppData.getResultRoute().getSegmentAt(position).isPoISegment()) {
            return VIEWTYPE_INDEX_POI;
        }
        else {
            return VIEWTYPE_INDEX_SEGMENT;
        }
    }

    @Override
    public DirectionsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        resources = viewGroup.getResources();
        if(viewType == VIEWTYPE_INDEX_SEGMENT){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.element_directions_routesegment, viewGroup, false);
        }
        else{
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.element_directions_poi, viewGroup, false);
        }
        return new DirectionsViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return AppData.getResultRoute().getSegmentToPoI().size();
    }

    @Override
    public void onBindViewHolder(final DirectionsViewHolder directionsVH, final int i) {
        if(directionsVH.getItemViewType() == VIEWTYPE_INDEX_POI){
            directionsVH.textDescription.setText(AppData.getResultRoute().getSegmentAt(i)
                    .getDirection());
            directionsVH.textTime.setText(AppData.getResultRoute().getSegmentAt(i)
                    .getFormattedVisitTime());
        }
        else if (directionsVH.getItemViewType() == VIEWTYPE_INDEX_SEGMENT) {
            directionsVH.textTime.setText(AppData.getResultRoute().getSegmentAt(i).getFormattedDuration());
            String mobility = AppData.getResultRoute().getSegmentAt(i).getMobility();

            if (mobility.equals(resources.getStringArray(R.array.parameters_mobility_options)[MOBILITY_INDEX_PUBLIC])) {
                directionsVH.iconMobility.setImageDrawable(ContextCompat.
                        getDrawable(directionsVH.itemView.getContext(),
                                R.drawable.ic_directions_subway_light_blue_a700_24dp));
                directionsVH.textDescription.setText(resources.getString(
                        R.string.mobility_description_public,
                        AppData.getResultRoute().getSegmentAt(i).getLine(),
                        AppData.getResultRoute().getSegmentAt(i).getStationEnd()));
            }
            else if (mobility.equals(resources.getStringArray(R.array.parameters_mobility_options)[MOBILITY_INDEX_CAR])) {
                directionsVH.iconMobility.setImageDrawable(ContextCompat.
                        getDrawable(directionsVH.itemView.getContext(),
                                R.drawable.ic_directions_car_light_blue_a700_24dp));
                directionsVH.textDescription.setText(resources.getString(
                        R.string.mobility_description_car,
                        AppData.getResultRoute().getSegmentAt(i).getStationEnd()));
            }
            else if (mobility.equals(resources.getStringArray(R.array.parameters_mobility_options)[MOBILITY_INDEX_WALKING])) {
                directionsVH.iconMobility.setImageDrawable(ContextCompat.
                        getDrawable(directionsVH.itemView.getContext(),
                                R.drawable.ic_directions_walk_light_blue_a700_24dp));
                directionsVH.textDescription.setText(resources.getString(
                        R.string.mobility_description_walking,
                        AppData.getResultRoute().getSegmentAt(i).getStationEnd()));
            }
            else if (mobility.equals(resources.getStringArray(R.array.parameters_mobility_options)[MOBILITY_INDEX_BIKE])) {
                directionsVH.textDescription.setText(resources.getString(
                        R.string.mobility_description_bike,
                        AppData.getResultRoute().getSegmentAt(i).getStationEnd()));
            }

        }

    }
}
