package client.api;

import client.*;
import client.model.template_response.ApiCollectionTemplate;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//this class is from octawave client, adapted to one step cloud
public class TemplatesApi {
    private ApiClient apiClient;

    public TemplatesApi() {
        this(Configuration.getDefaultApiClient());
    }

    public TemplatesApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }


    public com.squareup.okhttp.Call templatesGetCall(int workspace, int region) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "workspaces/" + workspace +"/regions/" + region + "/virtual_machines/new";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
                "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {

        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[]{"oauth2"};
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, null);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call templatesGetValidateBeforeCall(int workspace, int region) throws ApiException {

        com.squareup.okhttp.Call call = templatesGetCall(workspace, region);
        return call;

    }

    /**
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiCollectionTemplate templatesGet(int workspace, int region) throws ApiException {
        ApiResponse<ApiCollectionTemplate> resp = imageTemplatesWithHttpInfo(workspace, region);
        return resp.getData();
    }

    /**
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ApiCollectionTemplate> imageTemplatesWithHttpInfo(int workspace, int region) throws ApiException {
        com.squareup.okhttp.Call call = templatesGetValidateBeforeCall(workspace, region);
        Type localVarReturnType = new TypeToken<ApiCollectionTemplate>() {
        }.getType();
        return apiClient.execute(call, localVarReturnType);
    }
}
