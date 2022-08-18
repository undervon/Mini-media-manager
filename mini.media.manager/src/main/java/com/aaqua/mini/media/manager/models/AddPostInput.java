package com.aaqua.mini.media.manager.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddPostInput {

    private String title;
    private String image;
    private String description;
}
