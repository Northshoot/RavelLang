package edu.stanford.ravel.controller;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import edu.stanford.ledcontrol.model.LedStatusRepresentation;
import edu.stanford.ravel.defines.RavelDefines;
import edu.stanford.ravel.model.Registration;
import edu.stanford.ravel.model.RemoteServerResponse;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;


public class RemoteServerController {


    private static RemoteServer remoteServer;

    public static RemoteServer getServer(){
        if (remoteServer == null) {
            remoteServer = null;
            OkHttpClient client = new OkHttpClient();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.interceptors().add(interceptor);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RavelDefines.API_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            remoteServer = retrofit.create(RemoteServer.class);
        }
        return remoteServer;
    }
    public class RemoteResult {

        private String status;


        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        public void setResults(String results) {
            this.status = results;
        }
    }

    public interface RemoteServer {
        @POST("/led/add_status/")
        @Headers({"Content-Type: application/json"})
        Call<RemoteServerResponse.RemoteResult> add_status(@Body LedStatusRepresentation status);


        @POST("/gcm/v1/device/register/")
        @Headers({"Content-Type: application/json"})
        Call<RemoteServerResponse.RemoteResult>  register(@Body Registration registration);
    }

    //"Content-Type: application/json" -d '{"dev_id": "test1", "reg_id":"fPPse59La3Y:APA91bHLe8tZ6lTSidR5lzZ7avMOcDSrXkj_y81jIqQ3d4t3s33gbqGwKrMmsDbyE2pcKGSYX-Mufdx8d14eQLJBwJAeOP7_s0lw7xdCdAM4CFZwlCySdTnGX5EqLnT1Q0D8HDUcBPqU", "name":"test device"}'


}
