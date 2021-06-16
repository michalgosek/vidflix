package com.example.videly.video.dataaccess.mysql.videos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IDAggregate {
    private final Long videoId;
    private final Long categoryId;
}
