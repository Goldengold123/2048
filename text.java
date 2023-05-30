import java.util.Scanner;

public class text {

    static int winScore = 2048;
    static boolean win = false;

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
            if (arr[i] == winScore)
                win = true;
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

    public static int[][] copyArray(int[][] arr) {
        int[][] tmpArr = new int[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr[0].length; j++)
                tmpArr[i][j] = arr[i][j];
        return tmpArr;
    }

    public static boolean sameArray(int[][] arr1, int[][] arr2) {
        if (arr1.length != arr2.length || arr1[0].length != arr2[0].length)
            return false;
        for (int i = 0; i < arr1.length; i++)
            for (int j = 0; j < arr1[0].length; j++)
                if (arr1[i][j] != arr2[i][j])
                    return false;
        return true;
    }

    public static boolean isAlive(int[][] arr) {
        int[][] tmpArr;

        tmpArr = copyArray(arr);
        left(tmpArr);
        if (!sameArray(arr, tmpArr))
            return true;

        tmpArr = copyArray(arr);
        right(tmpArr);
        if (!sameArray(arr, tmpArr))
            return true;

        tmpArr = copyArray(arr);
        up(tmpArr);
        if (!sameArray(arr, tmpArr))
            return true;

        tmpArr = copyArray(arr);
        down(tmpArr);
        if (!sameArray(arr, tmpArr))
            return true;

        return false;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean alive = true;
        boolean moved = false;
        boolean asked = false;
        int BOARD_SIZE = 4;
        int board[][] = new int[BOARD_SIZE][BOARD_SIZE];
        int tmpBoard[][];
        char m;

        // { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 4, 0, 0, 0 }, { 8, 8, 2, 2 } };

        win = false;

        fillRandom(board, 2);

        while (alive) {
            if (win && !asked) {
                asked = true;
                System.out.println("You reached " + winScore + "! Enter q to quit or p to continue playing.");
                in.nextLine();
                while (true) {
                    m = in.nextLine().charAt(0);
                    if (m == 'q' || m == 'Q') {
                        alive = false;
                        break;
                    }
                    if (m == 'p' || m == 'P') {
                        break;
                    }
                }
            }
            moved = false;
            fillRandom(board, ((Math.random() < 0.75) ? 2 : 4));
            drawBoard(board);
            if (!isAlive(board))
                alive = false;
            if (!alive)
                break;
            tmpBoard = copyArray(board);
            while (!moved) {
                m = in.next().charAt(0);
                if (m == 'l' || m == 'L')
                    left(board);
                else if (m == 'r' || m == 'R')
                    right(board);
                else if (m == 'u' || m == 'U')
                    up(board);
                else if (m == 'd' || m == 'D')
                    down(board);
                else if (m == 'q' || m == 'Q') {
                    alive = false;
                    break;
                }
                moved = !sameArray(board, tmpBoard);
            }
        }
        if (win)
            System.out.println("WIN");
        else
            System.out.println("DEAD");
        in.close();
    }
}
