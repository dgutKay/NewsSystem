package tools;

import java.io.File;

public class FileTool {
	public static String root;

	// 获取完整路径的文件名中的路径（排除掉文件名）,最后是“\”
	public static String getDirectory(String fullFileName) {
		int index = fullFileName.lastIndexOf("\\");
		return fullFileName.substring(0, index);
	}

	// 获取完整路径中的文件名
	public static String getFileName(String fullFileName) {
		int index = fullFileName.lastIndexOf("\\");
		return fullFileName.substring(index + 1);
	}

	// 根据文件名获得文件（不带路径）的扩展名
	public static String getExtendedFileName(String fileName) {
		int index = fileName.lastIndexOf(".");
		return fileName.substring(index + 1);
	}

	// 生成随机文件名，文件名为当前时间的整数值，扩展名不变
	public static String getRandomFileNameByCurrentTime(String fileName) {
		return System.currentTimeMillis() + "." + getExtendedFileName(fileName);
	}

	// 根据完整路径生成随机文件名，文件名为当前时间的整数值，扩展名不变，路径不变
	public static String getRandomFileNameFromFullFileNameByCurrentTime(String fullFileName) {
		return getDirectory(fullFileName) + System.currentTimeMillis() + "."
				+ getExtendedFileName(getFileName(fullFileName));
	}

	// 删除文件
	public static boolean deleteFile(File file) {
		boolean result = false;
		int tryCnt = 0;

		while (!result && tryCnt++ < 10) {
			result = file.delete();
			System.gc(); // JVM 进行垃圾回收
		}
		return result;
	}

}
