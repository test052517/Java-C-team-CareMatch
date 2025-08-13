package CareMatchAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.*;
import org.json.*;

public class ShelterAPI {

	static final String SERVICE_KEY = "U5Fc8ClxFFw%2F1zGDrJ1FJ%2FUCGlEW80plhVQwzw3DPGmFA0aQkq9JSKkZhYDDB33xcQ1GMOcFXV1xpHpQ6ZzU1Q%3D%3D";
    static final String SIDO_URL = "https://apis.data.go.kr/1543061/abandonmentPublicService_v2/sido_v2";
    static final String SIGUNGU_URL = "https://apis.data.go.kr/1543061/abandonmentPublicService_v2/sigungu_v2";
    static final String SHELTER_URL = "https://apis.data.go.kr/1543061/abandonmentPublicService_v2/shelter_v2";
    static final String DB_URL = "jdbc:mysql://localhost:3306/CareMatch?serverTimezone=Asia/Seoul";
    static final String DB_USER = "root";
    static final String DB_PASS = "1234";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

            // 1. 전국 시도코드(upr_cd) 목록 얻기
            List<String> sidoList = new ArrayList<>();
            String sidoApiUrl = SIDO_URL + "?serviceKey=" + SERVICE_KEY + "&_type=json";
            JSONObject sidoResp = getJsonFromApi(sidoApiUrl);
            JSONArray sidoItems = sidoResp
                    .getJSONObject("response")
                    .getJSONObject("body")
                    .getJSONObject("items")
                    .getJSONArray("item");

            for (int i = 0; i < sidoItems.length(); i++) {
                String upr_cd = sidoItems.getJSONObject(i).getString("orgCd");
                sidoList.add(upr_cd);
            }

            // 2. Shelter insert 준비 (중복 무시)
            String insertSQL = "INSERT IGNORE INTO Shelter (shelter_id, shelter_name, telephone, address) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);

            Set<String> insertedShelterId = new HashSet<>(); // shelter_id 중복 방지

            // 3. 각 시도별 시군구 코드 얻고, 모든 조합으로 보호소 API 호출
            for (String upr_cd : sidoList) {
                // (1) 시군구 코드 얻기
                String sigunguApiUrl = SIGUNGU_URL + "?serviceKey=" + SERVICE_KEY + "&_type=json&upr_cd=" + upr_cd;
                JSONObject sigunguResp = getJsonFromApi(sigunguApiUrl);
                JSONObject sigunguBody = sigunguResp.getJSONObject("response").getJSONObject("body");
                if (!sigunguBody.getJSONObject("items").has("item")) continue;

                Object sigunguObj = sigunguBody.getJSONObject("items").get("item");
                JSONArray sigunguItems;
                if (sigunguObj instanceof JSONArray) {
                    sigunguItems = (JSONArray) sigunguObj;
                } else {
                    sigunguItems = new JSONArray();
                    sigunguItems.put((JSONObject) sigunguObj);
                }

                for (int j = 0; j < sigunguItems.length(); j++) {
                    String org_cd = sigunguItems.getJSONObject(j).getString("orgCd");

                    // (2) shelter_v2 API 호출
                    String apiUrl = SHELTER_URL + "?serviceKey=" + SERVICE_KEY
                            + "&_type=json"
                            + "&upr_cd=" + upr_cd
                            + "&org_cd=" + org_cd
                            + "&numOfRows=1000&pageNo=1";

                    System.out.println("요청 URL: " + apiUrl);

                    try {
                        JSONObject shelterResp = getJsonFromApi(apiUrl);
                        JSONObject body = shelterResp.getJSONObject("response").getJSONObject("body");
                        JSONObject items = body.getJSONObject("items");

                        if (!items.has("item")) {
                            System.out.println("보호소 없음 - upr_cd: " + upr_cd + ", org_cd: " + org_cd);
                            continue;
                        }

                        Object itemObj = items.get("item");
                        JSONArray itemArray;
                        if (itemObj instanceof JSONArray) {
                            itemArray = (JSONArray) itemObj;
                        } else {
                            itemArray = new JSONArray();
                            itemArray.put((JSONObject) itemObj);
                        }

                        for (int k = 0; k < itemArray.length(); k++) {
                            JSONObject item = itemArray.getJSONObject(k);
                            String careRegNo = item.optString("careRegNo");
                            if (insertedShelterId.contains(careRegNo)) continue; // shelter_id 중복 방지

                            String name = item.optString("careNm");
                            String tel = item.optString("careTel");
                            String addr = item.optString("careAddr");

                            pstmt.setString(1, careRegNo);
                            pstmt.setString(2, name);
                            pstmt.setString(3, tel);
                            pstmt.setString(4, addr);
                            pstmt.addBatch();
                            insertedShelterId.add(careRegNo);
                        }

                    } catch (Exception e) {
                        System.out.println("보호소 정보 조회 실패: " + upr_cd + " / " + org_cd);
                        e.printStackTrace();
                    }
                }
            }
            pstmt.executeBatch();
            System.out.println("Shelter 테이블에 보호소 정보 저장 완료");

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
