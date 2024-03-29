package discord.core.game;

public class GameEmoji {
    public static final String ONE = "\u0031\u20E3";
    public static final String TWO = "\u0032\u20E3";
    public static final String THREE = "\u0033\u20E3";
    public static final String FOUR = "\u0034\u20E3";
    public static final String FIVE = "\u0035\u20E3";
    public static final String SIX = "\u0036\u20E3";
    public static final String SEVEN = "\u0037\u20E3";

    public static final String A = "\uD83C\uDDE6";
    public static final String B = "\uD83C\uDDE7";
    public static final String C = "\uD83C\uDDE8";
    public static final String D = "\uD83C\uDDE9";
    public static final String H = "\uD83C\uDDED";
    public static final String N = "\uD83C\uDDF3";
    public static final String P = "\uD83C\uDDF5";
    public static final String S = "\uD83C\uDDF8";
    public static final String U = "\uD83C\uDDFA";
    public static final String Y = "\uD83C\uDDFE";

    public static final String DOUBLE_ARROW = "↔️";

    public static final String CHECKMARK = "\u2705";
    public static final String X = "❌";
    public static final String EXIT = "\uD83D\uDEAB";

    public static int numberEmojiToInt(String emoji) {
        return emoji.length() == 2 && emoji.charAt(1) == '\u20E3'
                ? emoji.charAt(0) - '\u0030' : 0;
    }

    public static String intToNumberEmoji(int number) {
        return new String(new char[]{(char) (number + '\u0030'), '\u20E3'});
    }

    public static String wordToLetterEmojis(String word) {
        String emojis = "";
        for (char letter : word.toLowerCase().toCharArray()) {
            emojis += letterToLetterEmoji(letter) + " ";
        }
        return emojis;
    }

    private static String letterToLetterEmoji(char letter) {
        char second = (char) (0xDD85 + letter);
        return "\uD83C" + second;
    }


}
