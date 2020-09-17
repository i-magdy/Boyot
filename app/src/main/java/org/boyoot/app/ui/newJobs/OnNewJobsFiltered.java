package org.boyoot.app.ui.newJobs;

import org.boyoot.app.database.Jobs;

import java.util.List;

public interface OnNewJobsFiltered {


    void onJobsFiltered(List<Jobs> jobs);
    void onResetFilterSelect(List<Jobs> jobs);
}
