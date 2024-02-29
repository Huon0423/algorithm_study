import java.io.*;
import java.util.*;

class Room {
	int x, y, dust;
	int added;

	Room(int x, int y, int d) {
		this.x = x;
		this.y = y;
		this.dust = d;
	}
}

public class Main {
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	static int N, M, T;
	static Room[][] rooms;
	static int[] dx = { 0, 1, 0, -1 }; // 오=>아래=>왼=>위
	static int[] dy = { 1, 0, -1, 0 };
	static int row;
	static int answer; /// 먼지의 양

	public static void main(String[] args) throws Exception {
		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		T = Integer.parseInt(st.nextToken());
		answer = 0;

		rooms = new Room[N][M];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				rooms[i][j] = new Room(i, j, Integer.parseInt(st.nextToken()));
				if (rooms[i][j].dust == -1) {
					row = i;
				}
			}
		}

		for (int t = 0; t < T; t++) {
			diffuseDust();
			timeSpaceBlast();
		}

		for (Room[] rs : rooms) {
			for (Room r : rs) {
				if (r.dust == -1) {
					continue;
				}
				answer += r.dust;
			}
		}

		System.out.println(answer);
	}

	// 확산을 일으키며 먼지가 다음 칸에 영향
	static void diffuseDust() {
		for (Room[] rs : rooms) {
			for (Room r : rs) {
				int count = 0; // 확산된 방의 수
				int diffused = (int) (r.dust / 5);
				for (int v = 0; v < 4; v++) {
					int nx = r.x + dx[v]; // 상하좌우 확인
					int ny = r.y + dy[v];
					if (!range(nx, ny) || rooms[nx][ny].dust == -1) {
						continue;
					}
					rooms[nx][ny].added += diffused;
					count++;
				}
				r.dust -= count * diffused;
			}
		}
		// 확산이 끝나고, 먼지 더해주기
		for (Room[] rs : rooms) {
			for (Room r : rs) {
				r.dust += r.added;
				r.added = 0;
			}
		}
	}

	// 시공의 돌풍이 먼지를 밀어낼 거야~
	static void timeSpaceBlast() {
		int upX = row - 1;
		int upY = 1;
		int upDust = rooms[upX][upY].dust;
		int tmp;
		int downX = row;
		int downY = 1;
		int downDust = rooms[downX][downY].dust;
		int v = 0;

		// 위로 돌기 => 반시계방향
		while (true) {
			int nx = upX + dx[v];
			int ny = upY + dy[v];
			if (!range(nx, ny)) {
				v = (v + 3) % 4;
				continue;
			}
			if (rooms[nx][ny].dust == -1) {
				rooms[nx][ny + 1].dust = 0;
				break;
			}
			tmp = rooms[nx][ny].dust;
			rooms[nx][ny].dust = upDust;
			upDust = tmp;
			upX = nx;
			upY = ny;
		}

		v = 0;
		// 아래로 돌기 => 시계방향
		while (true) {
			int nx = downX + dx[v];
			int ny = downY + dy[v];
			if (!range(nx, ny)) {
				v = (v + 1) % 4;
				continue;
			}
			if (rooms[nx][ny].dust == -1) {
				rooms[nx][ny + 1].dust = 0;
				break;
			}
			tmp = rooms[nx][ny].dust;
			rooms[nx][ny].dust = downDust;
			downDust = tmp;
			downX = nx;
			downY = ny;
		}
	}

	// 범위를 벗어났는지 검사
	static boolean range(int x, int y) {
		return (0 <= x && x < N && 0 <= y && y < M);
	}
}
