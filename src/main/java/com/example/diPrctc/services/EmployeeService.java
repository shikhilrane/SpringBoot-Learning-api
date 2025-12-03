package com.example.diPrctc.services;

import com.example.diPrctc.dto.EmployeeDTO;
import com.example.diPrctc.entities.EmployeeEntity;
import com.example.diPrctc.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(id);
        return employeeEntity.map(employeeEntity1 -> modelMapper.map(employeeEntity1, EmployeeDTO.class ));
    }

    public List<EmployeeDTO> getAllEmployees() {
        List<EmployeeEntity> employeeEntities = employeeRepository.findAll();
        return employeeEntities
                .stream()
                .map(employeeEntity -> modelMapper.map(employeeEntity, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    public EmployeeDTO createNewEmployee(EmployeeDTO inputEmployee) {
        EmployeeEntity toSaveEntiy = modelMapper.map(inputEmployee, EmployeeEntity.class);
        EmployeeEntity employeeEntity = employeeRepository.save(toSaveEntiy);
        return modelMapper.map(employeeEntity, EmployeeDTO.class);
    }

    public EmployeeDTO updateEmployeeById(Long id, EmployeeDTO employeeDTO) {
        EmployeeEntity employeeEntity = modelMapper.map(employeeDTO, EmployeeEntity.class);
        employeeEntity.setId(id);
        EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);
        return modelMapper.map(savedEmployeeEntity, EmployeeDTO.class);
    }

    public boolean isExistByEmployeeId(Long id){
        return employeeRepository.existsById(id);
    }

    public boolean deleteEmployeeById(Long id) {
        boolean exist = isExistByEmployeeId(id);
        if (!exist) return false;
        employeeRepository.deleteById(id);
        return true;
    }

    public EmployeeDTO partialUpdate(Long id, Map<String, Object> updates) {
        boolean exist = isExistByEmployeeId(id);
        if (!exist) return null;
        EmployeeEntity employeeEntity = employeeRepository.findById(id).get();
        updates.forEach((key,value) -> {
            Field fieldToBeUpdated = ReflectionUtils.getRequiredField(EmployeeEntity.class, key);
            fieldToBeUpdated.setAccessible(true);
            ReflectionUtils.setField(fieldToBeUpdated, employeeEntity, value);
        });
        EmployeeEntity employeeEntity1 = employeeRepository.save(employeeEntity);
        return modelMapper.map(employeeEntity1, EmployeeDTO.class);
    }
}

/*

    Receiving - private final Repository repository;
    Return - DTO

    ModelMapper - ModelMapper is used to automatically map one Java object to another, typically between DTOs and entities or vice versa.

    Method for GET ->
        1. Return Type - Optional<DTO>
        2. Parameter - As we have passed @PathVariable Long id, so we have to pass id here as well
        3. Receiving from DB - using repository and using Repository method findById(pass id) and save it Optional<Entity> entity
        4. Return - apply .map method on entity from 3. (pass new entity in it and using modelMapper.map(new entity with DTO.class))

    Method for GET (for all) ->
        1. Return Type - List<DTO>
        2. Parameter - Nothing
        3. Receiving from DB - using repository and using Repository method findAll() and save it List<Entity> entity
        4. Return - apply .map method on entity from 3. And apply .stream() .map(and pass new entity and return dto using modelMapper) .collect(collect in toList() of Collectors) methods

    Method for POST ->
        1. Return Type - DTO
        2. Parameter - Pass dto
        3. Push from DB - Using modelMapper, map the dto with Entity.class and save it to Entity
                          And using repository and using Repository method save(entity from modelMapper) and save it in Entity
        4. Return - map(entity from 3. with DTO.class) using modelMapper

    Method for PUT ->
        1. Return Type - DTO
        2. Parameter - id, Pass dto
        3. Receiving from DB - Using modelMapper, map the dto with Entity.class and save it to entity
                               use .set on entity and pass id in it
                               And using repository and using Repository method save(entity to it) and save it in Entity
        4. Return - map(entity from 3. with DTO.class) using modelMapper

    Method for DELETE ->
        1. Return Type - boolean
        2. Parameter - id
        3. Receiving from DB - using repository and using Repository method existsById(pass id) and save it boolean exist, use to check if this id present in DB or not
        4. Return - return false, if(!exist)
                    on repository use deleteById(pass id) in it
                    return true

    Method for PATCH ->
        1. Return type - DTO
        2. Parameter - id, Map<String, Object> updates
        3. Receiving from DB - using repository and using Repository method existsById(pass id) and save it boolean exist, use to check if this id present in DB or not
        4. Return - return null, if(!exist)
                    apply repository's method findById(pass id) and get() and store it in new entity
                    If the employee DOES exist:
                    Fetch the existing EmployeeEntity using findById(id).get()
                    Loop through all entries in the 'updates' map
                    For each entry:
                      Find the corresponding field in EmployeeEntity using reflection
                      Make the field accessible
                      Set the new value on the entity
                    Save the modified entity back to the database using save()
                    Convert the saved entity into EmployeeDTO using modelMapper
                    return map(entity from lastly created. with DTO.class) using modelMapper
*/