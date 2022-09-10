package com.pino.translateitapi.service;

import com.pino.translateitapi.dao.LanguageRepository;
import com.pino.translateitapi.exception.InternalServerErrorException;
import com.pino.translateitapi.model.dto.Language;
import com.pino.translateitapi.util.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class LanguageService {
    private final LanguageRepository languageRepository;

    // Map<languageCode, azureCode>
    private final Map<String, String> azureCodeCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void postConstruct() {
        initCodeCache();
    }

    private void initCodeCache() {
        // 清除 Cache
        azureCodeCache.clear();
        // 載入 Cache
        languageRepository.findAll().forEach(languageEntity -> {
            String languageCode = languageEntity.getCode();
            String azureCode = languageEntity.getAzureCode();
            azureCodeCache.put(languageCode, azureCode);
        });
    }

    public List<Language> getAllLanguage() {
        return ModelMapperUtils.mapList(languageRepository.findAll(), Language.class);
    }

    public String getAzureCodeCache(String languageCode) {
        String azureCode = azureCodeCache.getOrDefault(languageCode, null);
        if (azureCode == null) {
            azureCode = getAzureCode(languageCode);
            azureCodeCache.put(languageCode, azureCode);
        }
        return azureCode;
    }

    public String getAzureCode(String languageCode) {
        return languageRepository.findByCode(languageCode)
            .orElseThrow(() -> new InternalServerErrorException("無法識別的 Language Code：" + languageCode))
            .getCode();
    }
}
