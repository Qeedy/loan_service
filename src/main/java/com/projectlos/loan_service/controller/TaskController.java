package com.projectlos.loan_service.controller;

import com.projectlos.loan_service.dto.TaskDetailResponse;
import com.projectlos.loan_service.dto.TaskResponse;
import com.projectlos.loan_service.dto.LoanApplicationResponse;
import com.projectlos.loan_service.exception.BaseException;
import com.projectlos.loan_service.exception.TaskNotFoundException;
import com.projectlos.loan_service.exception.InvalidLoanApplicationException;
import com.projectlos.loan_service.exception.UnauthorizedException;
import com.projectlos.loan_service.service.CollateralService;
import com.projectlos.loan_service.service.IdentityDocumentService;
import com.projectlos.loan_service.service.LoanApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loan/tasks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Task Management", description = "Task Management APIs for BPMN Workflow")
public class TaskController {
    
    private final LoanApplicationService loanApplicationService;
    private final CollateralService collateralService;
    private final IdentityDocumentService identityDocumentService;
    private final TaskService taskService;
    private final RuntimeService runtimeService;
    
    @GetMapping
    @Operation(summary = "Get task list", 
               description = "Get list of tasks with filters for businessKey, customerName, and status")
    public ResponseEntity<List<TaskResponse>> getTaskList(
            @Parameter(description = "Filter by application number (businessKey)") 
            @RequestParam(value = "businessKey", required = false) String businessKey,
            @Parameter(description = "Filter by customer name") 
            @RequestParam(value = "customerName", required = false) String customerName,
            @Parameter(description = "Filter by status") 
            @RequestParam(value = "status", required = false) String status,
            Authentication authentication) {
        
        log.info("Getting task list with filters - businessKey: {}, customerName: {}, status: {}", 
                businessKey, customerName, status);
        
        // Extract user roles from authentication
        String username = authentication.getName();
        Collection<String> userRoles = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toList());
        
        log.info("User {} has roles: {}", username, userRoles);
        
        // Get tasks based on user roles (candidate groups)
        List<Task> tasks = taskService.createTaskQuery()
                .active()
                .taskCandidateGroupIn(userRoles)
                .list();
        
        // Apply business key filter
        if (businessKey != null && !businessKey.trim().isEmpty()) {
            tasks = tasks.stream()
                    .filter(task -> {
                        String taskBusinessKey = runtimeService.createProcessInstanceQuery()
                                .processInstanceId(task.getProcessInstanceId())
                                .singleResult()
                                .getBusinessKey();
                        return businessKey.equals(taskBusinessKey);
                    })
                    .collect(Collectors.toList());
        }
        
        // Apply customer name filter
        if (customerName != null && !customerName.trim().isEmpty()) {
            tasks = tasks.stream()
                    .filter(task -> {
                        Map<String, Object> variables = runtimeService.getVariables(task.getProcessInstanceId());
                        String taskCustomerName = (String) variables.get("customerName");
                        return taskCustomerName != null && 
                               taskCustomerName.toLowerCase().contains(customerName.toLowerCase());
                    })
                    .collect(Collectors.toList());
        }
        
