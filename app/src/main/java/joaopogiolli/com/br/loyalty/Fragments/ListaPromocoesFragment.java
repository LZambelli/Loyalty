package joaopogiolli.com.br.loyalty.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import joaopogiolli.com.br.loyalty.AssyncTasks.AsyncTaskListaPromocoesAdapter;
import joaopogiolli.com.br.loyalty.Models.Estabelecimento;
import joaopogiolli.com.br.loyalty.Models.Promocao;
import joaopogiolli.com.br.loyalty.R;
import joaopogiolli.com.br.loyalty.Utils.StaticUtils;

public class ListaPromocoesFragment extends Fragment {

    private Estabelecimento estabelecimento;
    private FloatingActionButton floatingActionButtonActivityEstabelecimentoPrincipal;
    private ListView listViewFragmentListaPromocoes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle(R.string.suasPromocoes);
        View view = inflater.inflate(R.layout.fragment_lista_promocoes, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {

            floatingActionButtonActivityEstabelecimentoPrincipal =
                    getActivity().findViewById(R.id.floatingActionButtonActivityEstabelecimentoPrincipal);
            registerForContextMenu(view.findViewById(R.id.listViewFragmentListaPromocoes));
            listViewFragmentListaPromocoes = view.findViewById(R.id.listViewFragmentListaPromocoes);
            if (floatingActionButtonActivityEstabelecimentoPrincipal.getVisibility() == View.GONE) {
                floatingActionButtonActivityEstabelecimentoPrincipal.setVisibility(View.VISIBLE);
            }
            estabelecimento = new Gson()
                    .fromJson(bundle.getString(StaticUtils.PUT_EXTRA_TIPO_ESTABELECIMENTO), Estabelecimento.class);
            AsyncTaskListaPromocoesAdapter asyncTaskListaPromocoesAdapter
                    = new AsyncTaskListaPromocoesAdapter(getContext(), estabelecimento, view, listViewFragmentListaPromocoes);
            asyncTaskListaPromocoesAdapter.execute();
        }

        return view;
    }

}
