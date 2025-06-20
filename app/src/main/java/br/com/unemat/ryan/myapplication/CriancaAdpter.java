package br.com.unemat.ryan.myapplication;

import android.app.Dialog; // IMPORTANTE: Nova importação
import android.content.Context;
// A importação do Intent não é mais necessária para esta função
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

import java.util.List;

public class CriancaAdpter extends ArrayAdapter<Convite> {

    public CriancaAdpter(Context context, List<Convite> convites) {
        super(context, 0, convites);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_convite, parent, false);
        }

        // Encontra os componentes do layout (sem alterações aqui)
        TextView nomeIdade = convertView.findViewById(R.id.nome_idade);
        TextView bairro = convertView.findViewById(R.id.bairro);
        Button btnConvocar = convertView.findViewById(R.id.btn_convocar);
        Button btnRecusar = convertView.findViewById(R.id.btn_recusar);
        ImageView doc1 = convertView.findViewById(R.id.documento_1);
        ImageView doc2 = convertView.findViewById(R.id.documento_2);
        ImageView doc3 = convertView.findViewById(R.id.documento_3);
        ImageView doc4 = convertView.findViewById(R.id.documento_4);
        ImageView doc5 = convertView.findViewById(R.id.documento_5);

        Convite convite = getItem(position);

        if (convite != null) {
            nomeIdade.setText(convite.getNomeIdade());
            bairro.setText(convite.getBairro());

            btnConvocar.setOnClickListener(v ->
                    Toast.makeText(getContext(), "Convocado: " + convite.getNomeIdade(), Toast.LENGTH_SHORT).show()
            );

            btnRecusar.setOnClickListener(v ->
                    Toast.makeText(getContext(), "Recusado: " + convite.getNomeIdade(), Toast.LENGTH_SHORT).show()
            );

            // =============================================
            //      LÓGICA DE CLIQUE ATUALIZADA
            // =============================================
            doc1.setOnClickListener(v -> mostrarImagemEmDialogo(R.drawable.d1));
            doc2.setOnClickListener(v -> mostrarImagemEmDialogo(R.drawable.d2));
            doc3.setOnClickListener(v -> mostrarImagemEmDialogo(R.drawable.d3));
            doc4.setOnClickListener(v -> mostrarImagemEmDialogo(R.drawable.d4));
            doc5.setOnClickListener(v -> mostrarImagemEmDialogo(R.drawable.d5));
        }

        return convertView;
    }

    // =================================================================
    // NOVO MÉTODO: Em vez de abrir uma Activity, ele abre um Dialog
    // =================================================================
    private void mostrarImagemEmDialogo(int imageResource) {
        // Cria o Dialog
        final Dialog dialog = new Dialog(getContext());
        // Define o layout que criamos para ser o conteúdo do Dialog
        dialog.setContentView(R.layout.dialog_imagem_flutuante);

        // Deixa o fundo do Dialog transparente para não vermos uma "caixa" branca
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Encontra a ImageView DENTRO do layout do Dialog
        ImageView imagemAmpliada = dialog.findViewById(R.id.imagem_flutuante);
        // Define qual imagem deve aparecer
        imagemAmpliada.setImageResource(imageResource);

        // Permite que o Dialog seja fechado ao clicar fora dele
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        // Mostra o Dialog na tela
        dialog.show();
    }
}