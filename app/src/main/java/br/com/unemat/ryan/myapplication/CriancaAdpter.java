package br.com.unemat.ryan.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CriancaAdpter extends ArrayAdapter<Convite> {

    public CriancaAdpter(Context context, List<Convite> convites) {
        super(context, 0, convites);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Convite convite = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_convite, parent, false);
        }

        TextView nomeIdade = convertView.findViewById(R.id.nome_idade);
        TextView bairro = convertView.findViewById(R.id.bairro);
        Button btnConvocar = convertView.findViewById(R.id.btn_convocar);
        Button btnRecusar = convertView.findViewById(R.id.btn_recusar);

        if (convite != null) {
            nomeIdade.setText(convite.getNomeIdade());
            bairro.setText(convite.getBairro());

            btnConvocar.setOnClickListener(v ->
                    Toast.makeText(getContext(), "Convocado: " + convite.getNomeIdade(), Toast.LENGTH_SHORT).show()
            );

            btnRecusar.setOnClickListener(v ->
                    Toast.makeText(getContext(), "Recusado: " + convite.getNomeIdade(), Toast.LENGTH_SHORT).show()
            );
        }

        return convertView;
    }
}
