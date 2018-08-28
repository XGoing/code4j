package com.xgoing.generator.form;

import com.xgoing.generator.util.CodeGenerator;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionEvent;

@Getter
public class MainForm {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Mysql数据库JDO自动生成");
        frame.setSize(350, 390);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);
        JLabel hostLabel = new JLabel("目标机: ");
        hostLabel.setBounds(10, 20, 80, 25);
        panel.add(hostLabel);
        JTextField hostText = new JTextField(20);
        hostText.setBounds(100, 20, 165, 25);
        panel.add(hostText);

        JLabel portLabel = new JLabel("端  口:");
        portLabel.setBounds(10, 50, 80, 25);
        panel.add(portLabel);
        JTextField portText = new JTextField(20);
        portText.setBounds(100, 50, 165, 25);
        panel.add(portText);

        JLabel userLabel = new JLabel("用户名:");
        userLabel.setBounds(10, 80, 80, 25);
        panel.add(userLabel);
        JTextField userText = new JTextField(20);
        userText.setBounds(100, 80, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setBounds(10, 110, 80, 25);
        panel.add(passwordLabel);
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 110, 165, 25);
        panel.add(passwordText);

        JLabel dbLabel = new JLabel("数据库:");
        dbLabel.setBounds(10, 140, 80, 25);
        panel.add(dbLabel);
        JTextField dbText = new JTextField(20);
        dbText.setBounds(100, 140, 165, 25);
        panel.add(dbText);

        JLabel tableLabel = new JLabel("表  名:");
        tableLabel.setBounds(10, 170, 80, 25);
        panel.add(tableLabel);
        JTextField tableText = new JTextField(20);
        tableText.setBounds(100, 170, 165, 25);
        panel.add(tableText);

        JLabel packageLabel = new JLabel("包  名:");
        packageLabel.setBounds(10, 200, 80, 25);
        panel.add(packageLabel);
        JTextField packageText = new JTextField(20);
        packageText.setBounds(100, 200, 165, 25);
        panel.add(packageText);

        JLabel filepathLabel = new JLabel("文件路径:");
        filepathLabel.setBounds(10, 230, 80, 25);
        panel.add(filepathLabel);
        JTextField filepathText = new JTextField(20);
        filepathText.setBounds(100, 230, 165, 25);
        panel.add(filepathText);

        // 创建登录按钮
        JButton loginButton = new JButton("确定");
        loginButton.setBounds(130, 300, 80, 25);
        panel.add(loginButton);
        loginButton.addActionListener((ActionEvent e) -> {
            String host = hostText.getText();
            String port = portText.getText();
            String username = userText.getText();
            String password = new String(passwordText.getPassword());
            String dbname = dbText.getText();
            String tablename = tableText.getText();
            String packagename = packageText.getText();
            String filepath = filepathText.getText();
            try {
                CodeGenerator.generateCode(host, port, username, password, dbname, tablename, packagename, filepath);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "失败", "提示", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
