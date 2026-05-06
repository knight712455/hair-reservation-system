# Hair Reservation System

## 1. 서비스 소개

Hair Reservation System은 미용실 예약을 위한 백엔드 API 서버입니다.
사용자는 디자이너(Resource)를 선택하여 특정 시간에 예약을 생성할 수 있습니다.

본 프로젝트는 **동시성 문제 해결**과 **예약 중복 방지 로직 구현**에 초점을 두었습니다.

---

## 2. 주요 기능

### User (회원)

* 회원 등록
* 회원 조회

### Resource (디자이너)

* 디자이너 등록
* 디자이너 조회

### Reservation (예약)

* 예약 생성
* 예약 조회 (페이징)
* 예약 취소
* 예약 시간 중복 방지
* 동시성 문제 해결 (DB Lock)

---

##  3. 기술 스택

* Java 17
* Spring Boot
* Spring Data JPA
* MySQL
* Gradle

---

##  4. ERD

```
User (1) ─── (N) Reservation (N) ─── (1) Resource

User
- id (PK)
- name

Resource
- id (PK)
- name

Reservation
- id (PK)
- user_id (FK)
- resource_id (FK)
- slot_start
- slot_end
- status
```

---

## 5. API 명세

### 6.예약 생성

* **POST** `/reservations`

#### Request

```
userId=1
resourceId=1
start=2026-05-04T10:00:00
end=2026-05-04T11:00:00
```

#### Response

```json
{
  "id": 1,
  "slotStart": "2026-05-04T10:00:00",
  "slotEnd": "2026-05-04T11:00:00",
  "status": "CONFIRMED"
}
```

---

###  예약 조회

* **GET** `/reservations?page=0&size=10`

#### Response

```json
{
  "content": [
    {
      "id": 1,
      "userName": "유저1",
      "resourceName": "디자이너1",
      "slotStart": "2026-05-04T10:00:00",
      "slotEnd": "2026-05-04T11:00:00",
      "status": "CONFIRMED"
    }
  ]
}
```

---

###  예약 취소

* **PATCH** `/reservations/{id}/cancel`

#### Response

```
취소 완료
```

---

##  6. 예약 정책

* 동일 디자이너(Resource)는 동일 시간대에 중복 예약 불가
* 예약 시간은 시작/종료 시간 기준으로 관리
* 예약 상태는 CONFIRMED / CANCELED 로 구분

---

##  7. 동시성 처리

### 문제

동시에 여러 요청이 들어올 경우 동일 시간대 예약이 중복 생성되는 문제가 발생

### 해결

* **Pessimistic Lock 적용**
* 트랜잭션 내부에서 충돌 체크

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
```

---

## 8. 버그 리포트

###  Bug 1: 시간 겹침 예약 허용

* 문제: 10:00~11:00 예약 존재 시 10:30~11:30 예약 가능
* 원인: 동일 시간 비교만 수행
* 해결: 시간 겹침 조건 적용

```
start < 기존 end AND end > 기존 start
```

---

###  Bug 2: 동시 요청 시 중복 예약 발생

* 문제: 동시에 요청 시 중복 예약 생성
* 원인: 조회 후 저장 사이 race condition 발생
* 해결: DB Lock 적용 (Pessimistic Lock)

---

###  Bug 3: 존재하지 않는 유저 예약 시 오류

* 문제: 없는 userId로 요청 시 서버 에러 발생
* 원인: 예외 처리 미흡
* 해결: Optional.orElseThrow 적용

---

## 📌9. 향후 개선 방향

* JWT 기반 인증/인가
* 지도 API 연동 (위치 기반 검색)
* 예약 검색 필터 (날짜, 디자이너)
* AWS 기반 배포
* 에러 응답 표준화

---

##  10. 실행 방법

### 서버 실행

```
./gradlew bootRun
```

### 테스트 데이터

```sql
INSERT INTO user (name) VALUES ('유저1');
INSERT INTO resource (name) VALUES ('디자이너1');
```

### API 테스트

```bash
# 예약 생성
curl -X POST "http://localhost:8080/reservations?userId=1&resourceId=1&start=2026-05-04T10:00:00&end=2026-05-04T11:00:00"

# 조회
curl "http://localhost:8080/reservations?page=0&size=10"

# 취소
curl -X PATCH http://localhost:8080/reservations/1/cancel
```
