package com.xgoing.generator.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

/**
 * 代码生成工具类.
 */
public class CodeGenerator {
    /**
     * 连接数据库生成指定表对应实体类.
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @param dbName
     * @param tableName
     */
    public static void generateCode(String host, String port, String username, String password, String dbName, String tableName, String packagename, String filepath) {
        if (filepath == null || filepath.length() == 0) {
            throw new RuntimeException();
        }
        File file = new File(filepath);
        if (!file.exists() || !file.canWrite() || !file.isDirectory()) {
            throw new RuntimeException();
        }
        BufferedWriter writer = null;
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?user=" + username + "&password=" + password;
            conn = DriverManager.getConnection(url);
            DatabaseMetaData dbMeta = conn.getMetaData();
            ResultSet tableRet = dbMeta.getTables(null, "%", tableName, new String[]{"TABLE"});
            boolean findTable = false;
            while (tableRet.next()) {
                findTable = true;
                break;
            }
            if (findTable) {
                String className = getJavaName(tableName, true);
                StringBuilder classBuilder = new StringBuilder();
                classBuilder.append("package ");
                classBuilder.append(packagename);
                classBuilder.append(";");
                classBuilder.append("\n");
                classBuilder.append("\n");
                ResultSet colRet1 = dbMeta.getColumns(null, "%", tableName, "%");
                classBuilder.append(getContent(colRet1, 0));
                classBuilder.append("\n");
                classBuilder.append("@Getter\n");
                classBuilder.append("@Setter\n");
                classBuilder.append("public class ");
                classBuilder.append(className);
                classBuilder.append(" { ");
                classBuilder.append("\n");
                ResultSet colRet2 = dbMeta.getColumns(null, "%", tableName, "%");
                classBuilder.append(getContent(colRet2, 1));
                classBuilder.append("\n");
                classBuilder.append("} ");
//                System.out.println(classBuilder.toString());
                writer = new BufferedWriter(new FileWriter(new File(filepath + "/" + className + ".java"), false));
                writer.write(classBuilder.toString());
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 提取类名.
     *
     * @param name
     * @param isTable
     * @return
     */
    private static String getJavaName(String name, boolean isTable) {
        if (name != null && name.length() > 0) {
            StringBuilder nameBuilder = new StringBuilder();
            String[] arr = name.split("_");
            for (String str : arr) {
                String upper = str.substring(0, 1).toUpperCase();
                nameBuilder.append(upper);
                String origin = str.substring(1, str.length());
                nameBuilder.append(origin);
            }
            if (isTable) {
                return nameBuilder.toString();
            } else {
                return nameBuilder.substring(0, 1).toLowerCase() + nameBuilder.substring(1, nameBuilder.length());
            }
        }
        return null;
    }

    /**
     * 引入包.
     *
     * @param colRet
     * @param type   0：表示引入；1：表示字段.
     * @return
     */
    private static String getContent(ResultSet colRet, int type) {
        StringBuilder contentBuilder = new StringBuilder();
        String columnName;
        String columnType;
        String columnComment;
        try {
            if (type == 0) {
                contentBuilder.append("import lombok.Getter;\n");
                contentBuilder.append("import lombok.Setter;\n");
            }
            while (colRet.next()) {
                columnName = colRet.getString("COLUMN_NAME");
                columnType = colRet.getString("TYPE_NAME");
                columnComment = colRet.getString("REMARKS");
                if (type == 0) {
                    String libName = getLibName(columnType);
                    if (libName != null) {
                        contentBuilder.append("import ");
                        contentBuilder.append(libName);
                        contentBuilder.append(";\n");
                    }
                }

                if (type == 1) {
                    String typeName = getTypeName(columnType);
                    contentBuilder.append("    /**\n");
                    contentBuilder.append("     *");
                    if (columnComment == null || columnComment.length() == 0) {
                        contentBuilder.append(columnName);
                    } else {
                        contentBuilder.append(columnComment);
                    }
                    contentBuilder.append(".\n");
                    contentBuilder.append("     */\n");
                    contentBuilder.append("    ");
                    contentBuilder.append("private ");
                    contentBuilder.append(typeName);
                    contentBuilder.append(" ");
                    contentBuilder.append(getJavaName(columnName, false));
                    contentBuilder.append(";\n");
                    contentBuilder.append("\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    private static String getLibName(String columnType) {
        String result = null;
        columnType = columnType.toLowerCase();
        switch (columnType) {
            case "datetime":
                result = "java.util.Date";
                break;
            case "timestamp":
                result = "java.util.Date";
                break;
            case "date":
                result = "java.util.Date";
                break;
            case "time":
                result = "java.util.Date";
                break;
            case "decimal":
                result = "java.math.BigDecimal";
                break;
            default:
                ;
                break;
        }
        return result;
    }

    private static String getTypeName(String columnType) {
        String result = null;
        columnType = columnType.toLowerCase();
        switch (columnType) {
            case "datetime":
                result = "Date";
                break;
            case "timestamp":
                result = "Date";
                break;
            case "date":
                result = "Date";
                break;
            case "time":
                result = "Date";
                break;
            case "tinyint":
                result = "int";
                break;
            case "smallint":
                result = "int";
                break;
            case "int":
                result = "int";
                break;
            case "int unsigned":
                result = "int";
                break;
            case "bigint":
                result = "long";
                break;
            case "char":
                result = "String";
                break;
            case "varchar":
                result = "String";
                break;
            case "text":
                result = "String";
                break;
            case "decimal":
                result = "BigDecimal";
                break;
            default:
                ;
                break;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(getJavaName("sys_user", true));
        System.out.println(getJavaName("login_name", false));
        System.out.println(getJavaName("Id", false));
        System.out.println(getJavaName("password", false));
    }
}