        try {
            // Convert to response format
            List<TaskResponse> taskResponses = tasks.stream()
                    .map(this::convertToTaskResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(taskResponses);
        } catch (BaseException e) {
            log.error("BaseException in getting task list: {}", e.getMessage(), e);
            throw e; // Re-throw BaseException to be handled by GlobalExceptionHandler
        } catch (Exception e) {
            log.error("Error getting task list: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/{taskId}/approve")
    @Operation(summary = "Approve task", 
               description = "Approve a task to proceed to the next step in the workflow")
    public ResponseEntity<Map<String, Object>> approveTask(
            @Parameter(description = "Task ID to approve") 
            @PathVariable(value = "taskId") String taskId,
            @RequestBody Map<String, Object> requestBody,
            Authentication authentication) {
        
        log.info("🔍 DEBUG: Approving task: {} with data: {}", taskId, requestBody);
        
        try {
            // Get task details
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            
            if (task == null) {
                log.error("❌ DEBUG: Task not found with ID: {}", taskId);
                throw new TaskNotFoundException("Task not found with ID: " + taskId);
            }
            
            log.info("✅ DEBUG: Task found - ID: {}, Name: {}, DefinitionKey: {}, ProcessInstanceId: {}", 
                    task.getId(), task.getName(), task.getTaskDefinitionKey(), task.getProcessInstanceId());
            
            // Get process variables for debugging
            Map<String, Object> processVariables = runtimeService.getVariables(task.getProcessInstanceId());
            log.info("🔍 DEBUG: Process variables: {}", processVariables);
            
            // Get business key
            String businessKey = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult()
                    .getBusinessKey();
            log.info("🔍 DEBUG: Business key: {}", businessKey);
            
            // Prepare variables based on task definition key
            Map<String, Object> variables = prepareApprovalVariables(task.getTaskDefinitionKey(), requestBody, authentication.getName(), (String) processVariables.get("applicationId"));
            log.info("🔍 DEBUG: Prepared approval variables: {}", variables);
            
            // Complete the task with approval variables
            log.info("🚀 DEBUG: About to complete task with variables: {}", variables);
            taskService.complete(taskId, variables);
            log.info("✅ DEBUG: Task completed successfully");
            
            Map<String, Object> response = new HashMap<>();
            response.put("taskId", taskId);
            response.put("status", "APPROVED");
            response.put("message", "Task approved successfully");
            response.put("taskDefinitionKey", task.getTaskDefinitionKey());
            
            return ResponseEntity.ok(response);
            
        } catch (BaseException e) {
            log.error("❌ DEBUG: BaseException in task approval {}: {}", taskId, e.getMessage(), e);
            throw e; // Re-throw BaseException to be handled by GlobalExceptionHandler
        } catch (Exception e) {
            log.error("❌ DEBUG: Error approving task {}: {}", taskId, e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to approve task");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/{taskId}")
    @Operation(summary = "Get task detail", 
               description = "Get detailed information about a specific task including application details")
    public ResponseEntity<TaskDetailResponse> getTaskDetail(
            @Parameter(description = "Task ID") 
            @PathVariable String taskId) {
        log.info("Getting task detail for task: {}", taskId);
        try {
            // Get task details
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                throw new TaskNotFoundException("Task not found with ID: " + taskId);
            }
            // Get process variables
            Map<String, Object> processVariables = runtimeService.getVariables(task.getProcessInstanceId());
            // Get business key (applicationNumber)
            String businessKey = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult()
                    .getBusinessKey();
            
            // Get application details using businessKey
            LoanApplicationResponse applicationResponse = null;
            if (businessKey != null) {
                try {
                    applicationResponse = loanApplicationService.getLoanApplicationByApplicationNumber(businessKey);
                } catch (Exception e) {
                    log.warn("Could not retrieve application for businessKey {}: {}", businessKey, e.getMessage());
                }
            }
            
            // Build simplified TaskDetailResponse - use process variables for now
            String customerName = (String) processVariables.get("customerName");
            BigDecimal requestedAmount = null;
            String applicationStatus = "ACTIVE";
            LocalDateTime applicationDate = null;
            
            // Try to get values from process variables as fallback
            if (processVariables.get("requestedAmount") != null) {
                try {
                    requestedAmount = new BigDecimal(processVariables.get("requestedAmount").toString());
                } catch (NumberFormatException e) {
                    log.warn("Could not parse requestedAmount from process variables");
                }
            }
            
            TaskDetailResponse response = TaskDetailResponse.builder()
                    .taskId(task.getId())
                    .taskName(task.getName())
                    .applicationNumber(businessKey)
                    .customerName(customerName)
                    .requestedAmount(requestedAmount)
                    .status(applicationStatus)
                    .assignee(task.getAssignee())
                    .createTime(convertToLocalDateTime(task.getCreateTime()))
                    .applicationDate(applicationDate)
                    .build();
            
            return ResponseEntity.ok(response);
            
        } catch (BaseException e) {
            log.error("BaseException in getting task detail {}: {}", taskId, e.getMessage(), e);
            throw e; // Re-throw BaseException to be handled by GlobalExceptionHandler
        } catch (Exception e) {
            log.error("Error getting task detail for task {}: {}", taskId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/{taskId}/reject")
    @Operation(summary = "Reject task", 
               description = "Reject a task and update application status accordingly")
    public ResponseEntity<Map<String, Object>> rejectTask(
            @Parameter(description = "Task ID to reject") 
            @PathVariable String taskId,
            @RequestBody Map<String, Object> requestBody,
            Authentication authentication) {
        
        // Validasi reason untuk rejection
        if (!requestBody.containsKey("reason") || requestBody.get("reason") == null || 
            requestBody.get("reason").toString().trim().isEmpty()) {
            throw new InvalidLoanApplicationException("reason is required for task rejection");
        }
        
        String reason = (String) requestBody.get("reason");
        log.info("Rejecting task: {} with reason: {}", taskId, reason);
        
        try {
            // Get task details
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            
            if (task == null) {
                throw new TaskNotFoundException("Task not found with ID: " + taskId);
            }
            
            // Prepare variables for rejection
            Map<String, Object> variables = prepareRejectionVariables(task.getTaskDefinitionKey(), requestBody, authentication.getName());
            
            // Complete the task with rejection variables
            taskService.complete(taskId, variables);
            
            Map<String, Object> response = new HashMap<>();
            response.put("taskId", taskId);
            response.put("status", "REJECTED");
            response.put("message", "Task rejected successfully");
            response.put("reason", reason);
            response.put("taskDefinitionKey", task.getTaskDefinitionKey());
            
            return ResponseEntity.ok(response);
            
        } catch (BaseException e) {
            log.error("BaseException in task rejection {}: {}", taskId, e.getMessage(), e);
            throw e; // Re-throw BaseException to be handled by GlobalExceptionHandler
        } catch (Exception e) {
            log.error("Error rejecting task {}: {}", taskId, e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to reject task");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    private Map<String, Object> prepareApprovalVariables(String taskDefinitionKey, Map<String, Object> requestBody, String userName, String applicationId) {
        Map<String, Object> variables = new HashMap<>();
        Long countCollateral = collateralService.countCollateralsByApplicationId(UUID.fromString(applicationId));
        Long countIdentityDocuments = identityDocumentService.countIdentityDocumentsByApplicationId(UUID.fromString(applicationId));
        Long countCollateralVerified = collateralService.countVerifiedCollateralsByApplicationId(UUID.fromString(applicationId));
        Long countIdentityDocumentsVerified = identityDocumentService.countVerifiedIdentityDocumentsByApplicationId(UUID.fromString(applicationId));
        switch (taskDefinitionKey) {
            case "maker-approval":
                if(countIdentityDocuments < 1 || countCollateral < 1)
                    throw new InvalidLoanApplicationException("Please add Collateral and Identity Documents before submit application");
                break;
            case "checker-approval":
                // Checker approval - set verification details
                if(countIdentityDocumentsVerified < countIdentityDocuments  || countCollateralVerified < countCollateral)
                    throw new InvalidLoanApplicationException("Please verify Collateral and Identity Documents before approve application");
                // Validasi variable yang diperlukan
                validateCheckerApprovalVariables(requestBody);
                
                variables.put("approvalStatus", "APPROVED");
                variables.put("applicationVerified", requestBody.get("applicationVerified"));
                variables.put("addressVerified", requestBody.get("addressVerified"));
                variables.put("contactVerified", requestBody.get("contactVerified"));
                variables.put("collateralVerified", requestBody.get("collateralVerified"));
                variables.put("checkerMessage", requestBody.get("message"));
                variables.put("checkerName", userName);
                break;
                
            case "approver-approval":
                // Approver approval - set final approval
                // Validasi variable yang diperlukan
                validateApproverApprovalVariables(requestBody);
                
                variables.put("approvalStatus", "APPROVED");
                variables.put("approvedAmount", requestBody.get("approvedAmount"));
                variables.put("approverUser", userName);
                break;
                
            default:
                log.warn("Unknown task definition key for approval: {}", taskDefinitionKey);
                break;
        }
        
        return variables;
    }
    
    private Map<String, Object> prepareRejectionVariables(String taskDefinitionKey, Map<String, Object> requestBody, String userName) {
        Map<String, Object> variables = new HashMap<>();
        
        switch (taskDefinitionKey) {
            case "maker-approval":
            case "checker-approval":
            case "approver-approval":
                // For all rejections, set the status and reason
                variables.put("approvalStatus", "REJECT");
                variables.put("reason", requestBody.get("reason"));
                variables.put("rejectedBy", userName);
                break;
                
            default:
                log.warn("Unknown task definition key for rejection: {}", taskDefinitionKey);
                break;
        }
        
        return variables;
    }
    
    private TaskResponse convertToTaskResponse(Task task) {
        Map<String, Object> processVariables = runtimeService.getVariables(task.getProcessInstanceId());
        String businessKey = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult()
                .getBusinessKey();
        
        return TaskResponse.builder()
                .taskId(task.getId())
                .taskName(task.getName())
                .applicationId((String) processVariables.get("applicationId"))
                .applicationNumber(businessKey)
                .customerId((String) processVariables.get("customerId"))
                .customerName((String) processVariables.get("customerName"))
                .createdAt(convertToLocalDateTime(task.getCreateTime()))
                .status(getLoanStatusFromTask(task))
                .build();
    }
    
    private com.projectlos.loan_service.enums.LoanStatus getLoanStatusFromTask(Task task) {
        // Map task definition key to appropriate status
        switch (task.getTaskDefinitionKey()) {
            case "maker-approval":
                return com.projectlos.loan_service.enums.LoanStatus.DRAFT;
            case "checker-approval":
                return com.projectlos.loan_service.enums.LoanStatus.SUBMITTED;
            case "approver-approval":
                return com.projectlos.loan_service.enums.LoanStatus.VERIFIED;
            default:
                return com.projectlos.loan_service.enums.LoanStatus.DRAFT;
        }
    }
    
    private java.time.LocalDateTime convertToLocalDateTime(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
    }
    
    /**
     * Validasi variable yang diperlukan untuk checker approval
     */
    private void validateCheckerApprovalVariables(Map<String, Object> requestBody) {
        if (!requestBody.containsKey("applicationVerified") || requestBody.get("applicationVerified") == null) {
            throw new InvalidLoanApplicationException("applicationVerified is required");
        }
        
        if (!requestBody.containsKey("addressVerified") || requestBody.get("addressVerified") == null) {
            throw new InvalidLoanApplicationException("addressVerified is required");
        }
        
        if (!requestBody.containsKey("contactVerified") || requestBody.get("contactVerified") == null) {
            throw new InvalidLoanApplicationException("contactVerified is required");
        }
        
        if (!requestBody.containsKey("collateralVerified") || requestBody.get("collateralVerified") == null) {
            throw new InvalidLoanApplicationException("collateralVerified is required");
        }
        
        if (!requestBody.containsKey("message") || requestBody.get("message") == null || 
            requestBody.get("message").toString().trim().isEmpty()) {
            throw new InvalidLoanApplicationException("message is required");
        }
    }
    
    /**
     * Validasi variable yang diperlukan untuk approver approval
     */
    private void validateApproverApprovalVariables(Map<String, Object> requestBody) {
        if (!requestBody.containsKey("approvedAmount") || requestBody.get("approvedAmount") == null) {
            throw new InvalidLoanApplicationException("approvedAmount is required");
        }
        
        try {
            Object amount = requestBody.get("approvedAmount");
            if (amount instanceof String) {
                Double.parseDouble((String) amount);
            } else if (amount instanceof Number) {
                if (((Number) amount).doubleValue() <= 0) {
                    throw new InvalidLoanApplicationException("approvedAmount must be greater than 0");
                }
            } else {
                throw new InvalidLoanApplicationException("approvedAmount must be a valid number");
            }
        } catch (NumberFormatException e) {
            throw new InvalidLoanApplicationException("approvedAmount must be a valid number");
        }
    }
}
