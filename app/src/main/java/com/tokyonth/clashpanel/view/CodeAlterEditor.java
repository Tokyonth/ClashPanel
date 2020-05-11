package com.tokyonth.clashpanel.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.res.ResourcesCompat;

import com.tokyonth.clashpanel.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeAlterEditor extends AppCompatEditText {

    private Rect rect;
    private Paint paint;
    private Paint dividerLine;
    private Paint paintLineBG;
    private Rect rectLineBG;

    private int lastLineCount;
    private int basePadding = 8;
    private int lineNumberPadding = basePadding;
    private boolean modified = true;
    private boolean realModified = false;
    private Handler updater = new Handler();

    public CodeAlterEditor(Context context) {
        super(context);
        init();
    }

    public CodeAlterEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        if (rect == null) {
            setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                    InputType.TYPE_TEXT_FLAG_CAP_SENTENCES |
                    InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            setGravity(Gravity.START | Gravity.TOP);
            setEllipsize(TextUtils.TruncateAt.END);
            setTypeface(Typeface.MONOSPACE);
            setTextSize(15);
            setPadding(basePadding, 0, 0, 0);
            rect = new Rect();
            paint = new Paint();
            paint.setFakeBoldText(false);
            paint.setAntiAlias(true);
            dividerLine = new Paint();
            dividerLine.setColor(Color.parseColor("#FFE7E7E7"));
            paint.setStyle(Paint.Style.FILL);
            paint.setTypeface(Typeface.MONOSPACE);
            paint.setTextSize(getTextSize() * 8 / 8);
            paint.setColor(Color.parseColor("#FFB0B0B0"));

            rectLineBG = new Rect();
            paintLineBG = new Paint();
            paintLineBG.setColor(Color.parseColor("#FFF0F0F0"));
            paintLineBG.setStyle(Paint.Style.FILL);
            setTextColor(Color.BLACK);
            setCursorVisible(true);
            setHorizontallyScrolling(true);
            SyntaxColorSet.colorKeyword = ResourcesCompat.getColor(getResources(), R.color.colorCodeKeyword, null);
            SyntaxColorSet.colorString = ResourcesCompat.getColor(getResources(), R.color.colorCodeString, null);
            SyntaxColorSet.colorVariable = ResourcesCompat.getColor(getResources(), R.color.colorCodeVariable, null);
            SyntaxColorSet.colorComment = ResourcesCompat.getColor(getResources(), R.color.colorCodeComment, null);
            SyntaxColorSet.colorNumber = ResourcesCompat.getColor(getResources(), R.color.colorCodeNumber, null);
            addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    updater.removeCallbacks(HighlightUpdater);
                    if (modified || !realModified) {
                        updater.postDelayed(HighlightUpdater, 800);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    public StringBuilder content() {
        Editable editable = new SpannableStringBuilder(getText());
        editable.clearSpans();
        return new StringBuilder(editable.toString());
    }

    private static class SyntaxPattern {
        static Pattern COMMENT = Pattern.compile("#[^\\n]*");
        static Pattern KEYWORD = Pattern.compile("\\b(if|fi|then|port|ip|Proxy|Proxy Group|Rule|done|echo)\\b");
        static Pattern STRING = Pattern.compile("\".*?\"", Pattern.MULTILINE);
        static Pattern VARIABLE = Pattern.compile("\\$[A-Za-z0-9_.]*");
        static Pattern NUMBER = Pattern.compile("\\b[0-9]*\\b");
    }

    private static class SyntaxColorSet {
        static int colorKeyword;
        static int colorString;
        static int colorVariable;
        static int colorComment;
        static int colorNumber;
    }

    private void clearSpans(Editable e) {
        // remove foreground color spans
        {
            ForegroundColorSpan[] spans = e.getSpans(
                    0,
                    e.length(),
                    ForegroundColorSpan.class);

            for (int i = spans.length; i-- > 0; ) {
                e.removeSpan(spans[i]);
            }
        }

        // remove background color spans
        {
            BackgroundColorSpan[] spans = e.getSpans(
                    0,
                    e.length(),
                    BackgroundColorSpan.class);

            for (int i = spans.length; i-- > 0; ) {
                e.removeSpan(spans[i]);
            }
        }
    }

    private Runnable HighlightUpdater = () -> {
        modified = false;
        realModified = true;
        Editable spans = getText();
        assert spans != null;
        clearSpans(spans);
        for (Matcher m = SyntaxPattern.KEYWORD.matcher(spans); m.find(); ) {
            spans.setSpan(
                    new ForegroundColorSpan(SyntaxColorSet.colorKeyword),
                    m.start(),
                    m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        for (Matcher m = SyntaxPattern.STRING.matcher(spans); m.find(); ) {
            spans.setSpan(
                    new ForegroundColorSpan(SyntaxColorSet.colorString),
                    m.start(),
                    m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        for (Matcher m = SyntaxPattern.VARIABLE.matcher(spans); m.find(); ) {
            spans.setSpan(
                    new ForegroundColorSpan(SyntaxColorSet.colorVariable),
                    m.start(),
                    m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        for (Matcher m = SyntaxPattern.COMMENT.matcher(spans); m.find(); ) {
            spans.setSpan(
                    new ForegroundColorSpan(SyntaxColorSet.colorComment),
                    m.start(),
                    m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        for (Matcher m = SyntaxPattern.NUMBER.matcher(spans); m.find(); ) {
            spans.setSpan(
                    new ForegroundColorSpan(SyntaxColorSet.colorNumber),
                    m.start(),
                    m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        modified = true;
    };

    private String appendWhiteSpaces(int lineNumber, int lineCount) {
        StringBuilder lineText = new StringBuilder();
        int fakeLN = lineNumber + 1;
        int wsCount = String.valueOf(lineCount).length() - String.valueOf(fakeLN).length();
//        for (int i = 0; i < wsCount; i++) {
//            lineText.append(" ");
//        }
        lineText.append(fakeLN);
        return lineText.toString();
    }

    private void adjustPadding(String maxLineText) {
        int textWidth = (int) paint.measureText(maxLineText);
        lineNumberPadding = basePadding + textWidth;
        setPadding(lineNumberPadding + 18, 0, 0, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       // int lineHeight = getBaseline();
        String maxLineText = appendWhiteSpaces(getLineCount() - 1, getLineCount());
        int width = lineNumberPadding - basePadding + 1;
        canvas.drawLine(width + 10, 0, width + 10, getHeight(), dividerLine);
        if (lastLineCount != getLineCount()) {
            adjustPadding(maxLineText);
            lastLineCount = getLineCount();
        }

        rectLineBG.bottom = getHeight();
        rectLineBG.top = 0;
        rectLineBG.left = 0;
        rectLineBG.right = width + 10;
        canvas.drawRect(rectLineBG, paintLineBG);

        for (int i = 0; i < getLineCount(); i++) {
            int lineBottom = getLayout().getLineBottom(i);
            int paddingTop = getPaddingTop();

            if (Integer.parseInt(appendWhiteSpaces(i, getLineCount())) < 10) {
                canvas.drawText(appendWhiteSpaces(i, getLineCount()), rect.left + 24, lineBottom - paint.descent() + paddingTop, paint);
            } else if (Integer.parseInt(appendWhiteSpaces(i, getLineCount())) < 100) {
                canvas.drawText(appendWhiteSpaces(i, getLineCount()), rect.left + 14, lineBottom - paint.descent() + paddingTop, paint);
            } else if (Integer.parseInt(appendWhiteSpaces(i, getLineCount())) < 1000) {
                canvas.drawText(appendWhiteSpaces(i, getLineCount()), rect.left + 4, lineBottom - paint.descent() + paddingTop, paint);
            }
        }
    }

}
