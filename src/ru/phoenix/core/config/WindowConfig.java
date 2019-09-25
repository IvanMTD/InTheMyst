package ru.phoenix.core.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class WindowConfig {

    private static WindowConfig instance = null;

    private File config;

    public WindowConfig(){
        File direct = new File("./data/config/window");
        config = new File (direct,"window_config.txt");
        if(config.exists()){
            String data = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(config.lastModified()));
            System.out.println(data);
        }else{
            System.out.println("File does not exist! Trying to create.");
            try {
                if(config.createNewFile()){
                    System.out.println("File created! " + config.getName());
                    setConfigDefault();
                }else{
                    System.out.println("Error! The window configuration file is not created!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setConfigDefault(){
        if(config.length() == 0){
            List<String> defaultConfig = new ArrayList<>();
            defaultConfig.add("full_screen");defaultConfig.add(Boolean.toString(false));
            defaultConfig.add("window_width");defaultConfig.add(Integer.toString(1280));
            defaultConfig.add("window_height");defaultConfig.add(Integer.toString(720));
            defaultConfig.add("samples");defaultConfig.add(Integer.toString(4));
            defaultConfig.add("gamma");defaultConfig.add(Float.toString(2.2f));
            defaultConfig.add("z_near");defaultConfig.add(Float.toString(0.01f));
            defaultConfig.add("z_far");defaultConfig.add(Float.toString(100.0f));
            writeFile(defaultConfig);
        }
    }

    public void setFullScreen(boolean fullScreen){
        List<String>param = getCurrentCopy();
        for(int i=0; i<param.size(); i++){
            if(param.get(i).equals("full_screen")){
                param.set(i+1,Boolean.toString(fullScreen));
            }
        }
        writeFile(param);
    }

    public boolean isFullScreen(){
        String param = getParam("full_screen");
        return param.equals("true");
    }

    public void setWidth(int width){
        List<String>param = getCurrentCopy();
        for(int i=0; i<param.size(); i++){
            if(param.get(i).equals("window_width")){
                param.set(i+1,Integer.toString(width));
            }
        }
        writeFile(param);
    }

    public int getWidth(){
        return Integer.parseInt(getParam("window_width"));
    }

    public void setHeight(int height){
        List<String>param = getCurrentCopy();
        for(int i=0; i<param.size(); i++){
            if(param.get(i).equals("window_height")){
                param.set(i+1,Integer.toString(height));
            }
        }
        writeFile(param);
    }

    public int getHeight(){
        return Integer.parseInt(getParam("window_height"));
    }

    public void setSamples(int samples){
        List<String>param = getCurrentCopy();
        for(int i=0; i<param.size(); i++){
            if(param.get(i).equals("samples")){
                param.set(i+1,Integer.toString(samples));
            }
        }
        writeFile(param);
    }

    public int getSamples(){
        return Integer.parseInt(getParam("samples"));
    }

    public void setGamma(float gamma){
        List<String>param = getCurrentCopy();
        for(int i=0; i<param.size(); i++){
            if(param.get(i).equals("gamma")){
                param.set(i+1,Float.toString(gamma));
            }
        }
        writeFile(param);
    }

    public float getGamma(){
        return Float.parseFloat(getParam("gamma"));
    }

    public void setNear(float zNear){
        List<String>param = getCurrentCopy();
        for(int i=0; i<param.size(); i++){
            if(param.get(i).equals("z_near")){
                param.set(i+1,Float.toString(zNear));
            }
        }
        writeFile(param);
    }

    public float getNear(){
        return Float.parseFloat(getParam("z_near"));
    }

    public void setFar(float zFar){
        List<String>param = getCurrentCopy();
        for(int i=0; i<param.size(); i++){
            if(param.get(i).equals("z_far")){
                param.set(i+1,Float.toString(zFar));
            }
        }
        writeFile(param);
    }

    public float getFar(){
        return Float.parseFloat(getParam("z_far"));
    }

    private void writeFile(List<String>param){
        try {
            FileWriter fw = new FileWriter(config.getAbsoluteFile());
            for(int i=0; i<param.size(); i+=2){
                fw.write(param.get(i) + " " + param.get(i + 1) + "\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getCurrentCopy(){

        List<String> temp = new ArrayList<>();

        try {
            FileReader fr = new FileReader(config.getAbsoluteFile());
            Scanner sc = new Scanner(fr);

            while (sc.hasNext()){
                temp.add(sc.next());
            }

            sc.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp;
    }

    private String getParam(String name){
        String param = null;
        try {
            FileReader fr = new FileReader(config.getAbsoluteFile());
            Scanner sc = new Scanner(fr);
            while (sc.hasNext()) {
                String temp = sc.next();
                if (temp.equals(name)) {
                    param = sc.next();
                }
            }

            sc.close();
            fr.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        return param;
    }

    public static WindowConfig getInstance(){
        if(instance == null){
            instance = new WindowConfig();
        }
        return instance;
    }
}
