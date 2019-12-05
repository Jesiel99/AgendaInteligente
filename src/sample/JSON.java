package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSON {

    private GsonBuilder builder;
    private String fileName;
    private Gson gson;

    public JSON(String fileName) throws IOException {
        setFileName(fileName);
        FileWriter writer = new FileWriter(fileName);
        writer.write("");
        builder = new GsonBuilder();
        gson = builder.create();
    }

    public JSON() {
        builder = new GsonBuilder();
        gson = builder.create();
    }

    public void gravar(List<Tarefa> lista) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(gson.toJson(lista));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gravarHorario(List<Intervalo> lista) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(gson.toJson(lista));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        public List<Intervalo> lerHorario() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            Type listType = new TypeToken<ArrayList<Intervalo>>() {
            }.getType();
            List<Intervalo> lista = new ArrayList<Intervalo>();
            lista = new Gson().fromJson(bufferedReader, listType);
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Tarefa> ler() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            Type listType = new TypeToken<ArrayList<Tarefa>>() {
            }.getType();
            List<Tarefa> lista = new ArrayList<Tarefa>();
            lista = new Gson().fromJson(bufferedReader, listType);
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
/*
    public void incluir(String array) throws IOException {
        // TODO Auto-generated method stub
        List<String> list = new ArrayList<String>();
        if (ler() == null) {
            list.add(array);
            gravar(list);
        } else {
            list = ler();
            list.add(array);
            gravar(list);
        }

    }
*/
    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName + ".json";
    }
}
