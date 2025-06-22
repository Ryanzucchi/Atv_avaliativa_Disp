// DocumentoAdapter.java
package br.com.unemat.ryan.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class DocumentoAdapter extends ArrayAdapter<Documento> {

    public DocumentoAdapter(@NonNull Context context, @NonNull List<Documento> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_documento, parent, false);
        }

        ImageView imagemDocumento = convertView.findViewById(R.id.imagem_documento_item);
        TextView nomeDocumento = convertView.findViewById(R.id.nome_documento_item);

        Documento documentoAtual = getItem(position);

        if (documentoAtual != null) {
            imagemDocumento.setImageResource(documentoAtual.getImagemResource());
            nomeDocumento.setText(documentoAtual.getNome());

            // Define o clique na imagem do item da lista
            imagemDocumento.setOnClickListener(v -> mostrarDialogoComOpcaoDeAlterar(documentoAtual));
        }

        return convertView;
    }

    private void mostrarDialogoComOpcaoDeAlterar(Documento documento) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_imagem_com_botao);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ImageView imagemAmpliada = dialog.findViewById(R.id.imagem_dialogo_alterar);
        Button btnAlterar = dialog.findViewById(R.id.btn_alterar_imagem);

        imagemAmpliada.setImageResource(documento.getImagemResource());

        // Ação do botão DENTRO do Dialog
        btnAlterar.setOnClickListener(v -> {
            // Aqui você colocaria a lógica para abrir a galeria e alterar a imagem
            Toast.makeText(getContext(), "Lógica para alterar: " + documento.getNome(), Toast.LENGTH_SHORT).show();
            dialog.dismiss(); // Fecha o diálogo após o clique
        });

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}