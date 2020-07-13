package org.boyoot.app.ui.appointment;

import org.boyoot.app.model.job.Directions;

public interface OnDirectionChange {

    public void onDirectionChanged(Directions directions);
    public void onSetDestinationPoints(double lat,double lng);
}
