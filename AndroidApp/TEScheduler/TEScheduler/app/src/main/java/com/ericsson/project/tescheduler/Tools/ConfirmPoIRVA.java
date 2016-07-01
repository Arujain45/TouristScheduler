package com.ericsson.project.tescheduler.Tools;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.ericsson.project.tescheduler.Objects.AppData;
import com.ericsson.project.tescheduler.Objects.PointOfInterest;
import com.ericsson.project.tescheduler.R;
import com.ericsson.project.tescheduler.Objects.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfirmPoIRVA extends RecyclerView.Adapter<ConfirmPoIRVA.ConfirmPoIViewHolder>
        implements AdapterView.OnItemSelectedListener{
    private List<PointOfInterest> pointsOfInterest;
    private List<PointOfInterest> cancelledPoIs;

    public class ConfirmPoIViewHolder extends RecyclerView.ViewHolder{
        CardView cardPI;
        TextView textPIName;
        Spinner spinnerVisitTime;
        ImageButton buttonCancel;
        TextView textUndo;

        ConfirmPoIViewHolder(final View itemView){
            super(itemView);
            cardPI = (CardView) itemView.findViewById(R.id.confirmpoi_cardview);
            textPIName = (TextView) itemView.findViewById(R.id.element_piname);
            spinnerVisitTime = (Spinner) itemView.findViewById(R.id.element_visitduration);
            buttonCancel = (ImageButton) itemView.findViewById(R.id.element_buttoncancel);
            textUndo = (TextView) itemView.findViewById(R.id.text_undo);
        }
    }

    public ConfirmPoIRVA(ArrayList<PointOfInterest> cancelledPoIs){
        this.pointsOfInterest = Request.getSelectedPoIs();
        this.cancelledPoIs = cancelledPoIs;
    }

    @Override
    public ConfirmPoIViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.element_confirm_poi, viewGroup, false);
        return new ConfirmPoIViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return pointsOfInterest.size();
    }

    @Override
    public void onBindViewHolder(final ConfirmPoIViewHolder confirmPoIVH, final int pos) {
        final int i = confirmPoIVH.getAdapterPosition();
        confirmPoIVH.textPIName.setText(pointsOfInterest.get(i).getName());

        final ArrayAdapter<CharSequence> adapterMobility;
        CharSequence[] itemArray = confirmPoIVH.itemView.getResources()
                .getStringArray(R.array.confirmpi_durations);
        List<CharSequence> itemList = new ArrayList<>(Arrays.asList(itemArray));
        itemList.add(0, Integer.toString(pointsOfInterest.get(i).getVisitTimeInMinutes()) + "min (rec'd)");
        adapterMobility = new ArrayAdapter<>(confirmPoIVH.itemView.getContext(),
                android.R.layout.simple_spinner_item, itemList);
        adapterMobility.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        confirmPoIVH.spinnerVisitTime.setAdapter(adapterMobility);
        confirmPoIVH.spinnerVisitTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                int visitTimeValue = -1;
                String selectedString = adapterView.getSelectedItem().toString();

                //Convert the select visit time to minutes (unit used in the system)
                if(selectedString.endsWith("(rec'd)")){
                    visitTimeValue = AppData.getVisitTimeFromPoI(Request.getSelectedPoIs().get(i));
                }
                else if(selectedString.endsWith("hour")){
                    visitTimeValue = 60;
                }
                else if(selectedString.endsWith("minutes")){
                    visitTimeValue = Integer.valueOf(selectedString.replace(" minutes", ""));
                }
                else if(selectedString.endsWith("hours")){
                    if(selectedString.charAt(1) == ':') {
                        int hours = Integer.valueOf(selectedString.substring(0, 1));
                        int minutes = Integer.valueOf(selectedString.substring(2, 4));
                        visitTimeValue = 60 * hours + minutes;
                    }
                    else {
                        visitTimeValue = 60*Integer.valueOf(selectedString.substring(0, 1));
                    }
                }

                Request.getSelectedPoIs().get(i).setVisitTimeInMinutes(visitTimeValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        confirmPoIVH.buttonCancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(confirmPoIVH.cardPI.getVisibility() == View.VISIBLE){
                    confirmPoIVH.cardPI.setVisibility(View.GONE);
                    cancelledPoIs.add(pointsOfInterest.get(i));
                    confirmPoIVH.textUndo.setVisibility(View.VISIBLE);
                }
            }
        });

        confirmPoIVH.textUndo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(confirmPoIVH.cardPI.getVisibility() == View.GONE){
                    confirmPoIVH.textUndo.setVisibility(View.GONE);
                    cancelledPoIs.remove(pointsOfInterest.get(i));
                    confirmPoIVH.cardPI.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {    }

    public void onNothingSelected(AdapterView<?> parent) {    }
}
