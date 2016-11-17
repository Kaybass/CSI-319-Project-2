package com.upmoon.alex.moongameoflife;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Alex on 11/13/2016.
 */

public class ConvertBoardToImage {
    int rows, columns;
    boolean mBoardArray[][];
    static String imgPath = "/img/GameOfLife.png";

    public ConvertBoardToImage() {

    }

    public void saveBoardAsPNG(Context context, GameOfLifeBoard boardToSave) {
        rows = boardToSave.getRows();
        columns = boardToSave.getColumns();
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                mBoardArray[i][j] = boardToSave.cellStatus(i, j);
            }
        }

        try {
            FileOutputStream fileOutputStream = context.openFileOutput(imgPath, Context.MODE_PRIVATE);

            Bitmap boardBitmap = Bitmap.createBitmap(rows, columns, Bitmap.Config.ALPHA_8);
            if(boardBitmap.compress(Bitmap.CompressFormat.PNG, 0, fileOutputStream)) {
                Toast.makeText(context, "Saved GameOfLife.png Successfully!", Toast.LENGTH_SHORT).show();
            } else {
                throw new IOException("Failed to compress boardBitmap to fileOutputStream.");
            }

            fileOutputStream.close();

            Toast.makeText(context, "Succesfully saved /img/GameOfLife.png", Toast.LENGTH_SHORT).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
