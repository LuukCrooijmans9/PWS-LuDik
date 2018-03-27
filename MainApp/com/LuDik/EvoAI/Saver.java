package com.LuDik.EvoAI;

import com.google.gson.Gson;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Saver {

	public static void saveObject(Object object, String specificMap, String fileName) {
		// 1. Convert object to JSON string
		Gson gson = new Gson();
		// String json = gson.toJson(object);
		System.out.println(object.getClass());
		File file = new File("DARWINSAVE" + "\\" + specificMap + "\\" + fileName + ".json");
		file.getParentFile().mkdirs();

		// 2. Convert object to JSON string and save into a file directly
		System.out.println(file.getAbsolutePath());
		try (FileWriter writer = new FileWriter(file.getPath())) {
			gson.toJson(object, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Saved to: " + file.getPath());
	}
	
	public static void savePathObject(Object object, String PathString, String fileName) {
		// 1. Convert object to JSON string
		Gson gson = new Gson();
		// String json = gson.toJson(object);
		System.out.println(object.getClass());
		File file = new File(PathString + File.separator + fileName + ".json");
		file.getParentFile().mkdirs();

		// 2. Convert object to JSON string and save into a file directly
		System.out.println(file.getAbsolutePath());
		try (FileWriter writer = new FileWriter(file.getPath())) {
			gson.toJson(object, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Saved to: " + file.getPath());
	}

	/**
	 * This function saves the buffimage to the folder specified by the pathstring,
	 * with the name specified by the fileName. Returns true if the operation was
	 * successful, false if the operation was unsuccessful.
	 * 
	 * @param buffImage
	 * @param pathString
	 * @param fileName
	 * @return boolean
	 */

	public static boolean savePNG(BufferedImage buffImage, String pathString, String fileName) {
		boolean isSuccessful = false;
		File f = new File(pathString + File.separator + fileName + ".png");
		f.getParentFile().mkdirs();
		try {
			ImageIO.write(buffImage, "PNG", f);
			isSuccessful = true;
		} catch (IOException e) {
			e.printStackTrace();
			isSuccessful = false;
		}
		return isSuccessful;
	}

}
