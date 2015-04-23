/*
 * Copyright (C) 2015 James Pekarek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package amagi82.flexibleratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.RatingBar;

public class FlexibleRatingBar extends RatingBar {

    private int colorOutlineOn = Color.rgb(0x11, 0x11, 0x11);
    private int colorOutlineOff = Color.rgb(0x61,0x61,0x61);
    private int colorOutlinePressed = Color.rgb(0xFF,0xB7,0x4D);
    private int colorFillOn = Color.rgb(0xFF,0x98,0x00);
    private int colorFillOff = Color.TRANSPARENT;
    private int colorFillPressedOn = Color.rgb(0xFF,0xB7,0x4D);
    private int colorFillPressedOff = Color.TRANSPARENT;
    private int polygonVertices = 5;
    private int polygonRotation = 0; //measured in degrees
    private int strokeWidth; //width of the outline
    private Paint paintInside = new Paint();
    private Paint paintOutline = new Paint();
    private Bitmap colorsJoined;
    private Path path = new Path();
    private RectF rectangle = new RectF();
    private Matrix matrix = new Matrix();
    private float interiorAngleModifier = 2.2F;
    private float dp = getResources().getDisplayMetrics().density;

    public FlexibleRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getXmlAttrs(context, attrs);
    }

    public FlexibleRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        getXmlAttrs(context, attrs);
    }

    public FlexibleRatingBar(Context context) {
        super(context);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = (int)(50 * dp * getNumStars());
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(heightSize, width/getNumStars());
        } else {
            //Be whatever you want
            height = width/getNumStars();
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    //Create a star polygon with any number of vertices, down to 2, which creates a diamond
    //If you enter 0 vertices, you get a circle
    private Path createStarBySize(float size, int steps) {
        //draw a simple circle if steps == 0
        if(steps == 0){
            path.addOval(new RectF(0, 0, size, size), Path.Direction.CW);
            path.close();
            return path;
        }
        float halfSize = size / 2.0F;
        float radius = halfSize / interiorAngleModifier; //Adjusts "pointiness" of stars
        float degreesPerStep = (float) Math.toRadians(360.0F / (float) steps);
        float halfDegreesPerStep = degreesPerStep / 2.0F;
        path.setFillType(Path.FillType.EVEN_ODD);
        float max = (float) (2.0F* Math.PI);
        path.moveTo(halfSize, 0);
        for (double step = 0; step < max; step += degreesPerStep) {
            path.lineTo((float)(halfSize - halfSize * Math.sin(step)), (float)(halfSize - halfSize * Math.cos(step)));
            path.lineTo((float)(halfSize - radius * Math.sin(step + halfDegreesPerStep)), (float)(halfSize - radius * Math.cos(step + halfDegreesPerStep)));
        }
        path.close();
        return path;
    }

    @Override
    public void onDraw(Canvas canvas) {

        //If starSize matches getHeight, the tips of the star can get cut off due to strokeWidth being added to the polygon size.
        //Make it a bit smaller to avoid this. Also decrease star size and spread them out rather than cutting them off if the
        //height is insufficient for the width.
        float starSize = Math.min(getHeight(), getWidth()/getNumStars());
        if (strokeWidth < 0 ) strokeWidth = (int)(starSize/15);
        starSize -= strokeWidth;

        BitmapShader shaderFill = createShader(colorFillOn, colorFillOff);
        BitmapShader shaderFillPressed = createShader(colorFillPressedOn, colorFillPressedOff);
        paintInside.setAntiAlias(true);
        paintOutline.setStrokeWidth(strokeWidth);
        paintOutline.setStyle(Paint.Style.STROKE);
        paintOutline.setStrokeJoin(Paint.Join.ROUND); //Remove this line to create pointy stars
        paintOutline.setAntiAlias(true);

        //Default RatingBar changes color when pressed. This replicates the effect.
        paintInside.setShader(isPressed()? shaderFillPressed : shaderFill);

        path.rewind();
        path = createStarBySize(starSize, polygonVertices);

        //Rotate star if desired, and resize to fit the available area. Height and width may change during rotation.
        //Other shapes may not be in desirable orientations, but you can rotate them with setPolygonRotation
        path.computeBounds(rectangle,  true);
        float maxDimension = Math.max(rectangle.height(), rectangle.width());
        matrix.setScale(starSize / (1.15F * maxDimension), starSize / (1.15F * maxDimension));
        if(polygonRotation != 0) matrix.preRotate(polygonRotation);
        path.transform(matrix);

        for (int i=0;i<getNumStars();++i) {
            //Default RatingBar only shows fractions in the interior, not the outline.
            paintOutline.setColor(isPressed()?  colorOutlinePressed : i<getRating()? colorOutlineOn : colorOutlineOff);

            path.computeBounds(rectangle,  true);
            path.offset((i+.5F)*getWidth()/getNumStars() - rectangle.centerX(), getHeight()/2 - rectangle.centerY());
            canvas.drawPath(path, paintInside);
            canvas.drawPath(path, paintOutline);
        }
    }

    //Create a BitmapShader, which is used to show fractions of stars
    private BitmapShader createShader(int colorOn, int colorOff){

        //Bitmap of width 0 will cause a crash. Make sure it's a positive number.
        int ratingWidth = (int)(getRating()*getWidth()/getNumStars());

        if(ratingWidth <= 0){
            colorsJoined = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            colorsJoined.eraseColor(colorOff);
        }else if(getWidth()-ratingWidth <= 0){
            colorsJoined = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            colorsJoined.eraseColor(colorOn);
        }else {
            Bitmap colorLeft = Bitmap.createBitmap(ratingWidth, getHeight(), Bitmap.Config.ARGB_8888);
            Bitmap colorRight = Bitmap.createBitmap(getWidth() - ratingWidth, getHeight(), Bitmap.Config.ARGB_8888);
            colorLeft.eraseColor(colorOn);
            colorRight.eraseColor(colorOff);
            colorsJoined = combineBitmaps(colorLeft, colorRight);
        }
        return new BitmapShader(colorsJoined, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    }

    //Combine two bitmaps side by side for use as a BitmapShader
    private Bitmap combineBitmaps(Bitmap leftBitmap, Bitmap rightBitmap) {
        colorsJoined = Bitmap.createBitmap(leftBitmap.getWidth() + rightBitmap.getWidth(), leftBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(colorsJoined);
        comboImage.drawBitmap(leftBitmap, 0f, 0f, null);
        comboImage.drawBitmap(rightBitmap, leftBitmap.getWidth(), 0f, null);

        return colorsJoined;
    }

    //Set any XML attributes that may have been specified
    private void getXmlAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FlexibleRatingBar, 0, 0);
        try {
            colorOutlineOn = a.getInteger(R.styleable.FlexibleRatingBar_colorOutlineOn, Color.rgb(0x11, 0x11, 0x11));
            colorOutlineOff = a.getInteger(R.styleable.FlexibleRatingBar_colorOutlineOff, Color.rgb(0x61,0x61,0x61));
            colorOutlinePressed = a.getInteger(R.styleable.FlexibleRatingBar_colorOutlinePressed, Color.rgb(0xFF,0xB7,0x4D));
            colorFillOn = a.getInteger(R.styleable.FlexibleRatingBar_colorFillOn, Color.rgb(0xFF,0x98,0x00));
            colorFillOff = a.getInteger(R.styleable.FlexibleRatingBar_colorFillOff, Color.TRANSPARENT);
            colorFillPressedOn = a.getInteger(R.styleable.FlexibleRatingBar_colorFillPressedOn, Color.rgb(0xFF,0xB7,0x4D));
            colorFillPressedOff = a.getInteger(R.styleable.FlexibleRatingBar_colorFillPressedOff, Color.TRANSPARENT);
            polygonVertices = a.getInteger(R.styleable.FlexibleRatingBar_polygonVertices, 5);
            polygonRotation = a.getInteger(R.styleable.FlexibleRatingBar_polygonRotation, 0);
            strokeWidth = (int) a.getDimension(R.styleable.FlexibleRatingBar_strokeWidth, -1);
        } finally {
            a.recycle();
        }
    }

    public void setColorOutlineOn(int colorOutlineOn) {
        this.colorOutlineOn = colorOutlineOn;
    }

    public void setColorOutlineOff(int colorOutlineOff) {
        this.colorOutlineOff = colorOutlineOff;
    }

    public void setColorOutlinePressed(int colorOutlinePressed) {
        this.colorOutlinePressed = colorOutlinePressed;
    }

    public void setColorFillOn(int colorFillOn) {
        this.colorFillOn = colorFillOn;
    }

    public void setColorFillOff(int colorFillOff) {
        this.colorFillOff = colorFillOff;
    }

    public void setColorFillPressedOn(int colorFillPressedOn) {
        this.colorFillPressedOn = colorFillPressedOn;
    }

    public void setColorFillPressedOff(int colorFillPressedOff) {
        this.colorFillPressedOff = colorFillPressedOff;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public void setPolygonVertices(int polygonVertices) {
        this.polygonVertices = polygonVertices;
    }

    public void setPolygonRotation(int polygonRotation) {
        this.polygonRotation = polygonRotation;
    }

    public void setInteriorAngleModifier(float interiorAngleModifier) {
        this.interiorAngleModifier = interiorAngleModifier;
    }

}
