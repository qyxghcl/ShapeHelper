
import javax.swing.*;
import java.awt.event.*;

public class MyDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JLabel cornerLabel;
    private MyDialogCallBack mCallBack;

    public MyDialog(MyDialogCallBack callback) {
        mCallBack = callback;
        setTitle("Shape Helper");
        setContentPane(contentPane);
        setModal(true);
        setSize(300, 300);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * 点击确定
     */
    private void onOK() {
        if (null != mCallBack) {
            mCallBack.ok(textField1.getText().trim()
                    , textField2.getText().trim()
                    , textField3.getText().trim()
                    , textField4.getText().trim());
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    /**
     * dialog回调
     */
    public interface MyDialogCallBack {
        /**
         * 获取参数回调
         *
         * @param corner     角度
         * @param angle      渐变角度
         * @param startColor 起始颜色
         * @param endColor   终止颜色
         */
        void ok(String corner, String angle, String startColor, String endColor);
    }
}
