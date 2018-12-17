package com.eazytec.bpm.admin.quartz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.eazytec.bpm.admin.quartz.TimingTaskManager;
import com.eazytec.bpm.model.TimingTask;
import com.eazytec.model.Role;
import com.eazytec.service.impl.GenericManagerImpl;



@Service("timingtaskManager")
public class TimingTaskManagerImpl extends GenericManagerImpl<TimingTask, String> implements TimingTaskManager{


	/*
	public TimingTask saveTimingTask(TimingTask task ) {
		// TODO Auto-generated method stub
		    //return employeeDao.saveTimingTask(employee);
        
	}
*/
/*
	@Override
	public TimingTask getTimingTask(String employeeId) {
		// TODO Auto-generated method stub
		return null;
	}
*/

	@Override
	public List<TimingTask> getTimingTask() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public com.eazytec.bpm.model.TimingTask getTimingtask() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public com.eazytec.bpm.model.TimingTask saveTimingtask(
			com.eazytec.bpm.model.TimingTask timingtask) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void removeTimingtask(String timingtaskname) {
		// TODO Auto-generated method stub
		
	}
}