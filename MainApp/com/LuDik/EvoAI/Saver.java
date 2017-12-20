package com.LuDik.EvoAI;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

}
