package cn.qzd.machinetool.FileIO;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Properties;



public class Fileservice{
		private Context context;
	protected HashMap<String, Properties> sections = new HashMap<String, Properties>();
	private transient String currtionSecion;
	private transient Properties current;
		public Fileservice(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
		}
		public Fileservice() {
			// TODO Auto-generated constructor stub
		}
		// 读取文件
		@SuppressWarnings("resource")
		public String getInputstream(String filename) {// 从文件名中获得inputstream
			// 注意FileInputStream是从相应连路径的文件中读数据 而不是写 注意注意
			FileInputStream fileInputStream = null;// 获得一个文件输入流对象
			// ByteArrayOutputStream是缓存流 与磁盘无关 不需要关闭
			ByteArrayOutputStream OutputStream = new ByteArrayOutputStream();
			// 获得sdcard文件跟目录路径
			File file = new File(Environment.getExternalStorageDirectory(),
					filename);// 创建一个新的文件
			// 是否sdcard卡开启
			BufferedReader read=null;
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {
				try {
reader(read);
					fileInputStream = new FileInputStream(file);// 找到相应文件的路径的输入流
					int len = 0;
					byte[] data = new byte[1024];
					// 读取数据到OutputStream中
					while ((len = fileInputStream.read(data)) != -1) {
						OutputStream.write(data, 0, len);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (fileInputStream != null) {
						fileInputStream = null;
					}
				}
			}
			return new String(OutputStream.toByteArray());
		}
		/**
		 * 我这个函数是写入文件到sdcard中
		 *
		 * @param Filename
		 *            文件名
		 * @param content
		 *            文件内容
		 * @return
		 */
		public boolean saveContentTosdCard(String Filename, String content) {
			boolean flag = false;
			// 注意FileOutputStream是打开特定的文件 在其中<<写>>东西 人不是 顾名思义第去读 也就是参照是软件的内存
			FileOutputStream outputStream = null;// 获得一个文件输出流
			BufferedWriter writer = null;
			// 获得sdcard的根目录Environment.getExternalStorageDirectory()
			// 注意这个file不是文件 而是 一个路径
			File file1 = new File("sdcard/Pictures/aqmzn");
			if (!file1.exists()){
				file1.mkdir();
			}

			File file = new File(Environment.getExternalStorageDirectory()+"/Pictures/aqmzn/",
					Filename);

			// 判断sdcard是否可用
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {
				try {
					outputStream = new FileOutputStream(file,false);//
					// 把指定的内容写入相应的文件中
					outputStream.write(content.getBytes());// 从edittext中获得我输入的字符
					// 并且放入outputStream中
					outputStream.close();
					flag = true;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					// 注意要关闭
					if (outputStream != null) {
						try {
							outputStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			return flag;
		}
/**
 * 设置每次读取文件一行
 * @param reader 文件流
 * @throws IOException
 */
	private void reader(BufferedReader reader) throws IOException {
		// TODO Auto-generated method stub
		String line = null;
		try {
			while((line = reader.readLine()) != null)
			{
				parseLine(line);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IOException("文件内容读取失败");
		}
	}

	/**
	 * 获取ini文件的属性值
	 * @param line ini文件每行数据
	 */
	private void parseLine(String line) {
		// TODO Auto-generated method stub
		try {
			if (line != null) {
				line = line.trim();
				if (line.matches("\\[.*\\]")) {
					currtionSecion = line.replaceFirst("\\[(.*)\\]", "$1");
					current = new Properties();
					sections.put(currtionSecion, current);
				} else if (line.matches(".*=.*")) {
					if (current != null) {
						int i = line.indexOf('=');
						String name = line.substring(0, i-1);

						String value = line.substring(i+2,i+3);

						current.setProperty(name, value);

					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 用于获取属性值的值
	 * @param section 整体属性的值
	 * @param name 属性值名字
	 * @return 属性值的值
	 */
	public String getValue(String section, String name)
	{

		Properties p = (Properties)sections.get(section);

		if(p == null)
		{
			return null;
		}
		String value = p.getProperty(name);

		return value;
	}
	}
