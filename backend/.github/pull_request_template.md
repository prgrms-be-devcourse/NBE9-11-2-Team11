## 📌PR 제목
feat: 카페 검색 기능 구현

- feat: 기능 추가
- fix: 버그 수정
- refactor: 리팩토링
- docs: 문서 수정
- test: 테스트 코드
- chore: 기타 작업


## 작업 내용
- 공통 응답 및 예외처리 설정 (RsData, CustomException, ErrorCode, GlobalExceptionHandler)
- 카페 엔티티 및 관련 Enum 추가
- QueryDSL 기반 동적 필터링 구현
- 카페 목록 조회 API 추가 (좌표 범위 + 필터링)
- 카페 단건 조회 API 추가
- 좌표 유효성 검증 추가 (@Valid)
- 환경변수 분리 (.env)


- [x] 기능 추가
- [ ] 버그 수정
- [x] 리팩토링
- [ ] 문서 수정


## 작업 목적
- 카카오 지도에 표시할 카페 목록을 조회하는 기능 구현
- 좌표 범위 기반 지역 검색 및 카테고리 필터링으로 원하는 카페 검색 가능
- 민감한 정보 (DB 계정, JWT 시크릿) 환경변수로 분리


## 테스트
- ### Postman 사용
- GET /api/V1/cafe - 전체 카페 목록 조회 확인
- GET /api/V1/cafe?hasWifi=true - 필터링 조회 확인
- GET /api/V1/cafe?swLat=37.50&swLng=127.03&neLat=37.52&neLng=127.07 - 좌표 범위 조회 확인
- GET /api/V1/cafe?swLat=91 - 잘못된 좌표값 예외처리 확인 (400-2 반환)

## 이슈
- SecurityConfig 임시로 전체 허용 상태 → JWT 필터 추가 시 수정 필요
- Cafe 엔티티 member_id nullable 설정 (회원 탈퇴 시 처리 방식 미정)
- .env 파일 로컬에서 직접 생성 필요

## 환경변수 설정
- .env 파일 생성 후 아래 값 설정(backend 폴더 바로 아래)
- DB_USERNAME=
- DB_PASSWORD=
- JWT_SECRET=

## 체크리스트
- [x] 빌드 정상 동작
- [ ] 테스트 코드 작성 / 통과
- [x] 컨벤션 준수 (네이밍, 코드 스타일 등)
