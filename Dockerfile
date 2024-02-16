FROM library/eclipse-temurin:17.0.10_7-jdk-alpine AS builder

RUN jlink --add-modules java.base,java.desktop,java.instrument,java.naming,java.scripting,java.security.jgss,java.sql,java.sql.rowset,java.xml,jdk.crypto.ec,jdk.jdwp.agent,jdk.jfr,jdk.management,jdk.management.agent,jdk.management.jfr,jdk.unsupported \
          --strip-debug \
          --output /javaruntime

FROM library/alpine:3.19.1

COPY --from=builder /javaruntime/ /opt/java/openjdk/

RUN ln -s /opt/java/openjdk/bin/java /usr/local/bin/java && \
    mkdir /app && \
    addgroup -S lhv && \
    adduser -S lhv -G lhv && \
    mkdir /var/log/lhv && \
    chown lhv: /var/log/lhv && \
    chmod u+w /var/log/lhv

WORKDIR /app

COPY target/lhv*.jar lhv.jar

USER lhv

ENV LOG_BASE_PATH=/var/log/lhv/

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar lhv.jar"]
