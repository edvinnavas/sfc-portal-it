# FETCH BASIC IMAGE.
FROM maven:3.8.5-jdk-11

# APPLICATION PLACED INTO /opt/app.
RUN mkdir -p /SFC_PORTAL_IT/py
RUN chmod -R 777 /SFC_PORTAL_IT/py
RUN mkdir -p /SFC_PORTAL_IT/pd
RUN chmod -R 777 /SFC_PORTAL_IT/pd
RUN mkdir -p /opt/app
RUN echo "America/Guatemala" > /etc/timezone
WORKDIR /opt/app

# SELECTIVELY ADD THE POM FILE AND INSTALL DEPENDENCIES.
COPY src /opt/app/src
RUN chmod -R 777 /opt/app/src

COPY pom.xml /opt/app/

RUN mvn install
RUN mvn package

# LOCAL APPLICATION PORT
EXPOSE 8080

# EXECUTE IT
CMD ["mvn", "exec:java"]
