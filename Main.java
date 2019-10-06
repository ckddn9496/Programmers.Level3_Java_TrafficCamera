import java.util.Comparator;
import java.util.PriorityQueue;

public class Main {

	public static void main(String[] args) {
		int[][] routes = {{-20,15},{-14,-5},{-18,-13},{-5,-3}};
		// return 2
		
		System.out.println(new Solution().solution(routes));
	}

}

class Solution {
	
	class Route {
		int start, end;
		public Route(int start, int end) {
			this.start = start;
			this.end = end;
		}
		@Override
		public String toString() {
			return "("+ start + ", " + end +  ")";
		}
	}
	
	Comparator<Route> compByStart = new Comparator<Route>() {
		public int compare(Route r1, Route r2) {
			return r1.start - r2.start;
		}
	};

	Comparator<Route> compByEnd = new Comparator<Route>() {
		public int compare(Route r1, Route r2) {
			return r1.end - r2.end;
		}
	};
	
	public int solution(int[][] routes) {
		int numOfCamera = 0;
		PriorityQueue<Route> startRouteHeap = new PriorityQueue<>(compByStart);
		PriorityQueue<Route> endRouteHeap = new PriorityQueue<>(compByEnd);
		
		for (int[] route : routes) {
			startRouteHeap.add(new Route(route[0], route[1]));
		}
		int curPosition = startRouteHeap.peek().start;
		while (!startRouteHeap.isEmpty()) {
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
		}
		if (!endRouteHeap.isEmpty())
			numOfCamera++;
		
		return numOfCamera;
	}
}