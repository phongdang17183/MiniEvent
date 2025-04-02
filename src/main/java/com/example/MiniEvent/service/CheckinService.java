package com.example.MiniEvent.service;

import com.example.MiniEvent.model.CheckinRequest;
import com.example.MiniEvent.model.Event;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

@Service
public class CheckinService {

    private static final double DISTANCE_THRESHOLD = 0.1; // 100m tính bằng km

    public String checkin(CheckinRequest request) throws Exception {
        Firestore db = FirestoreClient.getFirestore();

        // Lấy thông tin sự kiện từ Firestore
        DocumentSnapshot eventDoc = db.collection("events").document(request.getEventId()).get().get();
        if (!eventDoc.exists()) {
            return "Sự kiện không tồn tại";
        }

        Event event = eventDoc.toObject(Event.class);

        // Tính khoảng cách bằng Haversine
        double distance = calculateDistance(
                event.getLatitude(), event.getLongitude(),
                request.getUserLatitude(), request.getUserLongitude()
        );

        // Kiểm tra ngưỡng
        if (distance <= DISTANCE_THRESHOLD) {
            // Lưu thông tin điểm danh (tạm thời chỉ lưu request, có thể thêm logic sau)
            db.collection("checkins").document().set(request);
            return "Điểm danh thành công";
        } else {
            return "Bạn không ở gần sự kiện (khoảng cách: " + String.format("%.2f", distance) + " km)";
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Bán kính Trái Đất (km)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c; // Khoảng cách tính bằng km
    }
}
