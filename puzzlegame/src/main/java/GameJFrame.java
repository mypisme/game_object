import javax.swing.*;

public class GameJFrame extends JFrame {
    public GameJFrame() {
        this.setSize(608, 680);

        // 设置界面
        this.setTitle("拼图游戏");
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 设置菜单


        this.setVisible(true);
    }
}
