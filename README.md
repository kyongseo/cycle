
# 📢 cycle: Springboot와 AWS를 활용한 중고거래 사이트
<br><br>

## 🖊️ 프로젝트 설명
- 스프링부트를 이용하여 만든 쇼핑몰입니다.
- 판매자와 구매자로 나뉘어 기능이 분리됩니다.
- 로그인 세션을 이용한 ROLE별로 구매자와 판매자 페이지가 렌더링이 되고, 구매자는 장바구니에 물품을 담고 구매하고, 구매자 정보와 판매자 정보가 History Entity에 담기게 되고, 그걸 바탕으로 구매통계와 판매통계를 구현했습니다.
- 결제 기능 또한 구현 했습니다.

<br><br>


## 📆 프로젝트 기간

2023.04.23. ~ ing(2023.06.20)

| 기간                | 설명                                                         |
| ------------------- | ------------------------------------------------------------ |
| 4.23(토)            | 전체 회의 & 준비기간                                          |
| 4.25(월) ~ 4.30(토) | CRUD 설계                                                     |
| 4.31(일) ~ 5.03(수) | 메인화면 프론트 구성<br />DB 스키마 구성<br />메인화면 레이아웃 구성 |
| 5.04(목) ~ 5.15(월) | Spring security로 회원가입, 로그인 구현<br />API 스펙 구성하기<br />DB 스키마 최종 완료<br />판매자 및 구매자를 나눠 페이지 렌더링을 다르게 함 |
| 5.16(화) ~ 5.25(목) | Entity 클래스 설계 및 JPA로 연관관계 설정<br />마이페이지 제작<br /> 장바구니 구매 기능 구현 |
| 5.26(금) ~ 6.01(월) | 장바구니 구매 오류 해결<br />구매내역 구현<br /> 판매 글쓰기: Post API 제작<br /> 판매 통계 및 판매 순위 구현                                   |
| 6.02(화) ~ 6.18(목) | 판매 상세 페이지 구현<br />금액 충전 구현 및 최종 완성<br /> AWS S3에 이미지 저장 기능 구현                  |

<br><br>

## 📍 사용하는 툴

1. Java : 11 version
2. Spring Boot : 2.7.2
3. Build Tool : Gradle
4. DB : MariaDB Driver
5. Deploy : AWS EC2
6. Etc: Thymeleaf, Spring Security, JPA, AWS S3, Lombok

## 🛠 아키텍처

## 💾 DB 스키마 구성

<h3>DB 설계</h3>

- User
- Item
- Cart
- Cart_item
- Board
- History


## 🎯 구현 결과

#### 1) 회원가입/로그인
- [x] 회원가입
- [x] 로그인

#### 2) 메인 페이지(홈)

- [x] 물건 리스트 보기
- [x] 물건 업로드
- [x] 물건 상세페이지 보기

#### 3) 마이 페이지

- [x] 좋아요 내역, 판매/구매 내역
- [x] 내 정보 보기

<br><br>

## 🥁 실행 방법
## 🔖 노하우 공유

[[Spring] DAO와 DTO]([https://github.com/kyounggseo/share-knowhow/blob/main/share%20knowhow%20/%5BSpring%5D%20DAO%EC%99%80%20DTO.md])

[[Spring] Spring Data JPA 정리]([https://github.com/kyounggseo/share-knowhow/blob/main/share%20knowhow%20/%5BSpring%5D%20Spring%20Data%20JPA%20%EC%A0%95%EB%A6%AC.md])

[[Spring] Spring Security]([https://github.com/kyounggseo/share-knowhow/blob/main/share%20knowhow%20/%5BSpring%5D%20Spring%20Security.md])

[[Spring] Springboot build and deploy tools](https://github.com/kyounggseo/share-knowhow/blob/main/share%20knowhow%20/%5BSpring%5D%20Springboot%20build%20and%20deploy%20tools.md)

[[Spring] Thymeleaf정리](https://github.com/kyounggseo/share-knowhow/blob/main/share%20knowhow%20/%5BSpring%5D%20Thymeleaf%EC%A0%95%EB%A6%AC.md)

[[Spring] 도메인 클래스 관련 참고사항(1)](https://github.com/kyounggseo/share-knowhow/blob/main/share%20knowhow%20/%5BSpring%5D%20%EB%8F%84%EB%A9%94%EC%9D%B8%20%ED%81%B4%EB%9E%98%EC%8A%A4%20%EA%B4%80%EB%A0%A8%20%EC%B0%B8%EA%B3%A0%EC%82%AC%ED%95%AD(1).md)

[[Spring] 도메인 클래스 관련 참고사항(2)](https://github.com/kyounggseo/share-knowhow/blob/main/share%20knowhow%20/%5BSpring%5D%20%EB%8F%84%EB%A9%94%EC%9D%B8%20%ED%81%B4%EB%9E%98%EC%8A%A4%20%EA%B4%80%EB%A0%A8%20%EC%B0%B8%EA%B3%A0%EC%82%AC%ED%95%AD(2).md)

[[Spring] 서버 재시작하지 않고 view 변경 확인하기](https://github.com/kyounggseo/share-knowhow/blob/main/share%20knowhow%20/%5BSpring%5D%20%EC%84%9C%EB%B2%84%20%EC%9E%AC%EC%8B%9C%EC%9E%91%ED%95%98%EC%A7%80%20%EC%95%8A%EA%B3%A0%20view%20%EB%B3%80%EA%B2%BD%20%ED%99%95%EC%9D%B8%ED%95%98%EA%B8%B0.md)

## ☝ 제작 후기
- @Transactional 어노테이션에 대해 공부하게 되었습니다.
- JPA 연관관계에 대해 더욱 잘 알게되는 계기가 되었습니다.
- 세션과 ROLE을 이용하여 역할별 기능을 구분하는 페이지를 만드는 방법에 대해 알게 되었습니다.


<hr>
