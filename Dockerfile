FROM library/eclipse-temurin:17.0.10_7-jdk-alpine AS builder

RUN jlink --add-modules java.base,java.desktop,java.instrument,java.naming,java.scripting,java.security.jgss,java.sql,java.sql.rowset,java.xml,jdk.crypto.ec,jdk.jdwp.agent,jdk.jfr,jdk.management,jdk.management.agent,jdk.management.jfr,jdk.unsupported \
          --strip-debug \
          --output /javaruntime

WORKDIR /layers

COPY target/lhv*.jar lhv.jar

RUN java -Djarmode=layertools -jar lhv.jar extract

FROM library/alpine:3.19.1

COPY --from=builder /javaruntime/ /opt/java/openjdk/

RUN ln -s /opt/java/openjdk/bin/java /usr/local/bin/java && \
    addgroup -S lhv && \
    adduser -S lhv -G lhv && \
    mkdir /var/log/lhv && \
    chown lhv: /var/log/lhv && \
    chmod u+w /var/log/lhv

WORKDIR /app

COPY --from=builder layers/dependencies/ ./
COPY --from=builder layers/snapshot-dependencies/ ./
COPY --from=builder layers/spring-boot-loader/ ./
COPY --from=builder layers/application/ ./

ENV LOG_BASE_PATH=/var/log/lhv/

USER lhv

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} org.springframework.boot.loader.JarLauncher"]
