package com.becdoor.teanotes.view;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by paulz on 2016/10/27.
 */

public class ContainsEmojiEditText extends EditText {
    private int cursorPos;
    private String inputAfterText;
    private Context mContext;
    private boolean resetText;

    public ContainsEmojiEditText(Context paramContext) {
        super(paramContext);
        this.mContext = paramContext;
        initEditText();
    }

    public ContainsEmojiEditText(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.mContext = paramContext;
        initEditText();
    }

    public ContainsEmojiEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        this.mContext = paramContext;
        initEditText();
    }

    public static boolean containsEmoji(String paramString) {
        int j = paramString.length();
        int i = 0;
        while (true) {
            if (i >= j)
                return false;
            if (!isEmojiCharacter(paramString.charAt(i)))
                return true;
            i += 1;
        }
    }

    private void initEditText() {
        this.cursorPos = getSelectionEnd();
        addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable paramAnonymousEditable) {
            }

            public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
                if (!ContainsEmojiEditText.this.resetText) {
                    ContainsEmojiEditText.this.cursorPos = ContainsEmojiEditText.this.getSelectionEnd();
                    ContainsEmojiEditText.this.inputAfterText = paramAnonymousCharSequence.toString();
                }
            }

            public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
                if (!resetText) {
                    if (paramAnonymousInt3 >= 2) {//表情符号的字符长度最小为2
                        CharSequence input = paramAnonymousCharSequence.subSequence(cursorPos, cursorPos + paramAnonymousInt3);
                        if (containsEmoji(input.toString())) {
                            resetText = true;
                            Toast.makeText(mContext, "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                            //是表情符号就将文本还原为输入表情符号之前的内容
                            setText(inputAfterText);
                            CharSequence text = getText();
                            if (text instanceof Spannable) {
                                Spannable spanText = (Spannable) text;
                                Selection.setSelection(spanText, text.length());
                            }
                        }
                    }
                } else {
                    resetText = false;
                }
            }
        });
    }

    private static boolean isEmojiCharacter(char paramChar) {
        return (paramChar == 0) || (paramChar == '\t') || (paramChar == '\n') || (paramChar == '\r') || ((paramChar >= ' ') && (paramChar <= 55295)) || ((paramChar >= 57344) && (paramChar <= 65533)) || ((paramChar >= 65536) && (paramChar <= 1114111));
    }
}
