import random


BOARD_SIZE = 4

board = [[0 for i in range(BOARD_SIZE)] for j in range(BOARD_SIZE)]
# board = [
#     [8, 4, 2, 2, 2, 2, 4, 2],
#     [0, 0, 0, 0, 0, 0, 0, 0],
#     [0, 0, 0, 0, 0, 0, 0, 0],
#     [0, 0, 0, 0, 0, 0, 0, 0],
#     [0, 0, 0, 0, 0, 0, 0, 0],
#     [0, 0, 0, 0, 0, 0, 0, 0],
#     [0, 0, 0, 0, 0, 0, 0, 0],
#     [0, 0, 0, 0, 0, 0, 0, 0]
# ]

sx1 = random.randint(0, BOARD_SIZE-1)
sy1 = random.randint(0, BOARD_SIZE-1)

board[sx1][sy1] = 2


def drawBoard():
    for i in board:
        for j in i:
            print(j, end=" ")
        print()
    print()
    # print(board[0])


def left():
    for i in range(BOARD_SIZE):
        while 0 in board[i]:
            board[i].remove(0)
        idx = 0
        while (idx+1 < len(board[i])):
            if board[i][idx] == board[i][idx+1]:
                board[i][idx] *= 2
                board[i][idx+1] = 0
            idx += 1
        board[i] = board[i] + [0 for i in range(BOARD_SIZE - len(board[i]))]

def reflect():
    for i in range(BOARD_SIZE):
        board[i] = board[i][::-1]

def rotateCCW():
    tmp = [[0 for i in range(BOARD_SIZE)] for j in range(BOARD_SIZE)]
    for i in range(BOARD_SIZE):
        for j in range(BOARD_SIZE):
            tmp[BOARD_SIZE-1-j][i] = board[i][j]
    for i in range(BOARD_SIZE):
        for j in range(BOARD_SIZE):
            board[i][j] = tmp[i][j]

def rotateCW():
    tmp = [[0 for i in range(BOARD_SIZE)] for j in range(BOARD_SIZE)]
    for i in range(BOARD_SIZE):
        for j in range(BOARD_SIZE):
            tmp[j][BOARD_SIZE-1-i] = board[i][j]
    for i in range(BOARD_SIZE):
        for j in range(BOARD_SIZE):
            board[i][j] = tmp[i][j]


alive = True

while alive:
    sx = random.randint(0, BOARD_SIZE-1)
    sy = random.randint(0, BOARD_SIZE-1)
    while board[sx][sy] != 0:
        sx = random.randint(0, BOARD_SIZE-1)
        sy = random.randint(0, BOARD_SIZE-1)
    board[sx][sy] = 2

    drawBoard()

    m = input()

    if m == 'l':
        left()
    elif m == 'r':
        reflect()
        left()
        reflect()
    elif m == 'u':
        rotateCCW()
        left()
        rotateCW()
    elif m == 'd':
        rotateCW()
        left()
        rotateCCW()


