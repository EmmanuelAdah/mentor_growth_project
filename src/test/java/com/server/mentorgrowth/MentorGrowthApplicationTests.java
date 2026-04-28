//package com.server.mentorgrowth;
//
//import com.server.mentorgrowth.services.UserServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MockMvc.*;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import java.util.List;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//class MentorGrowthApplicationTests {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserServiceImpl userService;
//
//    @MockBean
//
//    @BeforeEach
//    void setUp() {
//
//    }
//
//    @Test
//    void getAllMentees_ShouldReturnListOfMentees() throws Exception {
//        when(userService.getAllMentees()).thenReturn(List.of());
//
//        mockMvc.perform(
//                get("/api/v1/user/mentees/all")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//}
