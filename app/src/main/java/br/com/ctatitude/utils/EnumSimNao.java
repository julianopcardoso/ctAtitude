package br.com.ctatitude.utils;

/**
 * EnumSimNao
 */
public enum EnumSimNao{
    /**
     * Sim enum sim nao.
     */
    SIM("S"),
    /**
     * Nao enum sim nao.
     */
    NAO("N");

    private final String valor;

    /**
     * Construtor
     * @param valorOpcao
     */
    EnumSimNao(String valorOpcao) {
        valor = valorOpcao;
    }

    /**
     * Função getValor
     *
     * @return String valor
     */
    public String getValor() {
        return valor;
    }
}
