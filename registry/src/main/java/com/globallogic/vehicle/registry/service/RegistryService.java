package com.globallogic.vehicle.registry.service;

import com.globallogic.vehicle.registry.controller.VehicleSO;
import com.globallogic.vehicle.registry.entities.Vehicle;
import com.globallogic.vehicle.registry.exceptions.RegistryResourceNotFound;
import com.globallogic.vehicle.registry.repository.RegistryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class RegistryService {

    @Autowired
    private RegistryRepository registryRepository;

    @Autowired
    protected ModelMapper modelMapper;

    public VehicleSO get(String vin) {
        Vehicle found = registryRepository.findByVin(vin);

        if (found == null) {
            throw new RegistryResourceNotFound("Vehicle with given VIN does not exist.");
        }

        return modelMapper.map(found, VehicleSO.class);
    }

    public List<VehicleSO> getAll() {
        List<Vehicle> found = registryRepository.findAll();

        return modelMapper.map(found, List.class);
    }

    public VehicleSO create(VehicleSO so) {
        Vehicle vehicle = modelMapper.map(so, Vehicle.class);

        return modelMapper.map(registryRepository.save(vehicle), VehicleSO.class);
    }

    public VehicleSO update(String vin, VehicleSO so) {
        Vehicle found = registryRepository.findByVin(vin);

        if (found == null) {
            throw new RegistryResourceNotFound("Vehicle with given VIN does not exist.");
        }

        found.setVin(so.getVin());
        found.setBrand(so.getBrand());
        found.setModel(so.getModel());
        found.setProductionYear(so.getProductionYear());

        return modelMapper.map(registryRepository.save(found), VehicleSO.class);
    }

    public void delete(String vin) {
        Vehicle found = registryRepository.findByVin(vin);

        if (found == null) {
            throw new RegistryResourceNotFound("Vehicle with given VIN does not exist.");
        }

        registryRepository.delete(found);
    }
}