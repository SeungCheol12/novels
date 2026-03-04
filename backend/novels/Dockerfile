# 대소문자 구분함
# FROM / WORKDIR / COPY / EXPOSE / ENTRYPOINT (주 명령어)
# 자동완성됨
# 사용할 JDK 버전 지정
FROM eclipse-temurin:17-jdk   

# aws 서버 폴더명 지정
WORKDIR /app

# 필요 파일 복사(프로젝트 명으로 압축된 파일 app.jar 로 복사)
# WORKDIR 을 지정했기 때문에 /app/app.jar 로 복사됨
COPY app.jar app.jar

# port 지정
# EXPOSE 8080

# 실행 명령 정의(EC2 안에서 했던 java -jar app.jar)
ENTRYPOINT ["java","-jar","app.jar"]