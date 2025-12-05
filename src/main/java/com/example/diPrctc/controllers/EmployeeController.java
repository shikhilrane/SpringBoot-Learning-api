package com.example.diPrctc.controllers;

import com.example.diPrctc.dto.EmployeeDTO;
import com.example.diPrctc.exceptions.ResourceNotFoundException;
import com.example.diPrctc.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(path = "/employees/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable (name = "employeeId") Long id){
        Optional<EmployeeDTO> employeeDTO = employeeService.getEmployeeById(id);
        return employeeDTO
                .map(employeeDTO1 -> ResponseEntity.ok(employeeDTO1))
                .orElseThrow(() -> new ResourceNotFoundException("Employee with given if not found"));
    }

//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<String> handleEmpNotFound(NoSuchElementException exception){
//        return new ResponseEntity<>("Employee not found for given id", HttpStatus.NOT_FOUND);
//    }

    @GetMapping(path = "/employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(){
        List<EmployeeDTO> employeeDTO = employeeService.getAllEmployees();
        return ResponseEntity.ok(employeeDTO);
    }

    @PostMapping(path = "/employees/postEmp")
    public ResponseEntity<EmployeeDTO> postEmpl(@RequestBody @Valid EmployeeDTO inputEmployee){
        EmployeeDTO asveEmployee = employeeService.createNewEmployee(inputEmployee);
        return new ResponseEntity<>(asveEmployee, HttpStatus.CREATED);
    }

    @PutMapping(path = "/employees/{id}")
    public ResponseEntity<EmployeeDTO> putEmpl(@RequestBody EmployeeDTO employeeDTO, @PathVariable Long id){
        EmployeeDTO putDto = employeeService.updateEmployeeById(id, employeeDTO);
        return ResponseEntity.ok(putDto);
    }

    @DeleteMapping(path = "/employees/{id}")
    public ResponseEntity<Boolean> deleteEmpl(@PathVariable Long id){
        boolean isDeleted = employeeService.deleteEmployeeById(id);
        if (isDeleted) return ResponseEntity.ok(true);
        return ResponseEntity.notFound().build();
    }

    @PatchMapping(path = "/employees/{id}")
    public ResponseEntity<EmployeeDTO> patchEmpl(@RequestBody Map<String, Object> updates,
                                 @PathVariable Long id){
        EmployeeDTO employeeDTO = employeeService.partialUpdate(id, updates);
        if (employeeDTO == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(employeeDTO);
    }
}

/*
    @RestController = @Controller + @ResponseBody

    Receiving - from Service by private final Service service;
    Return - DTO

    ResponseEntity is used to show status codes for each and every apis.

    GET -> getting single data from DB
        1. Annotation - @GetMapping("/url/{id}")  <- It will return data for id 1
        2. Return type - ResponseEntity<EmployeeDTO>
        3. Parameters - To get the id @PathVariable we can rename it by using (name=)
        4. Receiving - from service and passing id from parameter to get it and save it in <Optional> ( <Optional> is simply used to represent a value that may or may not be present)
        5. Return - Return DTO, and map (return new dto and apply .ok method on ResponseEntity and pass newly created object in it) OR orElse(ResponseEntity.notFound().build())

    GET (getting all data) ->
        1. Annotation - @GetMapping("url")  <- It will return all data
        2. Return Type - ResponseEntity<List<EmployeeDTO>>
        3. Parameters - Nothing
        4. Receiving - from service call the method and save it in DTO and in List
        5. Return - Return DTO, and apply .ok method on ResponseEntity and pass List of DTO from 3.

    POST ->
        1. Annotation - @PostMapping("url/postUser")  <- It will save data
        2. Return Type - ResponseEntity<EmployeeDTO>
        3. Parameters - @RequestBody Dto dto, (@RequestBody it converts json to java object using jackson (and @ResponseBody is reverse of it))
        4. Receiving - from service call the method and pass dto in it and save it in DTO
        5. Return - Return new ResponseEntity<>, and pass saved dto created from 4 and HttpStatus.CREATED in it

    PUT ->
        1. Annotation - @PutMapping("/url/{id}")  <- It will return data for id 1 to update user
        2. Return Type - ResponseEntity<EmployeeDTO>
        3. Parameters - @RequestBody Dto dto, and @PathVariable Long id in it
        4. Receiving - from service call the method, and pass id and dto in it and save it in DTO
        5. Return - Return ResponseEntity with .ok and pass DTO created from 4

    DELETE ->
        1. Annotation - @DeleteMapping("/url/{id}")  <- It will get data for id 1 to delete user
        2. Return Type - ResponseEntity<Boolean>
        3. Parameters - @PathVariable Long id in it
        4. Receiving - from service call the method, and pass id and save it in Boolean
        5. Return - if deleted from DB using 4. return ResponseEntity.ok(true) OR return ResponseEntity.notFound().build();

    PATCH ->
        1. Annotation - @PatchMapping("/url/{id}")  <- It will return data for id 1 to partially update user
        2. Return Type - ResponseEntity<EmployeeDTO>
        3. Parameters - @RequestBody Map<String,object> updates (it will get what to edit), and @PathVariable Long id in it
        4. Receiving - from service call the method, and pass id and updates in it and save it in DTO
        5. Return - if user not found then return ResponseEntity.notFound().build() OR return ResponseEntity.ok(DTO from 4.)
*/