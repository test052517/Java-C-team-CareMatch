package CareMatchAPI;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import org.json.*;

public class RecheckShelterAPI {

	static final String SERVICE_KEY = "U5Fc8ClxFFw%2F1zGDrJ1FJ%2FUCGlEW80plhVQwzw3DPGmFA0aQkq9JSKkZhYDDB33xcQ1GMOcFXV1xpHpQ6ZzU1Q%3D%3D";
    static final String SIDO_URL = "https://apis.data.go.kr/1543061/abandonmentPublicService_v2/sido_v2";
    static final String SIGUNGU_URL = "https://apis.data.go.kr/1543061/abandonmentPublicService_v2/sigungu_v2";
    static final String SHELTER_URL = "https://apis.data.go.kr/1543061/abandonmentPublicService_v2/shelter_v2";
    static final String DB_URL = "jdbc:mysql://localhost:3306/CareMatch?serverTimezone=Asia/Seoul";
    static final String DB_USER = "root";
    static final String DB_PASS = "1234";

    public static void main(String[] args) {
        // 1. 누락 shelter_id Set 준비 (여기에 복붙!)
        Set<String> missingShelterIds = new HashSet<>(Arrays.asList(
            "350650201200001","348548201700001","348543201300001","348541201300001","348537200900002",
            "348536201800001","348535202400001","348534202000001","348531201000001","348527200900001",
            "347524201400001","347521201900002","347520202000001","347518201000001","347510201600001",
            "347509200900001","347508202000001","347505201700001","347502201600001","346498202000001",
            "346496201800001","346494201700001","346488201700001","346487201800001","346484201900001",
            "346483202100001","346482201600003","346482201400002","346481200900001","346480201000001",
            "345479202100001","345471201800002","345470201300002","345469202000001","345468201200001",
            "345467201000001","345464202200001","345464201700001","345464201200001","345464200900001",
            "344568201700001","344558202400001","344461202400001","344460201300001","344457202000001",
            "344454202000006","344452202300001","344451201300001","344449202100001","343571201600001",
            "343557202300001","343445202200001","343444200900002","343443201500001","343442202200001",
            "343440201500002","343439201700001","342426201600001","342425201400001","342418201300001",
            "341560201000002","341559200900001","341553201700001","341417201800001","341408200900001",
            "341407201900001","341406202200002","341405201800001","341403202100001","341401202200001",
            "341393200900001","341392201300001","341391200900001","341386201800004","341386200900001",
            "341378201300002","341374202000001","330366202000001","326338201300001","326333201100001",
            "326326201000001","326325201300001","311313201000017","311312201100001","311307200900001",
            "311303202500005"
        ));

        Set<String> foundShelterIds = new HashSet<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String insertSQL = "INSERT IGNORE INTO Shelter (shelter_id, shelter_name, telephone, address) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);

            // 2. 전국 시도코드(upr_cd) 목록 얻기
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

            // 3. upr_cd/org_cd 조합 돌면서 shelter_v2에서 누락 shelter_id 검색!
            for (String upr_cd : sidoList) {
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
                    String apiUrl = SHELTER_URL + "?serviceKey=" + SERVICE_KEY
                            + "&_type=json"
                            + "&upr_cd=" + upr_cd
                            + "&org_cd=" + org_cd
                            + "&numOfRows=1000&pageNo=1";
                    try {
                        JSONObject shelterResp = getJsonFromApi(apiUrl);
                        JSONObject body = shelterResp.getJSONObject("response").getJSONObject("body");
                        JSONObject items = body.getJSONObject("items");
                        if (!items.has("item")) continue;
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
                            if (!missingShelterIds.contains(careRegNo) || foundShelterIds.contains(careRegNo))
                                continue;

                            String name = item.optString("careNm");
                            String tel = item.optString("careTel");
                            String addr = item.optString("careAddr");

                            pstmt.setString(1, careRegNo);
                            pstmt.setString(2, name);
                            pstmt.setString(3, tel);
                            pstmt.setString(4, addr);
                            pstmt.addBatch();
                            foundShelterIds.add(careRegNo);
                            System.out.println("insert: " + careRegNo + " / " + name);
                        }

                    } catch (Exception e) {
                        System.out.println("보호소 정보 조회 실패: " + upr_cd + " / " + org_cd);
                    }
                }
            }
            pstmt.executeBatch();

            // 4. API에서 끝까지 못 찾은 shelter_id는 아무것도 insert하지 않음!
            missingShelterIds.removeAll(foundShelterIds);
            for (String notFound : missingShelterIds) {
                System.out.println("API에서도 shelter 정보 없음: " + notFound + " (DB에 insert하지 않음)");
            }

            System.out.println("Shelter 테이블에 API로 조회된 shelter_id 정보만 insert 완료");

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
        int responseCode = connHttp.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HTTP Error " + responseCode + " for URL: " + apiUrl);
        }
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
