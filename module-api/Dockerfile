# ──────────── 1단계: 빌드 ────────────
FROM gradle:8.14.2-jdk17 AS builder
WORKDIR /home/gradle/project

# 전체 소스 복사 후
COPY . .
# module-api만 빌드 (테스트 제외)
RUN gradle :module-api:bootJar -x test

# ──────────── 2단계: 런타임 ──────────
FROM eclipse-temurin:17-jre-focal
WORKDIR /app

# 빌더에서 생성된 jar만 복사
COPY --from=builder /home/gradle/project/module-api/build/libs/module-api-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
