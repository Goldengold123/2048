import java.util.Scanner;

public class Main {

    public static void fillRandom(int[][] arr, int val) {
        int sx, sy;
        do {
            sx = (int) (Math.random() * arr.length);
            sy = (int) (Math.random() * arr.length);
        } while (arr[sx][sy] != 0);
        arr[sx][sy] = val;
    }

    public static void drawBoard(int[][] arr) {
        for (int[] line : arr) {
            for (int cell : line)
                System.out.print(cell + " ");
            System.out.println();
        }
        System.out.println();
    }

    public static int moveZerosToBack(int[] arr) {
        int nonZeros = 0;
        for (int j = 0; j < arr.length; j++) {
            if (arr[j] != 0) {
                arr[nonZeros++] = arr[j];
            }
        }
        for (int j = nonZeros; j < arr.length; j++)
            arr[j] = 0;
        return nonZeros;
    }

    public static void slideArrLeft(int[] arr) {
        int cnt;
        cnt = moveZerosToBack(arr);
        for (int i = 0; i + 1 < cnt; i++) {
            if (arr[i] == arr[i + 1]) {
                arr[i] *= 2;
                arr[i + 1] = 0;
            }
        }
        cnt = moveZerosToBack(arr);
    }

    public static void reflectRow(int[] arr) {
        for (int i = 0; 2 * i < arr.length; i++) {
            arr[i] += arr[arr.length - i - 1];
            arr[arr.length - i - 1] = arr[i] - arr[arr.length - i - 1];
            arr[i] -= arr[arr.length - i - 1];
        }
    }

    public static void reflect(int[][] arr) {
        for (int i = 0; i < arr.length; i++)
            reflectRow(arr[i]);
    }

    public static void rotateCCW(int[][] arr) {
        int[][] tmp = new int[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                tmp[arr.length - 1 - j][i] = arr[i][j];
            }
        }
        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr[0].length; j++)
                arr[i][j] = tmp[i][j];
    }

    public static void rotateCW(int[][] arr) {
        int[][] tmp = new int[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                tmp[j][arr.length - i - 1] = arr[i][j];
            }
        }
        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr[0].length; j++)
                arr[i][j] = tmp[i][j];
    }

    public static void left(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            slideArrLeft(arr[i]);
        }
    }

    public static void right(int[][] arr) {
        reflect(arr);
        left(arr);
        reflect(arr);
    }

    public static void up(int[][] arr) {
        rotateCCW(arr);
        left(arr);
        rotateCW(arr);
    }

    public static void down(int[][] arr) {
        rotateCW(arr);
        left(arr);
        rotateCCW(arr);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean alive = true;
        char m;
        int BOARD_SIZE = 4;
        int board[][] = new int[BOARD_SIZE][BOARD_SIZE];
        fillRandom(board, 2);
        while (alive) {
            fillRandom(board, ((Math.random() < 0.75) ? 2 : 4));
            drawBoard(board);
            m = in.next().charAt(0);
            if (m == 'l' || m == 'L')
                left(board);
            else if (m == 'r' || m == 'R')
                right(board);
            else if (m == 'u' || m == 'U')
                up(board);
            else if (m == 'd' || m == 'D')
                down(board);
            else if (m == 'q' || m == 'Q')
                alive = false;
        }
        in.close();
    }
}
