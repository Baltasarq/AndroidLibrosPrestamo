package com.example.mislibrosprestados.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.mislibrosprestados.R;
import com.example.mislibrosprestados.core.SqlIO;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar atributos
        this.sqlIO = new SqlIO( this.getApplicationContext() );

        // Inicializar vistas
        final ListView LV_LIBROS = this.findViewById( R.id.lvLibros );
        final Button BT_INSERTA = this.findViewById( R.id.btInserta );

        BT_INSERTA.setOnClickListener( (v) -> MainActivity.this.inserta() );

        this.cursorAdapter = new SimpleCursorAdapter(
            this,
            R.layout.entrada_libro_layout,
            null,
            new String[] { SqlIO.CAMPO_LIBROS_TITULO, SqlIO.CAMPO_LIBROS_DIAS_PRESTAMO },
            new int[] { R.id.lblTitulo, R.id.lblDias },
            0
        );

        LV_LIBROS.setAdapter( cursorAdapter );

        this.actualiza();
    }

    private void actualiza()
    {
        this.cursorAdapter.swapCursor(
                this.sqlIO.getCursorTodosLibros() );
    }

    private void inserta()
    {
        final AlertDialog.Builder DLG = new AlertDialog.Builder( this );
        final EditText ED_TITULO = new EditText( this );

        DLG.setTitle( "Nuevo libro" );
        DLG.setView( ED_TITULO );
        DLG.setPositiveButton("Guarda", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.sqlIO.inserta( ED_TITULO.getText().toString() );
                MainActivity.this.actualiza();
            }
        });

        DLG.create().show();
    }

    private SimpleCursorAdapter cursorAdapter;
    private SqlIO sqlIO;
}
