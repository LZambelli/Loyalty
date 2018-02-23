package joaopogiolli.com.br.loyalty.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import joaopogiolli.com.br.loyalty.AssyncTasks.AssyncTaskListaPromocoesAdapter;
import joaopogiolli.com.br.loyalty.Models.Estabelecimento;
import joaopogiolli.com.br.loyalty.R;
import joaopogiolli.com.br.loyalty.Utils.StaticUtils;

public class ListaPromocoesFragment extends Fragment {

    private Estabelecimento estabelecimento;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lista_promocoes, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            estabelecimento = new Gson()
                    .fromJson(bundle.getString(StaticUtils.PUT_EXTRA_TIPO_ESTABELECIMENTO), Estabelecimento.class);
            AssyncTaskListaPromocoesAdapter assyncTaskListaPromocoesAdapter
                    = new AssyncTaskListaPromocoesAdapter(getContext(), estabelecimento, view);
            assyncTaskListaPromocoesAdapter.execute();
        }

        return view;
    }
}
