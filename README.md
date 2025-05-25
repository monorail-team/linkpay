
# LinkPay
<img src ="https://github.com/user-attachments/assets/f2186103-7ab9-4f5a-8567-f8d3f2eddd68" width = 700px>

# 서비스 소개

## 결제
<img src ="https://github.com/user-attachments/assets/98223683-6617-49d6-af0d-9932036bda72" width = 700px>


<img src ="https://github.com/user-attachments/assets/de0e3e8e-a60f-4b57-bc61-af0834fa1123" width = 700px>

<img src ="https://github.com/user-attachments/assets/1b28da07-b935-4c21-a53a-d3506971fd6b" width = 700px>

<img src ="https://github.com/user-attachments/assets/31a55831-5544-4d9c-a498-f107e2e09334" width = 700px>



## 정산 

<img src ="https://github.com/user-attachments/assets/cd8d68d2-3f84-4de2-8fba-c4a4506c7899" width = 700px>



## 멀티 모듈

<img src ="https://github.com/user-attachments/assets/b319ea15-a8ad-4cda-bee1-e637578084f2" width = 700px>

#### 1. LinkPay (메인 서버)

> 사용자 지갑, 카드, 결제 요청 등 링크페이 서비스를 담당

- linkpay-api
  - 외부 요청을 처리
- linkpay-domain
  - 비즈니스 규칙과 도메인 모델을 정의하는 핵심 계층
- linkpay-in-system-available
  - 시스템 기능을 보조하는 내부 전용 모듈
- linkpay-support
  - 공통 유틸리티

------

#### 2. 정산 서버

> 정산용 배치 작업 처리

- linkpay-batch/settlement
- domain
- in-system-available
- support

------

#### 3. 은행 서버

> 외부 은행 서버로 계좌 생성, 입출금 등을 처리

- linkpay-banking
- in-system-available
- support

------

#### 4. Config 서버 (Private Git Repo 사용)

> 각 서비스의 설정 파일을 중앙에서 관리하고 제공하는 설정 서버입니다.



## 아키텍처

<img src ="https://github.com/user-attachments/assets/800d6b34-d64e-4adb-98cc-7c32a3d0b7f7" width = 700px>

## 사용 기술
### FE
<img src ="https://github.com/user-attachments/assets/4e798f49-7453-43eb-9657-6d28fe00bcc0" width = 700px>


### BE
<img src ="https://github.com/user-attachments/assets/035e9a43-b4c8-4d77-981b-dc27cae9e457" width = 700px>

### INFRA
<img src ="https://github.com/user-attachments/assets/c5fc6396-43c1-46b1-8f20-3647ab8bbf63" width = 700px>



# 팀원 소개

<!-- ![055](https://github.com/user-attachments/assets/267b3786-ff0d-4b72-94d1-f9e2610aa144) -->

<table>
  <tbody>
    <tr>
      <td align="center"><a href="https://github.com/Cheonwooo"><img src="https://avatars.githubusercontent.com/u/82378975?v=4" width="100px;" alt=""/></a></td>
      <td align="center"><a href="https://github.com/JJBINY"><img src="https://avatars.githubusercontent.com/u/97151887?v=4" width="100px;" alt=""/></a></td>
        <td align="center"><a href="https://github.com/elsa-kim"><img src="https://avatars.githubusercontent.com/u/116364101?v=4" width="100px;" alt=""/></a></td>
        <td align="center"><a href="https://github.com/mwjng"><img src="https://avatars.githubusercontent.com/u/87010483?v=4" width="100px;" alt=""/></a></td>
      <td align="center"><a href="https://github.com/GuKBABjOa"><img src="https://avatars.githubusercontent.com/u/128679235?v=4" width="100px;" alt=""/></a></td>
      <td align="center"><a href="https://github.com/PKafka0320"><img src="https://avatars.githubusercontent.com/u/66552990?v=4" width="100px;" alt=""/></a></td>
    </tr>
    <tr>
      <td align="center"><a href="https://github.com/Cheonwooo"><b> 천현우 </b></a><br />팀장, Infra<br /></td>
      <td align="center"><a href="https://github.com/JJBINY"><b> 강주빈 </b></a><br />BE, FE<br /></td>
      <td align="center"><a href="https://github.com/elsa-kim"><b> 김소현 </b></a><br />BE<br /></td>
      <td align="center"><a href="https://github.com/mwjng"><b> 정민우 </b></a><br />BE<br /></td>
      <td align="center"><a href="https://github.com/GuKBABjOa"><b> 진성일 </b></a><br />FE<br /></td>
      <td align="center"><a href="https://github.com/PKafka0320"><b> 최성환 </b></a><br />BE<br /></td>
    </tr>
  </tbody>
</table>


# 세부 기술 소개

## 지문 인증

결제를 진행할 때, 사용자는 스마트폰의 지문 인식 기능을 통해 본인임을 인증합니다. 여기서 사용자의 지문 정보가 서버로 전송되지 않고, 스마트폰 내부에 저장된 비밀 키(private key)가 사용됩니다. 이 키로 서버가 발급한 무작위 값(challenge)에 전자서명을 하여 본인 여부를 증명하게 됩니다.

## NFC 결제

사용자가 매장에서 결제할 때, 스마트폰을 결제 단말기에 가까이 대면(NFC) 결제 정보가 스마트폰으로 전송됩니다.
결제 정보는 서명된 형식으로 만들어져 위조가 불가능하고 스마트폰은 해당 정보를 받아 지문 인증을 통해 사용자가 실제로 결제를 승인했는지 확인합니다. 사용자의 서명과 함께 결제 정보가 서버로 전송되어 결제 내역의 무결성 검증 및 처리가 이루어집니다. 카드를 꺼낼 필요 없이 폰 하나만으로 오프라인에서도 빠르고 안전하게 결제할 수 있는 방식입니다.

## 정산 배치
정산 배치 작업은 하루 동안 발생한 결제 내역을 수집하여 각 가맹점에 얼마를 지급해야 하는지 자동으로 계산하고 저장하는 작업입니다. 실제로 송금을 하지는 않고 계산된 정산 금액을 정산 테이블에 저장합니다. 정산 로직은 스케줄링된 배치 작업으로 실행되고 트랜잭션 처리 및 데이터 정합성을 보장하기 위해 Outbox 패턴 등을 활용해 안전하게 처리됩니다.
