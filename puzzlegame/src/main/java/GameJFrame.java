import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Random;

public class GameJFrame extends JFrame implements KeyListener, ActionListener {
    // 常量定义
    private static final String IMAGE_PATH = "puzzlegame/image/animal/animal3/";
    private static final String BACK_GROUND_IMAGE = "puzzlegame/image/background.png";
    private static final String WIN_GAME_IMAGE = "puzzlegame/image/win.png";
    private static final String ABOUT_IMAGE = "puzzlegame/image/about.png";

    // 游戏配置
    private static final int GRID_SIZE = 4;
    private static final int CELL_SIZE = 105;
    private static final int IMAGE_OFFSET_X = 83;
    private static final int IMAGE_OFFSET_Y = 134;

    // 游戏状态
    private int[][] imageOrderData = new int[GRID_SIZE][GRID_SIZE];
    private int blankX; // 空白方块X坐标
    private int blankY; // 空白方块Y坐标
    private int stepCount = 0;
    private boolean gameWon = false;

    // 菜单项
    private final JMenuItem replayItem = new JMenuItem("重新游戏");
    private final JMenuItem reLoginItem = new JMenuItem("重新登录");
    private final JMenuItem closeItem = new JMenuItem("关闭游戏");
    private final JMenuItem accountItem = new JMenuItem("公众号");

    // 获胜数据
    private final int[][] winData = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}
    };

    // 步数标签
    private final JLabel stepCountLabel = new JLabel("步数: 0");

    public GameJFrame() {
        initJFrame();
        initJMenuBar();
        initData();
        initImage();
        this.setVisible(true);
    }

    private void initData() {
        int[] data = new int[GRID_SIZE * GRID_SIZE];
        for (int i = 0; i < data.length; i++) {
            data[i] = i;
        }

        Random random = new Random();
        // Fisher-Yates 洗牌算法
        for (int i = data.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = data[index];
            data[index] = data[i];
            data[i] = temp;
        }

        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0) {
                blankX = i / GRID_SIZE;
                blankY = i % GRID_SIZE;
            }
            imageOrderData[i / GRID_SIZE][i % GRID_SIZE] = data[i];
        }

        // 重置游戏状态
        stepCount = 0;
        gameWon = false;
        updateStepCount();
    }

    private void initImage() {
        this.getContentPane().removeAll();

        // 添加步数标签
        stepCountLabel.setBounds(50, 30, 100, 20);
        this.getContentPane().add(stepCountLabel);

        // 如果游戏胜利，显示胜利图片
        if (gameWon) {
            addImage(WIN_GAME_IMAGE);
        } else {
            // 添加拼图图片
            for (int x = 0; x < GRID_SIZE; x++) {
                for (int y = 0; y < GRID_SIZE; y++) {
                    if (imageOrderData[x][y] != 0) { // 空白块不显示图片
                        JLabel imageLabel = new JLabel(new ImageIcon(IMAGE_PATH + imageOrderData[x][y] + ".jpg"));
                        imageLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
                        imageLabel.setBounds(CELL_SIZE * y + IMAGE_OFFSET_X,
                                CELL_SIZE * x + IMAGE_OFFSET_Y,
                                CELL_SIZE, CELL_SIZE);
                        this.getContentPane().add(imageLabel);
                    }
                }
            }
        }

        // 添加背景
        addImage(BACK_GROUND_IMAGE);
        this.getContentPane().repaint();
    }

    private void addImage(String path) {
        JLabel backGroundLabel = new JLabel(new ImageIcon(path));
        backGroundLabel.setBounds(40, 40, 508, 560);
        this.getContentPane().add(backGroundLabel);
    }

    private void initJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu functionJMenu = new JMenu("功能");
        JMenu aboutJMenu = new JMenu("关于");

        // 绑定条目点击事件
        replayItem.addActionListener(this);
        reLoginItem.addActionListener(this);
        closeItem.addActionListener(this);
        accountItem.addActionListener(this);

        // 组装按钮
        functionJMenu.add(replayItem);
        functionJMenu.add(reLoginItem);
        functionJMenu.add(closeItem);
        aboutJMenu.add(accountItem);

        menuBar.add(functionJMenu);
        menuBar.add(aboutJMenu);
        this.setJMenuBar(menuBar);
    }

    private void initJFrame() {
        this.setSize(608, 680);
        this.setTitle("拼图游戏");
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // 不需要实现
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            // 显示完整图片
            this.getContentPane().removeAll();
            JLabel allImage = new JLabel(new ImageIcon(IMAGE_PATH + "complete.jpg")); // 假设有完整图片
            allImage.setBounds(83, 134, 508, 560);
            addImage(BACK_GROUND_IMAGE);
            this.getContentPane().repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameWon) {
            return; // 游戏已胜利，不再响应按键
        }

        int keyCode = e.getKeyCode();
        boolean moved = false;

        switch (keyCode) {
            case KeyEvent.VK_UP:
                moved = moveTile(blankX + 1, blankY); // 尝试移动下方的方块
                break;
            case KeyEvent.VK_DOWN:
                moved = moveTile(blankX - 1, blankY); // 尝试移动上方的方块
                break;
            case KeyEvent.VK_LEFT:
                moved = moveTile(blankX, blankY + 1); // 尝试移动右方的方块
                break;
            case KeyEvent.VK_RIGHT:
                moved = moveTile(blankX, blankY - 1); // 尝试移动左方的方块
                break;
            case KeyEvent.VK_W:
                // 直接胜利（作弊键）
                imageOrderData = Arrays.stream(winData).map(int[]::clone).toArray(int[][]::new);
                blankX = GRID_SIZE - 1;
                blankY = GRID_SIZE - 1;
                moved = true;
                break;
        }

        if (moved) {
            stepCount++;
            updateStepCount();
            checkWin();
            initImage();
        }
    }

    private boolean moveTile(int tileX, int tileY) {
        // 检查是否在边界内
        if (tileX < 0 || tileX >= GRID_SIZE || tileY < 0 || tileY >= GRID_SIZE) {
            System.out.println("无法移动");
            return false;
        }

        // 交换空白块和目标块
        imageOrderData[blankX][blankY] = imageOrderData[tileX][tileY];
        imageOrderData[tileX][tileY] = 0;

        // 更新空白块位置
        blankX = tileX;
        blankY = tileY;

        return true;
    }

    private void updateStepCount() {
        stepCountLabel.setText("步数: " + stepCount);
    }

    private void checkWin() {
        gameWon = Arrays.deepEquals(imageOrderData, winData);
        if (gameWon) {
            System.out.println("恭喜你赢了！步数: " + stepCount);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == replayItem) {
            initData();
            initImage();
        } else if (source == reLoginItem) {
            this.setVisible(false);
            new LoginJFrame(); // 假设有LoginJFrame类
        } else if (source == closeItem) {
            System.exit(0);
        } else if (source == accountItem) {
            showAboutDialog();
        }
    }

    private void showAboutDialog() {
        JDialog dialog = new JDialog();
        JLabel jLabel = new JLabel(new ImageIcon(ABOUT_IMAGE));
        jLabel.setBounds(0, 0, 258, 258);
        dialog.getContentPane().add(jLabel);
        dialog.setSize(344, 344);
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        dialog.setVisible(true);
    }
}