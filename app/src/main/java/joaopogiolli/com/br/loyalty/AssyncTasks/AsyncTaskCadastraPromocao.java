package joaopogiolli.com.br.loyalty.AssyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import joaopogiolli.com.br.loyalty.Firebase.FirebaseUtils;
import joaopogiolli.com.br.loyalty.Models.Estabelecimento;
import joaopogiolli.com.br.loyalty.Models.Promocao;
import joaopogiolli.com.br.loyalty.R;
import joaopogiolli.com.br.loyalty.Utils.StaticUtils;

/**
 * Created by jlago on 23/02/2018.
 */

public class AsyncTaskCadastraPromocao extends AsyncTask<Void, Void, Void> {

    private Promocao promocao;
    private Estabelecimento estabelecimento;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private Context context;

    public AsyncTaskCadastraPromocao(Context context, Promocao promocao, Estabelecimento estabelecimento) {
        this.promocao = promocao;
        this.context = context;
        this.estabelecimento = estabelecimento;
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(R.string.aguarde);
        progressDialog.setMessage(context.getString(R.string.criandoPromocao));
        firebaseDatabase = FirebaseUtils.getFirebaseDatabase(context);
        databaseReference = FirebaseUtils.getDatabaseReference(firebaseDatabase);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        databaseReference
                .child(StaticUtils.TABELA_PROMOCAO)
                .child(promocao.getId())
                .setValue(promocao);
        databaseReference
                .child(StaticUtils.TABELA_PROMOCAO)
                .orderByChild(StaticUtils.ID)
                .equalTo(promocao.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        atualizaDadosEstabelecimento();
                        progressDialog.dismiss();
                        StaticUtils.Toast(context, context.getString(R.string.PromocaoCriadaSucesso));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ((Activity) context).onBackPressed();
    }

    private void atualizaDadosEstabelecimento() {
        databaseReference
                .child(StaticUtils.TABELA_ESTABELECIMENTO)
                .child(estabelecimento.getId())
                .child(StaticUtils.QNT_PROMOCOES)
                .setValue(estabelecimento.getQntPromocoes() + 1);
    }
}
