package com.projectlos.loan_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectlos.loan_service.service.LoanApplicationService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoanApplicationService loanApplicationService;

    @MockBean
    private TaskService taskService;

    @MockBean
    private RuntimeService runtimeService;

    @Test
    @WithMockUser(roles = "MAKER")
    void testGetTaskList() throws Exception {
        // Given
        List<Task> mockTasks = new ArrayList<>();
        when(taskService.createTaskQuery()).thenReturn(mock(org.flowable.task.api.TaskQuery.class));
        when(taskService.createTaskQuery().active()).thenReturn(mock(org.flowable.task.api.TaskQuery.class));
        when(taskService.createTaskQuery().active().list()).thenReturn(mockTasks);

        // When & Then
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "CHECKER")
    void testApproveTask() throws Exception {
        // Given
        String taskId = "test-task-id";
        Task mockTask = mock(Task.class);
        when(mockTask.getTaskDefinitionKey()).thenReturn("checker-approval");
        when(taskService.createTaskQuery()).thenReturn(mock(org.flowable.task.api.TaskQuery.class));
        when(taskService.createTaskQuery().taskId(taskId)).thenReturn(mock(org.flowable.task.api.TaskQuery.class));
        when(taskService.createTaskQuery().taskId(taskId).singleResult()).thenReturn(mockTask);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("applicationVerified", true);
        requestBody.put("addressVerified", true);
        requestBody.put("message", "All verifications completed");

        // When & Then
        mockMvc.perform(post("/tasks/{taskId}/approve", taskId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.taskId").value(taskId));
    }

    @Test
    @WithMockUser(roles = "APPROVER")
    void testRejectTask() throws Exception {
        // Given
        String taskId = "test-task-id";
        Task mockTask = mock(Task.class);
        when(mockTask.getTaskDefinitionKey()).thenReturn("approver-approval");
        when(taskService.createTaskQuery()).thenReturn(mock(org.flowable.task.api.TaskQuery.class));
        when(taskService.createTaskQuery().taskId(taskId)).thenReturn(mock(org.flowable.task.api.TaskQuery.class));
        when(taskService.createTaskQuery().taskId(taskId).singleResult()).thenReturn(mockTask);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("reason", "Credit risk too high");

        // When & Then
        mockMvc.perform(post("/tasks/{taskId}/reject", taskId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"))
                .andExpect(jsonPath("$.reason").value("Credit risk too high"));
    }

    @Test
    @WithMockUser(roles = "MAKER")
    void testGetTaskDetail() throws Exception {
        // Given
        String taskId = "test-task-id";
        Task mockTask = mock(Task.class);
        when(mockTask.getId()).thenReturn(taskId);
        when(mockTask.getName()).thenReturn("Maker Approval");
        when(mockTask.getTaskDefinitionKey()).thenReturn("maker-approval");
        
        when(taskService.createTaskQuery()).thenReturn(mock(org.flowable.task.api.TaskQuery.class));
        when(taskService.createTaskQuery().taskId(taskId)).thenReturn(mock(org.flowable.task.api.TaskQuery.class));
        when(taskService.createTaskQuery().taskId(taskId).singleResult()).thenReturn(mockTask);

        // When & Then
        mockMvc.perform(get("/tasks/{taskId}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(taskId))
                .andExpect(jsonPath("$.taskName").value("Maker Approval"));
    }
}
