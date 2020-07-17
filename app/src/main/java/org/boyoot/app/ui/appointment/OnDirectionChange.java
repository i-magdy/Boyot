package org.boyoot.app.ui.appointment;

import org.boyoot.app.model.job.Directions;

public interface OnDirectionChange {

     void onDirectionChanged(Directions directions);
     void setDestinationAndOriginalMarks(double oLat,double oLng,double lat,double lng);
}
