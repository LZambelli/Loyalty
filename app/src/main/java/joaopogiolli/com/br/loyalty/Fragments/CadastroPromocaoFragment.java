package joaopogiolli.com.br.loyalty.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import joaopogiolli.com.br.loyalty.R;

/**
 * Created by Joao Poggioli on 22/02/2018.
 */

public class CadastroPromocaoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_promocao, container, false);
        if (view == null) {
        }
        return view;
    }
}
