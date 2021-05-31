package br.com.ctatitude.utils;

/**
 * Classe de utilitários
 */
public class Utils {

    /**
     * Converte Minutos:Segundos para segundos
     *
     * @param minutesSeconds the minutes seconds
     * @return integer
     */
    public static Integer convertMinutesSecondsToSeconds(String minutesSeconds) {
        String[] timerSeparado = minutesSeconds.split(":");
        Integer minutos = Integer.valueOf(timerSeparado[0]);
        Integer segundos = Integer.valueOf(timerSeparado[1]);
        return minutos * 60 + segundos * 1;
    }

    /**
     * Converte Horas:Minutos:Segundos para segundos
     *
     * @param hoursMinutesSeconds the hours minutes seconds
     * @return integer
     */
    public static Integer convertHoursMinutesSecondsToSeconds(String hoursMinutesSeconds) {
        String[] timerSeparado = hoursMinutesSeconds.split(":");
        Integer horas = Integer.valueOf(timerSeparado[0]);
        Integer minutos = Integer.valueOf(timerSeparado[1]);
        Integer segundos = Integer.valueOf(timerSeparado[2]);
        return horas * 3600 + minutos * 60 + segundos * 1;
    }

    /**
     * Converte segundos para Horas:Minutos:Segundos
     *
     * @param seconds the seconds
     * @return string
     */
    public static  String convertSecondsToHoursMinutesSeconds(Integer seconds) {
        return String.format("%02d", seconds / 3600) + ":" +
                String.format("%02d", (seconds / 60) % 60) + ":" +
                String.format("%02d", (seconds % 60));
    }

    /**
     * Converte segundos para Minutos:Segundos
     *
     * @param seconds the seconds
     * @return string
     */
    public static String convertSecondsToMinutesSeconds(Integer seconds) {
        return String.format("%02d", (seconds / 60) % 60) + ":" +
                String.format("%02d", (seconds % 60));
    }
}
