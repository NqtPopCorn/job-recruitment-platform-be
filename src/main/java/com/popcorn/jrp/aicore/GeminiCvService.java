package com.popcorn.jrp.aicore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.popcorn.jrp.domain.dto.aicore.AiCvParsingResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiCvService {

    private final ObjectMapper objectMapper;

    // 1. Lấy giá trị từ application.properties
    @Value("${google.api.key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String modelName;

    private Client client;

    // 2. Khởi tạo Client ngay khi Service được tạo
    // @PostConstruct đảm bảo chạy sau khi @Value đã inject xong giá trị
    @PostConstruct
    public void init() {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("API Key chưa được cấu hình trong application.properties");
        }

        try {
            this.client = new Client.Builder()
                    .apiKey(apiKey)  // TRUYỀN TRỰC TIẾP API KEY
                    .build();

            log.info("Gemini Client đã được khởi tạo thành công!");
        } catch (Exception e) {
            log.error("Không thể khởi tạo Gemini Client: ", e);
            throw new RuntimeException("Lỗi khởi tạo AI Client", e);
        }
    }


    public AiCvParsingResponse parseCvFromText(String rawText) {
        String prompt = buildPrompt(rawText);

        try {
            // 3. Gọi Gemini API (client đã được init ở trên)
            GenerateContentResponse response = client.models.generateContent(
                    modelName,
                    prompt,
                    null
            );

            String jsonResponseString = response.text();
            String cleanJson = cleanJsonString(jsonResponseString);

            return objectMapper.readValue(cleanJson, AiCvParsingResponse.class);

        } catch (Exception e) {
            log.error("Gemini API Error: ", e);
            throw new RuntimeException("Lỗi khi xử lý CV với AI: " + e.getMessage());
        }
    }

    // --- Các hàm phụ trợ giữ nguyên như cũ ---

    private String buildPrompt(String text) {
        return """
            You are a professional CV parser. Extract data from the following text into a strict JSON format.
            
            TEXT TO PARSE:
            %s
            
            RULES:
            1. Return ONLY raw JSON. No markdown (no ```json).
            2. Date format: YYYY-MM-DD. If 'Present', leave empty.
            3. 'category' must be exactly one of: EXPERIENCE, EDUCATION, PROJECT, SKILL.
            4. 'description' should be a bullet-point text.
            
            JSON STRUCTURE:
            {
              "fullName": "String",
              "designation": "String",
              "email": "String",
              "phone": "String",
              "city": "String",
              "summary": "String",
              "skills": ["String"],
              "languages": ["String"],
              "sections": [
                {
                  "category": "EXPERIENCE",
                  "title": "String",
                  "organization": "String",
                  "startDate": "YYYY-MM-DD",
                  "endDate": "YYYY-MM-DD",
                  "description": ["Bullet point 1", "Bullet point 2"]
                }
              ]
            }
            """.formatted(text);
    }

    public AiCvParsingResponse improveCvContent(String currentCvDataJson) {
        // Prompt chuyên dụng để Cải thiện nội dung
        String prompt = """
        You are a Professional Resume Writer (Harvard Standard).
        Your task is to POLISH and ENHANCE the following candidate profile.
        
        INPUT DATA (JSON):
        %s
        
        INSTRUCTIONS:
        1. **Summary**: Rewrite to be punchy, professional, highlighting years of experience and key strengths.
        2. **Experience**: Rewrite 'description' into a JSON Array of Strings (Bullet points) using strong ACTION VERBS (e.g., Spearheaded, Engineered, Optimized). Quantify results if possible.
        3. **Skills**: Group or refine skills if they are messy.
        4. **Structure**: Return the result in the EXACT SAME JSON Structure as the Input.
        5. **Output**: Return ONLY raw JSON. No Markdown.
        """.formatted(currentCvDataJson);

        try {
            // Gọi Gemini (như code cũ)
            GenerateContentResponse response = client.models.generateContent(
                    modelName, prompt, null
            );

            String cleanJson = cleanJsonString(response.text());
            return objectMapper.readValue(cleanJson, AiCvParsingResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("AI failed to improve CV: " + e.getMessage());
        }
    }

    private String cleanJsonString(String response) {
        if (response.contains("```json")) {
            return response.substring(response.indexOf("```json") + 7, response.lastIndexOf("```")).trim();
        }
        if (response.contains("```")) {
            return response.substring(response.indexOf("```") + 3, response.lastIndexOf("```")).trim();
        }
        return response.trim();
    }
}