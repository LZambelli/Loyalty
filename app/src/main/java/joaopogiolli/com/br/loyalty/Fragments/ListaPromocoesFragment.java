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

import com.google.gson.Gson;

import joaopogiolli.com.br.loyalty.AssyncTasks.AsyncTaskListaPromocoesAdapter;
import joaopogiolli.com.br.loyalty.Models.Estabelecimento;
import joaopogiolli.com.br.loyalty.R;
import joaopogiolli.com.br.loyalty.Utils.StaticUtils;

public class ListaPromocoesFragment extends Fragment {

    private Estabelecimento estabelecimento;
    private FloatingActionButton floatingActionButtonActivityEstabelecimentoPrincipal;

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
            if (floatingActionButtonActivityEstabelecimentoPrincipal.getVisibility() == View.GONE) {
                floatingActionButtonActivityEstabelecimentoPrincipal.setVisibility(View.VISIBLE);
            }
            estabelecimento = new Gson()
                    .fromJson(bundle.getString(StaticUtils.PUT_EXTRA_TIPO_ESTABELECIMENTO), Estabelecimento.class);
            AsyncTaskListaPromocoesAdapter asyncTaskListaPromocoesAdapter
                    = new AsyncTaskListaPromocoesAdapter(getContext(), estabelecimento, view);
            asyncTaskListaPromocoesAdapter.execute();
        }

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        MenuItem deletar = menu.add(getString(R.string.excluir));
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        MenuItem editar = menu.add(getString(R.string.editar));
        editar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });


    }
}
