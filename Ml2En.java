import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sabin Jose on 7/11/15.
 * <p/>
 * This algorithm transliterates Malayalam script to Roman characters ('Manglish')
 * Java Port of transliteration algorithm from http://github.com/knadh/ml2en
 * This work is licensed under Apache Licence v2.0
 */
public class Ml2En {

    private static Ml2En ml2En = null;

    private HashMap<String, String> vowels = new HashMap<String, String>();
    private HashMap<String, String> consonants = new HashMap<String, String>();
    private HashMap<String, String> compounds = new HashMap<String, String>();
    private HashMap<String, String> chillu = new HashMap<String, String>();
    private HashMap<String, String> modifiers = new HashMap<String, String>();

    //initialize Mapping
    private Ml2En() {
        vowels.put("അ", "a");
        vowels.put("ആ", "aa");
        vowels.put("ഇ", "i");
        vowels.put("ഈ", "ee");
        vowels.put("ഉ", "u");
        vowels.put("ഊ", "oo");
        vowels.put("ഋ", "ru");
        vowels.put("എ", "e");
        vowels.put("ഏ", "e");
        vowels.put("ഐ", "ai");
        vowels.put("ഒ", "o");
        vowels.put("ഓ", "o");
        vowels.put("ഔ", "au");
        //consonants
        consonants.put("ക", "k");
        consonants.put("ഖ", "kh");
        consonants.put("ഗ", "g");
        consonants.put("ഘ", "gh");
        consonants.put("ങ", "ng");
        consonants.put("ച", "ch");
        consonants.put("ഛ", "chh");
        consonants.put("ജ", "j");
        consonants.put("ഝ", "jh");
        consonants.put("ഞ", "nj");
        consonants.put("ട", "t");
        consonants.put("ഠ", "dt");
        consonants.put("ഡ", "d");
        consonants.put("ഢ", "dd");
        consonants.put("ണ", "n");
        consonants.put("ത", "th");
        consonants.put("ഥ", "th");
        consonants.put("ദ", "d");
        consonants.put("ധ", "dh");
        consonants.put("ന", "n");
        consonants.put("പ", "p");
        consonants.put("ഫ", "ph");
        consonants.put("ബ", "b");
        consonants.put("ഭ", "bh");
        consonants.put("മ", "m");
        consonants.put("യ", "y");
        consonants.put("ര", "r");
        consonants.put("ല", "l");
        consonants.put("വ", "v");
        consonants.put("ശ", "sh");
        consonants.put("ഷ", "sh");
        consonants.put("സ", "s");
        consonants.put("ഹ", "h");
        consonants.put("ള", "l");
        consonants.put("ഴ", "zh");
        consonants.put("റ", "r");
        //ccmpounds
        compounds.put("ക്ക", "kk");
        compounds.put("ഗ്ഗ", "gg");
        compounds.put("ങ്ങ", "ng");
        compounds.put("ക്ക", "kk");
        compounds.put("ച്ച", "cch");
        compounds.put("ജ്ജ", "jj");
        compounds.put("ഞ്ഞ", "nj");
        compounds.put("ട്ട", "tt");
        compounds.put("ണ്ണ", "nn");
        compounds.put("ത്ത", "tth");
        compounds.put("ദ്ദ", "ddh");
        compounds.put("ദ്ധ", "ddh");
        compounds.put("ന്ന", "nn");
        compounds.put("ന്ത", "nth");
        compounds.put("ങ്ക", "nk");
        compounds.put("ണ്ട", "nd");
        compounds.put("ബ്ബ", "bb");
        compounds.put("പ്പ", "pp");
        compounds.put("മ്മ", "mm");
        compounds.put("യ്യ", "yy");
        compounds.put("ല്ല", "ll");
        compounds.put("വ്വ", "vv");
        compounds.put("ശ്ശ", "sh");
        compounds.put("സ്സ", "s");
        compounds.put("ക്സ", "ks");
        compounds.put("ഞ്ച", "nch");
        compounds.put("ക്ഷ", "ksh");
        compounds.put("മ്പ", "mp");
        compounds.put("റ്റ", "tt");
        compounds.put("ന്റ", "nt");
        compounds.put("ന്ത", "nth");
        compounds.put("ന്ത്യ", "nthy");
        //chil
        chillu.put("ൽ", "l");
        chillu.put("ൾ", "l");
        chillu.put("ൺ", "n");
        chillu.put("ൻ", "n");
        chillu.put("ർ", "r");
        chillu.put("ൿ", "k");
        //modifiers
        modifiers.put("","");
        modifiers.put("ു്", "u");
        modifiers.put("ാ", "aa");
        modifiers.put("ി", "i");
        modifiers.put("ീ", "ee");
        modifiers.put("ു", "u");
        modifiers.put("ൂ", "oo");
        modifiers.put("ൃ", "ru");
        modifiers.put("െ", "e");
        modifiers.put("േ", "e");
        modifiers.put("ൈ", "y");
        modifiers.put("ൊ", "o");
        modifiers.put("ോ", "o");
        modifiers.put("ൌ", "ou");
        modifiers.put("ൗ", "au");
        modifiers.put("ഃ", "a");
        modifiers.put("", "");



    }

