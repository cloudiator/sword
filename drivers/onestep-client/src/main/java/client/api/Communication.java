package client.api;

import client.RequestFactory;
import client.OneStepResponse;
import client.model.ImageTemplatesResponse;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

@Slf4j
public class Communication {

    private OkHttpClient okHttpClient;
    private JSON json;

    public Communication() {
        okHttpClient = new OkHttpClient();
        json = new JSON();
    }

    public OneStepResponse<ImageTemplatesResponse> getImageTemplates() throws IOException {
        Request request = RequestFactory.createImageTemplateRequest();
        Type returnType = new TypeToken<ImageTemplatesResponse>() {
        }.getType();
        Call call = okHttpClient.newCall(request);

        Response response = call.execute();
        ImageTemplatesResponse body = handleResponse(response, returnType);
        return new OneStepResponse<>(response.code(), response.headers().toMultimap(), body);
    }

    public <T> T handleResponse(Response response, Type returnType) throws IOException {
        if (response.isSuccessful()) {
            if (returnType == null || response.code() == 204) {
                if (response.body() != null) {
                    response.body().close();
                }
                log.info("Aborting message body");
                return null;
            } else {
                return deserialize(response, returnType);
            }
        } else {
            String exceptionText = "status code = " + response.code();

            if (response.body() != null) {
                exceptionText += " body = " + response.body().string();
            }

            throw new IOException("Received wrong message from server: " + exceptionText);
        }
    }

    public <T> T deserialize(Response response, Type returnType) throws IOException {
        if (response == null || returnType == null) {
            return null;
        }

        if ("byte[]".equals(returnType.toString())) {
            // Handle binary response (byte array).
            return (T) response.body().bytes();
        }

        String respBody;
        if (response.body() != null)
            respBody = response.body().string();
        else
            respBody = null;


        if (respBody == null || "".equals(respBody)) {
            return null;
        }

        String contentType = response.headers().get("Content-Type");
        if (contentType == null) {
            contentType = "application/json";
        }
        if (isJsonMime(contentType)) {
            return json.deserialize(respBody, returnType);
        } else if (returnType.equals(String.class)) {
            // Expecting string, return the raw response body.
            return (T) respBody;
        } else {
            throw new ApiException(
                    "Content type \"" + contentType + "\" is not supported for type: " + returnType,
                    response.code(),
                    response.headers().toMultimap(),
                    respBody);
        }
    }
}
