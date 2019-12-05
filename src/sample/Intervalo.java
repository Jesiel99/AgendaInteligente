package sample;

import java.time.LocalTime;

public class Intervalo {

    Intervalo(String inicio, String fim) {
        this.inicio = Cast.stringToLocalTime(inicio);
        this.fim = Cast.stringToLocalTime(fim);
    }

    private LocalTime inicio, fim;

    public LocalTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalTime inicio) {
        this.inicio = inicio;
    }

    public LocalTime getFim() {
        return fim;
    }

    public void setFim(LocalTime fim) {
        this.fim = fim;
    }

    @Override
    public String toString() {
        return "HorariosIndisponiveis{" +
                "inicio=" + inicio +
                ", fim=" + fim +
                '}';
    }
}
