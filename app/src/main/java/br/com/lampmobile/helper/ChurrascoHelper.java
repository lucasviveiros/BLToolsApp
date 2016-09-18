package br.com.lampmobile.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.lampmobile.model.Churrasco;
import br.com.lampmobile.model.Historico;

public class ChurrascoHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Churrasco.db";
    private static final String TABLE_NAME = "churras";
    private static final String TABLE_HISTORICO_NAME = "churras_historico";

    public enum Tipo {
        CARNE, ACOMPANHAMENTO, BEBIDA, OUTROS
    }

    public ChurrascoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        criarTabela(db);
        incluirDados(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_HISTORICO_NAME);
        onCreate(db);
    }

    /**
     * Recupera todos os itens do churrasco.
     *
     * @param db - SQLiteDatabase
     * @return Map<>
     */
    public Map<Tipo, List<Churrasco>> getItens(SQLiteDatabase db) {
        String query = "SELECT * FROM " + TABLE_NAME;
        return executaQuery(db, query, null);
    }

    /**
     * Recupera apenas os itens ativos.
     *
     * @param db - SQLiteDatabase
     * @return Map<>
     */
    public Map<Tipo, List<Churrasco>> getItensAtivos(SQLiteDatabase db) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE ativo = ?";
        String[] args = {"1"};
        return executaQuery(db, query, args);
    }

    /**
     * Recuperar histórico
     *
     * @param db - SQLiteDatabase
     * @return List<Historico>
     */
    public List<Historico> getHistoricos(SQLiteDatabase db) {
        String query = "SELECT * FROM " + TABLE_HISTORICO_NAME ;
        return executaQueryHistorico(db, query);
    }

    /**
     * Salvar histórico.
     *
     * @param db - SQLiteDatabase
     * @param historico - Historico
     */
    public void salvarHistorico(SQLiteDatabase db, Historico historico) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("titulo", historico.getTitulo());
        contentValues.put("homens", (Integer) historico.getParams()[0]);
        contentValues.put("mulheres", (Integer) historico.getParams()[1]);
        contentValues.put("crianças", (Integer) historico.getParams()[2]);

        db.insert(TABLE_HISTORICO_NAME, null, contentValues );
    }

    public void apagarHistorico(SQLiteDatabase db, Historico historico) {
        db.delete(TABLE_HISTORICO_NAME,"id = ?", new String[]{historico.getId().toString()});
    }

    /**
     * Atualiza item de histórico para favorito ou não.
     *
     * @param db
     * @param historico
     */
    public void atualizarFavoritoHistorico(SQLiteDatabase db, Historico historico) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("favorito", historico.getFavorito()? Integer.valueOf(1) : Integer.valueOf(0));

        db.update(TABLE_HISTORICO_NAME, contentValues, "id = ?", new String[]{historico.getId().toString()});
    }


    /**
     * Atualiza dados no da lista no banco de dados.
     *
     * @param db - SQLiteDatabase
     * @param churrascos - List<Churrasco>
     */
    public void atualizarStatus(SQLiteDatabase db, List<Churrasco> churrascos) {
        ContentValues contentValues;

        for (Churrasco churrasco : churrascos) {
            contentValues = new ContentValues();
            contentValues.put("ativo", churrasco.getAtivo()? Integer.valueOf(1) : Integer.valueOf(0));

            db.update(TABLE_NAME, contentValues, "id = ?", new String[]{churrasco.getId().toString()});
        }
    }

    public Historico criarHistorico(String titulo, String homem, String mulher, String crianca) {
        Historico h = new Historico();
        h.setTitulo(titulo);
        Integer[] params = new Integer[3];
        params[0] = homem.isEmpty() ? Integer.valueOf(0) : Integer.valueOf(homem);
        params[1] = mulher.isEmpty() ? Integer.valueOf(0) : Integer.valueOf(mulher);
        params[2] = crianca.isEmpty() ? Integer.valueOf(0) : Integer.valueOf(crianca);;

        h.setParams(params);

        return h;
    }

    private Map<Tipo, List<Churrasco>> executaQuery(SQLiteDatabase db, String query, String[] args) {
        Cursor cursor = db.rawQuery(query, args);
        Map<Tipo, List<Churrasco>> resultado = new HashMap<>();
        Churrasco churrasco;
        while (cursor.moveToNext()) {
            churrasco = new Churrasco();
            churrasco.setId(cursor.getInt(0));
            churrasco.setItem(cursor.getString(1));
            churrasco.setTipo(Tipo.valueOf(cursor.getString(2)));
            churrasco.setAtivo(cursor.getInt(3)==1?Boolean.TRUE:Boolean.FALSE);

            if (resultado.get(churrasco.getTipo()) == null) {
                resultado.put(churrasco.getTipo(), new ArrayList<Churrasco>());
            }
            resultado.get(churrasco.getTipo()).add(churrasco);
        }

        cursor.close();
        return resultado;
    }

    private List<Historico> executaQueryHistorico(SQLiteDatabase db, String query) {
        Cursor cursor = db.rawQuery(query, null);
        List<Historico> historicos = new ArrayList<>();
        Historico historico;
        Integer[] params;
        while (cursor.moveToNext()) {
            historico = new Historico();
            params = new Integer[3];
            historico.setId(cursor.getInt(0));
            historico.setTitulo(cursor.getString(1));

            params[0] = cursor.getInt(2);
            params[1] = cursor.getInt(3);
            params[2] = cursor.getInt(4);

            historico.setParams(params);
            historico.setFavorito(cursor.getInt(5)==1?Boolean.TRUE:Boolean.FALSE);

            historicos.add(historico);
        }

        cursor.close();
        return historicos;
    }

    private void criarTabela(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer("CREATE TABLE ");
        sql.append(  TABLE_NAME                            );
        sql.append("(id INTEGER PRIMARY KEY,              ");
        sql.append(" item TEXT ,                          ");
        sql.append(" tipo TEXT ,                          ");
        sql.append(" ativo INTEGER );                     "); //0 (FALSE), 1 (TRUE)

        db.execSQL(sql.toString());

        sql = new StringBuffer("CREATE TABLE   ");
        sql.append(  TABLE_HISTORICO_NAME       );
        sql.append("(id INTEGER PRIMARY KEY,   ");
        sql.append(" titulo TEXT ,             ");
        sql.append(" homens INTEGER ,          ");
        sql.append(" mulheres INTEGER ,        ");
        sql.append(" crianças INTEGER ,        ");
        sql.append(" favorito INTEGER );       "); //0 (FALSE), 1 (TRUE)
        db.execSQL(sql.toString());
    }

    private void incluirDados(SQLiteDatabase db) {
        StringBuffer sql;
        sql = new StringBuffer("INSERT INTO                    ");
        sql.append(  TABLE_NAME                                 );
        sql.append(" VALUES                                    ");
        sql.append("(1 , 'Carne Bovina', 'CARNE', 1),          ");
        sql.append("(2 , 'Carne Suína', 'CARNE', 1),           ");
        sql.append("(3 , 'Coração', 'CARNE', 1),               ");
        sql.append("(4 , 'Frango', 'CARNE', 1),                ");
        sql.append("(5 , 'Linguiça', 'CARNE', 1),              ");

        sql.append("(6 , 'Arroz', 'ACOMPANHAMENTO', 1),        ");
        sql.append("(7 , 'Maionese', 'ACOMPANHAMENTO', 1),     ");
        sql.append("(8 , 'Farofa', 'ACOMPANHAMENTO', 1),       ");
        sql.append("(9 , 'Pão francês', 'ACOMPANHAMENTO', 1),  ");
        sql.append("(10, 'Pão de alho', 'ACOMPANHAMENTO', 1),  ");
        sql.append("(11, 'Queijo coalho', 'ACOMPANHAMENTO', 1),");
        sql.append("(12, 'Vinagrete', 'ACOMPANHAMENTO', 1),    ");
        sql.append("(13, 'Alface', 'ACOMPANHAMENTO', 1),       ");

        sql.append("(14, 'Cerveja', 'BEBIDA', 1),              ");
        sql.append("(15, 'Refrigerante', 'BEBIDA', 1),         ");
        sql.append("(16, 'Água', 'BEBIDA', 1),                 ");

        sql.append("(17, 'Carvão', 'OUTROS', 1),               ");
        sql.append("(18, 'Guardanapo', 'OUTROS', 1),           ");
        sql.append("(19, 'Copo', 'OUTROS', 1)                  ");

        db.execSQL(sql.toString());
    }

}
