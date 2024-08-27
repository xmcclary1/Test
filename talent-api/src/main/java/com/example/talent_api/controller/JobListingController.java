package com.example.talent_api.controller;

import com.example.talent_api.domain.JobListing;
import com.example.talent_api.exception.UnauthorizedException;
import com.example.talent_api.service.JobListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/job-listings")
public class JobListingController {

    @Autowired
    private JobListingService jobListingService;

    //@PreAuthorize("hasRole('candidate') or hasRole('manager')")
    @GetMapping
    public List<JobListing> getAllJobListings() {
        return jobListingService.getAllJobListings();
    }

//    @PreAuthorize("hasRole('candidate') or hasRole('manager')")
    @GetMapping("/{id}")
    public ResponseEntity<JobListing> getJobListingById(@PathVariable String id) {
        Optional<JobListing> jobListing = jobListingService.getJobListingById(id);
        return jobListing.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @PreAuthorize("hasRole('manager')")
    @PostMapping
    public JobListing createJobListing(@RequestBody JobListing jobListing) {
        return jobListingService.createJobListing(jobListing);
    }

//    @PreAuthorize("hasRole('manager')")
    @PutMapping("/{id}")
    public ResponseEntity<JobListing> updateJobListing(@PathVariable String id, @RequestBody JobListing jobListing) {
        JobListing updatedJobListing = jobListingService.updateJobListing(id, jobListing);
        return updatedJobListing != null ? ResponseEntity.ok(updatedJobListing) : ResponseEntity.notFound().build();
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteJobListing(@PathVariable String id) {
//        jobListingService.deleteJobListing(id);
//        return ResponseEntity.noContent().build();
//    }

//    @PreAuthorize("hasRole('manager')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJobListing(@PathVariable String id, @RequestParam String managerId) {
        Optional<JobListing> jobListing = jobListingService.getJobListingById(id);

        if (jobListing.isEmpty()) {
            // Job listing not found or already deleted
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job listing not found or already deleted.");
        }

        if (!jobListing.get().getPostedBy().equals(managerId)) {
            // Unauthorized attempt to delete the job listing
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this job listing.");
        }

        jobListingService.deleteJobListing(id, managerId);
        return ResponseEntity.ok("Job listing successfully deleted.");
    }

//    @PreAuthorize("hasRole('candidate') or hasRole('manager')")
    @GetMapping("/active")
    public List<JobListing> getAllActiveJobListings() {
        return jobListingService.getAllActiveJobListings();
    }



}
