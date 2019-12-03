package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Task implements Comparable<Task> {

    private static int count = 0;
    private LocalDateTime inicio, fim;
    private String nome, tipo, duracao, dificuldade, prioridade;


    public Task() {
        count++;
    }



    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public String getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(String dificuldade) {
        this.dificuldade = dificuldade;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
        String hourMinutes[] = duracao.split(":");
        int hours = Integer.parseInt(hourMinutes[0]);
        int minutes = Integer.parseInt(hourMinutes[1]);
        LocalDateTime fim = inicio;
        fim = fim.plusHours(hours);
        fim = fim.plusMinutes(minutes);
        this.fim = fim;
    }



    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int compareTo(Task other) {
//        int compare = Integer.compare(String.valueOf(this.beg), String.valueOf(other.beg));
        return 1;
    }

    @Override
    public String toString() {
        return "Task{" +
                "inicio=" + inicio +
                ", fim=" + fim +
                ", nome='" + nome + '\'' +
                ", tipo='" + tipo + '\'' +
                ", duracao='" + duracao + '\'' +
                ", dificuldade='" + dificuldade + '\'' +
                ", prioridade='" + prioridade + '\'' +
                '}';
    }
}