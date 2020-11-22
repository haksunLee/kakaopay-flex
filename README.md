# 카카오페이 사전과제 - 카카오페이 뿌리기 기능 구현하기
## 목차
- [개발 환경](#개발-환경)
- [프로젝트 실행 방법](#프로젝트-실행-방법)
- [요구사항 분석](#요구사항-분석)
- [API](#API)

---

## 개발 환경
- 기본 환경
    - IDE: IntelliJ IDEA
    - OS: Windows 10
    - GIT
- Server
    - Java8
    - Spring Boot 2.1.7
    - JPA
    - H2
    - Gradle
    - Junit5


## 프로젝트 실행 방법
### 소스코드 다운 및 실행

```
$ git clone https://github.com/haksunLee/kakaopay-flex.git
$ cd kakaopay-flex
$ ./gradlew clean build
$ java -jar build/libs/kakaopay-flex-0.0.1-SNAPSHOT.jar                       
```

- 접속 Base URI: `http://localhost:8080`

## 요구사항 분석
카카오페이가 제공하는 머니 뿌리기 기능의 간소화된 REST API 구현
### 기능 요구 사항
- 뿌리기, 받기, 조회 기능을 수행하는 REST API를 구현한다.
    - 요청한 사용자와 방의 식별값은 HTTP Header에 포함된다.
    - 사용자는 "X-USER-ID": 숫자, 방은 "X-ROOM-ID": 문자 형태로 전달된다.
    - 충분한 잔액을 보유하고 있다고 가정하고 관련 체크를 하지 않는다.

- 사용자는 다음 3가지 액션을 할 수 있다.

    ![use-case-image](./usecase_diagram01.png)

- 뿌리기 기능에 대해 다음 2개의 도메인을 정의 하였다.
    - Flex: 뿌리기 등록 성공 시, 생성된다.
    - FlexItem: 뿌려지는 인원만큼 생성된다.    
    
    ![class-image](./class_diagram01.png)
    
### 비기능 요구 사항
- 해당 어플리케이션이 다수의 서버에 다수의 인스턴스로 동작하더라도 기능에 문제가 없도록 설계되어야 한다.
    - Scalability

### Architecture
- 다수의 서버에 다수의 인스턴스로 동작가능 하도록 MSA 구현이 필수적이다. 이를 위해 구축 및 서비스가 간결한 Spring Boot를 채택하였으며 
REST API 기반으로 Client 또는 타 서비스와 손쉽게 인터페이스가 가능하도록 하였다.  
- Stateless한 서버를 구성하기 위해 토근(JWT) 기반 인증 방식을 채택하여 Scale out 방식의 확장이 가능하도록 하였다.

    ![architecture-image](./architecture01.png)

- Web Layer ↔ Service Layer 간에는 DTO를 통해 정보를 공유 한다.
- Service Layer ↔ Repository Layer 간에는 Domain을 통해 정보를 공유한다.
    
    ![sequence-image](./sequence_diagram01.png)
    
    ![sequence-image](./sequence_diagram02.png)
    
## API
### 1.뿌리기 API
- Request

```
http://localhost:8080/api/v1/flex
```

```
POST /api/v1/flex HTTP/1.1
```

- Response

```json
{
    "result": "SUCCESS",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInJvb21JZCI6InJvb20xIiwiYW1vdW50Ijo1MDAwMDAsImNvdW50Ijo0LCJpYXQiOjE2MDYwNDU1NTksImV4cCI6MTYwNjA0NjE1OX0.E7U39FmMF7Br07XaaBJSMJnGeo-7nnbUapQZDbEt7MA"
    }
}
```

- 뿌리기 기능 구현
  - `/api/v1/flex` POST URL 요청을 처리하는 `FlexRegistControllerTest` 클래스를 만듦
  - `@RequestHeader("X-USER-ID"), @RequestHeader("X-ROOM-ID"), @RequestBody에 amount, count를 받아 처리한다. 
  - 응답으로는 `token`값을 보내고 있다.
  - 데이터 확인은 테스트 및 PostMan으로 직접 서버에 요청하며 확인하였다.
  
- TEST Case: FlexRegistControllerTest
  - registFlex_normalCase()
    - 정상적으로 뿌리기를 등록합니다.
  - registFlex_abnormalCase01()
    - 뿌리기 등록 시, 필수 Header 값인 Room 정보를 누락시켜 호출합니다.
```
MockHttpServletResponse:
           Status = 200
    Error message = null
          Headers = [Content-Type:"application/json;charset=UTF-8"]
     Content type = application/json;charset=UTF-8
             Body = {"result":"FAIL","message":"org.springframework.web.bind.MissingRequestHeaderException: Missing request header 'X-ROOM-ID' for method parameter of type String"}
    Forwarded URL = null
   Redirected URL = null
          Cookies = []
```

### 2. 받기 API
- Request

```
http://localhost:8080/api/v1/flex/item
```

```
PUT /api/v1/flex/item HTTP/1.1
```

- Response

```json
{
    "result": "SUCCESS",
    "data": {
        "money": 80640
    }
}
```

- 받기 기능 구현
  - `/api/v1/flex/item` PUT URL 요청을 처리하는 `FlexItemUpdateController` 클래스를 만듦
  - `@RequestHeader("X-USER-ID")`, `@RequestHeader("X-ROOM-ID")`, `@RequestHeader("X-TOKEN")`을 받아 처리한다. 
  - 응답으로는 받게되는 `money`값을 보내고 있다.
  - 데이터 확인은 테스트 및 PostMan으로 직접 서버에 요청하며 확인하였다.
  
- TEST Case: `FlexItemUpdateControllerTest`
  - `updateFlexItem_normalCase()`
    - 정상적으로 받기 처리를 합니다.
    
  - `updateFlexItem_abnormalCase01()`
    - 받기 처리 시, 잘못된 Token 정보를 던집니다.    
```
MockHttpServletResponse:
           Status = 200
    Error message = null
          Headers = [Content-Type:"application/json;charset=UTF-8"]
     Content type = application/json;charset=UTF-8
             Body = {"result":"FAIL","code":"E002","message":"해당 뿌리기 건은 유효하지 않거나 마감되었습니다."}
```

  - `updateFlexItem_abnormalCase02()`
    - 받기 처리 시, 잘못된 Room 정보를 던집니다. 
```
MockHttpServletResponse:
           Status = 200
    Error message = null
          Headers = [Content-Type:"application/json;charset=UTF-8"]
     Content type = application/json;charset=UTF-8
             Body = {"result":"FAIL","code":"E004","message":"뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다."}
```

  - `updateFlexItem_abnormalCase03()`
    - 받기 처리 시, 뿌리기 등록자와 동일한 User 정보를 던집니다.
```
MockHttpServletResponse:
           Status = 200
    Error message = null
          Headers = [Content-Type:"application/json;charset=UTF-8"]
     Content type = application/json;charset=UTF-8
             Body = {"result":"FAIL","code":"E006","message":"자신이 뿌리기한 건은 자신이 받을 수 없습니다."}
```

  - `updateFlexItem_abnormalCase04()`
    - 받기 처리 시, 이전에 이미 받은 사용자 정보를 던집니다. 
```
MockHttpServletResponse:
           Status = 200
    Error message = null
          Headers = [Content-Type:"application/json;charset=UTF-8"]
     Content type = application/json;charset=UTF-8
             Body = {"result":"FAIL","code":"E008","message":"뿌리기 당 한 사용자는 한번만 받을 수 있습니다."}
```
### 3. 조회 API
- Request

```
http://localhost:8080/api/v1/flex
```

```
GET /api/v1/flex HTTP/1.1
```

- Response

```json
{
    "result": "SUCCESS",
    "data": {
        "createdDate": "2020-11-22T21:51:48.977",
        "amount": 500000,
        "totalReceivedMoney": 430020,
        "receivedItemInfo": [
            {
                "money": 181129,
                "receiverId": 2
            },
            {
                "money": 78795,
                "receiverId": 23
            },
            {
                "money": 170096,
                "receiverId": 41
            }
        ]
    }
}
```

- 조회 기능 구현
  - `/api/v1/flex` GET URL 요청을 처리하는 `FlexSearchController` 클래스를 만듦
  - `@RequestHeader("X-USER-ID")`, `@RequestHeader("X-ROOM-ID")`, `@RequestHeader("X-TOKEN")`을 받아 처리한다. 
  - 응답으로는 `createdDate`(뿌린 시간), `amount`(뿌린 금액), `totalReceivedMoney`(받기 완료된 금액), `receivedItemInfo`(받기 완료된 정보) ([`money`(받은 금액), `receiverId`(받은 사용자 아이디)] 리스트값을 보내고 있다.
  - 데이터 확인은 테스트 및 PostMan으로 직접 서버에 요청하며 확인하였다.
  
- TEST Case: `FlexSearchControllerTest`
  - `searchFlex_normalCase()`
    - 정상적으로 뿌리기 조회 처리를 합니다.
    
  - `searchFlexItem_abnormalCase01()`
    - 조회 처리 시, 잘못된 Token 정보를 던집니다.
```
MockHttpServletResponse:
           Status = 200
    Error message = null
          Headers = [Content-Type:"application/json;charset=UTF-8"]
     Content type = application/json;charset=UTF-8
             Body = {"result":"FAIL","code":"E003","message":"해당 뿌리기 건을 찾을 수 없습니다."}
```

  - `searchFlexItem_abnormalCase02()`
    - 조회 처리 시, 등록자가 아닌 User값을 던집니다.
```
MockHttpServletResponse:
           Status = 200
    Error message = null
          Headers = [Content-Type:"application/json;charset=UTF-8"]
     Content type = application/json;charset=UTF-8
             Body = {"result":"FAIL","code":"E007","message":"뿌린 사람 자신만 조회를 할 수 있습니다."}
```

