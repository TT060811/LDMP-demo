package com.logistics.platfrom.distribution.warehouse.service.Impl;

import com.logistics.platfrom.distribution.warehouse.entity.Warehouse;
import com.logistics.platfrom.distribution.warehouse.feign.WaybillServiceFeignClient;
import com.logistics.platfrom.distribution.warehouse.repository.InventoryRepository;
import com.logistics.platfrom.distribution.warehouse.service.WarehouseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class WarehouseRepositoryImpl implements WarehouseRepository{
    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private WaybillServiceFeignClient waybillServiceFeignClient;

    //仓库管理
    @Override
    public Warehouse saveWarehouse(Warehouse warehouse){
        if (warehouse.getWarehouseId() == null){
            String code = "WH" + System.currentTimeMillis();
            warehouse.setWarehouseCode(code);
        }
        warehouse.setCreateTime(LocalDateTime.now());
        return warehouseRepository.save( warehouse);
    }

    @Override
    public List<Warehouse> findAllWarehouse(){
        return warehouseRepository.findAll();
    }

    //入库操作
    @Override
    @Transactional
    public String inbound(Long warehouseId, Long waybillId){
        try{
            Warehouse warehouse = warehouseRepository.findById(warehouseId).orElseThrow(() -> new RuntimeException("仓库不存在"));
        }//检查仓库是否存在
    }
}
