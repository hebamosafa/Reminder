package com.example.lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Simulation {
    public ArrayList<Item> load_item(){
        ArrayList<Item> items=new ArrayList<Item>();
        helper("file1.txt",items);
        helper("file2.txt",items);
        return items;
    }
    public ArrayList<U1> loadU1(){
        ArrayList<Item> items=new ArrayList<Item>();
        ArrayList<U1> U1s=new ArrayList<U1>();
        items=load_item();
        U1s.add(new U1());
      for (Item a:items){
          if(U1s.get(U1s.size()-1).canCarry(a)){
          }
          else{
              U1s.add(new U1());
          }
      }
        return U1s;
    }
    void helper(String file,ArrayList<Item> items){


        try {
            Item item = new Item();
            File myObj = new File(file);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] myItem = data.split("=", 2);
                item.name=myItem[0];
                item.weight=Integer.parseInt(myItem[1]);
                items.add(item);
                System.out.println(data);
            }
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
