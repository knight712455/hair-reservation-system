# Hair Reservation Project

미용실 예약 서비스 프로젝트입니다.

React 기반 프론트엔드와 Spring Boot 기반 백엔드로 구성되어 있습니다.

---

# 기술 스택

## Frontend

* React
* Vite
* Tailwind CSS
* React Router DOM

## Backend

* Spring Boot
* Spring Security
* JWT Authentication
* JPA / Hibernate
* MySQL

---

# 주요 기능

* 회원가입
* 로그인 / JWT 인증
* 디자이너 조회
* 예약 생성
* 예약 취소
* 예약 시간 조회
* 휴무일 처리
* 지난 시간 예약 방지

---

# 프로젝트 구조

```bash
Hair-reservation
 ┣ backend
 ┣ frontend
 ┣ docs
 ┗ README.md
```

---

# 실행 방법

## Backend 실행

```bash
cd backend
./gradlew bootRun
```

## Frontend 실행

```bash
cd frontend
npm install
npm run dev
```

---

# API 문서

docs 폴더 참고

* 기능정의서
* API 명세서
* ERD
* 버그리포트

---

# 인증 방식

JWT 토큰 기반 인증 사용

Authorization Header

```bash
Bearer {token}
```

---

# 프로젝트 목표

예약 시스템의 기본 구조와 인증 흐름을 학습하고
실제 서비스 형태의 예약 기능 구현 경험을 목표로 제작하였습니다.
