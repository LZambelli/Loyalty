package joaopogiolli.com.br.loyalty.AssyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import joaopogiolli.com.br.loyalty.Adapters.ListaCartoesAdapter;
import joaopogiolli.com.br.loyalty.Adapters.ListaPromocoesAdapter;
import joaopogiolli.com.br.loyalty.Firebase.FirebaseUtils;
import joaopogiolli.com.br.loyalty.Models.Cartao;
import joaopogiolli.com.br.loyalty.Models.Estabelecimento;
import joaopogiolli.com.br.loyalty.Models.Promocao;
import joaopogiolli.com.br.loyalty.Models.Usuario;
import joaopogiolli.com.br.loyalty.R;
import joaopogiolli.com.br.loyalty.Utils.StaticUtils;

/**
 * Created by Joao Poggioli on 25/02/2018.
 */

public class AsyncTaskListaCartoes extends AsyncTask<Void, Void, Void> {

    private Usuario usuario;
    private Context context;
    private View view;
    private ListView listViewFragmentListaCartoes;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ProgressBar progressBarFragmentListaCartoes;
    private List<Cartao> listaCartao;
    private List<Promocao> listaPromocao;
    private List<Estabelecimento> listaEstabelecimento;
    private TextView textViewSemCartoesFragmentListaCartoes;

    public AsyncTaskListaCartoes(Context context, Usuario usuario, View view, ListView listViewFragmentListaCartoes) {
        this.context = context;
        this.usuario = usuario;
        this.view = view;
        this.listViewFragmentListaCartoes = listViewFragmentListaCartoes;
        firebaseDatabase = FirebaseUtils.getFirebaseDatabase(context);
        databaseReference = FirebaseUtils.getDatabaseReference(firebaseDatabase);
        progressBarFragmentListaCartoes = view.findViewById(R.id.progressBarFragmentListaCartoes);
        textViewSemCartoesFragmentListaCartoes = view.findViewById(R.id.textViewSemCartoesFragmentListaCartoes);
        listaCartao = new ArrayList<>();
        listaPromocao = new ArrayList<>();
        listaEstabelecimento = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBarFragmentListaCartoes.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        databaseReference
                .child(StaticUtils.TABELA_CARTAOES)
                .orderByChild(StaticUtils.ID_USUARIO)
                .equalTo(usuario.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listaCartao.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            listaCartao.add(data.getValue(Cartao.class));
                        }
                        carregaListaPromocao();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return null;
    }

    private void carregaListaPromocao() {
        for (Cartao cartao : listaCartao) {
            databaseReference
                    .child(StaticUtils.TABELA_PROMOCAO)
                    .orderByChild("id")
                    .equalTo(cartao.getIdPromocao())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Promocao promocao;
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                promocao = data.getValue(Promocao.class);
                                listaPromocao.add(promocao);
                            }
                            carregaListaEstabelecimento();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void carregaListaEstabelecimento() {
        for (Promocao promo : listaPromocao) {
            databaseReference
                    .child(StaticUtils.TABELA_ESTABELECIMENTO)
                    .orderByChild("id")
                    .equalTo(promo.getIdEstabelecimento())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Estabelecimento estabelecimento = data.getValue(Estabelecimento.class);
                                listaEstabelecimento.add(estabelecimento);
                            }
                            progressBarFragmentListaCartoes.setVisibility(View.GONE);
                            chamaAdapterCartoes();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void chamaAdapterCartoes() {
        if (listaCartao.size() != 0) {
            if (textViewSemCartoesFragmentListaCartoes.getVisibility() == View.VISIBLE) {
                textViewSemCartoesFragmentListaCartoes.setVisibility(View.GONE);
            }
            ListaCartoesAdapter listaCartoesAdapter = new ListaCartoesAdapter(context, listaCartao, listaPromocao, listaEstabelecimento);
            listViewFragmentListaCartoes.setAdapter(listaCartoesAdapter);
        } else {
            progressBarFragmentListaCartoes.setVisibility(View.GONE);
            textViewSemCartoesFragmentListaCartoes.setVisibility(View.VISIBLE);
        }
    }

}
