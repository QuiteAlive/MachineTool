package cn.qzd.machinetool.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2018/5/31.
 */

public class SQLHandle {

    /**
     * @param username
     * @param password
     * @return
     */
    //查询数据（登录）
    public int login(String username, String password) {
        int ce = 0;
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select * from QM_MT_A_User where LoginName='" + username + "'" + " and Password='" + password + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {//循环判断是否有这条数据
                ce = 1;
                // Toast.makeText(MainActivity.this, "cecg1", Toast.LENGTH_SHORT).show();
            }
            connect.close();
        } catch (Exception e) {
//            String sq = e.getMessage();
            Log.d("登录", e.getMessage());
            ce = 2;
        }
        return ce;
    }

    /**
     * 查询用户所属公司名称
     *
     * @param username
     * @return
     */
    public String company(String username) {
        String company = null;

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select Company from QM_MT_A_User where LoginName='" + username + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {//循环判断是否有这条数据
                company = rs.getString("Company");

                // Toast.makeText(MainActivity.this, "cecg1", Toast.LENGTH_SHORT).show();
            }
            connect.close();
        } catch (Exception e) {
//            String sq = e.getMessage();
            Log.d("登录", e.getMessage());

        }
        return company;
    }

    /**
     * 查询用户id用来判断是否为超级管理员
     *
     * @param username
     * @return
     */
    public int userid(String username) {
        int id = -1;

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select id from QM_MT_A_User where LoginName='" + username + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {//循环判断是否有这条数据
                id = rs.getInt("id");
            }
            connect.close();
        } catch (Exception e) {
            Log.d("登录", e.getMessage());
        }
        return id;
    }

    public int widther(int userid) {
        int widther = -1;

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "SELECT  RoleId FROM QM_MT_A_User_Role_Auth where UserId='" + userid + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {//循环判断是否有这条数据
                widther = rs.getInt("RoleId");

                // Toast.makeText(MainActivity.this, "cecg1", Toast.LENGTH_SHORT).show();
            }
            connect.close();
        } catch (Exception e) {
            String sq = e.getMessage();

        }
        return widther;
    }

    /**
     * @param username
     * @param password
     * @param address
     * @param Rname
     * @param number
     */
    //增加一条数据（注册）
    public void register(String username, String password, String address, String Rname, String number) {

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "INSERT INTO QM_MT_A_User " +
                    "(UserName,LoginName,PassWord,CellPhone,Address) " +
                    "VALUES('" + Rname + "','" + username + "','" + password + "','" + number + "','" + address + "')";
            Statement stmt = connect.createStatement();
            stmt.executeQuery(sql);
            ResultSet rs = stmt.executeQuery(sql);
            connect.close();

        } catch (Exception e) {
            //  Log.d("注册",e.getMessage());
            String ce = e.getMessage();
        }
    }

    /**
     * @param username
     * @return
     */
    //根据姓名查询数据（判断登录名是否重复）
    public boolean loginname(String username) {
        boolean ce = false;
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select * from QM_MT_A_User where LoginName='" + username + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                ce = true;
                // Toast.makeText(MainActivity.this, "cecg1", Toast.LENGTH_SHORT).show();
            }
            connect.close();
        } catch (Exception e) {
            String sq = e.getMessage();
        }
        return ce;
    }

    /**
     * 全部机床文件以及每个公司对应的机床文件
     *
     * @return
     */
    public static List<String> jcwj(int weight, String company) {
        ResultSet rs = null;
        String sql = null;
        List<String> list = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            if (weight == 22) {
                sql = "select FileName from QM_MT_MachineFile";
            } else {
                sql = "select FileName from QM_MT_MachineFile where Company='" + company + "'";
                ;
            }
            Statement stmt = connect.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {//循环判断是否有这条数据
                list.add(rs.getString("FileName"));
            }
            connect.close();
        } catch (Exception e) {
            String sq = e.getMessage();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                rs = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List top(int fileId) {
        List list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            String sql = "select FileTop from QM_MT_MachineFileTopBotton where FileId = '" + fileId + "' and IsChecked =1";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData();

            int columnCount = md.getColumnCount(); //Map rowData;
            while (rs.next()) {//循环判断是否有这条数据
              /*  Map rowData=new HashMap();
                for (int i =1;i<=columnCount;i++){
                    rowData.put(md.getCatalogName(i),rs.getObject(i));
                }*/
                list.add(rs.getString("FileTop"));
            }
            connect.close();
        } catch (Exception e) {
            String sq = e.getMessage();
        }
        return list;
    }

    public List bottom(int fileId) {
        List list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            String sql = "select FileBottom from QM_MT_MachineFileTopBotton where FileId = '" + fileId + "' and IsChecked =1";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData();

            int columnCount = md.getColumnCount(); //Map rowData;
            while (rs.next()) {//循环判断是否有这条数据
              /*  Map rowData=new HashMap();
                for (int i =1;i<=columnCount;i++){
                    rowData.put(md.getCatalogName(i),rs.getObject(i));
                }*/
                list.add(rs.getString("FileBottom"));
            }
            connect.close();
        } catch (Exception e) {
            String sq = e.getMessage();
        }
        return list;
    }

    public List lsxy(int fileId) {
        List list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            String sql = "select L,S,X,Y,KnifeName from QM_MT_KnifePoint WHERE FileId = '" + fileId + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //   ResultSetMetaData md = rs.getMetaData();

            // int columnCount = md.getColumnCount(); //Map rowData;
            while (rs.next()) {//循环判断是否有这条数据
              /*  Map rowData=new HashMap();
                for (int i =1;i<=columnCount;i++){
                    rowData.put(md.getCatalogName(i),rs.getObject(i));
                }
                list.add(rowData);*/
                String name = rs.getString("KnifeName");
                if (name.equals("钻刀")) {
                    list.add(String.valueOf(rs.getDouble("L")));
                    list.add(String.valueOf(rs.getDouble("X")));
                    list.add(String.valueOf(rs.getDouble("Y")));
                    list.add(String.valueOf(rs.getDouble("S")));
                    // list.add(String.valueOf(rs.getDouble("Z")));

                } else if (name.equals("螺旋刀")) {
                    list.add(String.valueOf(rs.getDouble("L")));
                    list.add(String.valueOf(rs.getDouble("X")));
                    list.add(String.valueOf(rs.getDouble("Y")));
                    list.add(String.valueOf(rs.getDouble("S")));
                    // list.add(String.valueOf(rs.getDouble("Z")));
                } else if (name.equals("锯片")) {
                    list.add(String.valueOf(rs.getDouble("L")));
                    list.add(String.valueOf(rs.getDouble("X")));
                    list.add(String.valueOf(rs.getDouble("Y")));
                    list.add(String.valueOf(rs.getDouble("S")));
                    // list.add(String.valueOf(rs.getDouble("Z")));
                }
                //  mon.tS[0] = rs.getDouble("S");

                //mon.setTails(rs.getString("KnifeName"));// 获得id列的值
               /* String name = rs.getString("S");//获得第二列的值
                mon.tL[0] = rs.getDouble("X");//获得第三列的值*/

                //  System.out.println(name+"==>"+X[0]+"==>"+Y[0]+"==>"+ L[0]+"==>"+S[0]);
                // System.out.println(mon.tL[0]);
            }
            connect.close();
        } catch (Exception e) {
            String sq = e.getMessage();
        }
        return list;
    }

    public List<String> Gcode() {
        List<String> list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            String sql = "select CodeType,Number from QM_MT_LatheCode";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                list.add(rs.getString("CodeType"));
                list.add(String.valueOf(rs.getInt("Number")));
            }
            connect.close();
        } catch (Exception e) {
            String sq = e.getMessage();
        }

        return list;
    }

    public List<String> Ftp(int fileId) {
        List<String> list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select  IP, Port, UserName, Password from QM_MT_SaveFtp where FileId='" + fileId + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                list.add(rs.getString("IP"));
                list.add(rs.getString("Port"));
               /* list.add(rs.getString("UserName"));
                list.add(rs.getString("Password"));*/
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int select_filenameid(String FileName) {
        int id = 0;

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            String sql = "SELECT ID FROM QM_MT_MachineFile WHERE FileName='" + FileName + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                id = rs.getInt("ID");
            }
            connect.close();
        } catch (Exception e) {
            String sq = e.getMessage();
        }

        return id;
    }

    //获取一个榫卯里面的所有刀路
    public List<String> select_pathtype(String SunMaoN0) {
        List<String> list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            String sql = "select PathName from QM_MT_SunMao_Path_R where SunMaoNameNo='" + SunMaoN0 + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                list.add(rs.getString("PathName"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int select_pathid(String PathName) {
        int pathid = 0;
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            String sql = "select ID from QM_MT_PathName where PathName LIKE '" + PathName + "%'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {//循环判断是否有这条数据
                pathid = rs.getInt("ID");
            }
            connect.close();
        } catch (Exception e) {
            String sq = e.getMessage();
        }
        return pathid;
    }

    public List<String> select_data(int PathId) {
        List<String> list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            String sql = "select Property from QM_MT_PathParameter where PathID = '" + PathId + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {//循环判断是否有这条数据
                list.add(rs.getString("Property"));
            }
            connect.close();
        } catch (Exception e) {
            String sq = e.getMessage();
        }
        return list;
    }

    //查询榫卯刀路的一个唯一刀路代号
    public int select_path_r_id(String PathName, String pathcode) {
        int path_r_id = 0;
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            String sql = "select ID from QM_MT_SunMao_Path_R where PathName = '" + PathName + "' and SunMaoNameNo='" + pathcode + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {//循环判断是否有这条数据
                path_r_id = rs.getInt("ID");
            }
            connect.close();
        } catch (Exception e) {
            String sq = e.getMessage();
        }
        return path_r_id;
    }

    //查找刀路AB轴信息
    public List<String> sunmao_AB(int path_r_id) {
        List<String> list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();
            String sql = "select * from QM_MT_ABaxisVal where SunMao_Path_ID='" + path_r_id + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {//循环判断是否有这条数据
                list.add(String.valueOf(rs.getInt("ISRad1Check")));
                list.add(rs.getString("ProfileFormula"));
                list.add(rs.getString("ProfileScope1"));
                list.add(rs.getString("ProfileScope2"));
                list.add(String.valueOf(rs.getInt("ISRad2Check")));
                list.add(rs.getString("AaxleFormula"));
                list.add(rs.getString("AaxleScope1"));
                list.add(rs.getString("AaxleScope2"));
                list.add(String.valueOf(rs.getInt("ISRad3Check")));
                list.add(rs.getString("BaxleFormula"));
                list.add(rs.getString("BaxleScope1"));
                list.add(rs.getString("BaxleScope2"));
                list.add(rs.getString("StartDot"));
                list.add(String.valueOf(rs.getInt("ISClockWise")));
            }
            connect.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> sunmao_parameter(int path_r_id) {
        List<String> list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            String sql = "select property,size from QM_MT_SunMaoPathParameterVal where SunMao_Path_ID='" + path_r_id + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                list.add(rs.getString("property"));
                list.add(rs.getString("size"));
            }
            connect.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> sunmao_parameter_size(int path_r_id) {
        List<String> list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            String sql = "select property,size from QM_MT_SunMaoPathParameterVal where SunMao_Path_ID='" + path_r_id + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                //list.add(rs.getString("property"));
                list.add(rs.getString("size"));
            }
            connect.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> sunmao_type() {
        List<String> list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            String sql = "select SunMaoMainName from QM_MT_SunMaoInfo";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                list.add(rs.getString("SunMaoMainName"));
            }
            connect.close();
        } catch (Exception e) {
            String sq = e.getMessage();
        }
        return list;
    }

    /***
     * SunMaoInfo表里的榫卯名字
     *
     * @param sunmao_type_name
     * @return
     */
    public String sunmao_type_code(String sunmao_type_name) {
        String sunmao_type_code = null;
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            String sql = "select SunMaoNo from QM_MT_SunMaoInfo where SunMaoMainName='" + sunmao_type_name + "' ";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                //list.add( rs.getString("SunMaoName"));
                sunmao_type_code = rs.getString("SunMaoNo");
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sunmao_type_code;
    }

    /**
     * 根据榫卯代号查找图片
     *
     * @param sunmao_type_code
     * @return
     */
    public Bitmap sunmao_type_image(String sunmao_type_code) {
        Bitmap bitmap = null;
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to databaseo
            // String sql = "select sImage from QM_MT_SunMaoInfo where SunMaoNo='" + sunmao_type_code + "' ";
            String sql = "select sImage from QM_MT_SunMaoInfo where SunMaoNo='" + sunmao_type_code + "' ";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {//循环判断是否有这条数据
                byte[] in = rs.getBytes("sImage");
                bitmap = BitmapFactory.decodeByteArray(in, 0, in.length);
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 根据父榫卯查找父榫卯图片
     *
     * @param fathername
     * @return
     */
    public Bitmap fsunmao_type_image(String fathername) {
        Bitmap bitmap = null;
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to databaseo
            // String sql = "select sImage from QM_MT_SunMaoInfo where SunMaoNo='" + sunmao_type_code + "' ";
            String sql = "select sImage from QM_MT_SunMaoInfo where SunMaoName='" + fathername + "' ";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {//循环判断是否有这条数据
                byte[] in = rs.getBytes("sImage");
                bitmap = BitmapFactory.decodeByteArray(in, 0, in.length);
                //bb = rs.getBytes("sImage");//通过列号得到
              /*  Blob  b=rs.getBlob("sImage");//
                OutputStream out=new FileOutputStream(new File("/mnt/sdcard/aqmzn/linshi.png"));//linshi.png为临时创建存放图片的
                out.write(b.getBytes(1, (int)b.length()));*/
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public List select_Knideinfo(String fSunMaoNo, String knifename) {
        List<String> list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            // String sql = "select knifeName from QM_MT_Knifeproperty_path_R where SunMao_Path_ID=" + SunMao_Path_ID + " and property='刀具直径' ";
            String sql = "select Property, PropertyVal from QM_MT_Sunmao_knife where SunMaoNo='" + fSunMaoNo + "' and KnifeName='" + knifename + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                //list.add( rs.getString("SunMaoName"));
                list.add(rs.getString("PropertyVal"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据父榫卯编号查询使用道具
     *
     * @param fSunMaoNo
     * @return
     */
    public List select_KnideName1(String fSunMaoNo) {
        List<String> list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select  knifename  from QM_MT_Sunmao_knife where SunMaoNo='" + fSunMaoNo + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                //list.add( rs.getString("SunMaoName"));
                list.add(rs.getString("knifeName"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }

    //查询刀具类型
    public String select_KnideName(int SunMao_Path_ID) {
        String kuifename = null;
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            // String sql = "select knifeName from QM_MT_Knifeproperty_path_R where SunMao_Path_ID=" + SunMao_Path_ID + " and property='刀具直径' ";
            String sql = "select knifeName from QM_MT_Knifeproperty_path_R where SunMao_Path_ID='" + SunMao_Path_ID + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                //list.add( rs.getString("SunMaoName"));
                kuifename = rs.getString("knifeName");
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kuifename;
    }

    //查询榫卯参数  sunmaono=榫卯代号,,,,,查找榫卯参数
    public List<String> sunmao_val_Porperty(String sunmaono) {
        List<String> list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            String sql = "select * from QM_MT_SunMao_Porperty_R where SunMaoNo='" + sunmaono + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                list.add(rs.getString("SunMaoPorperty"));
//                list.add( rs.getString("porpertyNo"));
//                list.add( rs.getString("PorpertyType"));
//                list.add( rs.getString("PorpertyVal"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //查找默认值
    public List<String> sunmao_val(String sunmaono) {
        List<String> list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            String sql = "select * from QM_MT_SunMao_Porperty_R where SunMaoNo='" + sunmaono + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                //  list.add( rs.getString("SunMaoPorperty"));
//                list.add( rs.getString("porpertyNo"));
//                list.add( rs.getString("PorpertyType"));
                list.add(rs.getString("PorpertyVal"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //查找榫卯数据代号
    public List<String> sunmao_val_porpertyNo(String sunmaono) {
        List<String> list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            String sql = "select * from QM_MT_SunMao_Porperty_R where SunMaoNo='" + sunmaono + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                //  list.add( rs.getString("SunMaoPorperty"));
                list.add(rs.getString("porpertyNo"));
//                list.add( rs.getString("PorpertyType"));
                //   list.add( rs.getString("PorpertyVal"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //查询算刀路值的公式
    public List<String> path_val(String sunmao_path_ID) {
        List<String> list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            //  String sql = "select ID,FileTop,FileBottom,SchemeName from QM_MT_MachineFileTopBotton where FileId = '"+fileId +"' and IsChecked =1";
            String sql = "select * from QM_MT_SunMaoPathParameterVal where SunMao_Path_ID='" + sunmao_path_ID + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                // list.add( rs.getString("size"));
                list.add(rs.getString("property"));
//                list.add( rs.getString("porpertyNo"));
//                list.add( rs.getString("PorpertyType"));
//                list.add( rs.getString("PorpertyVal"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 计算公式
     *
     * @param str
     * @return
     */
    public String valdata(String str) {
        String newstr = "";
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select " + str;
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {//循环判断是否有这条数据
                newstr = String.valueOf(rs.getDouble(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newstr;
    }

    /**
     * 查找榫卯类型（A,B,C）,以及对应的id
     *
     * @param sunmaoname
     * @return
     */
    public String[] sunmaotype(String sunmaoname) {
        String type[] = new String[2];
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "SELECT  ID, SunMaoType FROM  QM_MT_SunMaoInfo where SunMaoMainName='" + sunmaoname + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {//循环判断是否有这条数据
                type[0] = rs.getString("SunMaoType");
                type[1] = String.valueOf(rs.getInt("ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }

    /**
     * 根据组合榫卯的id查找子榫的名字以及代号
     *
     * @param id
     * @return
     */
    public List<String> sunmao_child(String id) {
        List<String> list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "SELECT  SunMaoNo,SunMaoName FROM  QM_MT_SunMaoChild where MainID='" + id + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                list.add(rs.getString("SunMaoNo"));
                list.add(rs.getString("SunMaoName"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据组合榫卯的id查找子榫的名字以及代号
     *
     * @param id
     * @return
     */
    public List<String> sunmao_child1(String id) {
        List<String> list = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "SELECT  SunMaoNo,SunMaoName FROM  QM_MT_SunMaoChild where MainID='" + id + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                list.add(rs.getString("SunMaoNo"));
                // list.add(rs.getString("SunMaoName"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int sonid(String childcode, String mainid) {
        int sonid = -1;

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "SELECT  ID FROM  QM_MT_SunMaoChild where MainID='" + mainid + "' and SunMaoNo='" + childcode + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                sonid = rs.getInt("ID");
                // list.add(rs.getString("SunMaoName"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sonid;
    }

    public List ALLsonid(String mainid) {
        List<String> allsonid = new ArrayList();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "SELECT  ID FROM  QM_MT_SunMaoChild where MainID='" + mainid + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据

                allsonid.add(rs.getString("ID"));
                // list.add(rs.getString("SunMaoName"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allsonid;
    }

    /**
     * 根据mainid查找子榫卯名字
     *
     * @param mainid
     * @return
     */
    public List childname(String mainid) {
        List<String> childname = new ArrayList<>();
        try {

            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "SELECT  SunMaoName FROM  QM_MT_SunMaoChild where ID='" + mainid + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据

                childname.add(rs.getString("SunMaoName"));
                // list.add(rs.getString("SunMaoName"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return childname;
    }
/************************第二次修改**************************/
    /**
     * 第二次修改查找父榫名称以及A类榫名称用来显示
     *
     * @return
     */
    public List fathername() {
        List<String> fathername = new ArrayList<>();
        try {

            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select  SunMaoName from   QM_MT_SunMaoInfo where ParentID='0' or ID=ParentID";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                fathername.add(rs.getString("SunMaoName"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fathername;
    }

    /**
     * 第二次修改查找父榫代号以及A类榫代号用来显示
     *
     * @return
     */
    public List fatherno() {
        List<String> childname = new ArrayList<>();
        try {

            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select  SunMaoNo from   QM_MT_SunMaoInfo where ParentID='0' or ID=ParentID";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                childname.add(rs.getString("SunMaoNo"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return childname;
    }

    /**
     * g根据父榫卯的名称查找parentid
     *
     * @param fathername
     * @return
     */
    public int ParentID(String fathername) {
        int parentid = 0;
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select  ParentID from   QM_MT_SunMaoInfo where SunMaoName='" + fathername + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                parentid = rs.getInt("ParentID");
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parentid;
    }

    /**
     * 根据父榫卯名称查找id用来查找子榫卯
     *
     * @param fathername
     * @return
     */
    public int infoid(String fathername) {
        int infoid = 0;
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select  ID from   QM_MT_SunMaoInfo where SunMaoName='" + fathername + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                infoid = rs.getInt("ID");
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return infoid;
    }

    /**
     * 根据父榫卯查找子榫卯的名称以及代号
     *
     * @param infoid
     * @return
     */
    public List info_parent(int infoid) {
        List<String> childnameno = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select SunMaoNo from QM_MT_SunMaoInfo where ParentID='" + infoid + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                childnameno.add(rs.getString("SunMaoNo"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return childnameno;
    }

    public String fatherno_id(int id) {
        String fatherno = "";
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select SunMaoNo from QM_MT_SunMaoInfo where ID='" + id + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                fatherno = rs.getString("SunMaoNo");

            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fatherno;
    }

    public String childname1(String childno) {
        String childname = "";
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select SunMaoName from QM_MT_SunMaoInfo where SunMaoNo='" + childno + "'";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                childname = rs.getString("SunMaoName");

            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return childname;
    }

    /*********************
     * 用来保存到本地数据库
     *****************/
    //判断是否能连上数据库
    public int judge() {
        int ce = 0;
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select UserName from QM_MT_A_User";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {//循环判断是否有这条数据
                ce = 1;
                // Toast.makeText(MainActivity.this, "cecg1", Toast.LENGTH_SHORT).show();
            }
            connect.close();
        } catch (Exception e) {
            Log.d("登录", e.getMessage());
            ce = 2;
        }
        return ce;
    }

    public ArrayList sunmaoinfo() {
        ArrayList info = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select ID, SunMaoNo, SunMaoName, ParentID, MachineType, sImage ,IsUse from   QM_MT_SunMaoInfo";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                info.add(rs.getInt("ID"));
                info.add(rs.getString("SunMaoNo"));
                info.add(rs.getString("SunMaoName"));
                info.add(rs.getInt("ParentID"));
                info.add(rs.getBytes("sImage"));
                info.add(rs.getInt("MachineType"));
                info.add(rs.getInt("IsUse"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public ArrayList sunmaopath() {
        ArrayList info = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select   SunMaoNameNo, PathName,ID from   QM_MT_SunMao_Path_R";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                info.add(rs.getString("SunMaoNameNo"));
                info.add(rs.getString("PathName"));
                info.add(rs.getInt("ID"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public ArrayList ABaxisval() {
        ArrayList info = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select  SunMao_Path_ID, ISRad1Check, ProfileFormula, ProfileScope1, ProfileScope2, ISRad2Check, AaxleFormula, AaxleScope1, AaxleScope2, ISRad3Check, BaxleFormula, BaxleScope1, BaxleScope2, StartDot, ISClockWise from  QM_MT_ABaxisVal";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                info.add(rs.getInt("SunMao_Path_ID"));
                info.add(rs.getInt("ISRad1Check"));
                info.add(rs.getString("ProfileFormula"));
                info.add(rs.getDouble("ProfileScope1"));
                info.add(rs.getDouble("ProfileScope2"));
                info.add(rs.getInt("ISRad2Check"));
                info.add(rs.getString("AaxleFormula"));
                info.add(rs.getDouble("AaxleScope1"));
                info.add(rs.getDouble("AaxleScope2"));
                info.add(rs.getInt("ISRad3Check"));
                info.add(rs.getString("BaxleFormula"));
                info.add(rs.getDouble("BaxleScope1"));
                info.add(rs.getDouble("BaxleScope2"));
                info.add(rs.getString("StartDot"));
                info.add(rs.getInt("ISClockWise"));

            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public ArrayList qm_user() {
        ArrayList info = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select PassWord, LoginName, Company,ID from  QM_MT_A_User";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                info.add(rs.getString("PassWord"));
                info.add(rs.getString("LoginName"));
                info.add(rs.getString("Company"));
                info.add(rs.getInt("ID"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public ArrayList qm_path_property() {
        ArrayList info = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select  property, size, SunMao_Path_ID from QM_MT_SunMaoPathParameterVal";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                info.add(rs.getString("property"));
                info.add(rs.getString("size"));
                info.add(rs.getInt("SunMao_Path_ID"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public ArrayList qm_data_property() {
        ArrayList info = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select SunMaoNo,SunMaoPorperty,porpertyNo,PorpertyVal,Image from QM_MT_SunMao_Porperty_R";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                info.add(rs.getString("SunMaoNo"));
                info.add(rs.getString("SunMaoPorperty"));
                info.add(rs.getString("porpertyNo"));
                info.add(rs.getString("PorpertyVal"));
//                if (String.valueOf(rs.getByte("Image"))=="0") {
//                    info.add(0);
//                   // info.add(rs.getBytes("Image"));
//                }else {
                    info.add(rs.getBytes("Image"));
               // }
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public ArrayList MachineFile() {
        ArrayList info = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select ID,ShaftId, FileName, Company ,ShaftType from  QM_MT_MachineFile";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                info.add(rs.getInt("ShaftId"));
                info.add(rs.getString("FileName"));
                info.add(rs.getString("Company"));
                info.add(rs.getInt("ID"));
                info.add(rs.getInt("ShaftType"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public ArrayList TopandBottom() {
        ArrayList info = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select FileTop, FileBottom, FileId, IsChecked from  QM_MT_MachineFileTopBotton";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                info.add(rs.getString("FileTop"));
                info.add(rs.getString("FileBottom"));
                info.add(rs.getInt("FileId"));
                info.add(rs.getInt("IsChecked"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public ArrayList LatheCode() {
        ArrayList info = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select  FileId, CodeType, Number, Instruction from  QM_MT_UserLatheCode";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                info.add(rs.getString("FileId"));
                info.add(rs.getString("CodeType"));
                info.add(rs.getString("Number"));
                info.add(rs.getString("Instruction"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public ArrayList SaveFtp() {
        ArrayList info = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select  FileId, IP, Port from  QM_MT_SaveFtp";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                info.add(rs.getInt("FileId"));
                info.add(rs.getString("IP"));
                info.add(rs.getString("Port"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public ArrayList KnifePoint() {
        ArrayList info = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select FileId, KnifeName, L, S, X, Y ,SafeY ,SafeZ from QM_MT_KnifePoint";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                info.add(rs.getInt("FileId"));
                info.add(rs.getString("KnifeName"));
                info.add(rs.getDouble("L"));
                info.add(rs.getDouble("S"));
                info.add(rs.getDouble("X"));
                info.add(rs.getDouble("Y"));
                info.add(rs.getString("SafeY"));
                info.add(rs.getString("SafeZ"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public ArrayList KnifePath_r() {
        ArrayList info = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select  SunMao_Path_ID, knifeName, property, size from QM_MT_Knifeproperty_path_R";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                info.add(rs.getInt("SunMao_Path_ID"));
                info.add(rs.getString("KnifeName"));
                info.add(rs.getString("property"));
                info.add(rs.getDouble("size"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public ArrayList user_role() {
        ArrayList info = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            Connection connect = conStr.connectionclasss();        // Connect to database
            String sql = "select  UserId, RoleId from QM_MT_A_User_Role_Auth";
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//循环判断是否有这条数据
                info.add(rs.getInt("UserId"));
                info.add(rs.getInt("RoleId"));
            }
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

}
