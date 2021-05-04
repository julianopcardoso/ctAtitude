package br.com.ctatitude.utils;

public enum EnumSimNao{
    SIM("S"), NAO("N");

    private final String valor;

    EnumSimNao(String valorOpcao) {
        valor = valorOpcao;
    }

    public String getValor() {
        return valor;
    }
}
