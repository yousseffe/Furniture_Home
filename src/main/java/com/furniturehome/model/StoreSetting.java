package com.furniturehome.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "store_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "store_id" , nullable = false)
    private UUID store_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String logo_url;

    @Column(nullable = false)
    private String about_img_url;

    @Column(nullable = false)
    private String about_description;

    private String terms_cond;

    @Column(nullable = false)
    private String facebook_url;

    @Column(nullable = false)
    private String whatsapp_n;

    @Column(nullable = false)
    private String phone_n;

    @Column(nullable = false)
    private String second_phone_n;

    @Column(nullable = false)
    private LocalDateTime updated_at;
}
