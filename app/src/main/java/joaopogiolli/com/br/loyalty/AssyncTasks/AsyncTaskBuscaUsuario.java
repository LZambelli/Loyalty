package joaopogiolli.com.br.loyalty.AssyncTasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import joaopogiolli.com.br.loyalty.Firebase.FirebaseUtils;
import joaopogiolli.com.br.loyalty.Models.Usuario;
import joaopogiolli.com.br.loyalty.R;
import joaopogiolli.com.br.loyalty.Utils.StaticUtils;

/**
 * Created by Joao Poggioli on 23/02/2018.
 */

public class AsyncTaskBuscaUsuario extends AsyncTask<Void, Void, Void> {


    private ProgressBar progressBarFragmentBuscaUsuario;
    private ListView listViewFragmentBuscaUsuario;
    private Context context;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private String busca;
    private List<Usuario> listaUsuario;

    public AsyncTaskBuscaUsuario(Context context, String busca) {
        this.context = context;
        firebaseDatabase = FirebaseUtils.getFirebaseDatabase(context);
        databaseReference = FirebaseUtils.getDatabaseReference(firebaseDatabase);
        this.busca = busca;
        listaUsuario = new ArrayList<>();
        initViews();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBarFragmentBuscaUsuario.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        if (!busca.isEmpty()) {
            databaseReference
                    .child(StaticUtils.TABELA_USUARIO)
                    .orderByChild(StaticUtils.EMAIL)
                    .startAt(busca)
                    .endAt(busca + StaticUtils.UF8FF)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listaUsuario.clear();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                listaUsuario.add(data.getValue(Usuario.class));
                            }
                            ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(context,
                                    android.R.layout.simple_list_item_1,
                                    listaUsuario);
                            listViewFragmentBuscaUsuario.setAdapter(adapter);
                            progressBarFragmentBuscaUsuario.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } else {
            progressBarFragmentBuscaUsuario.setVisibility(View.GONE);
            listViewFragmentBuscaUsuario.setAdapter(null);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    private void initViews() {
        progressBarFragmentBuscaUsuario = ((Activity) context).findViewById(R.id.progressBarFragmentBuscaUsuario);
        listViewFragmentBuscaUsuario = ((Activity) context).findViewById(R.id.listViewFragmentBuscaUsuario);
    }

}
