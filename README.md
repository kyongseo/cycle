# 📢 cycle: Springboot를 활용한 중고거래 사이트

> 전반적인 웹의 기본 소양이 되는 게시판 프로젝트입니다.
<img src="https://github.com/kyounggseo/cycle/assets/102573192/9e3c34bf-0358-4864-aa95-7fc41f03d3f9" width="60%" height="70%">

## 목차
- [들어가며](#들어가며)
  - [프로젝트 기획 배경](#1-프로젝트-기획-배경)    
  - [프로젝트 설명](#2-프로젝트-설명)    
  - [프로젝트 기간](#3-프로젝트-기간)     

- [구조 및 설계](#구조-및-설계)
  - [사용 기술](#1-사용-기술)
  - [아키텍처](#2-아키텍처)
  - [DB 설계](#3-DB-설계)
  - [API 설계](#4-API-설계)

- [구현 결과](#구현-결과)
  - [notion](#notion-링크)

- [노하우 공유](#노하우-공유)

- [마치며](#마치며)
  - [알게된 점](#1-알게된-점)
  - [후기](#2-후기)

<br><br>

## 들어가며
### 1. 프로젝트 기획 배경
중고 거래 서비스를 구현해봄으로써 판매자와 구매자를 구분하여 유저서비스를 최적화하고 자바 기반 백엔드 역량 향상과 새로운 기술 스택을 경험해보고자 기획하였습니다. 또한 무조건적으로 책과 강의를 따라하여 만드는 것이 아닌 내가 직접 기능에 대한 구현방법을 고민하고, 여러 자료를 찾아보며 적용하는 힘을 키우기 위해 프로젝트를 시작했습니다.
<br><br>

### 2. 프로젝트 설명
- 권한에 따라 다른 기능 분리
    - SpringSecurity를 활용해 MasterAdmin, User 권한에 따라 다른 기능 분리
- **게시판 -** CRUD 기능, 조회수, 페이징 및 검색 처리
- **사용자 -** Security 회원가입 및 로그인, 회원정보 수정, 회원가입시 유효성 검사 및 중복 검사 <!-- OAuth 2.0 구글, 네이버 로그인, -->
- 로그인 세션을 이용한 ROLE별로 구매자와 판매자 페이지가 렌더링이 되고, 구매자는 장바구니에 물품을 담고 구매하고, 구매자 정보와 판매자 정보가 History Entity에 담기게 되고, 그걸 바탕으로 구매통계와 판매통계를 구현했습니다.
- 결제 기능 또한 구현 했습니다.
<br><br>

### 3. 프로젝트 기간

2023.08. ~ 2023.09 (5주)

| 기간                | 설명                                                         |
| ------------------- | ------------------------------------------------------------ |
| 08.14 ~ 08.16       | 기획 및 설계 <br />  CRUD 설계                                 |
| 08.17 ~ 08.20       | 메인화면 프론트 구성<br />DB 스키마 구성<br />메인화면 레이아웃 구성 |
| 08.22 ~ 08.27        | Spring security로 회원가입, 로그인 구현<br />API 스펙 구성하기<br />DB 스키마 최종 완료<br />판매자 및 구매자를 나눠 페이지 렌더링을 다르게 함 |
| 08.29 ~ 09.02         | Entity 클래스 설계 및 JPA로 연관관계 설정<br />마이페이지 제작<br /> 장바구니 구매 기능 구현 |
| 09.02 ~ 09.06        | 장바구니 구매 오류 해결<br />구매내역 구현<br /> 판매 글쓰기: Post API 제작<br /> 판매 통계 및 판매 순위 구현 |
| 09.08 ~ 09.12        | 판매 상세 페이지 구현<br />금액 충전 구현 및 최종 완성 <br /> 
| 09.13 ~ 09.20        | 리팩토링 및 추가 기능 개선  | <!-- AWS S3에 이미지 저장 기능 구현 -->

<br><br>

## 구조 및 설계
### 1. 사용 기술

1. Java : 11 version
2. Spring Boot : 2.7.2
3. Build Tool : Gradle 7.2
4. DB : MariaDB Driver
<!-- 5. Deploy :  AWS EC2 -->
5. Etc: Thymeleaf, Spring Security, JPA, Lombok, Oauth2-client, Html/Css, JavaScript, Bootstrap 4.3.1
<br><br>

### 2. 아키텍처
<!-- ![image](https://github.com/kyounggseo/cycle/assets/102573192/8b5e8e47-e1f4-4486-a188-551dd4b9d510) 
![image](https://github.com/kyounggseo/cycle/assets/102573192/4b9c51ba-b8f1-4b53-b991-9b2bfe0d2a05)<br/> -->
<img src="https://github.com/kyounggseo/cycle/assets/102573192/4b9c51ba-b8f1-4b53-b991-9b2bfe0d2a05" width="50%" height="70%">
<br><br>

<!-- 
## 🛠 ERD
![image](https://github.com/kyounggseo/cycle/assets/102573192/3c9aa412-eed2-4727-88b4-784dfdb40b18)
<br><br>
-->

### 3. DB 설계

<!-- ![image](https://github.com/kyounggseo/cycle/assets/102573192/dd625fb1-8fad-49c7-811f-f32b1243b8bf) -->
<img src="https://github.com/kyounggseo/cycle/assets/102573192/dd625fb1-8fad-49c7-811f-f32b1243b8bf" width="50%" height="20%">

<h3>DB 설계</h3>
- User, Item, Cart, Cart_item, Board, History
<br/>

<details>
<summary>회원과 상품</summary>
<div markdown="1">

- 상품은 1명의 회원이 등록할 수 있고, 상품은 작성자(회원) 정보를 가지고 있다. <br>

   - 한 명의 회원은 여러 상품을 등록할 수 있다.<br>
   **⇒ User(1) - Item(N)**

</div>
</details>

<details>
<summary>상품과 상품이미지</summary>
<div markdown="1">

- 이미지는 자신이 어떤 상품에 해당되는지에 대한 정보를 가지고 있다. 

   - 한 개의 상품은 여러 이미지를 가질 수 있다. <br>
   **⇒ Item(1) - ItemPhoto(N)**
</div>
</details>

<details>
<summary>장바구니</summary>
<div markdown="1">

- 어떤 회원의 장바구니인지, 담긴 상품들은 무엇인지에 대한 정보를 가지고 있다.

   - 한 명의 회원은 장바구니에 여러 상품을 담을 수 있다. 
   = CartItem에 CartId를 검색하면 N개의 정보가 출력된다. <br>
   **⇒ User(1) - CartId(N)**
   - 한 개의 상품은 여러 회원의 장바구니에 담길 수 있다. 
   = CartItem에 itemId를 검색하면 N개의 정보가 출력된다. <br>
   **⇒ CartItem(N) - Item(1)**
</div>
</details>

<details>
<summary>주문</summary>
<div markdown="1">

- 어떤 회원의 주문인지, 담긴 상품들은 무엇인지에 대한 정보를 가지고 있다. (주문상품에서 해당 역할을 수행한다.)

   - 한 명의 회원은 여러 주문을 요청할 수 있다.
   = Cart_Item에 CartId를 검색하면 N개의 정보가 출력된다. <br>
   **⇒ User(1) - Cart(N)**
   - 한 개의 주문은 여러 상품들을 담을 수 있다.
    = Cart에 UserId를 검색하면 여러 주문(N) * 여러 상품(M)개의 정보가 출력된다. <br>
   ⇒ 다대다 매핑이니 주문과 상품 사이에 **주문상품** 테이블을 생성한다.
</div>
</details>

<details>
<summary>주문상품</summary>
<div markdown="1">

- 어떤 주문에 어떤 상품들이 담겨있는지에 대한 정보를 가지고 있다.

   - 한 개의 주문은 여러 상품들을 담을 수 있다.
   = Cart_Item에 CartId를 검색하면 N개의 정보가 출력 된다.<br>
   **⇒ Cart_Item(1) - Cart_Id(N)**
   - 한 개의 상품은 여러 주문에 담길 수 있다.
   = Cart_Item에 itemId를 검색하면 N개의 정보가 출력 된다. <br>
   **⇒ ItemId(N) - Cart_Item(1)** 
- 상품은 1명의 회원이 등록할 수 있고, 상품은 작성자(회원) 정보를 가지고 있다. <br>
   - 한 명의 회원은 여러 상품을 등록할 수 있다.<br>
   **⇒ User(1) - Item(N)**
</div>
</details>

<br/><br/>

### 4. API 설계

<br/><br/>

## 구현 결과

#### 1) 회원가입/로그인<br>

- [x] 회원가입<br>
- 회원가입시 닉네임, 비밀번호, 전화번호, 주소, 이메일를 입력함.<br>
 <img src="https://github.com/kyounggseo/cycle/assets/102573192/cc6f7c9d-8231-4237-ae83-83076b5ef352" width="40%" height="40%"> 
<br/>

- [x] 로그인<br>
- 회원가입 여부를 체크함.<br>
- 비밀번호 일치 여부를 체크함.<br>
- 보안을 고려하여 JWT(access token, refresh token) 방식을 통해 로그인 인증.<br>
<img src="https://github.com/kyounggseo/cycle/assets/102573192/c08ad3e6-fb47-401b-9d8b-33cc23c60e7a" width="40%" height="40%">
<br/>

#### 2) 판매자 메인 페이지(홈)<br>

- [x] 상품 등록 <br>
<img src="https://github.com/kyounggseo/cycle/assets/102573192/1835e715-e601-40a1-b241-452e41149789" width="40%" height="40%">
<br/>

- [x] 판매목록 및 판매통계, 판매량 순위<br>
- 판매자는 원하는 상품을 상품명, 수량, 가격, 이미지 등을 입력하여 올릴 수 있음.
- 현재까지 판매된 판매목록, 통계, 판매량 순위를 볼 수 있음.
  
<img src="https://github.com/kyounggseo/cycle/assets/102573192/c5c5707f-6e51-4140-8517-596907029358" width="40%" height="70%"> <img src="https://github.com/kyounggseo/cycle/assets/102573192/c74e9e4c-558a-4eba-8c1c-fa63d2f71f6e" width="40%" height="70%">
<br/>

#### 3) 판매자 마이 페이지 <br>

- [x] 내 정보 수정하기<br>
- 판매자는 닉네임, 주소, 전화번호, 사용자 사진 변경이 가능함.<br>
<img src="https://github.com/kyounggseo/cycle/assets/102573192/2c01bb40-ffeb-4841-9b52-b4d2be6a6eae" width="40%" height="70%">
<br/>

#### 4) 구매자 메인 페이지(홈) <br>

- [x] 장바구니 및 구매내역<br>
- 구매자는 원하는 상품을 장바구니에 담고 구매할 수 있음.

<img src="https://github.com/kyounggseo/cycle/assets/102573192/3dc711af-0779-4f3e-9a60-b28adc5d181c" width="40%" height="70%"> <img src="https://github.com/kyounggseo/cycle/assets/102573192/97856725-6d2b-4490-ba2d-31fa84c06640" width="40%" height="70%"> 
<br/>

#### 5) 구매자 마이 페이지 <br>
- [x] 내정보 수정하기<br>
- 구매자는 닉네임, 주소, 전화번호, 사용자 사진 변경이 가능함.<br>

<img src="https://github.com/kyounggseo/cycle/assets/102573192/9b9a7649-9937-46ba-a73e-7a1a81228b73" width="40%" height="70%"> <img src="https://github.com/kyounggseo/cycle/assets/102573192/a6fdecb4-c7c1-4725-9e83-a0b4f347156d" width="40%" height="70%"> 
<br/>

<!-- 
![image](https://github.com/kyounggseo/cycle/assets/102573192/9b9a7649-9937-46ba-a73e-7a1a81228b73)<br>
![image](https://github.com/kyounggseo/cycle/assets/102573192/a6fdecb4-c7c1-4725-9e83-a0b4f347156d)<br> -->

- [x] 금액 충전하기 <br>
- 구매자의 잔액이 부족할 경우, 카카오 QR결제를 통해 원하는 금액을 선택 후 QR코드로 금액을 충전함. <br> 
<!-- <img src="https://github.com/kyounggseo/cycle/assets/102573192/4d42f7ce-aab0-478f-bf7e-7e319a28e0dc" width="50%" height="0%"> 
<img src="https://github.com/kyounggseo/cycle/assets/102573192/4cf108cf-77ce-42fb-9578-7baab0bc0530" width="30%" height="10%"> 
<img src="https://github.com/kyounggseo/cycle/assets/102573192/528ff50c-5049-481b-b801-2b63a99f21e8" width="80%" height="0%"> -->
<img src="https://github.com/kyounggseo/cycle/assets/102573192/c40deace-9fb4-45c2-a1be-5a49763e8e1b" width="40%" height="300%"> 
<br/>

### notion 링크
[Notion link](https://www.notion.so/Cycle-e4e25da4a37b42258fbe5a3676250e5d?pvs=4)

<br/>

## 노하우 공유

<!-- 
[[Spring] DAO와 DTO](https://github.com/kyounggseo/share-knowhow/blob/main/share%20knowhow%20/%5BSpring%5D%20DAO%EC%99%80%20DTO.md) -->
[[Spring] DAO와 DTO](https://github.com/kyounggseo/cycle/issues/1#issue-2063206755)
[[Spring] Spring Data JPA 정리](https://github.com/kyounggseo/share-knowhow/blob/main/share%20knowhow%20/%5BSpring%5D%20Spring%20Data%20JPA%20%EC%A0%95%EB%A6%AC.md)

[[Spring] Spring Security](https://github.com/kyounggseo/share-knowhow/blob/main/share%20knowhow%20/%5BSpring%5D%20Spring%20Security.md)

[[Spring] Springboot build and deploy tools](https://github.com/kyounggseo/share-knowhow/blob/main/share%20knowhow%20/%5BSpring%5D%20Springboot%20build%20and%20deploy%20tools.md)

[[Spring] Thymeleaf정리](https://github.com/kyounggseo/share-knowhow/blob/main/share%20knowhow%20/%5BSpring%5D%20Thymeleaf%EC%A0%95%EB%A6%AC.md)

[[Spring] 도메인 클래스 관련 참고사항(1)](https://github.com/kyounggseo/share-knowhow/blob/main/share%20knowhow%20/%5BSpring%5D%20%EB%8F%84%EB%A9%94%EC%9D%B8%20%ED%81%B4%EB%9E%98%EC%8A%A4%20%EA%B4%80%EB%A0%A8%20%EC%B0%B8%EA%B3%A0%EC%82%AC%ED%95%AD(1).md)

[[Spring] 도메인 클래스 관련 참고사항(2)](https://github.com/kyounggseo/share-knowhow/blob/main/share%20knowhow%20/%5BSpring%5D%20%EB%8F%84%EB%A9%94%EC%9D%B8%20%ED%81%B4%EB%9E%98%EC%8A%A4%20%EA%B4%80%EB%A0%A8%20%EC%B0%B8%EA%B3%A0%EC%82%AC%ED%95%AD(2).md)

[[Spring] 서버 재시작하지 않고 view 변경 확인하기](https://github.com/kyounggseo/share-knowhow/blob/main/share%20knowhow%20/%5BSpring%5D%20%EC%84%9C%EB%B2%84%20%EC%9E%AC%EC%8B%9C%EC%9E%91%ED%95%98%EC%A7%80%20%EC%95%8A%EA%B3%A0%20view%20%EB%B3%80%EA%B2%BD%20%ED%99%95%EC%9D%B8%ED%95%98%EA%B8%B0.md)

<br><br>

## 마치며
### 1. 알게된 점
- @Transactional 어노테이션에 대해 공부하게 되었습니다.
- JPA 연관관계에 대해 더욱 잘 알게되는 계기가 되었습니다.
- 세션과 ROLE을 이용하여 역할별 기능을 구분하는 페이지를 만드는 방법에 대해 알게 되었습니다.
- Spring Framework의 동작과정을 익혔습니다.
- MVC구조의 서비스 흐름을 익혔습니다.
- 백엔드 개발자로서 프론트엔드와의 협업을 경험했습니다.
- JWT 토큰의 개념과 사용법을 익혔습니다.
- 백엔드 요청시 필요한 인증/인가 부분을 학습했습니다.
- Spring MVC에서 제공하는 Interceptor기능으로 요청 유효성을 판단할 수 있었습니다.


### 2. 후기
혼자 독학하며 처음 만들어본 프로젝트이기 때문에,   
공부한 내용을 사용해보는 설렘만큼이나 부족한 부분에 대한 아쉬움도 많이 남았습니다.   
효율적인 설계를 위해 고민하고 찾아보며 실제로 많이 공부할 수 있었던 부분도 많았습니다.   
책이나 블로그, 강의로 공부한 예제에서 납득했던 부분들은 실제로 코드를 짜면서 다양한 애로 사항을 마주했고   
'이 로직은 이 단계에서 처리하는게 맞는가', '각 레이어간 데이터 전달은 어떤 방식이든 DTO로 하는게 맞는가' 등   
여러 고민에 빠져 헤맨적도 있었지만, 다행히 결과는 대부분 최선을 찾았었던 것 같습니다.   
그리고 내가 만든 코드를 남에게 보여줬을 때, 누군가 코드의 근거를 물어본다면   
과연 자신 있게 나의 생각을 잘 얘기할 수 있을까 라는 생각을 굉장히 많이 하게 되었습니다.   
그래서 하나를 구현할 때 '이렇게 구현 하는 것이 과연 최선인가', '더 나은 Best Practice는 없을까'   
스스로 의심하고 고민하게 되는 습관을 가지게 되었습니다.   

두 번째로 기술적인 부분에서 더 공부하고 싶은 '방향'을 찾을 수 있었습니다.   
이번 프로젝트는 저에게 좋은 경험이 되었고, 저의 부족한 부분을 스스로 알 수 있는 좋은 계기가 되었습니다.   
부족한 부분에 대해 스스로 인지하고 있고, 더 깊게 공부하며 스스로 발전할 수 있는 '방향'을 다시한번 찾을 수 있게 되었습니다.   
이를 통해 더 나은 웹 애플리케이션을 만들 수 있을 것 같다는 자신감도 생겼습니다.   

끝까지 읽어주셔서 감사합니다.
