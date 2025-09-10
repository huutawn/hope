package com.llt.hope.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.llt.hope.document.JobDocument;
import com.llt.hope.document.PostDocument;
import com.llt.hope.document.PostVolunteerDocument;
import com.llt.hope.entity.Job;
import com.llt.hope.entity.MediaFile;
import com.llt.hope.entity.Post;
import com.llt.hope.entity.PostVolunteer;
import com.llt.hope.repository.elasticsearch.JobDocumentRepository;
import com.llt.hope.repository.elasticsearch.PostDocumentRepository;
import com.llt.hope.repository.elasticsearch.PostVolunteerDocumentRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@ConditionalOnProperty(value = "spring.elasticsearch.enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DocumentIndexingService {
    JobDocumentRepository jobDocumentRepository;
    PostDocumentRepository postDocumentRepository;
    PostVolunteerDocumentRepository postVolunteerDocumentRepository;

    /**
     * Index a Job entity to Elasticsearch
     */
    public void indexJob(Job job) {
        try {
            JobDocument jobDocument = JobDocument.builder()
                    .id(job.getId().toString())
                    .entityId(job.getId())
                    .title(job.getTitle())
                    .description(job.getDescription())
                    .requirements(job.getRequirements())
                    .responsibilities(job.getResponsibilities())
                    .benefits(job.getBenefits())
                    .suitableForDisability(job.getSuitableForDisability())
                    .location(job.getLocation())
                    .salaryMin(job.getSalaryMin())
                    .salaryMax(job.getSalaryMax())
                    .applicationDeadline(job.getApplicationDeadline())
                    .isActive(job.getIsActive())
                    .jobType(job.getJobType())
                    .createdAt(job.getCreatedAt().toLocalDate())
                    .views(job.getViews())
                    // Company information
                    .companyId(job.getCompany() != null ? job.getCompany().getId() : null)
                    .companyName(job.getCompany() != null ? job.getCompany().getName() : null)
                    .companyDescription(job.getCompany() != null ? job.getCompany().getDescription() : null)
                    .companyIndustry(job.getCompany() != null ? job.getCompany().getIndustry() : null)
                    .companySize(job.getCompany() != null ? job.getCompany().getSize() : null)
                    .companyLocation(job.getCompany() != null ? job.getCompany().getAddress() : null)
                    // Employer information
                    .employerId(job.getEmployer() != null ? job.getEmployer().getId() : null)
                    .employerEmail(job.getEmployer() != null ? job.getEmployer().getEmail() : null)
                    // Job category information
                    .jobCategoryId(job.getJobCategory() != null ? job.getJobCategory().getId() : null)
                    .jobCategoryName(job.getJobCategory() != null ? job.getJobCategory().getName() : null)
                    .build();

            jobDocumentRepository.save(jobDocument);
            log.info("Successfully indexed job with ID: {}", job.getId());
        } catch (Exception e) {
            log.error("Failed to index job with ID: {}", job.getId(), e);
        }
    }

    /**
     * Index a Post entity to Elasticsearch
     */
    public void indexPost(Post post) {
        try {
            // Extract image URLs from MediaFiles
            List<String> imageUrls = post.getImages() != null ? 
                post.getImages().stream()
                    .map(MediaFile::getUrl)
                    .collect(Collectors.toList()) : null;

            PostDocument postDocument = PostDocument.builder()
                    .id(post.getId().toString())
                    .entityId(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .isPublished(post.isPublished())
                    .isPinned(post.isPinned())
                    .isActive(post.isActive())
                    .createdAt(post.getCreatedAt().toLocalDate())
                    .type(post.getType())
                    .capital(post.getCapital())
                    .likes(post.getLikes())
                    // User information
                    .userId(post.getUser() != null ? post.getUser().getId() : null)
                    .userEmail(post.getUser() != null ? post.getUser().getEmail() : null)
                    .userName(post.getUser() != null && post.getUser().getProfile() != null ? 
                        post.getUser().getProfile().getFullName() : null)
                    .imageUrls(imageUrls)
                    .build();

            postDocumentRepository.save(postDocument);
            log.info("Successfully indexed post with ID: {}", post.getId());
        } catch (Exception e) {
            log.error("Failed to index post with ID: {}", post.getId(), e);
        }
    }

    /**
     * Index a PostVolunteer entity to Elasticsearch
     */
    public void indexPostVolunteer(PostVolunteer postVolunteer) {
        try {
            // Extract file URLs from MediaFiles
            List<String> fileUrls = postVolunteer.getFiles() != null ? 
                postVolunteer.getFiles().stream()
                    .map(MediaFile::getUrl)
                    .collect(Collectors.toList()) : null;

            PostVolunteerDocument postVolunteerDocument = PostVolunteerDocument.builder()
                    .id(postVolunteer.getId().toString())
                    .entityId(postVolunteer.getId())
                    .title(postVolunteer.getTitle())
                    .location(postVolunteer.getLocation())
                    .content(postVolunteer.getContent())
                    .stk(postVolunteer.getStk())
                    .bankName(postVolunteer.getBankName())
                    .fund(postVolunteer.getFund())
                    .totalAmount(postVolunteer.getTotalAmount())
                    .createAt(postVolunteer.getCreateAt().toLocalDate())
                    .status(postVolunteer.getStatus())
                    .isActive(postVolunteer.isActive())
                    // User information
                    .userId(postVolunteer.getUser() != null ? postVolunteer.getUser().getId() : null)
                    .userEmail(postVolunteer.getUser() != null ? postVolunteer.getUser().getEmail() : null)
                    .userName(postVolunteer.getUser() != null && postVolunteer.getUser().getProfile() != null ? 
                        postVolunteer.getUser().getProfile().getFullName() : null)
                    .fileUrls(fileUrls)
                    .build();

            postVolunteerDocumentRepository.save(postVolunteerDocument);
            log.info("Successfully indexed post volunteer with ID: {}", postVolunteer.getId());
        } catch (Exception e) {
            log.error("Failed to index post volunteer with ID: {}", postVolunteer.getId(), e);
        }
    }

    /**
     * Delete job document from Elasticsearch
     */
    public void deleteJobDocument(Long jobId) {
        try {
            jobDocumentRepository.deleteById(jobId.toString());
            log.info("Successfully deleted job document with ID: {}", jobId);
        } catch (Exception e) {
            log.error("Failed to delete job document with ID: {}", jobId, e);
        }
    }

    /**
     * Delete post document from Elasticsearch
     */
    public void deletePostDocument(Long postId) {
        try {
            postDocumentRepository.deleteById(postId.toString());
            log.info("Successfully deleted post document with ID: {}", postId);
        } catch (Exception e) {
            log.error("Failed to delete post document with ID: {}", postId, e);
        }
    }

    /**
     * Delete post volunteer document from Elasticsearch
     */
    public void deletePostVolunteerDocument(Long postVolunteerId) {
        try {
            postVolunteerDocumentRepository.deleteById(postVolunteerId.toString());
            log.info("Successfully deleted post volunteer document with ID: {}", postVolunteerId);
        } catch (Exception e) {
            log.error("Failed to delete post volunteer document with ID: {}", postVolunteerId, e);
        }
    }
}
