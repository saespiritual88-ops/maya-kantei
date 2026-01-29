# 1. 組み立て用の環境（Maven + JDK 17）
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# 依存関係をキャッシュ
COPY pom.xml .
RUN mvn dependency:go-offline

# ソースコードをコピーしてビルド
COPY src ./src
RUN mvn package -DskipTests

# 2. 実行用の環境（軽量なJDK 17）
FROM eclipse-temurin:17-jre
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# メモリ割り当てを調整 (Render Free Tier 512MBに合わせて384MBに設定 + オーバーヘッド分)
ENTRYPOINT ["java", "-Xmx384m", "-Xss512k", "-jar", "/app.jar"]
