package com.furniturehome.service;


import com.furniturehome.dto.StoreSettingDTO;
import com.furniturehome.model.StoreSetting;
import com.furniturehome.repository.StoreSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreSettingService {

    private final StoreSettingRepository storeSettingRepository;

    private StoreSettingDTO convertToDto(StoreSetting storeSetting){
        StoreSettingDTO storeSettingDTO = new StoreSettingDTO();
        storeSettingDTO.setStore_id(storeSetting.getStore_id());
        storeSettingDTO.setName(storeSetting.getName());
        storeSettingDTO.setLogo_url(storeSetting.getLogo_url());
        storeSettingDTO.setPhone_n(storeSetting.getPhone_n());
        storeSettingDTO.setFacebook_url(storeSetting.getFacebook_url());
        storeSettingDTO.setAbout_description(storeSetting.getAbout_description());
        storeSettingDTO.setAbout_img_url(storeSetting.getAbout_img_url());
        storeSettingDTO.setSecond_phone_n(storeSetting.getSecond_phone_n());
        storeSettingDTO.setUpdated_at(storeSetting.getUpdated_at());
        storeSettingDTO.setWhatsapp_n(storeSetting.getWhatsapp_n());
        storeSettingDTO.setTerms_cond(storeSetting.getTerms_cond());
        return storeSettingDTO;
    }

    private StoreSetting convertToEntity(StoreSettingDTO storeSettingDTO){
        StoreSetting storeSetting = new StoreSetting();
        storeSetting.setStore_id(storeSettingDTO.getStore_id());
        storeSetting.setName(storeSettingDTO.getName());
        storeSetting.setLogo_url(storeSettingDTO.getLogo_url());
        storeSetting.setPhone_n(storeSettingDTO.getPhone_n());
        storeSetting.setFacebook_url(storeSettingDTO.getFacebook_url());
        storeSetting.setAbout_description(storeSettingDTO.getAbout_description());
        storeSetting.setAbout_img_url(storeSettingDTO.getAbout_img_url());
        storeSetting.setSecond_phone_n(storeSettingDTO.getSecond_phone_n());
        storeSetting.setUpdated_at(storeSettingDTO.getUpdated_at());
        storeSetting.setWhatsapp_n(storeSettingDTO.getWhatsapp_n());
        storeSetting.setTerms_cond(storeSettingDTO.getTerms_cond());
        return storeSetting;
    }

    public StoreSettingDTO createStoreSetting(StoreSettingDTO storeSettingDTO){
        StoreSetting storeSetting = convertToEntity(storeSettingDTO);
        StoreSetting saved = storeSettingRepository.save(storeSetting);
        return convertToDto(saved);
    }

    public StoreSettingDTO getStoreSettings(UUID id){
        return storeSettingRepository.findById(id).map(this::convertToDto).orElseThrow(()->new RuntimeException("Not found"));
    }

    public StoreSettingDTO updateStoreSetting(UUID id, StoreSettingDTO storeSettingDTO){
        StoreSetting storeSetting = storeSettingRepository.findById(id).orElseThrow(()->new RuntimeException("Not found"));
        storeSetting.setName(storeSettingDTO.getName());
        storeSetting.setLogo_url(storeSettingDTO.getLogo_url());
        storeSetting.setPhone_n(storeSettingDTO.getPhone_n());
        storeSetting.setFacebook_url(storeSettingDTO.getFacebook_url());
        storeSetting.setAbout_description(storeSettingDTO.getAbout_description());
        storeSetting.setAbout_img_url(storeSettingDTO.getAbout_img_url());
        storeSetting.setSecond_phone_n(storeSettingDTO.getSecond_phone_n());
        storeSetting.setUpdated_at(storeSettingDTO.getUpdated_at());
        storeSetting.setWhatsapp_n(storeSettingDTO.getWhatsapp_n());
        storeSetting.setTerms_cond(storeSettingDTO.getTerms_cond());
        return convertToDto(storeSetting);
    }


}
