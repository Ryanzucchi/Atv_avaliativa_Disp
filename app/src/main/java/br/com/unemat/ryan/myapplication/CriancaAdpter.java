package br.com.unemat.ryan.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import java.io.IOException;
import java.util.List;

public class CriancaAdpter extends ArrayAdapter<Convite> {

    // Interface para comunicar a ação para a Activity
    public interface CriancaAdapterListener {
        void onAcaoConfirmada(Convite convite, String motivo, boolean foiConvocado);
    }

    private final CriancaAdapterListener mListener;

    public CriancaAdpter(@NonNull Context context, @NonNull List<Convite> convites, CriancaAdapterListener listener) {
        super(context, 0, convites);
        this.mListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_unificado, parent, false);
        }

        // --- Encontra todos os componentes ---
        TextView nomeIdade = convertView.findViewById(R.id.nome_idade);
        TextView bairro = convertView.findViewById(R.id.bairro);
        TextView situacao = convertView.findViewById(R.id.situacao); // Encontra o TextView do status...
        GridLayout gridLayout = convertView.findViewById(R.id.grid_documentos_container);
        LinearLayout layoutBotoesAdmin = convertView.findViewById(R.id.layout_botoes_admin);
        Button btnAdicionarDocumento = convertView.findViewById(R.id.btn_adicionar_documento);
        Button btnConvocar = convertView.findViewById(R.id.btn_convocar);
        Button btnRecusar = convertView.findViewById(R.id.btn_recusar);

        // --- Lógica de visibilidade ---
        layoutBotoesAdmin.setVisibility(View.VISIBLE);
        btnAdicionarDocumento.setVisibility(View.GONE);
        situacao.setVisibility(View.GONE); // ...E ESCONDE O CAMPO DE STATUS IMEDIATAMENTE.

        Convite convite = getItem(position);

        if (convite != null) {
            // Preenche apenas nome e bairro. A lógica do status foi removida daqui.
            nomeIdade.setText(convite.getNomeIdade());
            bairro.setText(convite.getBairro());

            // A lógica para exibir a grade de documentos continua a mesma
            gridLayout.removeAllViews();
            List<Object> documentos = convite.getDocumentos();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (int i = 0; i < documentos.size(); i++) {
                ImageView imageView = (ImageView) inflater.inflate(R.layout.grid_item_image, gridLayout, false);
                Object doc = documentos.get(i);

                // Carrega a imagem ou miniatura do PDF... (código sem alteração)
                if (doc instanceof Uri) {
                    String mimeType = getContext().getContentResolver().getType((Uri) doc);
                    if (mimeType != null && mimeType.equals("application/pdf")) {
                        Bitmap pdfThumbnail = getPdfThumbnail(getContext(), (Uri) doc);
                        if (pdfThumbnail != null) imageView.setImageBitmap(pdfThumbnail);
                        else imageView.setImageResource(R.drawable.ic_pdf_document);
                    } else {
                        Glide.with(getContext()).load(doc).placeholder(R.drawable.ic_launcher_background).into(imageView);
                    }
                } else {
                    Glide.with(getContext()).load(doc).placeholder(R.drawable.ic_launcher_background).into(imageView);
                }

                imageView.setOnClickListener(v -> abrirDialogoDePreview(doc));

                // Adiciona a imagem à grade... (código sem alteração)
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.width = 0;
                params.setMargins(4, 4, 4, 4);
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                imageView.setLayoutParams(params);
                gridLayout.addView(imageView);
            }

            // Define o clique dos botões de admin
            btnConvocar.setOnClickListener(v -> abrirDialogoDeMotivo(convite, true));
            btnRecusar.setOnClickListener(v -> abrirDialogoDeMotivo(convite, false));
        }

        return convertView;
    }

    /**
     * MÉTODO QUE ESTAVA FALTANDO (1/3): Abre a janela flutuante para PREVIEW do documento.
     */
    private void abrirDialogoDePreview(Object documento) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_preview_simples);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        ImageView imagemPreview = dialog.findViewById(R.id.imagem_preview);

        if (documento instanceof Uri && "application/pdf".equals(getContext().getContentResolver().getType((Uri) documento))) {
            imagemPreview.setImageResource(R.drawable.ic_pdf_document);
        } else {
            Glide.with(getContext()).load(documento).into(imagemPreview);
        }

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * MÉTODO QUE ESTAVA FALTANDO (2/3): Abre a janela flutuante para adicionar o MOTIVO da ação.
     */
    private void abrirDialogoDeMotivo(Convite convite, boolean isConvocacao) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_motivo);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tituloDialog = dialog.findViewById(R.id.dialog_titulo);
        EditText editMotivo = dialog.findViewById(R.id.edit_text_motivo);
        Button btnCancelar = dialog.findViewById(R.id.btn_cancelar_dialogo);
        Button btnConfirmar = dialog.findViewById(R.id.btn_confirmar_dialogo);

        tituloDialog.setText(isConvocacao ? "Motivo da Convocação" : "Motivo da Recusa");
        btnCancelar.setOnClickListener(v -> dialog.dismiss());
        btnConfirmar.setOnClickListener(v -> {
            String motivo = editMotivo.getText().toString().trim();
            if (motivo.isEmpty()) {
                editMotivo.setError("O motivo não pode ser vazio");
                return;
            }
            if (mListener != null) {
                mListener.onAcaoConfirmada(convite, motivo, isConvocacao);
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    /**
     * MÉTODO QUE ESTAVA FALTANDO (3/3): Usa o PdfRenderer para criar uma miniatura da capa do PDF.
     */
    private Bitmap getPdfThumbnail(Context context, Uri pdfUri) {
        Bitmap bitmap = null;
        try (ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(pdfUri, "r")) {
            if (pfd != null) {
                PdfRenderer renderer = new PdfRenderer(pfd);
                PdfRenderer.Page page = renderer.openPage(0);
                bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                page.close();
                renderer.close();
            }
        } catch (IOException e) {
            Log.e("PdfRendererError", "Erro ao gerar miniatura do PDF", e);
        }
        return bitmap;
    }
}