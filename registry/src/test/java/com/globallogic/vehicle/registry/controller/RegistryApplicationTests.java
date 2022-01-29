package com.globallogic.vehicle.registry.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globallogic.vehicle.registry.entities.Vehicle;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistryApplicationTests {

    protected ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    protected MockMvc mockMvc;

    @Test
    public void testGetVehicle_positive_ok() throws Exception {
        mockMvc
                .perform(get("/vehicles/{vin}", "KMHDN56D94U100747").header("Content-Type", "application/json"))
                .andDo(print()) //
                .andExpect(status().isOk()) //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)) //
                .andExpect(jsonPath("$.productionYear", Matchers.is(2022))) //
                .andExpect(jsonPath("$.brand", Matchers.is("VW"))) //
                .andExpect(jsonPath("$.model", Matchers.is("Golf"))) //
                .andExpect(jsonPath("$.vin", Matchers.is("KMHDN56D94U100747")));
    }

    @Test
    public void testUpdateVehicle_positive_ok() throws Exception {
        mockMvc
                .perform(put("/vehicles/{vin}", "2G1FP22K7X2166369").header("Content-Type", "application/json")
                        .content(jsonMapper.writeValueAsString(new Vehicle("2G1FP22K7X2166369", "Renault", "Masterr", 2016))))
                .andExpect(status().isOk()) //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)) //
                .andExpect(jsonPath("$.productionYear", Matchers.is(2016))) //
                .andExpect(jsonPath("$.brand", Matchers.is("Renault"))) //
                .andExpect(jsonPath("$.model", Matchers.is("Masterr"))) //
                .andExpect(jsonPath("$.vin", Matchers.is("2G1FP22K7X2166369")));
    }

    @Test
    public void testDeleteVehicle_positive_ok() throws Exception {
        mockMvc
                .perform(delete("/vehicles/{vin}", "KMHDN56D94U100747"))
                .andExpect(status().isAccepted());
    }

    @Test
    public void testCreateVehicle_positive_ok() throws Exception {
        mockMvc
                .perform(post("/vehicles")
                        .content(jsonMapper.writeValueAsString(new Vehicle("JTKJF5C72FJ003759", "Audi", "A7", 2020)))
                        .header("Content-Type", "application/json"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void testGetAllVehicles_positive_ok() throws Exception {
        mockMvc
                .perform(get("/vehicles").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id").isNotEmpty());
    }

}