package com.LuDik.EvoAI;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Saver {

	public static void saveObject(Object object,String specificMap, String fileName) {
		// 1. Convert object to JSON string
		Gson gson = new Gson();
		// String json = gson.toJson(object);
		System.out.println(object.getClass());
		File file = new File("C:\\" + "DARWINSAVE" + "\\" + specificMap + "\\" + fileName + ".json");
		file.getParentFile().mkdirs();

		// 2. Convert object to JSON string and save into a file directly
		System.out.println("Starting on: " + object);
		try (FileWriter writer = new FileWriter(file.getPath())) {
			System.out.println("step 1");
			gson.toJson(object, writer);
			System.out.println("Fin");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished: " + object);
		System.out.println("Saved to: " + file.getPath());
	}

}
