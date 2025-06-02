package br.com.unemat.ryan.myapplication;

public class Convite {
    private String nomeIdade;
    private String bairro;

    public Convite(String nomeIdade, String bairro) {
        this.nomeIdade = nomeIdade;
        this.bairro = bairro;
    }

    public String getNomeIdade() {
        return nomeIdade;
    }

    public void setNomeIdade(String nomeIdade) {
        this.nomeIdade = nomeIdade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
}
