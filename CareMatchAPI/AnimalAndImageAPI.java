package CareMatchAPI;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.json.*;

public class AnimalAndImageAPI {

    static final String SERVICE_KEY = "U5Fc8ClxFFw%2F1zGDrJ1FJ%2FUCGlEW80plhVQwzw3DPGmFA0aQkq9JSKkZhYDDB33xcQ1GMOcFXV1xpHpQ6ZzU1Q%3D%3D";
    static final String BASE_URL = "https://apis.data.go.kr/1543061/abandonmentPublicService_v2/abandonmentPublic_v2";

    static final String DB_URL = "jdbc:mysql://localhost:3306/CareMatch?serverTimezone=Asia/Seoul";
    static final String DB_USER = "root";
    static final String DB_PASS = "1234";

    public static void main(String[] args) {
        List<JSONObject> allAnimals = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String endDate = LocalDate.now().format(formatter);
        String startDate = LocalDate.now().minusYears(1).format(formatter);

        try {
            for (int page = 1; page <= 5; page++) {
                JSONArray items = fetchAnimalPage(startDate, endDate, page);
                if (items == null || items.length() == 0) break;

                for (int i = 0; i < items.length(); i++) {
                    allAnimals.add(items.getJSONObject(i));
                }
            }

            Collections.shuffle(allAnimals);
            System.out.println("총 수집된 동물 수: " + allAnimals.size());
            List<JSONObject> random100 = allAnimals.subList(0, Math.min(100, allAnimals.size()));

            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            String insertAnimalSQL = "INSERT INTO Animals "
                    + "(animal_id, age, weight, sex, neutered, happenDate, kind_id, shelter_id, status, image_id, animal_name) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            String insertImageSQL = "INSERT INTO Image (image_id, image_url) VALUES (?, ?)";

            PreparedStatement animalStmt = conn.prepareStatement(insertAnimalSQL);
            PreparedStatement imageStmt = conn.prepareStatement(insertImageSQL);

            for (JSONObject obj : random100) {
                String animal_id = obj.optString("desertionNo");
                String age = obj.optString("age").replaceAll("[^0-9]", "");
                String weightStr = obj.optString("weight").replace("Kg", "").replaceAll("[^0-9.]", "").trim();
                float weight = weightStr.isEmpty() ? 0 : Float.parseFloat(weightStr);
                String sex = obj.optString("sexCd");
                String neutered = obj.optString("neuterYn");
                String happenDate = obj.optString("happenDt");
                String kind_id = obj.optString("kindCd");
                String shelter_id = obj.optString("careRegNo");
                String status = obj.optString("processState");
                String image_url = obj.optString("popfile1");
                String image_id = "img_" + animal_id;
                String animal_name = "동물" + animal_id.substring(animal_id.length() - 4);

                animalStmt.setString(1, animal_id);
                animalStmt.setInt(2, age.isEmpty() ? 0 : Integer.parseInt(age));
                animalStmt.setFloat(3, weight);
                animalStmt.setString(4, sex);
                animalStmt.setString(5, neutered);
                animalStmt.setDate(6, Date.valueOf(happenDate.substring(0, 4) + "-" + happenDate.substring(4, 6) + "-" + happenDate.substring(6)));
                animalStmt.setString(7, kind_id);
                animalStmt.setString(8, shelter_id);
                animalStmt.setString(9, status);
                animalStmt.setString(10, image_id);
                animalStmt.setString(11, animal_name);
                animalStmt.addBatch();

                imageStmt.setString(1, image_id);
                imageStmt.setString(2, image_url);
                imageStmt.addBatch();
            }

            animalStmt.executeBatch();
            imageStmt.executeBatch();
            conn.close();

            System.out.println("API에서 가져온 랜덤 100개가 Animals 및 Image 테이블에 저장되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONArray fetchAnimalPage(String start, String end, int pageNo) {
        try {
            StringBuilder urlBuilder = new StringBuilder(BASE_URL);
            urlBuilder.append("?serviceKey=").append(SERVICE_KEY);
            urlBuilder.append("&_type=json");
            urlBuilder.append("&numOfRows=1000");
            urlBuilder.append("&pageNo=").append(pageNo);
            urlBuilder.append("&bgnde=").append(start);
            urlBuilder.append("&endde=").append(end);

            System.out.println("요청 URL: " + urlBuilder.toString());

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            System.out.println("응답 코드: " + conn.getResponseCode());

            if (conn.getResponseCode() != 200) {
                BufferedReader errReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String errLine;
                while ((errLine = errReader.readLine()) != null) {
                    System.err.println("API 오류 응답: " + errLine);
                }
                errReader.close();
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            conn.disconnect();

            JSONObject response = new JSONObject(sb.toString());
            return response.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");

        } catch (Exception e) {
            System.err.println("API 호출 실패 (page " + pageNo + ")");
            e.printStackTrace();
            return null;
        }
    }
}
