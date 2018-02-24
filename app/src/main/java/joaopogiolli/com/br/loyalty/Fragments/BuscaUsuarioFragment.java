package joaopogiolli.com.br.loyalty.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import joaopogiolli.com.br.loyalty.AssyncTasks.AsyncTaskBuscaUsuario;
import joaopogiolli.com.br.loyalty.R;

/**
 * Created by Joao Poggioli on 23/02/2018.
 */

public class BuscaUsuarioFragment extends Fragment {

    private EditText editTextFragmentBuscaUsuario;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_busca_usuario, container, false);
        initViews(view);
        initClicks();

        return view;

    }

    private void initViews(View view) {
        editTextFragmentBuscaUsuario = view.findViewById(R.id.editTextFragmentBuscaUsuario);
    }

    private void initClicks() {
        editTextFragmentBuscaUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String emailUsuario = editTextFragmentBuscaUsuario.getText().toString();
                AsyncTaskBuscaUsuario asyncTaskBuscaUsuario =
                        new AsyncTaskBuscaUsuario(getContext(), emailUsuario);
                asyncTaskBuscaUsuario.execute();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

}
