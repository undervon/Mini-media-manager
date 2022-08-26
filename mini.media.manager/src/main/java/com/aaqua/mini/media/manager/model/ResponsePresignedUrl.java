package com.aaqua.mini.media.manager.model;

import com.aaqua.mini.media.manager.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePresignedUrl {

    private Image image;
    private String url;
}
