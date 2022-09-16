package com.pino.translateitapi.manager;

import com.pino.translateitapi.dao.TranslationKeyRepository;
import com.pino.translateitapi.dao.TranslationRepository;
import com.pino.translateitapi.model.entity.TranslationKeyEntity;
import com.pino.translateitapi.util.BatchUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TranslationManager {

    private final TranslationRepository translationRepository;
    private final TranslationKeyRepository translationKeyRepository;

    @Transactional
    public void deleteByProjectOidAndLanguageCode(int projectOid, final String languageCode) {
        List<Integer> translationKeyOidList = findTranslationKeyOidByProjectOid(projectOid);
        BatchUtils.subBatchIterator(translationKeyOidList, 500,
            subOidList -> translationRepository.deleteByTranslationKeyOidInAndLanguageCode(subOidList, languageCode));
    }

    public List<Integer> findTranslationKeyOidByProjectOid(int projectOid) {
        return translationKeyRepository.findByProjectOid(projectOid)
            .stream()
            .map(TranslationKeyEntity::getOid)
            .toList();
    }
}
