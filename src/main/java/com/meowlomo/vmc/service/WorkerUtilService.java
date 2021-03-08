package com.meowlomo.vmc.service;

import com.meowlomo.vmc.model.Task;

public interface WorkerUtilService {

    boolean prepareToKillCurrentTask(Task task);
    
    boolean checkToken(String tokenString);
    
}
