package com.estgp.trabalhosensores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor rotationSensor;
    private Button btNewGame;
    private Button btStart;
    private ImageView ivMapBall;
    private float x;
    private float y;
    private Bitmap bitgamemap;
    private Bitmap bitgamemap2;
    private int margin = 10;
    private int min = 50;
    private int max = 200;
    private int widthPixels;
    private int heightPixels;
    private boolean buttonpress;
    private List<Float> listanegrax = new ArrayList<Float>();
    private List<Float> listanegray = new ArrayList<Float>();
    private List<Float> listanegrar = new ArrayList<Float>();
    private float xamarelo;
    private float yamarelo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            getSupportActionBar().hide();
        btNewGame = findViewById(R.id.bt_newGame);
        btStart = findViewById(R.id.bt_start);
        ivMapBall  = findViewById(R.id.iv_mapBall);
        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        heightPixels = displayMetrics.heightPixels;
        widthPixels = displayMetrics.widthPixels;

        try {
            sensorManager = (SensorManager)getSystemService(this.SENSOR_SERVICE);
            rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);

        } catch (Exception e) {
            Toast.makeText(this, "O dispositivo é incompatível", Toast.LENGTH_LONG).show();
        }

        btNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listanegrar.clear();
                listanegrax.clear();
                listanegray.clear();
                buttonpress = false;
                //server para obter o tamanho do ecrã do telemovel





                //desenhar jogo

                Bitmap bitmapnew = Bitmap.createBitmap(widthPixels,heightPixels,Bitmap.Config.ARGB_8888);
                bitgamemap = bitmapnew;

                Canvas canvasnew = new Canvas(bitmapnew);
                canvasnew.drawColor(Color.MAGENTA);


                Paint paint = new Paint(); //cria um paint para desenhar

                paint.setStyle(Paint.Style.FILL); //define o estilo do desenho //FILL = preenche o bitmap
                paint.setColor(Color.BLACK); //define a cor do desenho //RED = vermelho
                paint.setAntiAlias(true); //define se o desenho será suavizado
                int nBalls = (int)(Math.random() * (15 - 5 + 1) + 5);

                for (int i = 0; i<nBalls;i++){
                    float negrax;
                    float negray;
                    float negrar;

                    do {
                        negrax = (float) Math.random() * (widthPixels - min + 1) + min;
                        negray = (float) Math.random() * ((heightPixels - 450) - 450 + 1) + 450;
                        negrar = (float) (Math.random() * (max - min + 1) + min);

                        if(!((negrax+negrar>= widthPixels)||(negrax <= negrar))){
                            listanegray.add(i, negray);
                            listanegrar.add(i, negrar);
                            listanegrax.add(i, negrax);
                            break;
                        }
                    }while(true);
                    canvasnew.drawCircle(negrax,negray,negrar,paint); //desenha o retângulo na imagem

;
                }
                Paint pintura = new Paint(); //cria um paint para desenhar

                pintura.setStyle(Paint.Style.FILL); //define o estilo do desenho //FILL = preenche o bitmap
                pintura.setColor(Color.YELLOW); //define a cor do desenho //RED = vermelho
                pintura.setAntiAlias(true); //define se o desenho será suavizado
                float ballsize = 125;
                xamarelo = (float)(widthPixels/2);
                yamarelo = (float)heightPixels-ballsize;
                canvasnew.drawCircle(xamarelo,yamarelo,ballsize,pintura); //desenha o retângulo na imagem

                ivMapBall.setImageBitmap(bitgamemap);

        }

        });
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Drawable drawingMap = ivMapBall.getDrawable();

                bitgamemap2 = ((BitmapDrawable) drawingMap).getBitmap(); //pega o bitmap do drawable

                Bitmap bitmapGameCopy = bitgamemap2.copy(Bitmap.Config.ARGB_8888, true); //copia o bitmap original

                Paint pintura2 = new Paint(); //cria um paint para desenhar

                pintura2.setStyle(Paint.Style.FILL); //define o estilo do desenho //FILL = preenche o bitmap
                pintura2.setColor(Color.GREEN); //define a cor do desenho //RED = vermelho
                pintura2.setAntiAlias(true); //define se o desenho será suavizado


                Canvas canvas = new Canvas(bitmapGameCopy);
                canvas.drawCircle((float)(widthPixels/2),(float)(72.5),(float)62.5,pintura2); //desenha o retângulo na imagem

                ivMapBall.setImageDrawable(new BitmapDrawable(getResources(),bitmapGameCopy));

                x = widthPixels/2;
                y = (float) 72.5;

                buttonpress = true;

            }
        });
    }



    @Override
    public void onSensorChanged(SensorEvent event) {


        //parte do sensor do jogo

        if (event.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
            float[] rotMatrix = new float[9];
            float[] rotVals = new float[3];

            SensorManager.getRotationMatrixFromVector(rotMatrix, event.values);
            SensorManager.remapCoordinateSystem(rotMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Y, rotMatrix);
            SensorManager.getOrientation(rotMatrix, rotVals);

            float pitch = (float) Math.toDegrees(rotVals[1]);
            float roll = (float) Math.toDegrees(rotVals[2]);

            if (buttonpress == true){
                float tempx = (float)Math.cos(Math.toRadians(calculateAngle(pitch,roll)))*4; //Dá o valor onde vamos nos mexer no x
                float tempy = (float)Math.sin(Math.toRadians(calculateAngle(pitch,roll)))*4; //Dá o valor onde vamos nos mexer no y


                //############Reseta o ecrã para a bola não arrastar####################
                Bitmap bitmapGameCopy = bitgamemap.copy(Bitmap.Config.ARGB_8888, true);
                ivMapBall.setImageDrawable(new BitmapDrawable(getResources(),bitmapGameCopy));





                Drawable drawingMap2 = ivMapBall.getDrawable();

                bitgamemap2 = ((BitmapDrawable) drawingMap2).getBitmap(); //pega o bitmap do drawable

                Bitmap bitmapGameCopy2 = bitgamemap2.copy(Bitmap.Config.ARGB_8888, true); //copia o bitmap original

                Paint pintura2 = new Paint(); //cria um paint para desenhar

                pintura2.setStyle(Paint.Style.FILL); //define o estilo do desenho //FILL = preenche o bitmap
                pintura2.setColor(Color.GREEN); //define a cor do desenho //RED = vermelho
                pintura2.setAntiAlias(true); //define se o desenho será suavizado


                x = tempx+x;
                y = tempy+y;

                    for(int e = 0; e<listanegrax.size(); e++){
                        if (Math.pow((listanegrax.get(e)-x),2) + Math.pow((listanegray.get(e)-y),2) <= Math.pow((72.5+listanegrar.get(e)),2)){
                            buttonpress = false;
                            Toast.makeText(this,"Perdeu pressione Novo Jogo ou Iniciar para jogar de novo",Toast.LENGTH_LONG).show();
                        }
                    }
                if(Math.pow((x-xamarelo),2) + Math.pow((yamarelo-y),2) <= Math.pow((72.5+125),2)){
                    buttonpress = false;
                    Toast.makeText(this,"Ganhou pressione Novo Jogo ou Iniciar para jogar de novo",Toast.LENGTH_LONG).show();
                }
                if((x >= widthPixels || x <= 0) || (y>=heightPixels || y <= 0)){
                    buttonpress = false;
                    Toast.makeText(this,"Perdeu pressione Novo Jogo ou Iniciar para jogar de novo",Toast.LENGTH_LONG).show();
                }

                Canvas canvas = new Canvas(bitmapGameCopy2);
                canvas.drawCircle(x,y,(float)62.5,pintura2); //desenha o retângulo na imagem



                ivMapBall.setImageDrawable(new BitmapDrawable(getResources(),bitmapGameCopy2));






            }else{

            }



        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public int calculateAngle(float pitch, float roll) {

        if (pitch <= margin && pitch >= -margin && roll >= margin) {
            return 0;

        } else if (pitch <= -margin && roll >= margin) {
            return 45;

        } else if (pitch <= -margin && roll >= -margin && roll <= margin) {
            return 90;

        } else if (pitch <= -margin && roll <= -margin) {
            return 135;

        } else if (pitch <= margin && pitch >= -margin && roll <= -margin) {
            return 180;

        } else if (pitch >= margin && roll <= -margin) {
            return 225;

        } else if (pitch >= margin && roll >= -margin && roll <= margin) {
            return 270;

        } else if (pitch >= margin && roll >= margin) {
            return 315;

        } else {
            return 270;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, rotationSensor,
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

}