package br.com.lampmobile.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChurrascoHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Churrasco.db";
    public static final String TABLE_NAME = "churras";

    public enum Tipo {
        CARNE, ACOMPANHAMENTO, BEBIDA, OUTROS;
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
        onCreate(db);
    }

    /**
     * Recupera todos os itens do churrasco.
     *
     * @param db - SQLiteDatabase
     * @return Map<Tipo, List<Churrasco>>
     */
    public Map<Tipo, List<Churrasco>> getItens(SQLiteDatabase db) {
        String query = "SELECT * FROM " + TABLE_NAME;
        return executaQuery(db, query, null);
    }

    /**
     * Recupera apenas os itens ativos.
     *
     * @param db - SQLiteDatabase
     * @return Map<Tipo, List<Churrasco>>
     */
    public Map<Tipo, List<Churrasco>> getItensAtivos(SQLiteDatabase db) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE ativo = ?";
        String[] args = {"1"};
        return executaQuery(db, query, args);
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
            contentValues.put("ativo", churrasco.getAtivo()? new Integer(1) : new Integer(0));

            db.update(TABLE_NAME, contentValues, "id = ?", new String[]{churrasco.getId().toString()});
        }
    }

    private Map<Tipo, List<Churrasco>> executaQuery(SQLiteDatabase db, String query, String[] args) {
        Cursor cursor = db.rawQuery(query, args);
        Map<Tipo, List<Churrasco>> resultado = new HashMap<Tipo, List<Churrasco>>();
        Churrasco churrasco = null;
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

        return resultado;

    }

    private void criarTabela(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer("CREATE TABLE   ");
        sql.append(  TABLE_NAME                              );
        sql.append("(id INTEGER PRIMARY KEY,                ");
        sql.append(" item TEXT ,                            ");
        sql.append(" tipo TEXT ,                            ");
        sql.append(" ativo INTEGER );                       "); //0 (FALSE), 1 (TRUE)

        db.execSQL(sql.toString());
    }

    private void incluirDados(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer("INSERT INTO    ");
        sql.append(  TABLE_NAME                             );
        sql.append(" VALUES                                 ");
        sql.append("(1 , 'Carne Bovina', 'CARNE', 1),       ");
        sql.append("(2 , 'Carne Suína', 'CARNE', 1),        ");
        sql.append("(3 , 'Coração', 'CARNE', 1),            ");
        sql.append("(4 , 'Frango', 'CARNE', 1),             ");
        sql.append("(5 , 'Linguiça', 'CARNE', 1),           ");

        sql.append("(6 , 'Arroz', 'ACOMPANHAMENTO', 1),     ");
        sql.append("(7 , 'Maionese', 'ACOMPANHAMENTO', 1),  ");
        sql.append("(8 , 'Farofa', 'ACOMPANHAMENTO', 1),    ");
        sql.append("(9 , 'Pão francês', 'ACOMPANHAMENTO', 1),");
        sql.append("(10, 'Pão de alho', 'ACOMPANHAMENTO', 1),");
        sql.append("(11, 'Queijo coalho', 'ACOMPANHAMENTO', 1),");
        sql.append("(12, 'Vinagrete', 'ACOMPANHAMENTO', 1), ");
        sql.append("(13, 'Alface', 'ACOMPANHAMENTO', 1),    ");

        sql.append("(14, 'Cerveja', 'BEBIDA', 1),           ");
        sql.append("(15, 'Refrigerante', 'BEBIDA', 1),      ");
        sql.append("(16, 'Água', 'BEBIDA', 1),              ");

        sql.append("(17, 'Carvão', 'OUTROS', 1),            ");
        sql.append("(18, 'Guardanapo', 'OUTROS', 1),        ");
        sql.append("(19, 'Copo', 'OUTROS', 1)               ");

        db.execSQL(sql.toString());
    }

    public class Churrasco {
        private Integer id;
        private String item;
        private Tipo tipo;
        private Boolean ativo;
        private String resultado;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public Tipo getTipo() {
            return tipo;
        }

        public void setTipo(Tipo tipo) {
            this.tipo = tipo;
        }

        public Boolean getAtivo() {
            return ativo;
        }

        public void setAtivo(Boolean ativo) {
            this.ativo = ativo;
        }

        public String getResultado() {
            return resultado;
        }

        public void setResultado(String resultado) {
            this.resultado = resultado;
        }
    }
}
