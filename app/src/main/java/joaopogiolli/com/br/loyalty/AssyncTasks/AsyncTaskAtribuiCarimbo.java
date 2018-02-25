package joaopogiolli.com.br.loyalty.AssyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import joaopogiolli.com.br.loyalty.Firebase.FirebaseUtils;
import joaopogiolli.com.br.loyalty.Models.Cartao;
import joaopogiolli.com.br.loyalty.Models.Usuario;
import joaopogiolli.com.br.loyalty.R;
import joaopogiolli.com.br.loyalty.Utils.StaticUtils;

/**
 * Created by Joao Poggioli on 25/02/2018.
 */

public class AsyncTaskAtribuiCarimbo extends AsyncTask<Void, Void, Void> {

    private Cartao cartao;
    private Usuario usuario;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private Context context;
    private boolean ehCartaoNovo;

    public AsyncTaskAtribuiCarimbo(Context context, Cartao cartao, Usuario usuario, boolean ehCartaoNovo) {
        this.cartao = cartao;
        this.context = context;
        this.usuario = usuario;
        this.ehCartaoNovo = ehCartaoNovo;
        this.firebaseDatabase = FirebaseUtils.getFirebaseDatabase(context);
        this.databaseReference = FirebaseUtils.getDatabaseReference(firebaseDatabase);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(R.string.aguarde);
        progressDialog.setMessage(context.getString(R.string.atribuindoCarimbo));
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (!ehCartaoNovo) {
            databaseReference
                    .child(StaticUtils.TABELA_CARTAOES)
                    .child(cartao.getId())
                    .child(StaticUtils.QNT_CARIMBOS)
                    .setValue(cartao.getQntCarimbos() + 1);
        } else {
            databaseReference
                    .child(StaticUtils.TABELA_CARTAOES)
                    .child(cartao.getId())
                    .setValue(cartao);
        }
        databaseReference
                .child(StaticUtils.TABELA_CARTAOES)
                .orderByChild(StaticUtils.ID)
                .equalTo(cartao.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        progressDialog.dismiss();
                        StaticUtils.Toast(context, context.getString(R.string.carimboAtribuidoSucesso));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return null;
    }
}
