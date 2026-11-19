# Camorix 낙상 감지 데모

실제 환자 정보나 PostgreSQL/OAuth 계정 없이 Camorix의 환자 조회, 카메라 프레임 수신,
위험·낙상 알림(SSE), 이벤트 이력 저장 흐름을 로컬에서 확인할 수 있는 데모입니다.

## 준비 사항

- Java 21
- 인터넷 연결(최초 실행 시 Gradle과 라이브러리 다운로드에만 필요)

## 실행

Windows에서는 프로젝트 폴더에서 `run-demo.bat`을 실행합니다.

```powershell
.\run-demo.bat
```

macOS/Linux에서는 다음을 실행합니다.

```bash
chmod +x gradlew run-demo.sh
./run-demo.sh
```

서버가 시작되면 브라우저에서 <http://localhost:8080>을 엽니다. 대시보드에서 병실을
선택하고 `정상`, `주의`, `위험`, `낙상` 버튼을 누르면 실제 `/realtime/frame` API로
임시 카메라 프레임을 전송합니다. `위험`과 `낙상`은 SSE 알림과 이벤트 이력을 생성합니다.

H2 데이터베이스는 메모리에서만 동작하므로 서버를 종료하면 모든 데모 데이터가
초기화됩니다.

## 포함된 임시 데이터

- 301호: BED-1 김영수, BED-2 박정희
- 302호: BED-1 이순자, BED-2 최철호
- 과거 주의·위험·낙상 이벤트 3건

모든 이름, 연락처, 주소, 이메일은 테스트 전용 가상 데이터입니다.

## 주요 API

| 기능 | 메서드와 경로 |
| --- | --- |
| 전체 환자 | `GET /patient` |
| 환자 한 명 | `GET /patient/load/{roomNumber}/{bedId}` |
| 프레임 수신 | `POST /realtime/frame` |
| 알림 SSE | `GET /realtime/stream/notify?tabId={id}` |
| 실시간 프레임 SSE | `GET /realtime/stream/realtime?tabId={id}` |
| 이벤트 이력 | `GET /realtime/event` |
| H2 콘솔 | `GET /h2-console` |

H2 콘솔 접속 정보는 JDBC URL `jdbc:h2:mem:camorix-demo`, 사용자 `sa`, 빈 비밀번호입니다.

## 자동 테스트

```powershell
.\gradlew.bat test
```

테스트는 데모 프로필로 실행되며 임시 환자 조회와 낙상 프레임 수신·이력 저장을 검증합니다.

## 운영 환경 주의

데모 프로필은 인증을 끈 상태이므로 외부에 공개하지 마세요. 운영 실행 시에는 `demo`
프로필을 사용하지 말고, `application.yml`에 표시된 `DB_*`, OAuth, `JWT_SECRET` 환경
변수를 실제 값으로 설정해야 합니다. 원본 ZIP에 들어 있던 고정 DB/OAuth/JWT 값은
보안을 위해 환경 변수 자리표시자로 교체했습니다.
