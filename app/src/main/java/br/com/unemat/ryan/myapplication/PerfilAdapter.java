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
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

public class PerfilAdapter extends ArrayAdapter<Convite> {

    // Interface para comunicação com a ActivityMain
    public interface PerfilAdapterListener {
        void onAdicionarDocumentoClick(int position);
        void onAlterarDocumentoClick(int position, int documentoIndex);
    }

    private final PerfilAdapterListener mListener;

    // Construtor que recebe a "ponte" de comunicação
    public PerfilAdapter(@NonNull Context context, @NonNull List<Convite> objects, PerfilAdapterListener listener) {
        super(context, 0, objects);
        this.mListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Usa o layout unificado que criamos
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_unificado, parent, false);
        }

        // --- Encontra todos os componentes do layout ---
        TextView nomeIdade = convertView.findViewById(R.id.nome_idade);
        TextView bairro = convertView.findViewById(R.id.bairro);
        TextView situacao = convertView.findViewById(R.id.situacao);
        GridLayout gridLayout = convertView.findViewById(R.id.grid_documentos_container);
        LinearLayout layoutBotoesAdmin = convertView.findViewById(R.id.layout_botoes_admin);
        Button btnAdicionarDocumento = convertView.findViewById(R.id.btn_adicionar_documento);

        // --- Lógica de visibilidade dos botões para ESTE adapter ---
        layoutBotoesAdmin.setVisibility(View.GONE);       // Esconde "Convocar/Recusar"
        btnAdicionarDocumento.setVisibility(View.VISIBLE); // Mostra "Adicionar Documento"

        // Pega o objeto de dados da posição atual
        Convite conviteAtual = getItem(position);

        if (conviteAtual != null) {
            // --- Preenche os dados de texto ---
            nomeIdade.setText(conviteAtual.getNomeIdade());
            bairro.setText(conviteAtual.getBairro());
            String statusText = conviteAtual.getSituacao();
            situacao.setText("Status: " + statusText);

            // Lógica de cor para o status
            if (statusText.toLowerCase().contains("convocado")) {
                situacao.setTextColor(Color.parseColor("#388E3C"));
            } else if (statusText.toLowerCase().contains("analise")) {
                situacao.setTextColor(Color.parseColor("#0288D1"));
            } else {
                situacao.setTextColor(Color.parseColor("#F57C00"));
            }

            // --- Lógica para preencher a grade de documentos dinamicamente ---
            gridLayout.removeAllViews(); // Limpa a grade para evitar duplicatas ao rolar a lista
            List<Object> documentos = conviteAtual.getDocumentos();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            for (int i = 0; i < documentos.size(); i++) {
                ImageView imageView = (ImageView) inflater.inflate(R.layout.grid_item_image, gridLayout, false);
                Object doc = documentos.get(i);

                // Lógica para carregar imagem ou miniatura de PDF
                if (doc instanceof Uri) {
                    String mimeType = getContext().getContentResolver().getType((Uri) doc);
                    if (mimeType != null && mimeType.equals("application/pdf")) {
                        Bitmap pdfThumbnail = getPdfThumbnail(getContext(), (Uri) doc);
                        if (pdfThumbnail != null) {
                            imageView.setImageBitmap(pdfThumbnail);
                        } else {
                            imageView.setImageResource(R.drawable.ic_pdf_document);
                        }
                    } else {
                        Glide.with(getContext()).load(doc).placeholder(R.drawable.ic_launcher_background).into(imageView);
                    }
                } else { // Carrega um drawable (ex: R.drawable.d1)
                    Glide.with(getContext()).load(doc).placeholder(R.drawable.ic_launcher_background).into(imageView);
                }

                // Define o clique para alterar este documento específico
                final int docIndex = i + 1;
                imageView.setOnClickListener(v -> mostrarDialogoComOpcaoDeAlterar(position, docIndex));

                // Configura o layout da imagem para se ajustar na grade
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.width = 0;
                params.setMargins(4, 4, 4, 4);
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                imageView.setLayoutParams(params);

                gridLayout.addView(imageView); // Adiciona a imagem pronta à grade
            }

            // Define o clique para o botão "Adicionar Documento"
            btnAdicionarDocumento.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onAdicionarDocumentoClick(position);
                }
            });
        }
        return convertView;
    }

    /**
     * Mostra o diálogo flutuante quando uma imagem é clicada.
     */
    private void mostrarDialogoComOpcaoDeAlterar(int position, int documentoIndex) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_imagem_com_botao);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        ImageView imagemAmpliada = dialog.findViewById(R.id.imagem_dialogo_alterar);
        Button btnAlterar = dialog.findViewById(R.id.btn_alterar_imagem);

        Object docParaMostrar = getItem(position).getDocumentos().get(documentoIndex - 1);

        // No diálogo, mostramos o ícone genérico para PDF para simplicidade
        if (docParaMostrar instanceof Uri && "application/pdf".equals(getContext().getContentResolver().getType((Uri) docParaMostrar))) {
            imagemAmpliada.setImageResource(R.drawable.ic_pdf_document);
        } else {
            Glide.with(getContext()).load(docParaMostrar).into(imagemAmpliada);
        }

        btnAlterar.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onAlterarDocumentoClick(position, documentoIndex);
            }
            dialog.dismiss();
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * Usa o PdfRenderer do Android para criar uma imagem (Bitmap) da primeira página de um PDF.
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