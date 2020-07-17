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


    public com.squareup.okhttp.Call templatesGetCall(String source, String query, Integer pageSize, Integer pageNumber, String orderBy, String fields, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/templates";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (source != null)
            localVarQueryParams.addAll(apiClient.parameterToPair("source", source));
        if (query != null)
            localVarQueryParams.addAll(apiClient.parameterToPair("query", query));
        if (pageSize != null)
            localVarQueryParams.addAll(apiClient.parameterToPair("pageSize", pageSize));
        if (pageNumber != null)
            localVarQueryParams.addAll(apiClient.parameterToPair("pageNumber", pageNumber));
        if (orderBy != null)
            localVarQueryParams.addAll(apiClient.parameterToPair("orderBy", orderBy));
        if (fields != null)
            localVarQueryParams.addAll(apiClient.parameterToPair("fields", fields));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
                "application/json", "text/json", "application/xml", "text/xml"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {

        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if (progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                            .build();
                }
            });
        }

        String[] localVarAuthNames = new String[]{"oauth2"};
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call templatesGetValidateBeforeCall(String source, String query, Integer pageSize, Integer pageNumber, String orderBy, String fields, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {

        com.squareup.okhttp.Call call = templatesGetCall(source, query, pageSize, pageNumber, orderBy, fields, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Returns templates list
     * Acceptable order values are: Name, Version, creationDate, SystemCategory.
     *
     * @param source     Source (optional)
     * @param query      Query (optional)
     * @param pageSize   Page size (optional)
     * @param pageNumber Page number (optional)
     * @param orderBy    Order by (optional)
     * @param fields     Response fields filter (optional)
     * @return ApiCollectionTemplate
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiCollectionTemplate templatesGet(String source, String query, Integer pageSize, Integer pageNumber, String orderBy, String fields) throws ApiException {
        ApiResponse<ApiCollectionTemplate> resp = imageTemplatesWithHttpInfo(source, query, pageSize, pageNumber, orderBy, fields);
        return resp.getData();
    }

    /**
     * Returns templates list
     * Acceptable order values are: Name, Version, creationDate, SystemCategory.
     *
     * @param source     Source (optional)
     * @param query      Query (optional)
     * @param pageSize   Page size (optional)
     * @param pageNumber Page number (optional)
     * @param orderBy    Order by (optional)
     * @param fields     Response fields filter (optional)
     * @return ApiResponse&lt;ApiCollectionTemplate&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<ApiCollectionTemplate> imageTemplatesWithHttpInfo(String source, String query, Integer pageSize, Integer pageNumber, String orderBy, String fields) throws ApiException {
        com.squareup.okhttp.Call call = templatesGetValidateBeforeCall(source, query, pageSize, pageNumber, orderBy, fields, null, null);
        Type localVarReturnType = new TypeToken<ApiCollectionTemplate>() {
        }.getType();
        return apiClient.execute(call, localVarReturnType);
    }
}
