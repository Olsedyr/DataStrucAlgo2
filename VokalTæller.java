public class VokalTæller {
    public static int antalVokaler(String str, int l) {
        if (l < 0) return 0;
        return ("aeiouy".indexOf(str.charAt(l)) != -1 ? 1 : 0) + antalVokaler(str, l - 1);
    }
    public static void main(String[] args) {
        System.out.println(antalVokaler("stationsbygninger", "stationsbygninger".length() - 1));
    }
}