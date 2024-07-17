# Docker 镜像构建
# @author 薛宇彤
# @from 1604899092
FROM openjdk:17

# Copy local code to the container image.
WORKDIR /app

COPY target/article-springboot3-0.0.1-SNAPSHOT.jar article-springboot3-0.0.1-SNAPSHOT.jar

   # 暴露应用端口
EXPOSE 8080

# Run the web service on container startup.
ENTRYPOINT ["java","-jar","article-springboot3-0.0.1-SNAPSHOT.jar"]