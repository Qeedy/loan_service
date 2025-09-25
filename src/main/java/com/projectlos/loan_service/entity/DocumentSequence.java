package com.projectlos.loan_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "document_sequences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentSequence {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String prefix;
    
    @Column(nullable = false)
    private Long number;
}
