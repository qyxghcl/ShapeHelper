import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class MyShapeAction extends AnAction {
    /**
     * 当前项目
     */
    private Project project;
    /**
     * 圆角
     */
    private String mCorner;
    /**
     * 渐变角度
     */
    private String mAngle;
    /**
     * 起始颜色
     */
    private String mStartColor;
    /**
     * 终止颜色
     */
    private String mEndColor;

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        initDialog();
        refreshProject();
    }

    /**
     * 显示dialog获取数据
     */
    private void initDialog() {
        MyDialog dialog = new MyDialog(new MyDialog.MyDialogCallBack() {
            @Override
            public void ok(String corner, String angle, String startColor, String endColor) {
                mCorner = corner;
                mAngle = angle;
                mStartColor = startColor;
                mEndColor = endColor;
                excuteCreate();
            }
        });
        dialog.setVisible(true);
    }

    /**
     * 刷新项目
     */
    private void refreshProject() {
        project.getBaseDir().refresh(false, true);
    }

    /**
     * 创建文件夹
     */
    private void excuteCreate() {
        String content;
        content = ReadTemplateFile("gradient.txt");
        content = dealTemplateContent(content);
        writeToFile(content, getPath(), "sp_r" + mCorner
                + "_angle" + mAngle
                + "_" + mStartColor
                + "_2_" + mEndColor + ".xml");
        Messages.showMessageDialog(project,
                "success!",
                "information",
                Messages.getInformationIcon());
    }

    /**
     * 读取模板文件中的字符内容
     *
     * @param fileName 模板文件名
     * @return 模板文件中的内容
     */
    private String ReadTemplateFile(String fileName) {
        InputStream in = null;
        in = this.getClass().getResourceAsStream("/Template/" + fileName);
        String content = "";
        try {
            content = new String(readStream(in));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 输入流
     *
     * @param inputStream 输入流
     * @return byte[]
     * @throws IOException io异常
     */
    private byte[] readStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
            inputStream.close();
        }
        return outputStream.toByteArray();
    }

    /**
     * 替换模板中字符
     *
     * @param content 模板内容
     * @return 替换后的模板内容
     */
    private String dealTemplateContent(String content) {
        content = content.replace("$corner", mCorner);
        content = content.replace("$angle", mAngle);
        content = content.replace("$startColor", mStartColor);
        content = content.replace("$endColor", mEndColor);
        return content;
    }

    /**
     * 输出
     *
     * @param content   类中的内容
     * @param classPath 类文件路径
     * @param className 类文件名称
     */
    private void writeToFile(String content, String classPath, String className) {
        try {
            File floder = new File(classPath);
            if (!floder.exists()) {
                floder.mkdirs();
            }

            File file = new File(classPath + "/" + className);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取输出路径
     *
     * @return 包名文件路径
     */
    private String getPath() {
        return project.getBasePath() + "/ashape/";
    }
}
