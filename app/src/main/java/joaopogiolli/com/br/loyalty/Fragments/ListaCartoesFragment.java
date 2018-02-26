package joaopogiolli.com.br.loyalty.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;

import joaopogiolli.com.br.loyalty.AssyncTasks.AsyncTaskListaCartoes;
import joaopogiolli.com.br.loyalty.Models.Usuario;
import joaopogiolli.com.br.loyalty.R;
import joaopogiolli.com.br.loyalty.Utils.StaticUtils;

/**
 * Created by Joao Poggioli on 25/02/2018.
 */

public class ListaCartoesFragment extends Fragment {

    private Usuario usuario;
    private ListView listViewFragmentListaCartoes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(R.string.seusCartoes);
        View view = inflater.inflate(R.layout.fragment_lista_cartoes, container, false);
        listViewFragmentListaCartoes = view.findViewById(R.id.listViewFragmentListaCartoes);
        Bundle bundle = getArguments();
        if (bundle != null) {
            usuario = new Gson()
                    .fromJson(bundle.getString(StaticUtils.PUT_EXTRA_TIPO_USUARIO), Usuario.class);
            AsyncTaskListaCartoes asyncTaskListaCartoes
                    = new AsyncTaskListaCartoes(getContext(), usuario, view, listViewFragmentListaCartoes);
            asyncTaskListaCartoes.execute();
        }
        return view;
    }
}
