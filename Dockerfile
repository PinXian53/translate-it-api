FROM tomcat:8.5.82-jre17-temurin-jammy
# 因為要使用 ROOT.war 所以要先刪除原先存在的 ROOT 資料夾
RUN ["rm", "-fr", "/usr/local/tomcat/webapps/ROOT"]
COPY target/translate-it-api-*.war webapps/ROOT.war
RUN sed -i 's/port="8080"/port="8080"/' /usr/local/tomcat/conf/server.xml
EXPOSE 8080
# start application
CMD ["catalina.sh","run"]