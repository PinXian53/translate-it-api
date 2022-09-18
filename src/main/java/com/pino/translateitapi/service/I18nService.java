package com.pino.translateitapi.service;

import com.pino.translateitapi.constant.I18nTypeEnum;
import com.pino.translateitapi.dao.TranslationKeyRepository;
import com.pino.translateitapi.exception.BadRequestException;
import com.pino.translateitapi.exception.InternalServerErrorException;
import com.pino.translateitapi.manager.ProjectManager;
import com.pino.translateitapi.model.dto.KeyValue;
import com.pino.translateitapi.util.FileSystemUtils;
import com.pino.translateitapi.util.YamlUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class I18nService {

    private final String tmpFolder = System.getProperty("java.io.tmpdir");
    private static final int DEFAULT_INDENT_FACTOR = 0;
    private static final int PRETTY_INDENT_FACTOR = 4;

    private final ProjectManager projectManager;
    private final TranslationService translationService;

    private final TranslationKeyRepository translationKeyRepository;

    public String exportI18n(int projectOid, String languageCode, I18nTypeEnum i18nType, boolean pretty) {
        projectManager.validProjectOid(projectOid);
        translationService.validProjectNeedExistLanguageCode(projectOid, languageCode);
        switch (i18nType) {
            case JSON -> {
                return exportJson(projectOid, languageCode, pretty);
            }
            case YAML -> {
                return exportYaml(projectOid, languageCode);
            }
            case PROPERTIES -> {
                return exportProperties(projectOid, languageCode);
            }
            default -> throw new BadRequestException("尚未支援");
        }
    }

    public void exportI18nFile(
        int projectOid,
        String languageCode,
        I18nTypeEnum i18nType,
        boolean pretty,
        HttpServletResponse response) {

        String content = exportI18n(projectOid, languageCode, i18nType, pretty);
        Path filePath = Paths.get(tmpFolder, UUID.randomUUID() + i18nType.getExtension());
        writeToFile(content, filePath);

        try (FileInputStream fis = new FileInputStream(filePath.toFile())) {
            String exportFileName = languageCode + i18nType.getExtension();
            response.setContentType("application/octet-stream");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, getAttachmentContentDisposition(exportFileName));
            IOUtils.copy(fis, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new InternalServerErrorException("下載失敗，請通知相關人員協助");
        } finally {
            FileSystemUtils.safeDeleteFile(filePath.toFile());
        }
    }

    private static void writeToFile(String content, Path filePath) {
        try {
            Files.writeString(filePath, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new InternalServerErrorException("WriteToFile error", e);
        }
    }

    private String exportJson(int projectOid, String languageCode, boolean pretty) {
        JSONObject jsonObject = new JSONObject();
        getI18nKeyValue(projectOid, languageCode).forEach(keyValue ->
            jsonObject.put(keyValue.getKey(), keyValue.getValue())
        );
        return convertToJsonString(jsonObject, pretty);
    }

    private String exportYaml(int projectOid, String languageCode) {
        String jsonString = exportJson(projectOid, languageCode, false);
        try {
            return YamlUtils.toYaml(jsonString);
        } catch (Exception e) {
            throw new InternalServerErrorException("轉換成Yaml失敗(projectOid:%s)".formatted(projectOid), e);
        }
    }

    private String exportProperties(int projectOid, String languageCode) {
        StringBuilder stringBuilder = new StringBuilder();
        getI18nKeyValue(projectOid, languageCode).forEach(keyValue ->
            stringBuilder.append(keyValue.getKey()).append("=").append(keyValue.getValue()).append("\n")
        );
        return stringBuilder.toString();
    }

    private List<KeyValue> getI18nKeyValue(int projectOid, String languageCode) {
        return translationKeyRepository.getI18nKeyValue(projectOid, languageCode);
    }

    private String convertToJsonString(JSONObject jsonObject, boolean pretty) {
        return jsonObject.toString(pretty ? PRETTY_INDENT_FACTOR : DEFAULT_INDENT_FACTOR);
    }

    private String getAttachmentContentDisposition(String filename) {
        return "attachment;" + (StringUtils.isEmpty(filename) ? "" : "filename=\"" + filename + "\"");
    }
}
