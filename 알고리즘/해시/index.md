# Java HashMap — 코딩테스트 핵심 정리

## 1. 개념

`HashMap<K, V>`은 **키(key)**와 **값(value)**을 짝지어 저장한다. 키의 `hashCode()`로 저장 위치를 찾고, 같은 위치에 들어온 키는 `equals()`로 구별한다. **키는 중복될 수 없고**, 같은 키로 다시 `put`하면 이전 값이 덮어써진다. 값은 중복될 수 있다.

```java
import java.util.HashMap;
import java.util.Map;

class Main {
    public static void main(String[] args) {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("A", 10);
        scores.put("A", 20); // A의 값이 20으로 변경
        System.out.println(scores.get("A")); // 20
    }
}
```

- 조회·삽입·삭제의 **평균 시간 복잡도는 O(1)**이다. 충돌이 심하면 더 오래 걸릴 수 있으므로 최악을 무조건 O(1)로 가정하지 않는다.
- 항목 `n`개를 저장하는 공간 복잡도는 **O(n)**이다.
- 순회 순서는 보장되지 않는다. 입력 순서가 필요하면 `LinkedHashMap`, 키 정렬이 필요하면 `TreeMap`을 사용한다. `TreeMap`의 주요 연산은 O(log n)이다.
- `HashMap`은 `null` 키 하나와 `null` 값을 허용한다. 다만 코딩테스트에서는 `null`을 별도의 의미로 쓰지 않는 편이 실수를 줄인다.
- 사용자 정의 객체를 키로 쓰면 `equals()`와 `hashCode()`를 함께 구현해야 한다. 키로 사용 중인 객체의 비교 기준 필드를 변경하면 조회가 실패할 수 있다. 문자열(`String`), 정수(`Integer`)처럼 불변인 키가 다루기 쉽다.

## 2. 자주 쓰는 메서드

아래의 시간 복잡도는 해시 충돌이 심하지 않을 때의 평균값이다. `K`, `V`는 각각 키와 값의 타입이다.

| 메서드 | 용도 | 예시 | 평균 시간 |
| --- | --- | --- | --- |
| `put(key, value)` | 저장 또는 기존 값 변경; 이전 값 반환 | `map.put("a", 1)` | O(1) |
| `putIfAbsent(key, value)` | 키가 없거나 기존 값이 `null`일 때 저장 | `map.putIfAbsent("a", 1)` | O(1) |
| `get(key)` | 값 조회; 없으면 `null` | `map.get("a")` | O(1) |
| `getOrDefault(key, defaultValue)` | 키가 없으면 기본값 반환 | `map.getOrDefault("a", 0)` | O(1) |
| `containsKey(key)` | 키 존재 여부 확인 | `map.containsKey("a")` | O(1) |
| `remove(key)` | 키와 값 삭제; 이전 값 반환 | `map.remove("a")` | O(1) |
| `size()` / `isEmpty()` | 원소 수 / 비어 있는지 확인 | `map.size()` | O(1) |
| `keySet()` / `values()` / `entrySet()` | 키 / 값 / 키·값 쌍을 순회하는 뷰 | `map.entrySet()` | 전체 순회 O(n) |
| `computeIfAbsent(key, function)` | 값이 없거나 `null`이면 계산해 저장 | `map.computeIfAbsent("a", k -> new ArrayList<>())` | 평균 O(1) + 함수 비용 |
| `merge(key, value, function)` | 키가 없으면 저장, 있으면 기존 값과 합쳐 저장 | `map.merge("a", 1, Integer::sum)` | 평균 O(1) + 함수 비용 |

```java
import java.util.HashMap;
import java.util.Map;

class Main {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("apple", 2);
        map.put("apple", map.getOrDefault("apple", 0) + 1); // 3
        map.merge("banana", 1, Integer::sum);                // 1

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
```

`getOrDefault`는 **키가 아예 없을 때만** 기본값을 반환한다. 키는 있는데 값이 `null`이면 `null`이 반환된다. 값이 `null`일 수도 있는 맵에서는 `get(key) == null`만으로 키 존재 여부를 판단하지 말고 `containsKey(key)`를 사용한다. `containsValue(value)`는 값을 찾기 위해 순회하므로 O(n)이다.

## 3. 빈출 풀이 패턴

### 빈도 세기: 같은 값이 몇 번 나왔는가?

문자열 배열에서 각 단어의 등장 횟수를 센다. 전체 시간 O(n), 추가 공간 O(k) (`k`: 서로 다른 단어 수).

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    Map<String, Integer> countWords(String[] words) {
        Map<String, Integer> count = new HashMap<>();
        for (String word : words) {
            count.put(word, count.getOrDefault(word, 0) + 1);
            // 같은 뜻: count.merge(word, 1, Integer::sum);
        }
        return count;
    }
}
```

예: `["a", "b", "a"]` → `a: 2`, `b: 1` (출력·순회 순서는 달라질 수 있다). 완주하지 못한 선수 찾기, 중복 횟수 확인, 애너그램 판별 등에 응용한다.

### 보수 찾기: 합이 target인 두 수의 인덱스

현재 값 `x`를 볼 때 앞서 본 값 중 `target - x`가 있는지 확인한다. **조회한 뒤 현재 값을 저장**하면 같은 원소를 두 번 선택하지 않는다. 평균 시간 O(n), 추가 공간 O(n).

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>(); // 숫자 -> 인덱스
        for (int i = 0; i < nums.length; i++) {
            int need = target - nums[i];
            if (seen.containsKey(need)) {
                return new int[] {seen.get(need), i};
            }
            seen.put(nums[i], i);
        }
        return new int[0]; // 정답이 없을 때
    }
}
```

예: `nums = [3, 3]`, `target = 6` → `[0, 1]`.

### 그룹화: 한 키에 여러 값 모으기

분류 기준을 키로, 그 기준에 속하는 원소 목록을 값으로 저장한다. 평균 시간 O(n), 추가 공간 O(n).

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Solution {
    Map<Integer, List<String>> groupByLength(String[] words) {
        Map<Integer, List<String>> groups = new HashMap<>();
        for (String word : words) {
            groups.computeIfAbsent(word.length(), key -> new ArrayList<>())
                  .add(word);
        }
        return groups;
    }
}
```

예: `["a", "bb", "c"]` → 길이 `1` 그룹에 `["a", "c"]`, 길이 `2` 그룹에 `["bb"]`.

## 4. 실전 체크

- 존재 여부만 필요하면 `HashSet`을 사용한다. 값이 필요할 때 `HashMap`을 사용한다.
- `Map<Integer, Integer>`처럼 제네릭에는 기본형 `int` 대신 래퍼형 `Integer`를 쓴다.
- 같은 키의 값을 반복해서 읽을 때 `entrySet()`으로 순회하면 키와 값을 함께 얻을 수 있다.
- 결과에 정렬된 순서가 필요하다면 키를 따로 정렬하거나 `TreeMap`을 선택한다.
