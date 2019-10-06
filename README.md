# Programmers.Level3_Java_TrafficCamera

## 프로그래머스 > 탐욕법(Greedy) > 단속카메라

### 1. 문제설명

문제: https://programmers.co.kr/learn/courses/30/lessons/42884

input으로 도로를 지나는 차랑의 경로 정보가 `int[][] routes`로 주어진다. 이때, 모든 차량이 한 번은 단속용 카메라를 만나도록 하려면 최소 몇 대의 카메라를 설치해야 하는지를 return하는 문제.

* 제한사항
> 차량의 진입, 진출 지점에 카메라가 설치되어 있어도 카메라를 만난것으로 간주한다.

### 2. 풀이

입력으로 들어오는 `routes`에 대해, `Route`클래스를 생성하고 우선순위 큐를 두어 도로의 진입점에 대하여 정렬된 자료를 가질 `PriorityQueue`에 담는다.
```java
class Route {
  int start, end;
  public Route(int start, int end) {
    this.start = start;
    this.end = end;
  }
}
  
Comparator<Route> compByStart = new Comparator<Route>() {
  public int compare(Route r1, Route r2) {
    return r1.start - r2.start;
  }
};
 
public int solution(int[][] routes) {
  ...
  PriorityQueue<Route> startRouteHeap = new PriorityQueue<>(compByStart);

  for (int[] route : routes) {
    startRouteHeap.add(new Route(route[0], route[1]));
  }
  ... 
}
```

우선순위 큐에서 첫번째 `Route`의 시작위치부터, 더 이상 `startRouteHeap.isEmpty()`가 될때까지 반복하여 검사를 진행한다. 검사는 차량이 진입할 때 `startRouteHeap`에서 진입하는 차량을 `poll()`하여 진출위치에 따라 오름차순으로 정렬할 우선순위 힙 `endRouteHeap`에 넣어주는 행위와, 차량이 진출할 때 카메라를 설치하고 `endRouteHeap`을 비워주는 행위로 구성된다. 차량이 진출할 때 카메라를 설치하고 힙을 비워주는 이유는 진입한 차량 중 가장 앞서서 진출하는 위치 전에 카메라를 설치한다면 모든 `endRouteHeap`에 존재하는 차량에 대해서도 단속이 가능하기 때문이다. 즉, 남은 `endRouteHeap`에 존재하는 차량에 대해서는 더 이상의 추가 단속카메라가 필요없기 때문에 고려대상에서 제외하는것과 같은 행위다.


```java
Comparator<Route> compByEnd = new Comparator<Route>() {
  public int compare(Route r1, Route r2) {
    return r1.end - r2.end;
  }
};

public int solution(int[][] routes) {
  ...
  int numOfCamera = 0;
  PriorityQueue<Route> endRouteHeap = new PriorityQueue<>(compByEnd);

  for (int[] route : routes) {
    startRouteHeap.add(new Route(route[0], route[1]));
  }
  int curPosition = startRouteHeap.peek().start;
  while (!startRouteHeap.isEmpty()) {
    // start 검사
    if (curPosition == startRouteHeap.peek().start) {
      endRouteHeap.add(startRouteHeap.poll());
      while (!startRouteHeap.isEmpty() && curPosition == startRouteHeap.peek().start) {
        endRouteHeap.add(startRouteHeap.poll());
      }
    }
    // end 검사
    if (!endRouteHeap.isEmpty() && curPosition == endRouteHeap.peek().end) {
      numOfCamera++;
      endRouteHeap.clear();
    }
    curPosition++;
   ...
}
```

더 이상 진입할 차량이 없다면 반복을 종료하고, `endRouteHeap`에 남아있는 차량이 있는지 확인한다. 이 차량들은 아직 단속카메라로 검사할 수 없는 위치에 있는 경로위의 차량들이므로 이를 위해 카메라를 한 대 추가한다. 만약 `endRouteHeap.isEmpty()`일 경우 추가하지않고 카메라를 return한다.

```java
if (!endRouteHeap.isEmpty())
    numOfCamera++;

  return numOfCamera;
```

### 3. 느낀점

다른사람의 풀이를 보며 두 가지의 개선점이 있었다.

첫번째, 우선순위 큐를 이용하지 않아도 되었다. 진출위치에 대한 우선순위큐는 진입하는 차량이 있을 때 마다 `minPosition`과 같이 가장 일찍 진출하는 차량의 위치를 기록해두어 해결 가능하며, 진입위치에 대한 우선순위 큐 또한 `Arrays.sort()`를 이용하여 정렬 후 순차적으로 읽어서 해결할 수 있다.

두번째, 모든 위치에 대하여 조사하지 않아도 되었다. `curPosition++`을 하는 대신 진입위치에 대하여 정렬된 `routes`의 `진입 위치`를 순차적으로 가져오며, 위의 방법으로 기록한 `가장 일찍 진출하는 차량의 위치`보다 뒤에 있다면 카메라를 추가하는 식으로 구현 가능하다. `==`대신 `<`연산자로 더 많은 반복을 줄일 수 있다. 
