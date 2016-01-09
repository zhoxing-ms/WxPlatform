package cn.chudo.tools;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
public class PropertiesUtil {
	public String filePath=this.getClass().getResource("/sdk.properties").getPath(); 
	public Properties propertie;
	public PropertiesUtil(){	
		propertie=PropertiesUtil.getProperties(filePath);
	}	
	public PropertiesUtil(String path){
		propertie=PropertiesUtil.getProperties(path);
	}
	public static void addProperties(String key[], String value[], String file) {
		Properties iniFile = getProperties(file);
		FileOutputStream oFile = null;
		try {
			iniFile.put(key, value);
			oFile = new FileOutputStream(file, true);
			iniFile.store(oFile, "modify properties file");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oFile != null) {
					oFile.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取配置文件
	 * 
	 * @return
	 */
	public static Properties getProperties(String file) {
		Properties pro = null;
		// 从文件mdxbu.properties中读取网元ID和模块ID信息
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			pro = new Properties();
			pro.load(in);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pro;
	}

	/**
	 * 保存属性到文件中
	 * 
	 * @param pro
	 * @param file
	 */
	public static void saveProperties(Properties pro, String file) {
		if (pro == null) {
			return;
		}
		FileOutputStream oFile = null;
		try {
			oFile = new FileOutputStream(file, false);
			pro.store(oFile, "modify properties file");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oFile != null) {
					oFile.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 修改属性文件
	 * 
	 * @param key
	 * @param value
	 */
	public static void updateProperties(String key, String value, String file) {
		// key为空则返回
		if (key == null || "".equalsIgnoreCase(key)) {
			return;
		}
		Properties pro = getProperties(file);
		if (pro == null) {
			pro = new Properties();
		}
		pro.put(key, value);
		// 保存属性到文件中
		saveProperties(pro, file);
	}
}
