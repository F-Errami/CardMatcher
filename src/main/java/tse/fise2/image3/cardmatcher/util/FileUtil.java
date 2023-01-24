package tse.fise2.image3.cardmatcher.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The FileUtil class contains methods that provide utility functions for working with files and directories.
 *
 **/

public class FileUtil {

	/**
	 * Creates a folder with the given name
	 *
	 * @param x the name of the folder to be created
	 */
	public static void CreateFolder(String x) {
		File dossier = new File(x);
		boolean res = dossier.mkdir();
		if (res) {
			System.out.println("The folder has been created.");
		} else {
			System.out.println("The folder already exists.");
		}
	}

	/**
	 * Creates a file with the given name and extension
	 *
	 * @param x the name of the file to be created
	 * @param ext the extension of the file to be created
	 */
	public static void CreateFile(String x, String ext) {
		File file = new File(x + "." + ext);

		try {
			if (file.createNewFile()) {
				System.out.println("The file " + x + "." + ext + " has been created");
			} else {
				System.out.println("The file " + x + "." + ext + " already exists");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Copies all the content of a certain directory to the learning database
	 *
	 * @param path the path of the directory to be copied
	 * @throws IOException
	 */
	public static void copyfolder(String path) throws IOException {
		String userHome = System.getProperty("user.dir"); // return c:\Users\${current_user_name}
		String folder = userHome + "/apprentissage";

		FileUtils.copyDirectory(new File(path), new File(folder), true);
	}

	/**
	 * Copies a file to the learning database
	 *
	 * @param path the path of the file to be copied
	 * @throws IOException
	 */
	public static void copyfile(String path) throws IOException {
		String userHome = System.getProperty("user.dir"); // return c:\Users\${current_user_name}
		String folder = userHome + "/apprentissage";

		FileUtils.copyFileToDirectory(new File(path), new File(folder));
	}
}