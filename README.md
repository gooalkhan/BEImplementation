# 기상청 예보 API 연동 프로젝트

## 목표
* 입사지원한 회사 코딩테스트 과제
* 서비스키와 데이터타입(JSON으로만 구현)을 제외하고 기상청 API와 동일한 API를 제공
* 클라이언트 요청시 데이터베이스에 보유하고 있는 자료라면 기상청에 요청하지 않고 바로 전달
* 기상청 API와 마찬가지로 여러건의 자료가 검색되는 경우에는 페이징 기능을 제공
* 단위 테스트 기능 구현완료
* 통합 테스트 기능(미구현)
* Spring Rest Docs(미구현)

## 기능
* 중기 육상 예보 조회 (/midfcst/getMidLandFcst?)
* 단기 예보 조회 (VilageFcst/getVilageFcst?)
* 초단기 예보 조회 (UltraSrtFcst/getUltraSrtFcst?)

## 기술 스택
* Spring Boot 2.7.18
* JPA
* MySQL 8
* H2 Database(개발용)
* Java 17
* 그 외는 BOM 참조

## 제작 기간
2024년 1월 10일 ~ 2024년 1월 12일

## 제작자
gooalkhan(단독)

## 기타
* 본 프로젝트에 수록된 기상청 API키는 2024년 1월 19일에 폐기될 예정입니다.
* 작성시에는 stackoverflow, 개인 블로그를 우선하여 참조하였으며, 찾고자 하는 내용이 없을 시에는 Jetbrains AI Assistant(인텔리제이 내장 AI)의 도움을 받았습니다.(jetbrainsAIAssitant.txt파일 참조)