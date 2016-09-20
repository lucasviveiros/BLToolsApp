package br.com.lampmobile.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.lampmobile.model.Historico;

public class CombustivelHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Combustivel.db";
    private static final String TABLE_HISTORICO_NAME = "combustivel_historico";

    public CombustivelHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        criarTabela(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_HISTORICO_NAME);
        onCreate(db);
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
        contentValues.put("gasolina", (Double) historico.getParams()[0]);
        contentValues.put("etanol", (Double) historico.getParams()[1]);

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



    public Historico criarHistorico(String titulo, String gasolina, String etanol) {
        Historico h = new Historico();
        h.setTitulo(titulo);
        Double[] params = new Double[2];
        params[0] = gasolina.isEmpty() ? Double.valueOf(0) : Double.valueOf(gasolina);
        params[1] = etanol.isEmpty() ? Double.valueOf(0) : Double.valueOf(etanol);

        h.setParams(params);

        return h;
    }

    private List<Historico> executaQueryHistorico(SQLiteDatabase db, String query) {
        Cursor cursor = db.rawQuery(query, null);
        List<Historico> historicos = new ArrayList<>();
        Historico historico;
        Double[] params;
        while (cursor.moveToNext()) {
            historico = new Historico();
            params = new Double[2];
            historico.setId(cursor.getInt(0));
            historico.setTitulo(cursor.getString(1));

            params[0] = cursor.getDouble(2);
            params[1] = cursor.getDouble(3);

            historico.setParams(params);
            historico.setFavorito(cursor.getInt(4)==1?Boolean.TRUE:Boolean.FALSE);

            historicos.add(historico);
        }

        cursor.close();
        return historicos;
    }

    private void criarTabela(SQLiteDatabase db) {
        StringBuffer sql;

        sql = new StringBuffer("CREATE TABLE   ");
        sql.append(  TABLE_HISTORICO_NAME       );
        sql.append("(id INTEGER PRIMARY KEY,   ");
        sql.append(" titulo TEXT ,             ");
        sql.append(" gasolina DOUBLE ,          ");
        sql.append(" etanol DOUBLE ,          ");
        sql.append(" favorito INTEGER );       "); //0 (FALSE), 1 (TRUE)
        db.execSQL(sql.toString());
    }
}
