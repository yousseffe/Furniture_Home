package com.furniturehome.repository;

import com.furniturehome.model.StoreSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoreSettingRepository extends JpaRepository<StoreSetting, UUID> {

}
