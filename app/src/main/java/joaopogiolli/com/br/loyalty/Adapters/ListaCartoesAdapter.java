package joaopogiolli.com.br.loyalty.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import joaopogiolli.com.br.loyalty.Firebase.FirebaseUtils;
import joaopogiolli.com.br.loyalty.Models.Cartao;
import joaopogiolli.com.br.loyalty.Models.Estabelecimento;
import joaopogiolli.com.br.loyalty.Models.Promocao;
import joaopogiolli.com.br.loyalty.R;
import joaopogiolli.com.br.loyalty.Utils.StaticUtils;

/**
 * Created by Joao Poggioli on 25/02/2018.
 */

public class ListaCartoesAdapter extends BaseAdapter {

    private List<Cartao> listaCartao;
    private List<Promocao> listaPromocao;
    private List<Estabelecimento> listaEstabelecimento;
    private final Context context;
    private TextView textViewNomePromocaoListaCartoesAdapter;
    private TextView textViewDescricaoPromocaoListaCartoesAdapter;
    private ImageView imageViewFotoEstabelecimentoListaCartoesAdapter;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageDatabase;

    public ListaCartoesAdapter(Context context, List<Cartao> listaCartao, List<Promocao> listaPromocao,
                               List<Estabelecimento> listaEstabelecimento) {
        this.context = context;
        this.listaCartao = listaCartao;
        this.listaPromocao = listaPromocao;
        this.listaEstabelecimento = listaEstabelecimento;
        firebaseStorage = FirebaseUtils.getFirebaseStorage();
    }

    @Override
    public int getCount() {
        return listaCartao.size();
    }

    @Override
    public Object getItem(int position) {
        return listaCartao.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.adapter_lista_cartoes, parent, false);

            Cartao cartao = listaCartao.get(position);
            Promocao promocao = null;
            Estabelecimento estabelecimento = null;
            if (cartao != null) {
                promocao = getPromocao(cartao.getIdPromocao());
            }
            if (promocao != null) {
                estabelecimento = getEstabelecimento(promocao.getIdEstabelecimento());
            }

            if (cartao != null && promocao != null && estabelecimento != null) {
                textViewNomePromocaoListaCartoesAdapter =
                        view.findViewById(R.id.textViewNomePromocaoListaCartoesAdapter);
                textViewDescricaoPromocaoListaCartoesAdapter =
                        view.findViewById(R
                                .id.textViewDescricaoPromocaoListaCartoesAdapter);

                LinearLayout linearLayout1 = view.findViewById(R.id.linearLayout1);
                LinearLayout linearLayout2 = view.findViewById(R.id.linearLayout2);
                LinearLayout linearLayout3 = view.findViewById(R.id.linearLayout3);
                LinearLayout linearLayout4 = view.findViewById(R.id.linearLayout4);
                imageViewFotoEstabelecimentoListaCartoesAdapter =
                        view.findViewById(R.id.imageViewFotoEstabelecimentoListaCartoesAdapter);

                textViewNomePromocaoListaCartoesAdapter.setText(promocao.getTitulo());
                String descPromo = promocao.getDescricao();

                if (descPromo.length() > 34) {
                    String caracteresEspeciais = descPromo.substring(33, 34);
                    if (caracteresEspeciais.equals("!") || caracteresEspeciais.equals(",") ||
                            caracteresEspeciais.equals(".") || caracteresEspeciais.equals(" ")) {
                        descPromo = descPromo.substring(0, 29) + "...";
                    } else {
                        descPromo = descPromo.substring(0, 34) + "...";
                    }
                    textViewDescricaoPromocaoListaCartoesAdapter.setText(descPromo);
                } else {
                    textViewDescricaoPromocaoListaCartoesAdapter.setText(descPromo);
                }

                int countCarimbos = promocao.getQuantidadeCarimbos();
                int countCarimbado = cartao.getQntCarimbos();
                int auxCount = 0;

                for (int i = 0; i < countCarimbos; i++) {
                    CheckBox checkBox = new CheckBox(context);
                    if (countCarimbado != auxCount) {
                        checkBox.setChecked(true);
                        checkBox.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
                        auxCount++;
                    }
                    checkBox.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                    checkBox.setEnabled(false);
                    checkBox.setFocusable(false);
                    if (i < 5) {
                        linearLayout1.addView(checkBox);
                    } else if (i < 10) {
                        linearLayout2.addView(checkBox);
                    } else if (i < 15) {
                        linearLayout3.addView(checkBox);
                    } else if (i < 20) {
                        linearLayout4.addView(checkBox);
                    }
                }

                storageDatabase = firebaseStorage.getReference(StaticUtils.ESTABELECIMENTO_IMAGES + "/" + estabelecimento.getId() + ".png");
                if (storageDatabase != null) {
                    final View finalView = view;
                    storageDatabase.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(finalView.getContext())
                                    .load(uri)
                                    .into(imageViewFotoEstabelecimentoListaCartoesAdapter);
                        }
                    });
                }
            }
        }

        return view;
    }

    private Estabelecimento getEstabelecimento(String idEstabelecimento) {
        for (Estabelecimento estabelecimento : listaEstabelecimento) {
            if (estabelecimento.getId().equals(idEstabelecimento)) {
                return estabelecimento;
            }
        }
        return null;
    }

    public Promocao getPromocao(String id) {
        for (Promocao promocao : listaPromocao) {
            if (promocao.getId().equals(id)) {
                return promocao;
            }
        }
        return null;
    }
}
