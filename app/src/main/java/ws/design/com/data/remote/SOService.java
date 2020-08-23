package ws.design.com.data.remote;


import retrofit2.Call;
import retrofit2.http.GET;
import ws.design.com.data.model.SOAnswersResponse;

/**
 * Created by Chike on 12/3/2016.
 */

public interface SOService {

    @GET("graphs")
    Call<SOAnswersResponse> getAnswers();

//    // RxJava
//    // Observable<SOAnswersResponse> getAnswers();
//
//    @GET("/answers?order=desc&sort=activity&site=stackoverflow")
//    Call<List<SOAnswersResponse>> getAnswers(@Query("tagged") String tags);

}
