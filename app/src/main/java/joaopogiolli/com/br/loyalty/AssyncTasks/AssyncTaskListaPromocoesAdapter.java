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

import joaopogiolli.com.br.loyalty.Adapters.ListaPromocoesAdapter;
import joaopogiolli.com.br.loyalty.Firebase.FirebaseUtils;
import joaopogiolli.com.br.loyalty.Models.Estabelecimento;
import joaopogiolli.com.br.loyalty.Models.Promocao;
import joaopogiolli.com.br.loyalty.R;
import joaopogiolli.com.br.loyalty.Utils.StaticUtils;

/**
 * Created by Joao Poggioli on 22/02/2018.
 */

public class AssyncTaskListaPromocoesAdapter extends AsyncTask<Void, Void, Void> {

    private Context context;
    private Estabelecimento estabelecimento;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private List<Promocao> listaPromocao;
    private ListView listViewFragmentListaPromocoes;
    private ProgressBar progressBarFragmentListaPromocoes;
    private TextView textViewSemPromocoesFragmentListaPromocoes;

    public AssyncTaskListaPromocoesAdapter(Context context, Estabelecimento estabelecimento, View view) {
        this.estabelecimento = estabelecimento;
        this.context = context;
        firebaseDatabase = FirebaseUtils.getFirebaseDatabase(context);
        databaseReference = FirebaseUtils.getDatabaseReference(firebaseDatabase);
        progressBarFragmentListaPromocoes = view.findViewById(R.id.progressBarFragmentListaPromocoes);
        listViewFragmentListaPromocoes = view.findViewById(R.id.listViewFragmentListaPromocoes);
        textViewSemPromocoesFragmentListaPromocoes = view.findViewById(R.id.textViewSemPromocoesFragmentListaPromocoes);
        listaPromocao = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBarFragmentListaPromocoes.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        databaseReference
                .child(StaticUtils.TABELA_CARTAOES)
                .orderByChild("idUsuario")
                .equalTo(estabelecimento.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listaPromocao.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            listaPromocao.add(data.getValue(Promocao.class));
                        }
                        progressBarFragmentListaPromocoes.setVisibility(View.GONE);
                        chamaAdapterPromocoes();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return null;
    }

    private void chamaAdapterPromocoes() {
        if (listaPromocao.size() != 0) {
            if (textViewSemPromocoesFragmentListaPromocoes.getVisibility() == View.VISIBLE) {
                textViewSemPromocoesFragmentListaPromocoes.setVisibility(View.GONE);
            }
            ListaPromocoesAdapter listaPromocoesAdapter = new ListaPromocoesAdapter(context, listaPromocao);
            listViewFragmentListaPromocoes.setAdapter(listaPromocoesAdapter);
        } else {
            textViewSemPromocoesFragmentListaPromocoes.setVisibility(View.VISIBLE);
        }
    }

}
