package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
	// write your code here
        try {
            File names = new File("/Users/maksim/Desktop/CardsFolder/Names.txt");
            Scanner reader = new Scanner(names);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                System.out.println(line);
                //JSONObject root = mapper.readValue(new File("json_file"), JSONObject.class);
            }
        } catch (FileNotFoundException e){
            System.out.println("File not found.");
        }
    }
}
