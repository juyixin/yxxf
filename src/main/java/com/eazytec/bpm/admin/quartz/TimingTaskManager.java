package com.eazytec.bpm.admin.quartz;

import java.util.List;

import com.eazytec.bpm.model.TimingTask;
import com.eazytec.model.Role;



public interface TimingTaskManager  {
    /**
     * {@inheritDoc}
     */
    List<TimingTask> getTimingTask();

    /**
     * {@inheritDoc}
     */
    TimingTask getTimingtask();

    /**
     * {@inheritDoc}
     */
    TimingTask saveTimingtask(TimingTask timingtask);

    /**
     * {@inheritDoc}
     */
    void removeTimingtask(String timingtaskname);
}