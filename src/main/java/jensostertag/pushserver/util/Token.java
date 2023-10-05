package jensostertag.pushserver.util;

import jensostertag.pushserver.data.Config;

import java.util.Random;

public class Token {
    private static final String TOKEN_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.,_+-*#@%&<>";


    public static String randomToken() {
        Random random = new Random();

        StringBuilder token = new StringBuilder();
        for(int i = 0; i < Config.CHANNEL_TOKEN_LENGTH; i++) {
            token.append(Token.TOKEN_CHARACTERS.charAt(random.nextInt(Token.TOKEN_CHARACTERS.length())));
        }

        return token.toString();
    }
}
