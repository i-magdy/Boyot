package org.boyoot.app.ui.googleSheet;

import org.boyoot.app.database.GoogleSheet;

import java.util.List;

public interface OnGoogleSheetFiltered {

    void onGoogleSheetFiltered(List<GoogleSheet> filteredSheetList);
}
