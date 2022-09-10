package com.pino.translateitapi.service;

import com.pino.translateitapi.exception.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AzureTranslationService implements OnlineTranslation {
    @Value("${microsoft.azure.translate.key}")
    private String subscriptionKey;
    @Value("${microsoft.azure.translate.location}")
    private String location;

    private static final OkHttpClient client = new OkHttpClient();
    private static final String APPLICATION_JSON = "application/json";
    private static final MediaType mediaType = MediaType.parse(APPLICATION_JSON);

    private final LanguageService languageService;

    public String translate(String content, String toLanguageCode) {
        return this.translate(content, null, toLanguageCode);
    }

    public String translate(String content, String fromLanguageCode, String toLanguageCode) {
        try {
            HttpUrl.Builder urlBuilder = new HttpUrl.Builder()
                .scheme("https")
                .host("api.cognitive.microsofttranslator.com")
                .addPathSegment("/translate")
                .addQueryParameter("api-version", "3.0")
                .addQueryParameter("to", getAzureCode(toLanguageCode));

            if (!StringUtils.isEmpty(fromLanguageCode)) {
                urlBuilder.addQueryParameter("from", getAzureCode(fromLanguageCode));
            }

            RequestBody body = RequestBody.create(getTranslateRequestBody(content), mediaType);
            Request request = new Request.Builder().url(urlBuilder.build()).post(body)
                .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                .addHeader("Ocp-Apim-Subscription-Region", location)
                .addHeader("Content-type", APPLICATION_JSON)
                .build();
            try (Response response = client.newCall(request).execute()) {
                int statusCode = response.code();
                String responseBody = response.body().string();
                if (statusCode != 200) {
                    String message = response.message();
                    String msg = "Status Code: %s, message： %s, Response Body: %s".formatted(
                        statusCode, message, responseBody);
                    throw new InternalServerErrorException(msg);
                }
                return getTranslateResult(responseBody);
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("Azure translate error", e);
        }

    }

    private String getAzureCode(String languageCode) {
        return languageService.getAzureCodeCache(languageCode);
    }

    private String getTranslateRequestBody(String content) throws JSONException {
        return new JSONArray().put(new JSONObject().put("text", content)).toString();
    }

    private String getTranslateResult(String responseBody) throws JSONException {
        return new JSONArray(responseBody)
            .getJSONObject(0)
            .getJSONArray("translations")
            .getJSONObject(0)
            .getString("text");
    }

}
