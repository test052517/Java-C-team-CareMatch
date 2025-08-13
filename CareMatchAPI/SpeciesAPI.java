package CareMatchAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import org.json.*;

public class SpeciesAPI {
	static final String SERVICE_KEY = "U5Fc8ClxFFw%2F1zGDrJ1FJ%2FUCGlEW80plhVQwzw3DPGmFA0aQkq9JSKkZhYDDB33xcQ1GMOcFXV1xpHpQ6ZzU1Q%3D%3D";
    static final String KIND_URL = "https://apis.data.go.kr/1543061/abandonmentPublicService_v2/kind_v2";
    static final String DB_URL = "jdbc:mysql://localhost:3306/CareMatch?serverTimezone=Asia/Seoul";
    static final String DB_USER = "root";
    static final String DB_PASS = "1234";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            // 1. 동물 종류(개, 고양이, 기타)별로 모두 반복 (type: "417000" = 개, "422400" = 고양이, "429900" = 기타)
            String[] typeCodes = {"417000", "422400", "429900"};
            String[] typeNames = {"개", "고양이", "기타"};

            String insertSQL = "INSERT IGNORE INTO Species (kind_id, kind_name, type_name) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);

            for (int idx = 0; idx < typeCodes.length; idx++) {
                String typeCd = typeCodes[idx];
                String typeName = typeNames[idx];

                String apiUrl = KIND_URL + "?serviceKey=" + SERVICE_KEY
                        + "&_type=json&up_kind_cd=" + typeCd
                        + "&numOfRows=1000&pageNo=1";

                JSONObject resp = getJsonFromApi(apiUrl);
                JSONObject items = resp
                        .getJSONObject("response")
                        .getJSONObject("body")
                        .getJSONObject("items");
                if (!items.has("item")) continue;

                Object itemObj = items.get("item");
                JSONArray kindArray;
                if (itemObj instanceof JSONArray) {
                    kindArray = (JSONArray) itemObj;
                } else {
                    kindArray = new JSONArray();
                    kindArray.put((JSONObject) itemObj);
                }

                for (int i = 0; i < kindArray.length(); i++) {
                    JSONObject item = kindArray.getJSONObject(i);
                    String kindCd = item.optString("kindCd");
                    String kindNm = item.optString("kindNm");
                    pstmt.setString(1, kindCd);
                    pstmt.setString(2, kindNm);
                    pstmt.setString(3, typeName);
                    pstmt.addBatch();
                }
            }
            pstmt.executeBatch();
            System.out.println("Species 테이블에 모든 종 정보 저장 완료!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // JSON 응답 파싱 함수
    private static JSONObject getJsonFromApi(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connHttp = (HttpURLConnection) url.openConnection();
        connHttp.setRequestMethod("GET");
        connHttp.setRequestProperty("Content-type", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader(
                connHttp.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        connHttp.disconnect();

        return new JSONObject(sb.toString());
    }
}
