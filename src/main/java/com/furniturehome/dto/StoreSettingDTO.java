package com.furniturehome.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreSettingDTO {
    private UUID store_id;
    private String name;
    private String logo_url;
    private String about_img_url;
    private String about_description;
    private String terms_cond;
    private String facebook_url;
    private String whatsapp_n;
    private String phone_n;
    private String second_phone_n;
    private LocalDateTime updated_at;
}
