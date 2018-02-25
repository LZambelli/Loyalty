package joaopogiolli.com.br.loyalty.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import joaopogiolli.com.br.loyalty.AssyncTasks.AsyncTaskAtribuiCarimbo;
import joaopogiolli.com.br.loyalty.AssyncTasks.AsyncTaskBuscaUsuario;
import joaopogiolli.com.br.loyalty.Firebase.FirebaseUtils;
import joaopogiolli.com.br.loyalty.Models.Cartao;
import joaopogiolli.com.br.loyalty.Models.Estabelecimento;
import joaopogiolli.com.br.loyalty.Models.Promocao;
import joaopogiolli.com.br.loyalty.Models.Usuario;
import joaopogiolli.com.br.loyalty.R;
import joaopogiolli.com.br.loyalty.Utils.StaticUtils;

/**
 * Created by Joao Poggioli on 23/02/2018.
 */

public class BuscaUsuarioFragment extends Fragment {

    private EditText editTextFragmentBuscaUsuario;
    private ListView listViewFragmentBuscaUsuario;
    private Promocao promocao;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_busca_usuario, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            promocao = new Gson()
                    .fromJson(bundle.getString(StaticUtils.PUT_EXTRA_TIPO_PROMOCAO), Promocao.class);
            initViews(view);
            initClicks();
            getActivity().setTitle(R.string.atribuirCarimbo);
            firebaseDatabase = FirebaseUtils.getFirebaseDatabase(getContext());
            databaseReference = FirebaseUtils.getDatabaseReference(firebaseDatabase);
        }

        return view;

    }

    private void initViews(View view) {
        editTextFragmentBuscaUsuario = view.findViewById(R.id.editTextFragmentBuscaUsuario);
        listViewFragmentBuscaUsuario = view.findViewById(R.id.listViewFragmentBuscaUsuario);
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

        listViewFragmentBuscaUsuario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final Usuario usuario = (Usuario) listViewFragmentBuscaUsuario.getItemAtPosition(position);
                final List<Cartao> listaCartao = new ArrayList<>();
                final List<Cartao> listaCartaoSize = new ArrayList<>();
                databaseReference
                        .child(StaticUtils.TABELA_CARTAOES)
                        .orderByChild(StaticUtils.ID_USUARIO)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    Cartao cartaoAux = data.getValue(Cartao.class);
                                    if (cartaoAux.getIdPromocao().equals(promocao.getId()) &&
                                            cartaoAux.getIdUsuario().equals(usuario.getId())) {
                                        listaCartao.add(cartaoAux);
                                    }
                                    listaCartaoSize.add(cartaoAux);
                                }
                                atribuiCarimbo(listaCartao, listaCartaoSize, usuario);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        });
    }

    private void atribuiCarimbo(List<Cartao> listaCartao, List<Cartao> listaCartaoSize, Usuario usuario) {
        Cartao cartaoAux = new Cartao();
        if (listaCartao.size() > 0) {
            cartaoAux = listaCartao.get(0);
            AsyncTaskAtribuiCarimbo asyncTaskAtribuiCarimbo =
                    new AsyncTaskAtribuiCarimbo(getContext(), cartaoAux, usuario, false);
            asyncTaskAtribuiCarimbo.execute();
        } else {
            String uuid =
                    usuario
                            .getId()
                            .substring(0, usuario.getId().length() - 1) +
                            (listaCartaoSize.size() + 1);
            cartaoAux.setId(uuid);
            cartaoAux.setQntCarimbos(1);
            cartaoAux.setIdPromocao(promocao.getId());
            cartaoAux.setIdUsuario(usuario.getId());
            AsyncTaskAtribuiCarimbo asyncTaskAtribuiCarimbo =
                    new AsyncTaskAtribuiCarimbo(getContext(), cartaoAux, usuario, true);
            asyncTaskAtribuiCarimbo.execute();
        }
    }

}
