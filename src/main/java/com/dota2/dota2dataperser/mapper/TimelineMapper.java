package com.dota2.dota2dataperser.mapper;

import com.dota2.dota2dataperser.dto.opendota.OpenDotaMatchDto;
import com.dota2.dota2dataperser.entity.MatchAdvantageTimelineEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TimelineMapper {

    public List<MatchAdvantageTimelineEntity> toEntities(OpenDotaMatchDto dto) {
        List<Integer> gold = dto.getRadiantGoldAdv();
        List<Integer> xp = dto.getRadiantXpAdv();

        int size = Math.max(
                gold != null ? gold.size() : 0,
                xp != null ? xp.size() : 0
        );

        List<MatchAdvantageTimelineEntity> result = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            MatchAdvantageTimelineEntity entity = new MatchAdvantageTimelineEntity();

            entity.setMatchId(dto.getMatchId());
            entity.setMinute(i);

            entity.setRadiantGoldAdv(valueAt(gold, i));
            entity.setRadiantXpAdv(valueAt(xp, i));

            result.add(entity);
        }

        return result;
    }

    private Integer valueAt(List<Integer> list, int index) {
        if (list == null || index >= list.size()) {
            return null;
        }

        return list.get(index);
    }
}