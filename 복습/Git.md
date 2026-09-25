# Git에서 생성 파일 관리하기

## `.gitignore`

`.gitignore`는 Git이 아직 추적하지 않는 파일 중 버전 관리에서 제외할 경로를 지정한다. Java의 `*.class`, IntelliJ의 `out/`, Gradle의 `.gradle/`처럼 다시 생성할 수 있는 빌드 결과와 사용자별 파일을 보통 제외한다.

```gitignore
*.class
/out/
.gradle/
```

- `*.class`: 모든 하위 경로의 Java 바이트코드 파일을 제외한다.
- `/out/`: 저장소 루트의 `out` 디렉터리만 제외한다.
- `.gradle/`: 어느 위치에서든 이름이 `.gradle`인 경로를 제외한다.

## 이미 추적 중인 파일

`.gitignore`는 이미 Git이 추적 중인 파일에는 소급 적용되지 않는다. 추적만 해제하고 로컬 파일은 남기려면 다음 명령을 사용한다.

```bash
git rm --cached 경로
```

그 뒤 ignore 규칙을 추가하면 이후 컴파일로 만들어진 파일이 다시 추적되지 않는다.
