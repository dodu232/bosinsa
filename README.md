
# 🛍️ Bosinsa

> **이커머스 플랫폼 / 멀티모듈 아키텍처 기반 개인 프로젝트 (2025.07 ~ 진행 중)**

**Bosinsa**는 도메인 중심 설계(DDD)와 이벤트 기반 아키텍처를 적용한 **이커머스 플랫폼**입니다.  
기능 확장과 유지보수를 용이하게 하기 위해 **7개 모듈로 구성된 멀티모듈 구조**를 설계했으며,  
**Kafka + Outbox 패턴**을 통해 트랜잭션 정합성과 이벤트 처리 신뢰성을 확보했습니다.

---

## 🎯 프로젝트 개요

- **기간:** 2025.07 ~ 진행 중  
- **목표:** 확장성과 안정성을 모두 갖춘 이커머스 백엔드 아키텍처 구축  
- **핵심 키워드:** DDD, 멀티모듈, Kafka, Outbox Pattern, 트랜잭션 정합성  

---

## 🏗️ 아키텍처 개요

### 모듈 구성

| 모듈 | 역할 |
|------|------|
| **module-api** | REST API 제공 (사용자, 상품, 주문, 장바구니 등) |
| **module-batch** | 주문 통계 및 배치 처리 |
| **module-domain** | 핵심 비즈니스 로직과 엔티티 |
| **module-common** | 공통 유틸리티 및 설정 |
| **module-contracts** | 모듈 간 DTO, 인터페이스 정의 |
| **module-external** | 외부 결제 및 배송 API 연동 |
| **module-messaging** | Kafka 기반 메시징 처리 |

---

## ⚙️ 기술 스택

| 구분 | 기술 |
|------|------|
| **Backend** | Java 17, Spring Boot 3.2.7, Spring JPA, Spring Security |
| **Database** | MySQL 8.0 |
| **Cache** | Redis |
| **Messaging** | Apache Kafka |
| **Build/Deploy** | Gradle, Docker & Docker Compose |

---

## 💡 기술적 도전과 해결

| 주제 | 문제 | 해결 | 결과 |
|------|------|------|------|
| **아키텍처 확장성 개선** | 기능 추가와 테스트 주기 단축으로 단일 모듈 구조 유지보수 어려움 | 7개 모듈로 분리된 멀티모듈 아키텍처 설계로 관심사 분리 및 독립 배포 구조 구축 | 테스트 효율 향상, 변경 영향 최소화 |
| **이벤트 처리 신뢰성 확보** | 비동기 이벤트 처리 중 메시지 손실 및 중복 발생 가능성 | Kafka + Outbox Pattern 적용으로 트랜잭션 내 이벤트 발행 및 재시도 로직 구현 | 트랜잭션 정합성 강화 |
| **결제 및 주문 처리 안정화** | Toss Payments 연동 시 결제 실패 케이스에서 데이터 불일치 발생 | 결제 상태 기반 예외 처리 및 트랜잭션 복구 로직 추가 | 결제 오류 시 데이터 정합성 유지 |
| **배치 처리 효율 개선** | 대량 주문 통계 집계 시 메모리 부담 발생 | Spring Batch 구조 도입 및 JPA Stream + Fetch Size 조합으로 스트리밍 처리 | 배치 실행 시 메모리 사용 효율 개선 |

---

## 🔄 이벤트 기반 아키텍처

- **Outbox Pattern**으로 트랜잭션 안정성 확보  
- **Kafka 이벤트 스트리밍**으로 서비스 간 데이터 동기화  
- **Eventual Consistency**를 유지하며 비동기 처리 안정화  

*(참고: CQRS 구조는 추후 도입 예정이며, 현재는 Repository 패턴 기반으로 명령/조회 로직을 관리합니다.)*

---

## 📊 배치 및 통계 처리

- 주문 통계 집계(`OrderDailyStat`) 자동화  
- **Spring Batch 기반 배치 처리 구조**로 작업 단위 제어  
- Tasklet 기반 트랜잭션 처리로 데이터 정합성 보장
- CommandLineRunner를 통한 배치 작업 실행 및 관리

---

## 🌐 주요 API 예시

| 도메인 | 엔드포인트 |
|--------|-------------|
| 인증 | `POST /api/v1/auth/signin`, `/api/v1/auth/social/{provider}`|
| 주소 | `POST /api/v1/me/addresses` |
| 상품 | `GET /api/v1/products`, `GET /api/v1/products/{productId}` |
| 주문 | `POST /api/v1/orders`, `GET /api/v1/orders/{orderId}` |
| 장바구니 | `GET /api/v1/carts`, `POST /api/v1/carts` |

---

## 👤 담당 역할

- 전체 **아키텍처 설계 및 멀티모듈 구조 정의**
- **Kafka + Outbox 기반 이벤트 처리 플로우 구현**
- **주문·결제 도메인 개발 및 예외 처리 설계**
- **Spring Batch 기반 통계 처리 로직 개발**
- **테스트 코드 작성 및 CI 환경 구성**

---

## 🔮 향후 개선 방향

- 상품 검색 기능에 **Elasticsearch** 도입 검토  
- **Optimistic Lock 기반 재고 동시성 제어 로직** 구현 예정  
- **CQRS 및 Saga 패턴 기반 보상 트랜잭션** 도입 검토  
- **GitHub Actions + Docker Registry** 기반 CI/CD 파이프라인 구축 예정
- JPA Stream + Fetch Size 조합을 통한 대용량 데이터 처리 최적화 예정

---

## 📦 실행 방법

```bash
# 저장소 클론
git clone <repository-url>
cd bosinsa

# 인프라 환경 실행 (MySQL, Redis, Kafka 등)
docker-compose up -d

# API 서버 실행
./gradlew :module-api:bootRun

# Batch 서버 실행
./gradlew :module-batch:bootRun
````

---

> ⚡ **Bosinsa는 학습 및 실무 아키텍처 설계 역량 강화를 위한 개인 프로젝트입니다.**

