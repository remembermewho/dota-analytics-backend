package com.dota2.dota2dataperser.service;

import com.dota2.dota2dataperser.exception.OpenDotaDailyLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Component
public class OpenDotaDailyQuotaService {

    private final JdbcTemplate jdbcTemplate;

    @Value("${opendota.rate-limit.requests-per-day:3000}")
    private int requestsPerDay;

    @Value("${opendota.rate-limit.zone-id:UTC}")
    private String zoneId;

    public OpenDotaDailyQuotaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void reserveRequestSlot() {
        int safeRequestsPerDay = Math.max(1, requestsPerDay);
        LocalDate usageDate = LocalDate.now(ZoneId.of(zoneId));

        List<Integer> result = jdbcTemplate.query(
                """
                INSERT INTO opendota_api_usage (
                    usage_date,
                    request_count,
                    created_at,
                    updated_at
                )
                VALUES (?, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                ON CONFLICT (usage_date)
                DO UPDATE SET
                    request_count = opendota_api_usage.request_count + 1,
                    updated_at = CURRENT_TIMESTAMP
                WHERE opendota_api_usage.request_count < ?
                RETURNING request_count
                """,
                (rs, rowNum) -> rs.getInt("request_count"),
                usageDate,
                safeRequestsPerDay
        );

        if (result.isEmpty()) {
            int currentCount = getTodayRequestCount();

            throw new OpenDotaDailyLimitExceededException(
                    "OpenDota daily request limit exceeded. " +
                            "date=" + usageDate +
                            ", currentCount=" + currentCount +
                            ", limit=" + safeRequestsPerDay
            );
        }
    }

    public int getTodayRequestCount() {
        LocalDate usageDate = LocalDate.now(ZoneId.of(zoneId));

        Integer count = jdbcTemplate.queryForObject(
                """
                SELECT COALESCE(
                    (
                        SELECT request_count
                        FROM opendota_api_usage
                        WHERE usage_date = ?
                    ),
                    0
                )
                """,
                Integer.class,
                usageDate
        );

        return count != null ? count : 0;
    }
}