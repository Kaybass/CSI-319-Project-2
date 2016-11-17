package com.upmoon.alex.moongameoflife;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

import static java.security.AccessController.getContext;

/**
 * Created by Alex on 11/13/2016.
 */

public class ConvertBoardToImage {
    int rows, columns;
    int mBoardArray[];
    static String imgPath = "/img/GameOfLife.png";

    public ConvertBoardToImage() {

    }

    public void saveBoardAsPNG(Context context) {
        rows = CurrentBoard.getInstance().getBoard().getRows();
        columns = CurrentBoard.getInstance().getBoard().getColumns();

        int rowIndex = rows * 20, columnIndex = columns * 20;

        mBoardArray = new int[rowIndex * columnIndex];

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                Log.d("**Save:", " Accessing - (" + Integer.toString(i) + ", " + Integer.toString(j) + ")");
                for (int k = 0; k < 20; k++)
                {
                    if(CurrentBoard.getInstance().getBoard().cellStatus(i, j))
                    {
                        mBoardArray[rowIndex + columnIndex + k] = Color.GREEN;
                    }
                    else
                    {
                        mBoardArray[rowIndex + columnIndex + k] = Color.GRAY;
                    }
                }
                columnIndex = columnIndex + 20;
            }
            rowIndex = rowIndex + 20;
        }

        try
        {
            FileOutputStream fileOutputStream = context.openFileOutput(imgPath, Context.MODE_PRIVATE);

            Bitmap boardBitmap = Bitmap.createBitmap(mBoardArray, rows, columns, Bitmap.Config.ALPHA_8);
            if(boardBitmap.compress(Bitmap.CompressFormat.PNG, 0, fileOutputStream)) {
                Toast.makeText(context, "Saved GameOfLife.png Successfully!", Toast.LENGTH_SHORT).show();
            } else {
                throw new IOException("Failed to compress boardBitmap to fileOutputStream.");
            }

            fileOutputStream.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
