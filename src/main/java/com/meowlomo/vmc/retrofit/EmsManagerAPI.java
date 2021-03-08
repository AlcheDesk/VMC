package com.meowlomo.vmc.retrofit;

import com.meowlomo.vmc.model.MeowlomoResponse;
import com.meowlomo.vmc.model.Task;
import com.meowlomo.vmc.model.Worker;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EmsManagerAPI {
	
	@POST("api/workers")
	Call<MeowlomoResponse> registerWorkerToManager(@Body Worker[] workers);
	
	//TODO
	@PATCH("api/workers/{uuid}")
	Call<MeowlomoResponse>  updateWorkerToManager(@Path("uuid") String uuid, @Body Worker worker);
	
	@GET("api/workers/{uuid}")
	Call<MeowlomoResponse>  getWorkerFromManager(@Path("uuid") String uuid);
	
	@GET("api/tasks/{uuid}")
	Call<MeowlomoResponse>  sendTaskStartToManager(@Path("uuid") String uuid);
	
	@PATCH("api/tasks")
	Call<MeowlomoResponse>  updateTaskToManager(@Body Task[] task);
	
	@GET("api/groups")
	Call<MeowlomoResponse>  getGroup();
	
	@GET("api/statuses")
	Call<MeowlomoResponse>  getStatus();
	
	@GET("api/operatingSystems")
	Call<MeowlomoResponse>  getOperatingSystem();
	
	@GET("api/jobTypes")
	Call<MeowlomoResponse>  getJobType();
	
	@GET("api/taskTypes")
	Call<MeowlomoResponse>  getTaskType();
	   
    @GET("api/info/client")
    Call<MeowlomoResponse> getWorkerSelfIP();
}
