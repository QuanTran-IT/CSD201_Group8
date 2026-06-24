/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.Scanner;

/**
 *
 * @author ADMIN
 */
public class Inputter {
    private static Scanner sc = new Scanner(System.in);
    
    public static String getString(String message) {
        System.out.print(message);
        return sc.next().trim();
    }
    
    public static String inputAndLoop(String message, String error, String pattern) {
        while (true) {
            String input = getString(message);
            input = input.replaceAll("\\s+", " "); //remove space if there are 2 spaces upper
            if (input.matches(pattern)) {
                return input;
            }
            System.out.println(error);
            if (!confirmYesNo("Do you want to enter again ? (Y/N): ")) {
                return null;
            }
        }
    }
    
    public static int getInt(String message, String error, String pattern) {
        String numberInString = inputAndLoop(message, error, pattern);
        if (numberInString != null) {
            return Integer.parseInt(numberInString);
        } else {
            return -1;
        }
        
    }
    
    public static String getStringRegex(String messageInfo,
            String error, String pattern) {
        do {
            String input = getString(messageInfo);
            
            if (input.trim().isEmpty()) {
                return input.trim();
            }
            input = input.replaceAll("\\s+", " "); //remove space if there are 2 spaces upper
            if (input.matches(pattern)) {
                return input;
            }
            System.out.println(error);
        } while (true);
    }
    
    public static int getChoice(String message, String errorOutOfRange, String errorOfNumber, int min, int max) {
        do {
            try {
                int number = Integer.parseInt(getString(message));
                if (number >= min && number <= max) {
                    return number;
                } else {
                    System.out.println(errorOutOfRange);
                }
            } catch (NumberFormatException e) {
                System.out.println(errorOfNumber);
               
            }
        } while (true);
    }
    
    public static int getIntAllowEmpty(String messageInfo, String error, int min) {
        do {
            String input = getString(messageInfo).trim();            
            if (input.isEmpty()) {
                return -1;                
            }            
            try {
                int value = Integer.parseInt(input);
                if (value >= min) {
                    return value;
                } else {
                    System.out.println(error);                    
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
               
            }
        } while (true);
    }
    
    public static boolean confirmYesNo(String message) {
        String confirm = Inputter.getString(message);
        while (true) {
            if (confirm.matches("[YNyn]")) {
                break;
            }
            System.out.println("Just Y or N !");
            confirm = Inputter.getString(message);
        }
        return confirm.equalsIgnoreCase("Y");
    }
}
