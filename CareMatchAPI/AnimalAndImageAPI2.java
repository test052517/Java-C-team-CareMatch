package CareMatchAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class AnimalAndImageAPI2 {

    static final String SERVICE_KEY = "U5Fc8ClxFFw/1zGDrJ1FJ/UCGlEW80plhVQwzw3DPGmFA0aQkq9JSKkZhYDDB33xcQ1GMOcFXV1xpHpQ6ZzU1Q==";
    static final String BASE_URL = "https://apis.data.go.kr/1543061/abandonmentPublicService_v2/abandonmentPublic_v2";

    public static void main(String[] args) {
        try {
            List<String> shelterIds = Arrays.asList(
                "311304200900003", "311316201100001", "326334202100001", "326339201300001", "326340201100001",
                "327342201400001", "327345201400001", "327346201500009", "327346201800001", "327346201900001",
                "328351200900001", "328353200900001", "328354201700003", "328356201000001", "328357202400001",
                "329363201300001", "330368202000001", "331369201900001", "331370200900001", "331371201000001",
                "341394201400001", "341409201400001", "341554201300008", "342419200900001", "342420201500001"
            );

            int totalCount = 0;
            Set<String> printedIds = new HashSet<>();

            for (String shelterId : shelterIds) {
                if (totalCount >= 10) break;

                StringBuilder urlBuilder = new StringBuilder(BASE_URL);
                urlBuilder.append("?serviceKey=").append(URLEncoder.encode(SERVICE_KEY, "UTF-8"));
                urlBuilder.append("&_type=json");
                urlBuilder.append("&numOfRows=10");
                urlBuilder.append("&pageNo=1");
                urlBuilder.append("&upkind=422400");
                urlBuilder.append("&careRegNo=").append(shelterId);

                URL url = new URL(urlBuilder.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                }
                rd.close();
                conn.disconnect();

                String resText = response.toString().trim();
                if (!resText.startsWith("{")) continue;

                JSONObject json = new JSONObject(resText);
                JSONObject body = json.getJSONObject("response").getJSONObject("body");
                if (!body.has("items")) continue;

                JSONObject itemsObj = body.getJSONObject("items");
                if (!itemsObj.has("item")) continue;

                JSONArray items;
                Object item = itemsObj.get("item");
                if (item instanceof JSONArray) {
                    items = (JSONArray) item;
                } else {
                    items = new JSONArray();
                    items.put((JSONObject) item);
                }

                for (int i = 0; i < items.length(); i++) {
                    JSONObject a = items.getJSONObject(i);
                    String animalId = a.optString("desertionNo");

                    if (printedIds.contains(animalId)) continue;

                    // ✅ 중복 아니면 출력
                    printedIds.add(animalId);
                    totalCount++;

                    String kind = a.optString("kindCd");
                    String sex = a.optString("sexCd");
                    String neuter = a.optString("neuterYn");
                    String date = a.optString("happenDt");
                    String status = a.optString("processState");

                    String image = a.optString("popfile");
                    if (image == null || image.isEmpty()) {
                        for (int idx = 1; idx <= 8; idx++) {
                            image = a.optString("popfile" + idx);
                            if (image != null && !image.isEmpty()) break;
                        }
                    }

                    System.out.println("ID: " + animalId);
                    System.out.println("종: " + kind + " / 날짜: " + date);
                    System.out.println("성별: " + sex + ", 중성화: " + neuter + ", 상태: " + status);
                    System.out.println("이미지: " + image);
                    System.out.println("보호소 ID: " + shelterId);
                    System.out.println("----------------------------------");
                    break; // 한 보호소당 1마리만
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
