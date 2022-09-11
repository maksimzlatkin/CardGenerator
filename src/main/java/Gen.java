//package com.company;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Gen {

    public static void main(String[] args){
        // write your code here
        FileWriter file;

        try {
            //Files.delete(Paths.get("Cards"));
            //new File("Cards").mkdirs();

            File names = new File("Names.csv");
            Scanner reader = new Scanner(names);

            String lastName = "Card";

            String temp = new String(Files.readAllBytes(Paths.get("Card.pass/pass.json")));
            JSONObject tempjson = new JSONObject(temp);

            file = new FileWriter("temp.json");
            file.write(tempjson.toString(2));
            file.flush();
            file.close();

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] split = line.split(",");
                String number = split[split.length - 1];
                line = "";

                for (int i = 0; i < split.length - 1; i++) {
                    line += split[i] + " ";
                }
                //line = line.substring(0, line.length() - 1);
                line = split[0];
                number = number.substring(0, 3) + "-" + number.substring(3, 6) + "-" + number.substring(6, number.length());
                GenerateCard(line, lastName, number);
                lastName = line;
            }

            String contents = new String(Files.readAllBytes(Paths.get("temp.json")));
            JSONObject root = new JSONObject(contents);

            file = new FileWriter("Card.pass/pass.json");
            file.write(root.toString(2));
            file.flush();
            file.close();

            File remover = new File("temp.json");
            remover.delete();

        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static void GenerateCard(String name, String lastName, String number) {
        try {
            System.out.println(name + ", " + lastName);

            FileWriter file;
            String contents = new String(Files.readAllBytes(Paths.get("temp.json")));
            JSONObject root = new JSONObject(contents);
            JSONObject storeCard = root.getJSONObject("storeCard");
            JSONArray auxiliaryFields = storeCard.getJSONArray("secondaryFields");
            JSONObject o = (JSONObject) auxiliaryFields.get(0);

            JSONArray headerFields = storeCard.getJSONArray("headerFields");
            JSONObject h = (JSONObject) headerFields.get(0);

            JSONArray backFields = storeCard.getJSONArray("backFields");
            JSONObject b = (JSONObject) backFields.get(1);
            String text = b.getString("value");
            //text = name + " " + text.substring(0, 15) + number + text.substring(15 + number.length());
            text = text.replaceAll("##Name##", name);
            text = text.replaceAll("##Phone##", number);

            b.put("value", text);
            o.put("value", name);
            h.put("value", number);
            String cardJSON = root.toString(2);

            file = new FileWriter("Card.pass/pass.json");
            file.write(cardJSON);
            file.flush();
            file.close();

            Process process = Runtime.getRuntime().exec("./signpass -p Card.pass");
            System.out.println(process);
            process.waitFor();

            //MOVING CARD.PKPASS TO A FOLDER + CHANGING FILE NAME
            File checker = new File("Cards/" + name.replaceAll(" ", "") + ".pkpass");

            File cpkpass = new File("Card.pkpass");
            File npkpass = new File(name.replaceAll(" ", "") + ".pkpass");
            if (checker.exists()){
                System.out.println("Card already exists under this name.");
                checker.delete();
            }
            if (cpkpass.renameTo(npkpass)) {
                System.out.println("Name changed" + " " + cpkpass.getName());
            }
            Path p = Files.move
                    (Paths.get(name.replaceAll(" ", "") + ".pkpass"),
                            Paths.get("Cards/" + name.replaceAll(" ", "") + ".pkpass"));
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        catch (InterruptedException e){
            System.out.println(e.getMessage());
        }
    }
}
