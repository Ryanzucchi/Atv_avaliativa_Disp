// Documento.java
package br.com.unemat.ryan.myapplication;

public class Documento {
    private String nome;
    private int imagemResource; // Para guardar a referência da imagem (ex: R.drawable.d1)

    public Documento(String nome, int imagemResource) {
        this.nome = nome;
        this.imagemResource = imagemResource;
    }

    public String getNome() {
        return nome;
    }

    public int getImagemResource() {
        return imagemResource;
    }
}