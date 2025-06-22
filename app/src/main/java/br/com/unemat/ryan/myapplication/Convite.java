package br.com.unemat.ryan.myapplication;

import java.util.ArrayList;
import java.util.List;

public class Convite {
    private String nomeIdade;
    private String bairro;
    private String situacao;
    private List<Object> documentos;

    public Convite(String nomeIdade, String bairro, String situacao, List<Object> documentos) {
        this.nomeIdade = nomeIdade;
        this.bairro = bairro;
        this.situacao = situacao;
        this.documentos = documentos;
    }

    public Convite(String nomeIdade, String bairro, String situacao) {
        this(nomeIdade, bairro, situacao, new ArrayList<>());
    }

    public Convite(String nomeIdade, String bairro) {
        this(nomeIdade, bairro, "Status não informado", new ArrayList<>());
    }

    public String getNomeIdade() { return nomeIdade; }
    public String getBairro() { return bairro; }
    public String getSituacao() { return situacao; }
    public List<Object> getDocumentos() { return documentos; }
}