package com.popcorn.jrp.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Listing string like "abc, xyz, ..."
 * @author truong
 */
@Converter(autoApply = true)
public class JsonListStringConverter implements AttributeConverter<List<String>, String> {

    // Khởi tạo ObjectMapper (thread-safe)
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Chuyển List<String> thành một chuỗi JSON.
     * Ví dụ: ["Java", "Spring"] -> "[\"Java\",\"Spring\"]"
     */
    @Override
    public String convertToDatabaseColumn(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "[]"; // Trả về mảng rỗng JSON thay vì null
        }
        try {
            // Tuần tự hóa list thành chuỗi JSON
            return objectMapper.writeValueAsString(values);
        } catch (JsonProcessingException e) {
            // Ném ra runtime exception nếu không thể chuyển đổi
            throw new IllegalArgumentException("Lỗi khi serialize List<String> thành JSON", e);
        }
    }

    /**
     * Chuyển chuỗi JSON từ DB thành List<String>.
     * Ví dụ: "[\"Java\",\"Spring\"]" -> ["Java", "Spring"]
     */
    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty() || dbData.equals("[]")) {
            return new ArrayList<>();
        }
        try {
            // Giải tuần tự hóa chuỗi JSON thành List<String>
            return objectMapper.readValue(dbData, new TypeReference<List<String>>() {});
        } catch (IOException e) {
            // Ném ra runtime exception nếu không thể parse JSON
            throw new IllegalArgumentException("Lỗi khi deserialize JSON thành List<String>", e);
        }
    }
}
