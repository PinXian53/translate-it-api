package com.pino.translateitapi.service;

import com.pino.translateitapi.dao.LanguageRepository;
import com.pino.translateitapi.exception.BadRequestException;
import com.pino.translateitapi.exception.InternalServerErrorException;
import com.pino.translateitapi.model.dto.Language;
import com.pino.translateitapi.util.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class LanguageService {
    private final LanguageRepository languageRepository;

    private final List<Language> languageList = new ArrayList<>();
    private final List<String> languageCodeCache = new ArrayList<>();
    // Map<languageCode, azureCode>
    private final Map<String, String> azureCodeCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void postConstruct() {
        initCodeCache();
    }

    private void initCodeCache() {
        // 清除 Cache
        languageCodeCache.clear();
        azureCodeCache.clear();
        // 載入 Cache
        languageRepository.findAll().forEach(languageEntity -> {
            String languageCode = languageEntity.getCode();
            String azureCode = languageEntity.getAzureCode();
            languageList.add(ModelMapperUtils.map(languageEntity, Language.class));
            languageCodeCache.add(languageCode);
            azureCodeCache.put(languageCode, azureCode);
        });
    }

    public List<Language> getAllLanguage() {
        return languageList;
    }

    public void validLanguageCode(String languageCode) {
        if (!isCorrectLanguageCode(languageCode)) {
            throw new BadRequestException("無法識別的語系資料");
        }
    }

    public boolean isCorrectLanguageCode(String languageCode) {
        return languageCodeCache.contains(languageCode);
    }

    public String getDescription(String languageCode) {
        return languageList.stream()
            .filter(o -> o.getCode().equals(languageCode))
            .findFirst()
            .orElseThrow(() -> new InternalServerErrorException("無法識別的 Language Code：" + languageCode))
            .getDescription();
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