    private String replaceValues(String inputString, Map<String, String> map) {

        for (Map.Entry<String, String> entry : map.entrySet()) {

            inputString = inputString.replaceAll(entry.getKey(), entry.getValue());
        }

        return inputString.toString();
    }

    private String replaceModifiedGlyps(HashMap<String, String> glyph, String input) {

        String regex = "(" + String.join("|", glyph.keySet()) + ")(" + String.join("|", modifiers.keySet() + ")");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            if (matcher.group(2).trim().length() > 0 && !matcher.group(2).equals("്")) {
                input = input.replaceAll(matcher.group(0), glyph.get(matcher.group(1)) + modifiers.get(matcher.group(2)));
            }
        }
        return input;
    }

    //Process input string
    private String transliterate(String input) {

        input = input.replaceAll("(?u)\\xE2\\x80\\x8C", "");
        //replace modifed glyphs
        input = replaceModifiedGlyps(compounds, input);
        input = replaceModifiedGlyps(vowels, input);
        input = replaceModifiedGlyps(consonants, input);

        //replace unmodified glyphs
        for (Map.Entry<String, String> entry : compounds.entrySet()) {

            input = input.replaceAll(entry.getKey() + "്([\\w])", entry.getValue() + "$1");
            input = input.replaceAll(entry.getKey() + "്", entry.getValue() + "u");
            input = input.replaceAll(entry.getKey(), entry.getValue() + "a");

        }

        for (Map.Entry<String, String> entry : consonants.entrySet()) {

            input = input.replaceAll(entry.getKey() + "(?!്)", entry.getValue() + "a");
            input = input.replaceAll(entry.getKey() + "്(?![\\s\\)\\(\\[\\]\\.;,\\\"'\\/\\\\\\%\\!]|$)", entry.getValue());
            input = input.replaceAll(entry.getKey() + "്", entry.getValue() + "u");
            input = input.replaceAll(entry.getKey(), entry.getValue());
        }
        input = replaceValues(input, vowels);
        input = replaceValues(input, chillu);
        input = input.replaceAll("ം", "m");
        input = replaceValues(input, modifiers);
        //capitalize first word of a sentence
        input = capitalize(input);
        //input = input.replaceAll("^\\s*(\\w)|[.?!]\\s*(\\w)", "$1$2".toUpperCase());


        return input;
    }

    //Capitalizes char after . ! and ?
    private String capitalize(String input) {
        boolean capitalize = true;
        int pos = 0;
        StringBuilder sb = new StringBuilder(input);
        while (pos < sb.length()) {
            if (sb.charAt(pos) == '.' || sb.charAt(pos) == '!' || sb.charAt(pos) == '?') {
                capitalize = true;
            } else if (capitalize && !Character.isWhitespace(sb.charAt(pos))) {
                sb.setCharAt(pos, Character.toUpperCase(sb.charAt(pos)));
                capitalize = false;
            }
            pos++;
        }
        return new String(sb);

    }

    //Factory Method called to convert ml text
    public static String convert2EN(String input) {
        if (ml2En == null) {
            ml2En = new Ml2En();
        }
        return ml2En.transliterate(input);

    }

}
