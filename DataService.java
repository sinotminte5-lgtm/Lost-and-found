package com.lostid.model;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class DataService {
    private static final String USERS_FILE = "users.txt";
    private static final String LOST_FILE = "lost_reports.txt";
    private static final String FOUND_FILE = "found_reports.txt";

    // --- SAVE USER (For Registration) ---
    public static void saveUser(String name, String dept, String section, String contact, String pass) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(name + "|" + dept + "|" + section + "|" + contact + "|" + pass);
            writer.newLine();
        }
    }

    // --- AUTHENTICATION ---
    public static String authenticate(String user, String pass) {
        if (user.trim().equalsIgnoreCase("admin") && pass.equals("admin123")) return "ADMIN";
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length >= 5 && p[0].equalsIgnoreCase(user.trim()) && p[4].equals(pass)) return "USER";
            }
        } catch (IOException e) { return "ERROR"; }
        return "FAIL";
    }

    // --- SAVE LOST REPORT (Reported by Owner) ---
    public static void saveLostReport(String ownerName, String id, String type, String loc) throws IOException {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(LOST_FILE, true))) {
            // Format: OwnerName|ID|Type|Location
            w.write(ownerName + "|" + id + "|" + type + "|" + loc);
            w.newLine();
        }
    }

    // --- SAVE FOUND REPORT (Reported by Finder) ---
    public static void saveFoundReport(String finderName, String id, String loc) throws IOException {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(FOUND_FILE, true))) {
            // Format: FinderName|ID|Location/Description
            w.write(finderName + "|" + id + "|" + loc);
            w.newLine();
        }
    }

    // --- UPDATED MATCHING LOGIC ---
    // This logic ensures the Admin sees two DIFFERENT names for Owner and Finder
    public static List<String> getDetailedMatches() {
        List<String> matches = new ArrayList<>();
        try {
            List<String[]> losts = getAllLost();    // [Owner, ID, Type, Loc]
            List<String[]> founds = getAllFound();  // [Finder, ID, Loc]

            for (String[] l : losts) {
                for (String[] f : founds) {
                    // Check if ID numbers match (Index 1 in both files)
                    if (l.length > 1 && f.length > 1 && l[1].trim().equalsIgnoreCase(f[1].trim())) {
                        String owner = l[0];   // Zerihun
                        String finder = f[0];  // Yabsra
                        String idNum = l[1];   // 2222

                        matches.add("ID: " + idNum + " | Owner: " + owner + " | Found by: " + finder);
                    }
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return matches;
    }

    // --- DELETE LOGIC ---
    public static void deleteRecord(String type, String uniqueId) throws IOException {
        String filename = type.equalsIgnoreCase("Users") ? USERS_FILE :
                type.equalsIgnoreCase("Lost") ? LOST_FILE : FOUND_FILE;
        File file = new File(filename);
        if (!file.exists()) return;

        List<String> lines;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            lines = reader.lines()
                    .filter(line -> !line.contains("|" + uniqueId + "|") && !line.startsWith(uniqueId + "|"))
                    .collect(Collectors.toList());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    // --- DATA RETRIEVAL HELPERS ---
    public static List<String[]> getAllUsers() throws IOException { return readFile(USERS_FILE); }
    public static List<String[]> getAllLost() throws IOException { return readFile(LOST_FILE); }
    public static List<String[]> getAllFound() throws IOException { return readFile(FOUND_FILE); }

    private static List<String[]> readFile(String fileName) throws IOException {
        List<String[]> data = new ArrayList<>();
        File f = new File(fileName);
        if (!f.exists()) return data;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) data.add(line.split("\\|"));
            }
        }
        return data;
    }

    // --- STATISTICS COUNTERS ---
    public static int getTotalUsers() { return countLines(USERS_FILE); }
    public static int getActiveLost() { return countLines(LOST_FILE); }
    public static int getPendingFound() { return countLines(FOUND_FILE); }

    private static int countLines(String filename) {
        File f = new File(filename);
        if (!f.exists()) return 0;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            int count = 0;
            while (r.readLine() != null) count++;
            return count;
        } catch (IOException e) { return 0; }
    }
}