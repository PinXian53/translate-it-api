package com.pino.translateitapi.dao;

import com.pino.translateitapi.model.dto.Corpus;
import com.pino.translateitapi.model.entiry.CorpusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CorpusRepository extends JpaRepository<CorpusEntity, Integer> {

    @Query("select new com.pino.translateitapi.model.dto.Corpus( " +
        " corpus.oid, corpusKey.key, sourceCorpus.translation, corpus.translation) " +
        " from CorpusKeyEntity corpusKey " +
        " left join CorpusEntity corpus " +
        " on corpusKey.oid = corpus.corpusKeyOid and corpus.languageCode = :languageCode " +
        " left join CorpusEntity sourceCorpus " +
        " on corpusKey.oid = sourceCorpus.corpusKeyOid and sourceCorpus.languageCode = :sourceLanguageCode " +
        " where corpusKey.projectOid = :projectOid ")
    List<Corpus> findByLanguageCode(Integer projectOid, String languageCode, String sourceLanguageCode);
}
