package com.furniturehome.controller;

import com.furniturehome.dto.StoreSettingDTO;
import com.furniturehome.service.StoreSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/storesetting")
@RequiredArgsConstructor
public class StoreSettingController {
    private final StoreSettingService storeSettingService;


    @PostMapping
    public ResponseEntity<StoreSettingDTO>createStoreSetting(@RequestBody StoreSettingDTO storeSettingDTO){
        return ResponseEntity.ok(storeSettingService.createStoreSetting(storeSettingDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreSettingDTO>getStoreSetting(@PathVariable UUID id){
        return ResponseEntity.ok(storeSettingService.getStoreSettings(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreSettingDTO>updateStoreSetting(@PathVariable UUID id, @RequestBody StoreSettingDTO storeSettingDTO){
        return ResponseEntity.ok(storeSettingService.updateStoreSetting(id, storeSettingDTO));
    }
}
