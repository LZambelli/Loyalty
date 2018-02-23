package joaopogiolli.com.br.loyalty.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

import joaopogiolli.com.br.loyalty.AssyncTasks.AssyncTaskCadastraPromocao;
import joaopogiolli.com.br.loyalty.Models.Estabelecimento;
import joaopogiolli.com.br.loyalty.Models.Promocao;
import joaopogiolli.com.br.loyalty.R;
import joaopogiolli.com.br.loyalty.Utils.StaticUtils;

/**
 * Created by Joao Poggioli on 22/02/2018.
 */

public class CadastroPromocaoFragment extends Fragment {

    private TextInputLayout textInputLayoutTituloFragmentCadastroPromocao;
    private EditText editTextTituloFragmentCadastroPromocao;
    private TextInputLayout textInputLayoutDescricaoFragmentCadastroPromocao;
    private EditText editTextDescricaoFragmentCadastroPromocao;
    private TextInputLayout textInputLayoutQntCarimbosFragmentCadastroPromocao;
    private EditText editTextQntCarimbosFragmentCadastroPromocao;
    private String titulo;
    private String descricao;
    private int qntCarimbos;
    private String data;
    private Estabelecimento estabelecimento;
    private Promocao promocao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle(R.string.cadastroPromocao);
        View view = inflater.inflate(R.layout.fragment_cadastro_promocao, container, false);
        setHasOptionsMenu(true);
        initView(view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            estabelecimento = new Gson()
                    .fromJson(bundle.getString(StaticUtils.PUT_EXTRA_TIPO_ESTABELECIMENTO), Estabelecimento.class);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_cadastro_promocao_drawer, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_confirma_cadastro:
                if (verficaCampos()) {
                    promocao = new Promocao();
                    String uuid =
                            estabelecimento
                                    .getId()
                                    .substring(0, estabelecimento.getId().length() - 1) +
                                    (estabelecimento.getQntPromocoes() + 1);
                    promocao.setId(uuid);
                    promocao.setIdEstabelecimento(estabelecimento.getId());
                    promocao.setDescricao(descricao);
                    promocao.setTitulo(titulo);
                    promocao.setQuantidadeCarimbos(qntCarimbos);
                    promocao.setDataCricao(data);
                    cadastraPromocao();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView(View view) {
        textInputLayoutTituloFragmentCadastroPromocao = view.findViewById(R.id.textInputLayoutTituloFragmentCadastroPromocao);
        editTextTituloFragmentCadastroPromocao = view.findViewById(R.id.editTextTituloFragmentCadastroPromocao);
        textInputLayoutDescricaoFragmentCadastroPromocao = view.findViewById(R.id.textInputLayoutDescricaoFragmentCadastroPromocao);
        editTextDescricaoFragmentCadastroPromocao = view.findViewById(R.id.editTextDescricaoFragmentCadastroPromocao);
        textInputLayoutQntCarimbosFragmentCadastroPromocao = view.findViewById(R.id.textInputLayoutQntCarimbosFragmentCadastroPromocao);
        editTextQntCarimbosFragmentCadastroPromocao = view.findViewById(R.id.editTextQntCarimbosFragmentCadastroPromocao);
    }

    private boolean verficaCampos() {

        boolean retorno = true;
        titulo = editTextTituloFragmentCadastroPromocao.getText().toString();
        descricao = editTextDescricaoFragmentCadastroPromocao.getText().toString();

        String carimbos = editTextQntCarimbosFragmentCadastroPromocao.getText().toString();
        if (!carimbos.isEmpty()) {
            qntCarimbos = Integer.parseInt(carimbos);
        } else {
            qntCarimbos = 0;
        }

        if (titulo.isEmpty() || titulo.equals(null)) {
            retorno = false;
            textInputLayoutTituloFragmentCadastroPromocao.setHintTextAppearance(R.style.TextLabelErrorHint);
            textInputLayoutTituloFragmentCadastroPromocao.setError(getString(R.string.erroPreenchimentoObrigatorio));
        } else {
            textInputLayoutTituloFragmentCadastroPromocao.setError(null);
            textInputLayoutTituloFragmentCadastroPromocao.setHintTextAppearance(R.style.TextLabelHint);
        }

        if (descricao.isEmpty() || descricao.equals(null)) {
            retorno = false;
            textInputLayoutDescricaoFragmentCadastroPromocao.setHintTextAppearance(R.style.TextLabelErrorHint);
            textInputLayoutDescricaoFragmentCadastroPromocao.setError(getString(R.string.erroPreenchimentoObrigatorio));
        } else {
            textInputLayoutDescricaoFragmentCadastroPromocao.setError(null);
            textInputLayoutDescricaoFragmentCadastroPromocao.setHintTextAppearance(R.style.TextLabelHint);
        }

        if (qntCarimbos <= 0) {
            retorno = false;
            textInputLayoutQntCarimbosFragmentCadastroPromocao.setHintTextAppearance(R.style.TextLabelErrorHint);
            textInputLayoutQntCarimbosFragmentCadastroPromocao.setError(getString(R.string.erroValorInvalido));
        } else {
            textInputLayoutQntCarimbosFragmentCadastroPromocao.setError(null);
            textInputLayoutQntCarimbosFragmentCadastroPromocao.setHintTextAppearance(R.style.TextLabelHint);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        data = simpleDateFormat.format(new Date());

        return retorno;
    }

    private void cadastraPromocao() {
        AssyncTaskCadastraPromocao assyncTaskCadastraPromocao =
                new AssyncTaskCadastraPromocao(getContext(), promocao, estabelecimento);
        assyncTaskCadastraPromocao.execute();
    }

}
