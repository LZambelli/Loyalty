package joaopogiolli.com.br.loyalty.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import joaopogiolli.com.br.loyalty.Models.Promocao;
import joaopogiolli.com.br.loyalty.R;

/**
 * Created by Joao Poggioli on 22/02/2018.
 */

public class ListaPromocoesAdapter extends BaseAdapter {

    private List<Promocao> listaPromocao;
    private final Context context;
    private TextView textViewTituloAdapterListaPromocoes;
    private TextView textViewDescricaoAdapterListaPromocoes;
    private TextView textViewDataAdapterListaPromocoes;

    public ListaPromocoesAdapter(Context context, List<Promocao> listaPromocao) {
        this.context = context;
        this.listaPromocao = listaPromocao;
    }

    @Override
    public int getCount() {
        return listaPromocao.size();
    }

    @Override
    public Object getItem(int index) {
        return listaPromocao.get(index);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.adapter_lista_promocoes, parent, false);
            initView(view);
            Promocao promocao = listaPromocao.get(index);
            textViewTituloAdapterListaPromocoes.setText(promocao.getTitulo());
            textViewDescricaoAdapterListaPromocoes.setText(promocao.getDescricao());
            textViewDataAdapterListaPromocoes.setText(promocao.getDataCricao());
        }

        return view;
    }

    private void initView(View view) {
        textViewTituloAdapterListaPromocoes = view.findViewById(R.id.textViewTituloAdapterListaPromocoes);
        textViewDescricaoAdapterListaPromocoes = view.findViewById(R.id.textViewDescricaoAdapterListaPromocoes);
        textViewDataAdapterListaPromocoes = view.findViewById(R.id.textViewDataAdapterListaPromocoes);
    }
}
