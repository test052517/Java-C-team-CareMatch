package Talk.common;

import java.util.*;

public final class KV {
    private KV(){}

    // {k=v&k2=v2} -> Map
    public static Map<String,String> parse(String s){
        Map<String,String> m = new LinkedHashMap<>();
        if (s == null) return m;
        String t = s.trim();
        if (t.startsWith("{") && t.endsWith("}")) t = t.substring(1, t.length()-1);
        if (t.isEmpty()) return m;
        String[] parts = t.split("&");
        for (String p : parts) {
            if (p.isEmpty()) continue;
            int i = p.indexOf('=');
            if (i < 0) continue;
            String k = unescape(p.substring(0,i));
            String v = unescape(p.substring(i+1));
            m.put(k, v);
        }
        return m;
    }

    // Map -> k=v&k2=v2
    public static String build(Map<String,String> map){
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String,String> e : map.entrySet()) {
            if (!first) sb.append('&');
            first = false;
            sb.append(escape(e.getKey())).append('=').append(escape(e.getValue()));
        }
        return sb.toString();
    }

    // 안전하게 인코딩 (역슬래시, 퍼센트, &, =, 개행)
    private static String escape(String s){
        if (s == null) return "";
        return s
                .replace("\\", "%5c")
                .replace("%", "%25")
                .replace("&", "%26")
                .replace("=", "%3d")
                .replace("\n", "%0a");
    }

    // 디코딩 (escape의 역순)
    private static String unescape(String s){
        if (s == null) return "";
        return s
                .replace("%0a", "\n")
                .replace("%3d", "=")
                .replace("%26", "&")
                .replace("%25", "%")
                .replace("%5c", "\\");
    }
}
