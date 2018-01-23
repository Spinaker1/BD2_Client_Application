/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author User
 */
public class GUI extends javax.swing.JFrame {
    protected final DBMananger dbManager;
    
    public GUI(DBMananger dbM) 
    {
        dbManager = dbM;
    }
        
    protected String hash(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } 
        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    protected String incrementId(String column, String table) {
        String query = "select max(" + column + ") as cnt FROM " + table;
        String idString = dbManager.executeQuery(query, "cnt");
        int id = Integer.parseInt(idString) + 1;
        return String.valueOf(id);
    }
    
    protected boolean isInTable(String column, String table, String value) {
        String query = "select count("+column+") as cnt from " + table + " where "+column+"='"+value+"'";
        String findAccountString = dbManager.executeQuery(query, "cnt");
        int findAccountInt = Integer.parseInt(findAccountString);
        if (findAccountInt > 0) {
            return true;
        }
        return false;
    }
    
    public static void changeWindow(javax.swing.JFrame actualWindow, javax.swing.JFrame newWindow) {
        actualWindow.dispose(); //closes the window--cannot be recovered
        newWindow.setVisible(true); //shows it
    }
}
