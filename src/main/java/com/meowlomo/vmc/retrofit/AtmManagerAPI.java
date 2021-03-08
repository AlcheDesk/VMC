package com.meowlomo.vmc.retrofit;

import com.meowlomo.vmc.model.File;
import com.meowlomo.vmc.model.InstructionResult;
import com.meowlomo.vmc.model.MeowlomoResponse;
import com.meowlomo.vmc.model.Run;
import com.meowlomo.vmc.model.StepLog;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AtmManagerAPI {
    @POST("api/testCases/{testCaseId}/runs")
    Call<MeowlomoResponse> addRunToTestCase(@Path("testCaseId") long testCaseId, @Body Run run);

    @POST("api/runs/{runId}/instructionResults")
    Call<MeowlomoResponse> addInstructionResultToRun(@Path("uuid") String uuid);

    @POST("api/instructionResults/{instructionResultId}/stepLog")
    Call<MeowlomoResponse> addStepLogToInstructionResult(@Path("instructionResultId") long instructionResultId,
            @Body StepLog stepLog);

    @POST("api/instructionResults/{instructionResultId}/files")
    Call<MeowlomoResponse> addFile(@Path("instrcutionResultId") long instrcutionResultId, @Body File file);

    @PATCH("api/runs")
    Call<MeowlomoResponse> finishRuns(@Body Run[] run);
    
    @PATCH("api/runs/{runId}")
    Call<MeowlomoResponse> updateRun(@Path("runId") long runId, @Body Run run);

    @PATCH("api/instructionResults/{instructionResultId}")
    Call<MeowlomoResponse> finishInstructionResult(@Path("instructionResultId") long instructionResultId,
            @Body InstructionResult instructionResult);
    
    @GET("api/elementTypes/driverTypes")
    Call<MeowlomoResponse> getElementDriverMap();
}
