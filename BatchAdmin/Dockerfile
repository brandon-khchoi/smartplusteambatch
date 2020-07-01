FROM java:8
VOLUME /tmp
ARG JAR_FILE
ADD target/BatchAdmin-0.0.1.jar app.jar
ADD http://corewatch.lunasoft.co.kr:9090/agent/pinpoint-agent-1.8.5.tar.gz pinpoint.tar.gz
RUN tar -zxvf pinpoint.tar.gz
ENV JAVA_OPTS=""
ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom ${JAVA_OPTS} -jar app.jar
