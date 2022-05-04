package com.ispmimtic.minicrypto;

import android.content.Context;
import android.widget.Toast;

import com.ispmimtic.minicrypto.Activities.MainActivity;

public class AffineCipher {

    static int a = 17;
    static int b = 20;

    public static String crypterDonnee(String message) {
        message = message.toUpperCase();
        char[] msg = message.toCharArray();

        String cipher = "";
        for (int i = 0; i < msg.length; i++)
        {
            if (msg[i] != ' ' && estAlphabet(msg[i]))
            {
                cipher = cipher
                        + (char) ((((a * (msg[i] - 'A')) + b) % 26) + 'A');
            }
            else
            {
                cipher += msg[i];
            }
        }
        return cipher;
    }
    public static String decrypterDonnee(String cipher) {
        String msg = "";
        int a_inv = 0;
        int flag = 0;
        for (int i = 0; i < 26; i++)
        {
            flag = (a * i) % 26;
            if (flag == 1)
            {
                a_inv = i;
            }
        }
        for (int i = 0; i < cipher.length(); i++)
        {
            if (cipher.charAt(i) != ' ' && estAlphabet(cipher.charAt(i)))
            {
                msg = msg + (char) (((a_inv *
                        ((cipher.charAt(i) + 'A' - b)) % 26)) + 'A');
            }
            else
            {
                msg += cipher.charAt(i);
            }
        }
        String txt1 = "", txt2 = "";
        char[] message = msg.toCharArray();
        for (int i = 0; i < message.length; i++) {
            if (i != 0)
                txt1 += message[i];
            else
                txt2 += message[i];
        }
        txt2 = txt2.toUpperCase();
        txt1 = txt1.toLowerCase();
        msg = txt2 + txt1;
        return msg;
    }

    static boolean estAlphabet(char lettre) {
        boolean b = false;
        char alphabet[] = {'a','b','c','d','e','f','g','h','i','j','k','l',
                'm','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        char alphabetM[] = {'A','B','C','D','E','F','G','H','I','J','K','L',
                'M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        for (int i = 0; i < alphabet.length; i++) {
            if (lettre == alphabet[i] || lettre == alphabetM[i]) {
                b = true;
                break;
            }
        }
        return b;
    }



}